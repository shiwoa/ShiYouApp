package com.jiaoyu.fragment;

import android.os.Bundle;

import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;

/**
 * Created by bishuang on 2018/1/8.
 * 我的文章笔记的fragment
 */

public class MyArticleFragment extends BaseFragment{
    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_my_article;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {

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

    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
