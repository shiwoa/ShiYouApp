package com.jiaoyu.fragment;

import android.os.Bundle;

import com.jiaoyu.adapter.BookCourseDetailsAdapter;
import com.jiaoyu.adapter.BookCoursePlayAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/11.
 * 图书课程播放-视频信息的Fragment
 */

public class BookCourseInforFragment extends BaseFragment{

    private NoScrollListView commendRec; //评论的列表
    private List<String> dataList = new ArrayList<>();
    private BookCoursePlayAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_course_play_infor;
    }

    /**
     * 初始化组件
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        commendRec = findViewById(R.id.commend_rec);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (dataList != null){
            dataList.clear();
        }
        for (int i = 0;i<6; i++){
            dataList.add("评论测试标题"+i);
        }
        adapter = new BookCoursePlayAdapter(getActivity(),dataList);
        commendRec.setAdapter(adapter);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
