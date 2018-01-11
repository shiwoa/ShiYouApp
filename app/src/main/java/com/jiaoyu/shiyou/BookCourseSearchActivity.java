package com.jiaoyu.shiyou;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jiaoyu.adapter.BookCourseAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/10.
 * 书城 - 课程 - 搜索的类
 */

public class BookCourseSearchActivity extends BaseActivity{

    private TextView cancel; //取消
    private List<String> dataList = new ArrayList<>();//搜索列表集合
    private RecyclerView searchRec; //搜索列表
    private BookCourseAdapter adapter; //搜索的适配器

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_course_search;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        cancel = (TextView) findViewById(R.id.cancel);
        searchRec = (RecyclerView) findViewById(R.id.searche_rec);
        if (dataList != null){
            dataList.clear();
        }
        for (int i = 0;i<6; i++){
            dataList.add("搜索课程测试标题"+i);
        }

        searchRec.setLayoutManager(new LinearLayoutManager(BookCourseSearchActivity.this));
        searchRec.setNestedScrollingEnabled(false);
        adapter = new BookCourseAdapter(R.layout.item_book_course,BookCourseSearchActivity.this,dataList,"search");
        searchRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(BookCourseSearchActivity.this,"点击"+position));
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //取消
        cancel.setOnClickListener(view -> BookCourseSearchActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
