package com.witlife.p2pinvest.util;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.witlife.p2pinvest.common.MyApplication;

/**
 * Created by bruce on 23/08/2017.
 */

public class UIUtils {

    public static Context getContext(){
        return MyApplication.context;
    }

    public static Handler getHandler(){
        return MyApplication.handler;
    }

    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    public static View getView(int viewId){
        View view = View.inflate(getContext(), viewId, null);
        return view;
    }

    public static String[] getStringArray(int stringId){
        String[] strings = getContext().getResources().getStringArray(stringId);
        return strings;
    }

    public static int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int px2dp(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static void runOnUiThread(Runnable runnable) {

        if(isInMainThread()){
            runnable.run();
        } else {
            UIUtils.getHandler().post(runnable);
        }
    }

    private static boolean isInMainThread() {
        int currentTid = android.os.Process.myTid();
        return MyApplication.mainThreadId == currentTid;
    }
}
