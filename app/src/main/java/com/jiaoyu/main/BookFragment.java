package com.jiaoyu.main;

import android.os.Bundle;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/5.
 * 图书的fragment
 */

public class BookFragment extends BaseFragment{

    private NoScrollGridView shiyouGrid,shekeGrid; //石油列表 社科列表
    private List<String> datalist = new ArrayList<>();
    private List<String> datashekelist = new ArrayList<>();
    private BookAdapter shiyouAdapter,shekeAdapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_main_book;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        shiyouGrid = findViewById(R.id.shiyou_grid);
        shekeGrid = findViewById(R.id.sheke_grid);
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
            datashekelist.clear();
        }

        for (int i = 0; i < 6; i++){
            datalist.add("石油测试标题"+i);
        }
        shiyouAdapter = new BookAdapter(getActivity(),datalist);
        shiyouGrid.setAdapter(shiyouAdapter);
        for (int i = 0; i < 2; i++){
            datashekelist.add("社科测试标题"+i);
        }
        shekeAdapter = new BookAdapter(getActivity(),datashekelist);
        shekeGrid.setAdapter(shekeAdapter);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
