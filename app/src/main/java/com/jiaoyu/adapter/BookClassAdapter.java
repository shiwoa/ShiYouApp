package com.jiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.view.XCRoundImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bishuang on 2018/1/5.
 * 图书展示的适配器
 */

public class BookClassAdapter extends BaseAdapter{

    private Context context;
    private List<EntityPublic> datalist = new ArrayList<>();
    int [] scolor = new int[]{R.color.color_320,R.color.google_blue,R.color.color_57,R.color.google_green,
                            R.color.google_red,R.color.google_yellow,R.color.colorPrimary,R.color.colorPrimaryDark,
            R.color.colorSubtitleText,R.color.color_52,R.color.color_99};

    public BookClassAdapter(Context context, List<EntityPublic> datalist) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_book_class, null);
            vholder.title = (TextView) view.findViewById(R.id.title);
            vholder.icon = (CircleImageView) view.findViewById(R.id.icon);
            view.setTag(vholder);
        }else{
            vholder = (ViewHolder) view.getTag();
        }
        int index = (int) Math.floor(Math.random()*scolor.length);
        vholder.title.setText(datalist.get(i).getSubjectName());
        vholder.icon.setImageResource(scolor[index]);
        return view;
    }
    class ViewHolder{
        TextView title;
        CircleImageView icon;
    }
}
