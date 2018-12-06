/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cv.sddn.takeassistant;

import android.content.Context;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.cv.sddn.takeassistant.Activity.MainActivity;

import java.io.File;

/**
 * Created by ruanshimin on 2017/4/20.
 */

public class RecognizeService {

    public interface ServiceListener {
        public void onResult(String result);
    }

    public static void recNumbers(Context ctx, String filePath, final ServiceListener listener) {
        OcrRequestParams param = new OcrRequestParams();
        param.setImageFile(new File(filePath));
        OCR.getInstance(ctx).recognizeNumbers(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
//                StringBuilder sb = new StringBuilder();
//                for (Word word : result.getWordList()) {
//                    sb.append(word.getWords());
//                    resultList.add(word.getWords());
//                    sb.append("\n");
//                }
//                txtResult.setText(sb);
//                setData();

//                Log.e(MainActivity.ACTIVITY_SERVICE, "11111: " +  result.getClass());
//                Log.e(MainActivity.ACTIVITY_SERVICE, "22222: " +  result);
                  Log.e(MainActivity.ACTIVITY_SERVICE, "33333: " +  result.getJsonRes());
//                Log.e(MainActivity.ACTIVITY_SERVICE, "44444: " +  result.toString());
//                Log.e(MainActivity.ACTIVITY_SERVICE, "55555: " +  result.getLogId());
                  listener.onResult(result.getJsonRes());

            }
            @Override
            public void onError(OCRError error) {
                listener.onResult(error.getMessage());
            }
        });
    }


}
