package com.witlife.p2pinvest.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.util.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WithdrawActivity extends BaseActivity {


    @Bind(R.id.et_input_money)
    EditText etInputMoney;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.btn_withdraw)
    Button btnWithdraw;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.INVISIBLE);
        tvTitle.setText("Withdraw");
    }

    @Override
    public void initData() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentActivity();
            }
        });

        btnWithdraw.setClickable(false);

        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String money = etInputMoney.getText().toString().trim();

                if(TextUtils.isEmpty(money)){
                    btnWithdraw.setClickable(false);
                    btnWithdraw.setBackgroundResource(R.drawable.btn_02);
                } else {
                    btnWithdraw.setClickable(true);
                    btnWithdraw.setBackgroundResource(R.drawable.btn_01);
                }
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WithdrawActivity.this, "Money will be transfered to your account in 24 hours", Toast.LENGTH_SHORT).show();

                UIUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeCurrentActivity();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw;
    }

}
