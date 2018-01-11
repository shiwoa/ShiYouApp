package com.jiaoyu.main;

import android.os.Bundle;

import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;

/**
 * Created by bishuang on 2018/1/5.
 * 社区的fragment
 */

public class CommunityFragment extends BaseFragment{

    private static CommunityFragment communityFragment;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_main_community;
    }

    /**
     * 获取本类对象的方法
     */
    public static CommunityFragment getInstance() {
        if (communityFragment == null) {
            communityFragment = new CommunityFragment();
        }
        return communityFragment;
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
