package com.jiaoyu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/5.
 * 图书展示的适配器
 */

public class MyBookListAdapter extends BaseAdapter{

    private Context context;
    private List<EntityPublic> datalist = new ArrayList<>();

    public MyBookListAdapter(Context context, List<EntityPublic> datalist) {
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
            vholder.book_img = (ImageView) view.findViewById(R.id.book_img);
            vholder.name = (TextView) view.findViewById(R.id.name);
            vholder.price = (TextView) view.findViewById(R.id.price);
            view.setTag(vholder);
        }else{
            vholder = (ViewHolder) view.getTag();
        }
        vholder.title.setText(datalist.get(i).getEbookName());
        vholder.name.setText(datalist.get(i).getAuthor());
        if (!TextUtils.isEmpty(datalist.get(i).getNowPrice())){
            if (!TextUtils.isEmpty(datalist.get(i).getIsfree()+"")){
                if (datalist.get(i).getIsfree() == 1){ //免费
                    vholder.price.setText(R.string.free);
                }else{
                    vholder.price.setText(datalist.get(i).getNowPrice());
                }
            }
        }else{
            vholder.price.setText(R.string.no_tv);
        }
        GlideUtil.loadImage(context, Address.IMAGE_NET+datalist.get(i).getEbookImg(),
                vholder.book_img);
        vholder.title.setText(datalist.get(i).getEbookName());
        return view;
    }
    class ViewHolder{
        TextView title,name,price;
        ImageView book_img;
    }
}
