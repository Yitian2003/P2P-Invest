package com.witlife.p2pinvest.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by bruce on 23/08/2017.
 */

public class ActivityManager {

    private static ActivityManager activityManager = new ActivityManager();
    private ActivityManager(){

    }

    public static ActivityManager getInstance(){
        return activityManager;
    }

    /*private static ActivityManager activityManager = null;

    private ActivityManager(){

    }

    public static ActivityManager getInstance(){
        if(activityManager == null){
            activityManager = new ActivityManager();
        }
        return activityManager;
    }*/

    private Stack<Activity> activityStack = new Stack<>();

    public void add(Activity activity){
        if(activity != null){
            activityStack.add(activity);
        }
    }

    public void remove(Activity activity){

        if(activity != null){
            for(int i = activityStack.size() - 1; i > 0; i--){
                Activity currentActivity = activityStack.get(i);
                if(currentActivity.getClass().equals(activity.getClass())){
                    currentActivity.finish();
                    activityStack.remove(i);
                }
            }
        }
    }

    public void removeCurrent(){
        Activity activity = activityStack.get(activityStack.size() - 1);
        activity.finish();
        activityStack.remove(activityStack.size() - 1);
    }

    public void removeAll(){
        for(int i = activityStack.size() - 1; i > 0; i--){
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public int size(){
        return activityStack.size();
    }
}
