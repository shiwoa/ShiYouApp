package com.jiaoyu.shiyou;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookCourseAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/10.
 * 书城 -直播列表 -课程的类
 */

public class BookCourseActivity extends BaseActivity{

    private LinearLayout back; //返回
    private TextView search; //搜索
    private List<String> dataList = new ArrayList<>();
    private RecyclerView courseRec;
    private BookCourseAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_course;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        back = (LinearLayout) findViewById(R.id.book_course_back);
        search = (TextView) findViewById(R.id.book_course_search);
        courseRec = (RecyclerView) findViewById(R.id.book_course_rec);

        if (dataList != null){
            dataList.clear();
        }
        for (int i = 0;i<6; i++){
            dataList.add("课程测试标题"+i);
        }

        courseRec.setLayoutManager(new LinearLayoutManager(BookCourseActivity.this));
        courseRec.setNestedScrollingEnabled(false);
        adapter = new BookCourseAdapter(R.layout.item_book_course,BookCourseActivity.this,dataList,"course");
        courseRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->{
            openActivity(BookCourseDetailsActivity.class);
                });
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //搜索
        search.setOnClickListener(view -> openActivity(BookCourseSearchActivity.class));
        //返回
        back.setOnClickListener(view -> BookCourseActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
