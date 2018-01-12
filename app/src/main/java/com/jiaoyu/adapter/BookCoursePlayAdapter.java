package com.jiaoyu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiaoyu.shiyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 图书课程详情的适配器
 */

public class BookCoursePlayAdapter extends BaseAdapter{

    private Context context;
    private List<String> dataList = new ArrayList<>();

    public BookCoursePlayAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vholder = null;
        if (view == null){
            vholder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_course_details_content, null);
            vholder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(vholder);
        }else{
            vholder = (ViewHolder) view.getTag();
        }
        vholder.title.setText(dataList.get(i));
        return view;
    }
    class ViewHolder{
        TextView title;
    }

}
