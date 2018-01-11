package com.jiaoyu.shiyou;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.login.LoginActivity;

/**
 * Created by bishuang on 2018/1/9.
 * 图书详情的类
 */

public class BookDetailsActivity extends BaseActivity{

    private TextView title,recommend; //标题 推荐
    private LinearLayout back; //返回

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_details;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.read);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        recommend = (TextView) findViewById(R.id.book_det_reccommend);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //推荐
        recommend.setOnClickListener(view -> openActivity(BookRecommendActivity.class));
        //返回
        back.setOnClickListener(view -> BookDetailsActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
