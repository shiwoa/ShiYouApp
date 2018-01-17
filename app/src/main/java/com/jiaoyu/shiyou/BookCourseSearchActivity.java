package com.jiaoyu.shiyou;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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
 * 书城 - 课程 - 搜索的类
 */

public class BookCourseSearchActivity extends BaseActivity{

    private TextView cancel; //取消
    private List<EntityCourse> dataList = new ArrayList<>();//搜索列表集合
    private RecyclerView searchRec; //搜索列表
    private BookCourseAdapter adapter; //搜索的适配器
    private EditText content;//搜索内容
    private int page = 1; //当前页

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
        content = (EditText) findViewById(R.id.content);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //取消
        cancel.setOnClickListener(view -> BookCourseSearchActivity.this.finish());
        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    //获取课程列表的方法
                    getData(page,content.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {
//        //获取课程列表的方法
//        getData(page,);
    }

    /**
     * @date 2018/1/14 15:19
     * @Description: 获取课程列表的方法
     */
    public void getData(int page,String searchName) {
        Map<String, String> map = new HashMap<>();
        map.put("page.currentPage", String.valueOf(page));
        map.put("searchName", String.valueOf(searchName));
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
                                searchRec.setLayoutManager(new LinearLayoutManager(BookCourseSearchActivity.this));
                                adapter = new BookCourseAdapter(R.layout.item_book_course,BookCourseSearchActivity.this,dataList);
                                searchRec.setAdapter(adapter);
                                adapter.setOnItemClickListener((adapter1, view, position) ->{
                                    openActivity(BookCourseDetailsActivity.class);
                                });

                            }else{
                                showStateEmpty();
                            }

                        } else {
                            ToastUtil.showWarning(BookCourseSearchActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
