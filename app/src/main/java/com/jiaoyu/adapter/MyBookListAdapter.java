package com.jiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiaoyu.shiyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/5.
 * 图书展示的适配器
 */

public class MyBookListAdapter extends BaseAdapter{

    private Context context;
    private List<String> datalist = new ArrayList<>();

    public MyBookListAdapter(Context context, List<String> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_my_book_list, null);
            vholder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(vholder);
        }else{
            vholder = (ViewHolder) view.getTag();
        }
        vholder.title.setText(datalist.get(i));
        return view;
    }
    class ViewHolder{
        TextView title;
    }
}
