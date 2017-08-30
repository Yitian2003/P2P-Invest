package com.witlife.p2pinvest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
