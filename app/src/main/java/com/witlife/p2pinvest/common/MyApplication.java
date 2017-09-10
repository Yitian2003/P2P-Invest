package com.witlife.p2pinvest.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by bruce on 23/08/2017.
 */

public class MyApplication extends Application {

    public static Context context;
    public static Handler handler;
    public static Thread mainThread;
    public static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();

        //CrashHandler.getInstance().init();

    }
}
