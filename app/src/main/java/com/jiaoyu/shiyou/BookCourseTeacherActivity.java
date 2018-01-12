package com.jiaoyu.shiyou;

import android.widget.LinearLayout;

import com.jiaoyu.base.BaseActivity;

/**
 * Created by bishuang on 2018/1/11.
 * 专家主页
 */

public class BookCourseTeacherActivity extends BaseActivity{

    private LinearLayout back;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_course_teacher;
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
        back.setOnClickListener(view -> BookCourseTeacherActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
