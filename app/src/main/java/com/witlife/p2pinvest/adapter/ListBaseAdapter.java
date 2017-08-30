package com.witlife.p2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by bruce on 28/08/2017.
 */

public abstract class ListBaseAdapter<T> extends BaseAdapter {

    List<T> list;
    private Context context;

    public ListBaseAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ListBaseHolder<T> holder;

        if( view == null){
            holder = getHolder();
        } else {
            holder = (ListBaseHolder<T>) view.getTag();
        }

        T t = list.get(i);
        holder.setData(t);

        return holder.getItemView();
    }

    protected abstract ListBaseHolder getHolder();
}
