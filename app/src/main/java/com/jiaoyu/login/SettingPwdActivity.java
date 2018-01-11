package com.jiaoyu.login;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.shiyou.R;

/**
 * Created by bishuang on 2018/1/9.
 * 设置新密码的类
 */

public class SettingPwdActivity extends BaseActivity{

    private TextView complete,title; //完成 标题
    private LinearLayout back; //返回

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_setting_pwd;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        complete = (TextView) findViewById(R.id.set_pwd_complete);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.set_pwd_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //完成
        complete.setOnClickListener(view -> SettingPwdActivity.this.finish());
        //返回
        back.setOnClickListener(view -> SettingPwdActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
