package com.witlife.p2pinvest.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.common.AppNetConfig;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.util.MD5Utils;
import com.witlife.p2pinvest.util.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserRegisterActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.et_register_number)
    EditText etRegisterNumber;
    @Bind(R.id.et_register_name)
    EditText etRegisterName;
    @Bind(R.id.et_register_pwd)
    EditText etRegisterPwd;
    @Bind(R.id.et_register_pwdagain)
    EditText etRegisterPwdagain;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.INVISIBLE);
        tvTitle.setText("Register");
    }

    @Override
    public void initData() {
        // register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etRegisterName.getText().toString().trim();
                String phone = etRegisterNumber.getText().toString().trim();
                String pwd = etRegisterPwd.getText().toString().trim();
                String pwdAgain = etRegisterPwdagain.getText().toString().trim();
                // check input
                if (checkRegisterInput(name, phone, pwd, pwdAgain)) {
                    // send info to server
                    sentInfo(name, phone, pwd);
                }

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) UserRegisterActivity.this).removeCurrentActivity();
            }
        });
    }

    private void sentInfo(String name, String phone, String pwd) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("password", MD5Utils.MD5(pwd));
        params.put("phone", phone);
        client.post(AppNetConfig.USERREGISTER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(UserRegisterActivity.this, "Network Failure", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(String content) {
                boolean isExist = false;
                try {
                    JSONObject object = new JSONObject(content);
                    isExist = object.getBoolean("isExist");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isExist) { // registered
                    Toast.makeText(UserRegisterActivity.this, "The phone is registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserRegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                }
                ((BaseActivity)UserRegisterActivity.this).removeCurrentActivity();
            }
        });
    }

    private boolean checkRegisterInput(String name, String phone, String pwd, String pwdAgain) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
            Toast.makeText(this, "Please fill in the form", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pwd.equals(pwdAgain)) {
            Toast.makeText(this, "Password are not the same", Toast.LENGTH_SHORT).show();
            etRegisterPwd.setText("");
            etRegisterPwdagain.setText("");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_register;
    }

}
