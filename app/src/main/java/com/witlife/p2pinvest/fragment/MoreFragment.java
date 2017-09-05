package com.witlife.p2pinvest.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.activity.GestureEditActivity;
import com.witlife.p2pinvest.activity.UserRegisterActivity;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.common.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bruce on 22/08/2017.
 */

public class MoreFragment extends BaseFragment {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.btn_password)
    ToggleButton btnPassword;
    @Bind(R.id.tv_reset)
    TextView tvReset;
    @Bind(R.id.ll_contact)
    LinearLayout llContact;
    @Bind(R.id.tv_feedback)
    TextView tvFeedback;
    @Bind(R.id.tv_share)
    TextView tvShare;
    @Bind(R.id.tv_about)
    TextView tvAbout;

    private SharedPreferences sp;
    private boolean isFirstSet = true;

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void initTitle() {
        tvTitle.setText("More");
        ivSetting.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
    }

    @Override
    public void initData(String content) {
        sp = getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        // register new user
        doRegist();
        // read the status of the toggle button
        getGesturePassword();
        // setup lock
        setGesturePassword();
    }

    private void setGesturePassword() {
        btnPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    String inputCode = sp.getString("inputCode", "");

                    if (TextUtils.isEmpty(inputCode)){ // first time set up the lock

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setCancelable(false)
                                .setMessage("Do you want to set gesture lock now?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getActivity(), "Gesture Lock On", Toast.LENGTH_SHORT).show();
                                        sp.edit().putBoolean("isChecked", true).commit();
                                        ((BaseActivity)getActivity()).startNewActivity(GestureEditActivity.class, null);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getActivity(), "Gesture Lock Off", Toast.LENGTH_SHORT).show();
                                        sp.edit().putBoolean("isChecked", false).commit();
                                        btnPassword.setChecked(false);
                                    }
                                }).create()
                                .show();

                    } else {//
                        Toast.makeText(getActivity(), "Gesture Lock On", Toast.LENGTH_SHORT).show();
                        sp.edit().putBoolean("isChecked", true).commit();
                    }


                } else {
                    Toast.makeText(getActivity(), "Gesture Lock Off", Toast.LENGTH_SHORT).show();
                    sp.edit().putBoolean("isChecked", false).commit();
                }
            }
        });
    }

    private void doRegist() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)getActivity()).startNewActivity(UserRegisterActivity.class, null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void getGesturePassword() {
        boolean isChecked = sp.getBoolean("isChecked", false);
        btnPassword.setChecked(isChecked);
    }
}
