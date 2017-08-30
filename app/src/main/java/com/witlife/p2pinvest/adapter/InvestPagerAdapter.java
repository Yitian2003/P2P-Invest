package com.witlife.p2pinvest.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.witlife.p2pinvest.common.BaseFragment;
import com.witlife.p2pinvest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 25/08/2017.
 */

public class InvestPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;
    private Context context;

    public InvestPagerAdapter(Context context, FragmentManager childFragmentManager, ArrayList<BaseFragment> fragments) {
        super(childFragmentManager);
        this.fragments = fragments;
        this.context = context;
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseFragment fragment=null;
        try{
            fragment=(BaseFragment)super.instantiateItem(container,position);
        }catch (Exception e){

        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] titles = context.getResources().getStringArray(R.array.invest_title);
        return titles[position];
    }
}
