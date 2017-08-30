package com.witlife.p2pinvest.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.MainBean;
import com.witlife.p2pinvest.common.AppNetConfig;
import com.witlife.p2pinvest.common.BaseFragment;
import com.witlife.p2pinvest.ui.RoundProgress;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bruce on 22/08/2017.
 */

public class MainFragment extends BaseFragment {
    @Bind(R.id.slider)
    SliderLayout slider;
    @Bind(R.id.indicator)
    PagerIndicator indicator;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_rate)
    TextView tvRate;
    @Bind(R.id.btn_join)
    Button btnJoin;
    @Bind(R.id.roundProgress)
    RoundProgress roundProgress;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_text_title)
    TextView tvTextTitle;

    private MainBean bean;
    private int currentProgress;

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    public void initTitle() {
        tvTitle.setText("Main");
        ivSetting.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
    }

    @Override
    public void initData(String content) {
        Gson gson = new Gson();
        bean = gson.fromJson(content, MainBean.class);
        initSlider();
        initMainText();
    }

    private void initMainText() {
        tvTextTitle.setText(bean.getProInfo().getName());
        tvRate.setText(bean.getProInfo().getYearRate() + "%");

        currentProgress = Integer.parseInt(bean.getProInfo().getProgress());

        new Thread(new Runnable() {
            @Override
            public void run() {
                roundProgress.setMax(100);
                for (int i = 0; i < currentProgress; i++) {
                    roundProgress.setProgress(i + 1);
                    SystemClock.sleep(40);
                    roundProgress.postInvalidate();
                }
            }
        }).start();
    }

    private void initSlider() {
        MainBean.ImageArrBean imageBanner;
        if (bean != null) {
            for (int i = 0; i < bean.getImageArr().size(); i++) {
                imageBanner = bean.getImageArr().get(i);
                TextSliderView sliderView = new TextSliderView(getContext());
                sliderView.image(imageBanner.getIMAURL());
                sliderView.description(imageBanner.getDESC());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                slider.addSlider(sliderView);
            }
        }
        slider.setDuration(3000);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setPresetTransformer(SliderLayout.Transformer.CubeIn);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //slider.stopAutoCycle();
        ButterKnife.unbind(this);
    }
}
