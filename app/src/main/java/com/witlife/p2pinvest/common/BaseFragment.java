package com.witlife.p2pinvest.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.ui.LoadingPage;

import butterknife.ButterKnife;

/**
 * Created by bruce on 24/08/2017.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        loadingPage = new LoadingPage(container.getContext()) {
            @Override
            public int layoutId() {
                return getLayoutId();
            }

            @Override
            protected void onSuccess(LoadingPage.ResultState resultState, View view_success) {
                ButterKnife.bind(BaseFragment.this, view_success);
                initTitle();
                initData(resultState.getContent());
            }

            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            protected String url() {
                return getUrl();
            }
        };

        return loadingPage;
    }

    protected abstract RequestParams getParams();

    protected abstract String getUrl();

    public abstract int getLayoutId();

    public abstract void initTitle();

    public abstract void initData(String content);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showData(){
        loadingPage.showData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showData();
    }
}
