package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.shiyou.R;

import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 图书课程详情的适配器
 */

public class BookCourseDetailsAdapter extends BaseQuickAdapter<EntityPublic,BaseViewHolder>{

    private Context context;

    public BookCourseDetailsAdapter(int layoutResId, Context context, @Nullable List<EntityPublic> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityPublic item) {
        helper.setText(R.id.title,item.getName());
        if (item.getTimeStr() != null){
            helper.setText(R.id.time,"时长："+item.getTimeStr());
        }else{
            helper.setText(R.id.time,"暂无返回");
        }
    }
}
