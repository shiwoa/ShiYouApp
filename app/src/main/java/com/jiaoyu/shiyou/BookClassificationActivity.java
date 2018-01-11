package com.jiaoyu.shiyou;

import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.BookClassAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/10.
 * 书城-本社图书的类
 */

public class BookClassificationActivity extends BaseActivity{

    private TextView title; //标题
    private LinearLayout back; //返回
    private GridView classGrud;
    private List<String> datalist = new ArrayList<>();
    private BookClassAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_classification;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.classification);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        classGrud = (GridView) findViewById(R.id.book_class_grid);

        for (int i = 0; i < 12; i++){
            datalist.add("石油经营"+i);
        }
        adapter = new BookClassAdapter(BookClassificationActivity.this,datalist);
        classGrud.setAdapter(adapter);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> BookClassificationActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
