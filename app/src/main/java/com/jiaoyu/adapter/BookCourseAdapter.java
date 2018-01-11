package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.GlideUtil;

import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 书城-直播列表的适配器
 */

public class BookCourseAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    private Context context;
    private String type; //course课程 search搜索

    public BookCourseAdapter(int layoutResId, Context context, @Nullable List<String> data,String string) {
        super(layoutResId, data);
        this.context = context;
        this.type = string;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView playNum = helper.getView(R.id.num);
        TextView price = helper.getView(R.id.price);
        if(type.equals("course")){ //课程
           playNum.setVisibility(View.GONE);
           price.setVisibility(View.GONE);
        }else{//课程搜索
           playNum.setVisibility(View.VISIBLE);
           price.setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.title,item);
        ImageView courseImg = helper.getView(R.id.courseImg);
        GlideUtil.loadImage(context,
                "http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468754570856573.jpg",courseImg);

    }
}
