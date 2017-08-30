package com.witlife.p2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.InvestProductBean;
import com.witlife.p2pinvest.ui.RoundProgress;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bruce on 25/08/2017.
 */

public class InvestProductListAdapter extends ListBaseAdapter<InvestProductBean.DataBean> {

    private Context context;

    public InvestProductListAdapter(Context context, List<InvestProductBean.DataBean> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    protected ListBaseHolder getHolder() {
        return new MyViewHolder(context);
    }

/*    private List<InvestProductBean.DataBean> products;
    private Context context;

    public InvestProductListAdapter(Context context, List<InvestProductBean.DataBean> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public InvestProductBean.DataBean getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            view = View.inflate(context, R.layout.item_invest_produt, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        InvestProductBean.DataBean item = products.get(i);

        holder.tvTitle.setText(item.getName());
        holder.tvSum.setText(item.getMoney());
        holder.tvRate.setText(item.getYearRate());
        holder.tvDuration.setText(item.getSuodingDays());
        holder.rpProgress.setProgress(Integer.parseInt(item.getProgress()));
        holder.rpProgress.setMax(100);

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_sum)
        TextView tvSum;
        @Bind(R.id.tv_rate)
        TextView tvRate;
        @Bind(R.id.tv_duration)
        TextView tvDuration;
        @Bind(R.id.rp_progress)
        RoundProgress rpProgress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }*/
}
