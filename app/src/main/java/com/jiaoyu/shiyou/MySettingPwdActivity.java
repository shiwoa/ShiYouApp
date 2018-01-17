package com.jiaoyu.shiyou;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;

/**
 * Created by bishuang on 2018/1/15.
 * 我的-系统设置-修改密码
 */

public class MySettingPwdActivity extends BaseActivity{

    private LinearLayout back;
    private TextView title; //标题 课程

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_my_setting_pwd;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        back = (LinearLayout) findViewById(R.id.public_head_back);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.update_pwd);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> MySettingPwdActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
