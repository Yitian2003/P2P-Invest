package com.witlife.p2pinvest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.adapter.InvestPagerAdapter;
import com.witlife.p2pinvest.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bruce on 22/08/2017.
 */

public class InvestFragment extends BaseFragment {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;

    private Context context;

    private InvestPagerAdapter pagerAdapter;
    private ArrayList<BaseFragment> fragments;
    private List<String> titles;

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
        return R.layout.fragment_invest;
    }

    @Override
    public void initTitle() {
        tvTitle.setText("Invest");
        ivSetting.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
    }

    @Override
    public void initData(String data) {

        context = getContext();

        initViewPager(data);
        initTab();
    }

    private void initTab() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initViewPager(String data) {

        titles = new ArrayList<>();

        fragments = new ArrayList<BaseFragment>();

        InvestAllFragment fragment1 = new InvestAllFragment();
        fragments.add(fragment1);
        InvestRecFragment fragment2 = new InvestRecFragment();
        fragments.add(fragment2);
        InvestHotFragment fragment3 = new InvestHotFragment();
        fragments.add(fragment3);

        pagerAdapter = new InvestPagerAdapter(context, getChildFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
