package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.shiyou.R;

import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 我的已文章笔记的适配器
 */

public class MyCommunityAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    private Context context;

    public MyCommunityAdapter(int layoutResId, Context context, @Nullable List<String> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.title,item);
    }
}
