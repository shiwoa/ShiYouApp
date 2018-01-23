package com.jiaoyu.shiyou;

import android.widget.Button;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.read.Main2Activity;

/**
 * Created by bishuang on 2018/1/21.
 */

public class StartActivity extends BaseActivity{

    private Button btn;
    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_start;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
      btn = findViewById(R.id.btn1);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        btn.setOnClickListener(view -> openActivity(Main2Activity.class));
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
