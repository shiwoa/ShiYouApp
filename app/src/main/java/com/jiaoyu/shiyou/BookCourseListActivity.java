package com.jiaoyu.shiyou;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.BookCourseAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.EntityCourse;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/10.
 * 书城 -直播列表 -课程的类
 */

public class BookCourseListActivity extends BaseActivity{

    private LinearLayout back; //返回
    private TextView search; //搜索
    private List<EntityCourse> dataList = new ArrayList<>();
    private RecyclerView courseRec;
    private BookCourseAdapter adapter;
    private int page = 1; //当前页

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
        showStateLoading(courseRec);
        //获取课程列表的方法
        getData(page);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //搜索
        search.setOnClickListener(view -> openActivity(BookCourseSearchActivity.class));
        //返回
        back.setOnClickListener(view -> BookCourseListActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {
        //获取课程列表的方法
        getData(page);
    }

    /**
     * @date 2018/1/14 15:19
     * @Description: 获取课程列表的方法
     */
    public void getData(int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page.currentPage", String.valueOf(page));
        OkHttpUtils.get().params(map).url(Address.COURSE_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showStateError();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                showStateContent();
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        EntityPublic entity = response.getEntity();
                        if (response.isSuccess()) {
                            if (dataList != null){
                                dataList.clear();
                            }
                            List<EntityCourse> courseList = entity.getCourseList();
                            if (courseList.size() != 0){
                                dataList.addAll(courseList);
                                courseRec.setLayoutManager(new LinearLayoutManager(BookCourseListActivity.this));
                                adapter = new BookCourseAdapter(R.layout.item_book_course,BookCourseListActivity.this,dataList);
                                courseRec.setAdapter(adapter);
                                adapter.setOnItemClickListener((adapter1, view, position) ->{
                                    Intent intent = new Intent();
                                    intent.setClass(BookCourseListActivity.this,BookCourseDetailsActivity.class);
                                    intent.putExtra("courseId",dataList.get(position).getId());
                                    startActivity(intent);
                                });

                            }else{
                                showStateEmpty();
                            }

                        } else {
                            ToastUtil.showWarning(BookCourseListActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

}
