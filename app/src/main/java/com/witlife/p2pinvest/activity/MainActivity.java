package com.witlife.p2pinvest.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.fragment.InvestFragment;
import com.witlife.p2pinvest.fragment.MainFragment;
import com.witlife.p2pinvest.fragment.MeFragment;
import com.witlife.p2pinvest.fragment.MoreFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final int TWICE_BACK = 1;
    public static final String ME_FRAGMENT = "me_fragment";

    @Bind(R.id.framelayout)
    FrameLayout framelayout;
    @Bind(R.id.btn_main)
    RadioButton btnMain;
    @Bind(R.id.btn_invest)
    RadioButton btnInvest;
    @Bind(R.id.btn_me)
    RadioButton btnMe;
    @Bind(R.id.btn_more)
    RadioButton btnMore;

    private FragmentTransaction transaction;
    private MainFragment mainFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;
    private InvestFragment investFragment;

    private boolean isFirstClick = true;

    @Override
    protected void initTitle() {
    }

    @Override
    public void initData() {
        btnMain.performClick();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TWICE_BACK){
                isFirstClick = true;
            }
        }
    };

    @OnClick({R.id.btn_main, R.id.btn_invest, R.id.btn_me, R.id.btn_more})
    public void showTab(View view) {
        FragmentManager manager = this.getSupportFragmentManager();
        transaction = manager.beginTransaction();

        resetButton();

        switch (view.getId()) {

            case R.id.btn_main:
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.framelayout, mainFragment);
                }
                transaction.show(mainFragment);
                break;

            case R.id.btn_invest:
                if (investFragment == null) {
                    investFragment = new InvestFragment();
                    transaction.add(R.id.framelayout, investFragment);
                }
                transaction.show(investFragment);
                break;

            case R.id.btn_me:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.framelayout, meFragment, ME_FRAGMENT);
                }
                transaction.show(meFragment);

                break;

            case R.id.btn_more:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.framelayout, moreFragment);
                }
                transaction.show(moreFragment);
                break;
        }
        transaction.commit();
    }

    private void resetButton() {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (investFragment != null) {
            transaction.hide(investFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && isFirstClick){
            isFirstClick = false;
            Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(TWICE_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
