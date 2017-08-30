package com.witlife.p2pinvest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.witlife.p2pinvest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.tv_welcome_version)
    TextView tvWelcomeVersion;
    @Bind(R.id.rl_welcome)
    RelativeLayout rlWelcome;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        setAnimation();

        handler.sendEmptyMessageDelayed(100, 4000);
    }

    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());

        /*alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
*/
        rlWelcome.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
