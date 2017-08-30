package com.witlife.p2pinvest.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.UserBean;
import com.witlife.p2pinvest.common.AppNetConfig;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.util.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_login_number)
    TextView tvLoginNumber;
    @Bind(R.id.et_login_number)
    EditText etLoginNumber;
    @Bind(R.id.rl_login)
    RelativeLayout rlLogin;
    @Bind(R.id.tv_login_pwd)
    TextView tvLoginPwd;
    @Bind(R.id.et_login_pwd)
    EditText etLoginPwd;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    public void initData() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAll();
                startNewActivity(MainActivity.class, null);
            }
        });
    }

    private void login(View view) {
        String number = etLoginNumber.getText().toString().trim();
        String password = etLoginPwd.getText().toString().trim();
        
        if(!TextUtils.isEmpty(number) && !TextUtils.isEmpty(password)){
            String url = AppNetConfig.LOGIN;
            RequestParams params = new RequestParams();
            params.put("phone", number);
            params.put("password", MD5Utils.MD5(password));

            client.post(this, url, params, new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String content) {
                    try {
                        JSONObject object = new JSONObject(content);
                        boolean success = object.getBoolean("success");

                        if(success){
                            String data = object.getString("data");
                            UserBean user = new Gson().fromJson(data, UserBean.class);

                            // save user info
                            saveUser(user);

                            //reload ui
                            removeAll();
                            startNewActivity(MainActivity.class, null);
                        } else {
                            Toast.makeText(LoginActivity.this, "Username is not exist or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    Toast.makeText(LoginActivity.this, "Network is down", Toast.LENGTH_SHORT).show();
                    
                }
            });
        } else {
            Toast.makeText(this, "Username or password can't be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("User Login");
        ivSetting.setVisibility(View.INVISIBLE);
    }
}
