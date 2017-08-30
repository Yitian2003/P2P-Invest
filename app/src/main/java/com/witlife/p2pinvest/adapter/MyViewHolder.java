package com.witlife.p2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.InvestProductBean;
import com.witlife.p2pinvest.ui.RoundProgress;
import com.witlife.p2pinvest.util.UIUtils;

import butterknife.Bind;

/**
 * Created by bruce on 28/08/2017.
 */

public class MyViewHolder extends ListBaseHolder<InvestProductBean.DataBean> {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_sum)
    TextView tvSum;
    @Bind(R.id.p_minzouzi)
    TextView pMinzouzi;
    @Bind(R.id.tv_rate)
    TextView tvRate;
    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.p_minnum)
    TextView pMinnum;
    @Bind(R.id.rp_progress)
    RoundProgress rpProgress;

    public MyViewHolder(Context context) {
        super(context);
    }

    @Override
    public View initView(Context context) {
        return View.inflate(context, R.layout.item_invest_produt, null);
    }

    @Override
    protected void loadData() {
        InvestProductBean.DataBean data = this.getData();

        tvTitle.setText(data.getName());
        tvSum.setText(data.getMoney());
        tvRate.setText(data.getYearRate());
        tvDuration.setText(data.getSuodingDays());
        rpProgress.setProgress(Integer.parseInt(data.getProgress()));
        rpProgress.setMax(100);
    }
}
