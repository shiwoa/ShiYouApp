package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.entity.EntityCourse;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.GlideUtil;

import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 书城-直播列表的适配器
 */

public class BookCourseAdapter extends BaseQuickAdapter<EntityCourse,BaseViewHolder>{

    private Context context;

    public BookCourseAdapter(int layoutResId, Context context, @Nullable List<EntityCourse> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityCourse item) {
        helper.setText(R.id.title,item.getName());
        helper.setText(R.id.price,"￥"+item.getCurrentprice());
        helper.setText(R.id.zhang_num,item.getLessionnum()+"节课");
        helper.setText(R.id.num,item.getPageBuycount()+"人已购买");
        helper.setText(R.id.teacher,"专家："+item.getOwnerName());
        helper.setText(R.id.teacher_tab,item.getColTagNames());
        ImageView courseImg = helper.getView(R.id.courseImg);
        GlideUtil.loadImage(context, Address.IMAGE_NET+item.getMobileLogo(),courseImg);

    }
}
