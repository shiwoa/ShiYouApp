package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.GlideUtil;

import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 书城-直播列表的适配器
 */

public class BookLiveNowAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    private Context context;

    public BookLiveNowAdapter(int layoutResId, Context context, @Nullable List<String> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.title,item);
        ImageView courseImg = helper.getView(R.id.courseImg);
        GlideUtil.loadImage(context,
                "http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468754570856573.jpg",courseImg);
    }
}
