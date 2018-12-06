package com.cv.sddn.takeassistant.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.cv.sddn.takeassistant.File.FileUtil;
import com.cv.sddn.takeassistant.Adaper.OcrAdater;
import com.cv.sddn.takeassistant.bean.OcrResult;
import com.cv.sddn.takeassistant.R;
import com.cv.sddn.takeassistant.RecognizeService;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.cv.sddn.takeassistant.ToolClass.JsonHelper.getJsonStrFromNetData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int REQUEST_CODE_NUMBERS = 126;
    private static final int REQUEST_CODE_NUMBER = 120;

    private List<String> resultList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private TextView phonenum;
    private String[] mPerms = {Manifest.permission.CALL_PHONE,Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSIONS = 100;
    private List<OcrResult> OcrResultList;
    private OcrAdater ocrAdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化recyclerView
        initrecycler();
        requestPermission();
        //phonenum = (TextView) findViewById(R.id.phonenum);
        alertDialog = new AlertDialog.Builder(this);
        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);
        navigationView.setNavigationItemSelectedListener(this);
        // 数字识别
        findViewById(R.id.basic_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_NUMBER);
            }

        });

        findViewById(R.id.general_basic_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_NUMBERS);
            }
        });
        // 请选择您的初始化方式
        initAccessToken();
        //initAccessTokenWithAkSk();
    }


    private void initrecycler() {
        //获取查询数据库中数据
        OcrResultList = LitePal.findAll(OcrResult.class);
        recyclerView = findViewById(R.id.ocr_list);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layout.setReverseLayout(true);//列表翻转
        recyclerView.setLayoutManager(layout);
        //设置LayoutManager为LinearLayoutManager
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置Adapter
        recyclerView.setAdapter(ocrAdater);

    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }
    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }


    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
    private void infoPopText(final String result) {
        alertText("", result);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    //请求所需权限
    private void requestPermission() {
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            EasyPermissions.requestPermissions(this, "获取应有权限",PERMISSIONS, mPerms);
            //Log.d(TAG, "onClick: 获取读写内存权限,Camera权限和wifi权限");
        } else {
            EasyPermissions.requestPermissions(this, "获取读写内存权限,Camera权限和wifi权限", PERMISSIONS, mPerms);
        }
    }
    // 识别成功回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，数字
        if (requestCode == REQUEST_CODE_NUMBERS && resultCode == Activity.RESULT_OK) {
            RecognizeService.recNumbers(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            //2、使用JSONArray
                            Log.e(MainActivity.ACTIVITY_SERVICE, "获取words_result_num数字个数:" + getJsonObjects(result));
                            for (int i = 0; i < getJsonObjects(result).size(); i++) {
                                Log.e(MainActivity.ACTIVITY_SERVICE, "获取words_result_num数字个数:" + getJsonObjects(result).get(i));
                                try {
                                    resultList.add((String) getJsonObjects(result).get(i).get("words"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //存入数据
                            setData();
                        }
                    });
        }
        if (requestCode == REQUEST_CODE_NUMBER && resultCode == Activity.RESULT_OK) {
            RecognizeService.recNumbers(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            //2、使用JSONArray
                            Log.e(MainActivity.ACTIVITY_SERVICE, "获取words_result_num数字个数:" + getJsonObjects(result));
                            for (int i = 0; i < getJsonObjects(result).size(); i++) {
                                Log.e(MainActivity.ACTIVITY_SERVICE, "获取words_result_num数字个数:" + getJsonObjects(result).get(i));
                                try {
                                    resultList.add((String) getJsonObjects(result).get(i).get("words"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //存入数据
                            setDatacall();
                            
                        }
                    });
        }

    }

    private void setDatacall() {
        int a = 0;
        for (String string : resultList) {
            if (string.length() == 11) {
                a++;
                //phonenum.setText(string);
                Date dt = new Date();
                Log.e(MainActivity.ACTIVITY_SERVICE, "当前时间:" + dt.toString());
                OcrResult ocrResult = new OcrResult();
                ocrResult.setPhone_num(string);
                ocrResult.setCall_time(dt);
                ocrResult.save();
                callPhone(string);
                //刷新页面，更新数据
                this.initrecycler();
                continue;
            }

        }
        if(resultList.size()==0){
            Log.e(MainActivity.ACTIVITY_SERVICE, "没有识别到数字");
            Toast.makeText(getApplicationContext(), "没有识别到数字！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_NUMBER);
        }else if(a==0){
            Toast.makeText(getApplicationContext(), "没有识别到电话号码", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_NUMBER);
        }
        resultList.clear();
    }


    @SuppressLint("MissingPermission")
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e(ACCOUNT_SERVICE, e.toString());
        }
    }
    private void setData() {
        int a = 0;

        for (String string : resultList) {
            if (string.length() == 11) {
                Date dt = new Date();
                a++;
                //phonenum.setText(string);
                OcrResult ocrResult = new OcrResult();
                ocrResult.setPhone_num(string);
                ocrResult.setCall_time(dt);
                ocrResult.save();
                //刷新页面，更新数据
                this.initrecycler();
                Toast.makeText(getApplicationContext(), "存入"+a+"条数据", Toast.LENGTH_SHORT).show();
                continue;
            }
        }

        if(resultList.size()==0){
            Log.e(MainActivity.ACTIVITY_SERVICE, "没有识别到数字");
            Toast.makeText(getApplicationContext(), "没有识别到数字！", Toast.LENGTH_SHORT).show();
        }else if(a==0){
            Toast.makeText(getApplicationContext(), "没有识别到电话号码", Toast.LENGTH_SHORT).show();
        }
        resultList.clear();
    }

    //json字符串转对象
    public static   List<JSONObject> getJsonObjects(String JsonString)
    {
        JsonString = getJsonStrFromNetData(JsonString);
        ArrayList<JSONObject> array = new ArrayList<JSONObject>();
        try {
            JSONArray entries = new JSONArray(JsonString);
            for (int i = 0; i < entries.length(); i++) {
                JSONObject jsObject = entries.getJSONObject(i);
                if(jsObject != null)
                {
                    array.add(jsObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(this).release();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //右上角才菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //滑出菜单
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}



