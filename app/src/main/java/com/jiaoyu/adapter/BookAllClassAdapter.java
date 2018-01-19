package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.GlideUtil;

import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 书城-专区模块-查看更多的适配器
 */

public class BookAllClassAdapter extends BaseQuickAdapter<EntityPublic,BaseViewHolder>{

    private Context context;
    private int selectpot = -1; //选中id

    public BookAllClassAdapter(int layoutResId, Context context, @Nullable List<EntityPublic> data) {
        super(layoutResId, data);
        this.context = context;
    }

    public void getSelect(int position){
        this.selectpot = position;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityPublic item) {
        TextView title = helper.getView(R.id.title);
        title.setText(item.getSubjectName());
        LinearLayout titlebg = helper.getView(R.id.title_lin);
        if (selectpot == helper.getAdapterPosition()){
            titlebg.setBackgroundResource(R.drawable.bg_main);
            title.setTextColor(context.getResources().getColor(R.color.White));
        }else{
            titlebg.setBackgroundResource(R.drawable.line_gray);
            title.setTextColor(context.getResources().getColor(R.color.color_bb));
        }
    }
}
