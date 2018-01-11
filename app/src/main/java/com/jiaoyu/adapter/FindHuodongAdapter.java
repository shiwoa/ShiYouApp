package com.jiaoyu.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by bishuang on 2018/1/9.
 * 发现-活动的adapter
 */

public class FindHuodongAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public FindHuodongAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
