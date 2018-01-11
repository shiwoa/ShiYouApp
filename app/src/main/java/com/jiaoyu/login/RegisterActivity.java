package com.jiaoyu.login;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.shiyou.R;

/**
 * Created by bishuang on 2018/1/9.
 * 注册的类
 */

public class RegisterActivity extends BaseActivity{

    private TextView toLogin,title; //去登陆 标题
    private LinearLayout back; //返回

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_register;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        toLogin = (TextView) findViewById(R.id.reg_to_login);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.register_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        toLogin.setOnClickListener(view -> {
            openActivity(LoginActivity.class);
            RegisterActivity.this.finish();
        });
        //返回
        back.setOnClickListener(view -> RegisterActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
