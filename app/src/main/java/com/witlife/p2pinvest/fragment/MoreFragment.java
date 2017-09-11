package com.witlife.p2pinvest.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.activity.GestureEditActivity;
import com.witlife.p2pinvest.activity.UserRegisterActivity;
import com.witlife.p2pinvest.common.AppNetConfig;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.common.BaseFragment;
import com.witlife.p2pinvest.util.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
    @Bind(R.id.tv_phone)
    TextView tvPhone;

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
        // reset lock
        resetGesturePassword();
        // contact service
        contactService();
        // feedback
        sendFeedback();
        // share to friends
        share();
    }

    private void share() {
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("P2P Invest");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("This is awesome P2P Invest app platform");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(getActivity());
    }

    private String department = "none";

    private void sendFeedback() {
        final View dialog = View.inflate(getActivity(), R.layout.dialog_feedback, null);

        final RadioGroup rg = dialog.findViewById(R.id.rg_feedback);
        final EditText feedback = dialog.findViewById(R.id.et_feed_content);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                RadioButton rb = rg.findViewById(id);
                department = rb.getText().toString();
            }
        });

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setView(dialog)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RequestParams params = new RequestParams();
                                params.put("department", department);
                                params.put("content", feedback.getText().toString().trim());

                                AsyncHttpClient client = new AsyncHttpClient();

                                client.post(AppNetConfig.FEEDBACK, params, new AsyncHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(String content) {
                                        Toast.makeText(getActivity(), "Feedback Send Successfully", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Throwable error, String content) {
                                        Toast.makeText(getActivity(), "Feedback Send Fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    private void contactService() {
        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to call customer service?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String phone = tvPhone.getText().toString().trim();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phone));

                                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                                    return;
                                }
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void resetGesturePassword() {
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnPassword.isChecked()) {
                    ((BaseActivity) getActivity()).startNewActivity(GestureEditActivity.class, null);
                } else {
                    Toast.makeText(getActivity(), "Gesture lock is off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setGesturePassword() {
        btnPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    String inputCode = sp.getString("inputCode", "");

                    if (TextUtils.isEmpty(inputCode)) { // first time set up the lock

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setCancelable(false)
                                .setMessage("Do you want to set gesture lock now?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getActivity(), "Gesture Lock On", Toast.LENGTH_SHORT).show();
                                        sp.edit().putBoolean("isChecked", true).commit();
                                        ((BaseActivity) getActivity()).startNewActivity(GestureEditActivity.class, null);
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
                ((BaseActivity) getActivity()).startNewActivity(UserRegisterActivity.class, null);
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
