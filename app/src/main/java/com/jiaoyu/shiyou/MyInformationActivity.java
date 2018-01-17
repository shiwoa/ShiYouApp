package com.jiaoyu.shiyou;

import android.widget.LinearLayout;

import com.jiaoyu.base.BaseActivity;

/**
 * Created by bishuang on 2018/1/14.
 * 我的 个人信息的类
 */

public class MyInformationActivity extends BaseActivity{

    private LinearLayout back;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_information;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        back = (LinearLayout) findViewById(R.id.public_head_back);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> MyInformationActivity.this.finish());

    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
