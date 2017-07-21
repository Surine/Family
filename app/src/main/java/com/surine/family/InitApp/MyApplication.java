package com.surine.family.InitApp;

import android.app.Application;

import com.surine.family.Utils.AgoraManager;

import org.litepal.LitePalApplication;

/**
 * Created by surine on 2017/5/30.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(getApplicationContext());
        AgoraManager.getInstance().init(getApplicationContext());
    }
}


