package com.cv.sddn.takeassistant.Adaper;

import com.cv.sddn.takeassistant.Activity.MainActivity;
import com.cv.sddn.takeassistant.bean.OcrResult;
import com.cv.sddn.takeassistant.R;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sddn on 2018/11/28.
 * OcrAdater适配器
 *
 */

public class OcrAdater extends RecyclerView.Adapter<OcrAdater.MyViewHolder> {

    private MainActivity context;
    private List<OcrResult> ocrResultlist;
    private int CAMERA_REQUESTCODE = 120;

    //继承RecyclerView.ViewHolder抽象类的自定义ViewHolder(内部类)
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView timeView;
        TextView numView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            timeView = itemView.findViewById(R.id.timeView);
            numView = itemView.findViewById(R.id.numView);
        }

    }

    // 构造函数
    public OcrAdater(List<OcrResult> ocrResultlist, MainActivity context) {
        this.ocrResultlist = ocrResultlist;
        this.context = context;
    }

    //创建ViewHolder，
    //③ 在Adapter中实现3个方法
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化得到Item布局文件的View对象
        //View v = View.inflate(context, R.layout.ocr_item, null);
        //LayoutInflater.from指定写法
        
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ocr_item, parent, false);
        //返回MyViewHolder的对象
        return new MyViewHolder(v);

    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (ocrResultlist.size()==0){
            Log.e(MainActivity.ACTIVITY_SERVICE, "获取数字:" + ocrResultlist.size());
        }
        String num = String.valueOf(position + 1);
        final OcrResult ocrResult = ocrResultlist.get(position);
        Log.e(MainActivity.ACTIVITY_SERVICE, "获取数字:" + ocrResult);
        holder.timeView.setText(new SimpleDateFormat("MM-dd  HH:mm:ss").format(ocrResult.getCall_time()));
        holder.textView.setText(ocrResult.getPhone_num());
        holder.numView.setText(num);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
                Log.e(MainActivity.ACTIVITY_SERVICE, "获取数字:");
                callPhone(ocrResult.getPhone_num());
            }

        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] items = new String[]{"删除"};
                // 创建对话框构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 设置参数
                builder.setTitle("提示")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = ocrResultlist.get(position).getId();
                                deleteData(id);
                                removeItem(position);
                                Log.e(MainActivity.ACTIVITY_SERVICE, "删除成功:" + position);
                            }
                        });
                builder.create().show();
                return false;
            }
        });
    }

    public void deleteData(int id) {
        LitePal.deleteAll(OcrResult.class, "id = ?", String.valueOf(id));
        Log.e(MainActivity.ACTIVITY_SERVICE, "deleteData: " + id);
    }

    public void removeItem(int pos) {
        ocrResultlist.remove(pos);
        notifyItemRemoved(pos);
        if (pos != ocrResultlist.size()) {
            notifyItemRangeChanged(pos, ocrResultlist.size() - pos);
        }
    }


    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }
    //返回Item的数量
    @Override
    public int getItemCount() {
        return ocrResultlist.size();
    }
}
