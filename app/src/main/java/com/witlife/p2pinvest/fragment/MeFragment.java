package com.witlife.p2pinvest.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.activity.BarCharActivity;
import com.witlife.p2pinvest.activity.DepositActivity;
import com.witlife.p2pinvest.activity.GestureVerifyActivity;
import com.witlife.p2pinvest.activity.LineCharActivity;
import com.witlife.p2pinvest.activity.LoginActivity;
import com.witlife.p2pinvest.activity.MainActivity;
import com.witlife.p2pinvest.activity.PieCharActivity;
import com.witlife.p2pinvest.activity.UserInfoActivity;
import com.witlife.p2pinvest.activity.WithdrawActivity;
import com.witlife.p2pinvest.bean.UserBean;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.common.BaseFragment;
import com.witlife.p2pinvest.util.BitmapUtils;
import com.witlife.p2pinvest.util.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bruce on 22/08/2017.
 */

public class MeFragment extends BaseFragment {

    public static final String USER_INFO = "user_info";
    public static final int VERIFY_CODE = 10002;
    @Bind(R.id.iv_avator)
    ImageView ivAvator;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.btn_deposit)
    Button btnDeposit;
    @Bind(R.id.btn_withdraw)
    Button btnWithdraw;
    @Bind(R.id.rl_invest)
    RelativeLayout rlInvest;
    @Bind(R.id.rl_invest_direct)
    RelativeLayout rlInvestDirect;
    @Bind(R.id.rl_asset)
    RelativeLayout rlAsset;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    private UserBean user;

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
        return R.layout.fragment_me;
    }

    @Override
    public void initTitle() {
        tvTitle.setText("My Asset");
        ivSetting.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.GONE);
    }

    @Override
    public void initData(String content) {
        isLogin();

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(USER_INFO, user);
                ((BaseActivity) getActivity()).startNewActivity(UserInfoActivity.class, bundle);
            }
        });

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).startNewActivity(DepositActivity.class, null);
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).startNewActivity(WithdrawActivity.class, null);
            }
        });

        rlInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).startNewActivity(LineCharActivity.class, null);
            }
        });

        rlInvestDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).startNewActivity(BarCharActivity.class, null);
            }
        });

        rlAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).startNewActivity(PieCharActivity.class, null);
            }
        });
    }

    private void isLogin() {
        SharedPreferences sp = this.getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String name = sp.getString("name", "");

        if (TextUtils.isEmpty(name)) {
            doLogin(name);
        } else {
            doUser();
        }

    }

    private void doUser() {
        // check whether gesture lock is on, if is on, input gesture
        SharedPreferences sp = getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        boolean isChecked = sp.getBoolean("isChecked", false);
        if (isChecked) {
            Intent intent = new Intent(getActivity(), GestureVerifyActivity.class);
            startActivityForResult(intent, VERIFY_CODE);
        } else {
            displayUserInfo();
        }
    }


    private void doLogin(String name) {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Notice")
                .setMessage("You haven's login.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((BaseActivity) getActivity()).startNewActivity(LoginActivity.class, null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        readImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isSuccessVerified;

        if (requestCode == VERIFY_CODE) {
            isSuccessVerified = data.getBooleanExtra("verify_result", false);

            if (isSuccessVerified) {
                displayUserInfo();
            } else {
                ((BaseActivity) getActivity()).removeAll();
                ((BaseActivity) getActivity()).startNewActivity(MainActivity.class, null);
            }
        }
    }

    private boolean readImage() {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path1: storage/sdcard/Android/data/package name/files
            filesDir = this.getActivity().getExternalFilesDir("");
        } else {//internal storage
            //path: data/data/package name/files
            filesDir = this.getActivity().getFilesDir();
        }

        File file = new File(filesDir, "icon.png");

        if (file.exists()) {
            // file --> memory
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ivAvator.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void displayUserInfo(){
        user = ((BaseActivity) this.getActivity()).readUser();

        tvName.setText(user.getName());

        if (readImage()) return;// if there is image in local, do not request network image/

        Picasso.with(getActivity())
                .load(user.getImageurl()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {

                //rescale bitmap
                Bitmap bitmap = BitmapUtils.zoom(source, UIUtils.dp2px(70), UIUtils.dp2px(70));

                bitmap = BitmapUtils.circleBitmap(source);
                source.recycle();
                return bitmap;

                
            }

            @Override
            public String key() {
                return "";
            }
        })
                .into(ivAvator);
    }
}
