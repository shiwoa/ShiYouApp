package com.jiaoyu.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.shiyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/9.
 * 图书详情的推荐人的adapter
 */

public class BookRecommendAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    private List<Boolean> isSelectList; //设置选择

    public void setIsSelect(List<Boolean> isSelect){
        this.isSelectList = isSelect;
    }
    public BookRecommendAdapter(int layoutResId, @Nullable List<String> data,List<Boolean> isSelect) {
        super(layoutResId, data);
        this.isSelectList = isSelect;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.commend_name,item);
        ImageView selectImg = helper.getView(R.id.commend_select);
        if (isSelectList.get(helper.getAdapterPosition())){
            selectImg.setBackgroundResource(R.drawable.commend_yes);
        }else {
            selectImg.setBackgroundResource(R.drawable.commend_no);
        }
    }
}
