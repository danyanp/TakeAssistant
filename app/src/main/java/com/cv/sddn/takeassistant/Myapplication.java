package com.cv.sddn.takeassistant;

/**
 * Created by sddn on 2018/11/29.
 */

import android.app.Application;

import org.litepal.LitePal;

public class Myapplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}