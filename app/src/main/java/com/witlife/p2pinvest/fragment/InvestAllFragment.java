package com.witlife.p2pinvest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.adapter.InvestProductListAdapter;
import com.witlife.p2pinvest.bean.InvestProductBean;
import com.witlife.p2pinvest.common.AppNetConfig;
import com.witlife.p2pinvest.common.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bruce on 25/08/2017.
 */

public class InvestAllFragment extends BaseFragment {

    @Bind(R.id.lv_product_list)
    ListView lvProductList;
    @Bind(R.id.tv_running_text)
    TextView tvRunningText;

    private InvestProductListAdapter adapter;

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.PRODUCT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invest_all;
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initData(String content) {

        tvRunningText.setFocusable(true);
        tvRunningText.setFocusableInTouchMode(true);
        tvRunningText.requestFocus();

        InvestProductBean productBean = new Gson().fromJson(content, InvestProductBean.class);

        adapter = new InvestProductListAdapter(getContext(), productBean.getData());
        lvProductList.setAdapter(adapter);
    }
}
