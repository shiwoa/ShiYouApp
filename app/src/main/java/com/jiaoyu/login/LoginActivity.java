package com.jiaoyu.login;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.shiyou.R;

/**
 * Created by bishuang on 2018/1/9.
 * 登陆的类
 */

public class LoginActivity extends BaseActivity{

    private TextView toRegister,title,forget; //去注册 标题 忘记密码
    private LinearLayout back; //返回


    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_login;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        toRegister = (TextView) findViewById(R.id.login_to_register);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.login_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        forget = (TextView) findViewById(R.id.login_forget_pwd);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //忘记密码
        forget.setOnClickListener(view -> openActivity(ForgetPwdActivity.class));
        //去注册
        toRegister.setOnClickListener(view ->{
            openActivity(RegisterActivity.class);
            LoginActivity.this.finish();
          });
        //返回
        back.setOnClickListener(view -> LoginActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
