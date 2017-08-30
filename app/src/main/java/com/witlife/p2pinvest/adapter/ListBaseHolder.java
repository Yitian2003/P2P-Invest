package com.witlife.p2pinvest.adapter;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by bruce on 28/08/2017.
 */

public abstract class ListBaseHolder<T> {

    private View itemView;
    private T data;
    private Context context;

    public ListBaseHolder(Context context) {
        this.context = context;
        itemView = initView(context);
        itemView.setTag(this);
        ButterKnife.bind(this, itemView);
    }

    public void setData(T t) {
        this.data = t;
        loadData();
    }

    public T getData(){
        return data;
    }

    public View getItemView(){
        return itemView;
    }

    public abstract View initView(Context context);

    protected abstract void loadData();
}
