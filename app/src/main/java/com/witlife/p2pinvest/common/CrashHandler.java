package com.witlife.p2pinvest.common;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.witlife.p2pinvest.util.UIUtils;

/**
 * Created by bruce on 23/08/2017.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler exceptionHandler;

    private static CrashHandler crashHandler = null;

    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        if(crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init(){
        exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(UIUtils.getContext(), "Uncommon Exception", Toast.LENGTH_SHORT).show();

                Looper.loop();
            }
        }).start();

        collectionException(throwable);

        try {
            Thread.sleep(2000);
            ActivityManager.getInstance().removeCurrent();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void collectionException(Throwable throwable){
        final String exMessage = throwable.getMessage();
        final String message = Build.DEVICE + ";" + Build.MODEL + ";" +Build.PRODUCT + ";" + Build.VERSION.SDK_INT;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Exception", exMessage);
                Log.e("Exception", message);
            }
        }).start();
    }
}
