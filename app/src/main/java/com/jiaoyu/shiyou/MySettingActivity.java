package com.jiaoyu.shiyou;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;

/**
 * Created by bishuang on 2018/1/14.
 * 我的-系统设置的类
 */

public class MySettingActivity extends BaseActivity{

    private LinearLayout back;
    private TextView title; //标题 课程
    private RelativeLayout updatePwd,introduce; //修改密码 系统介绍

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_setting;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        back = (LinearLayout) findViewById(R.id.public_head_back);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.my_set);
        updatePwd = (RelativeLayout) findViewById(R.id.set_pwd);
        introduce = (RelativeLayout) findViewById(R.id.set_introduce);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> MySettingActivity.this.finish());
        //修改密码
        updatePwd.setOnClickListener(view -> openActivity(MySettingPwdActivity.class));
        //系统介绍
        introduce.setOnClickListener(view -> openActivity(MySettingIntroduceActivity.class));
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
