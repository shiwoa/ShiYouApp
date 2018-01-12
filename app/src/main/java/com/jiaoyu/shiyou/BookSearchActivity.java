package com.jiaoyu.shiyou;

import android.widget.LinearLayout;

import com.jiaoyu.base.BaseActivity;

/**
 * Created by bishuang on 2018/1/12.
 *
 */

public class BookSearchActivity extends BaseActivity{

    private LinearLayout back;//返回

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_search;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        back = (LinearLayout) findViewById(R.id.book_course_back);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        back.setOnClickListener(view -> BookSearchActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
