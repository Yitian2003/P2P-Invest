package com.witlife.p2pinvest.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.UserBean;
import com.witlife.p2pinvest.common.ActivityManager;

import butterknife.ButterKnife;

/**
 * Created by bruce on 29/08/2017.
 */

public abstract class BaseActivity extends FragmentActivity{

    public AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        ActivityManager.getInstance().add(this);

        initTitle();

        initData();
    }

    protected abstract void initTitle();

    public abstract void initData();

    public abstract int getLayoutId();

    public void startNewActivity(Class Activity, Bundle bundle){
        Intent intent = new Intent(this, Activity);

        if(bundle != null && bundle.size() > 0){
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    public void removeCurrentActivity(Activity activity){
        ActivityManager.getInstance().removeCurrent();
    }

    public void removeAll(){
        ActivityManager.getInstance().removeAll();
    }

    public void saveUser(UserBean user) {
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", user.getName());
        editor.putString("imageUrl", user.getImageurl());
        editor.putString("phone", user.getPhone());
        editor.putBoolean("iscredit", user.isCredit());

        editor.commit();
    }

    public UserBean readUser(){
        UserBean user = new UserBean();
        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user.setName(sp.getString("name", ""));
        user.setImageurl(sp.getString("imageUrl", ""));
        user.setCredit(sp.getBoolean("iscredit", false));
        user.setPhone(sp.getString("phone", ""));
        return user;
    }
}
