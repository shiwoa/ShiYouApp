package com.jiaoyu.fragment;

import android.os.Bundle;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.MyBookListAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 我的书架的fragment
 */

public class MyBookFragment extends BaseFragment{

   private List<String> datalist = new ArrayList<>();
   private MyBookListAdapter adapter;
   private NoScrollGridView dataGrid;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_my_book;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
       dataGrid = findViewById(R.id.my_book_grid);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (datalist != null){
            datalist.clear();
        }
        for (int i = 0; i < 8; i++){
            datalist.add("书架测试标题"+i);
        }
        adapter = new MyBookListAdapter(getActivity(),datalist);
        dataGrid.setAdapter(adapter);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
