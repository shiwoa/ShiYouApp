package com.jiaoyu.login;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.shiyou.R;

/**
 * Created by bishuang on 2018/1/9.
 * 忘记密码的类
 */

public class ForgetPwdActivity extends BaseActivity{

    private TextView next,title; //下一步
    private LinearLayout back; //返回

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_forget_pwd;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        next = (TextView) findViewById(R.id.forget_next);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.forget_pwd_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        next.setOnClickListener(view -> openActivity(SettingPwdActivity.class));
        //返回
        back.setOnClickListener(view -> ForgetPwdActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
