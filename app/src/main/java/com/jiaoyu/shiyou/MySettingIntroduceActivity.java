package com.jiaoyu.shiyou;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;

/**
 * Created by bishuang on 2018/1/15.
 * 我的设置-系统介绍
 */

public class MySettingIntroduceActivity extends BaseActivity{

    private LinearLayout back;
    private TextView title; //标题

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_setting_introduce;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        back = (LinearLayout) findViewById(R.id.public_head_back);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.system_introduce);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> MySettingIntroduceActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
