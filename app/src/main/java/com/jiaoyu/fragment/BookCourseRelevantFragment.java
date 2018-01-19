package com.jiaoyu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiaoyu.adapter.BookCourseDetailsAdapter;
import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.shiyou.BookCourseDetailsActivity;
import com.jiaoyu.shiyou.BookCoursePlayActivity;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/11.
 * 图书课程播放-相关推荐的Fragment
 */

public class BookCourseRelevantFragment extends BaseFragment{

    private RecyclerView relevantRec;
    private List<EntityPublic> listLive = new ArrayList<>();
    private BookCourseDetailsAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_course_play_relevant;
    }

    /**
     * 初始化组件
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        relevantRec = findViewById(R.id.relecant_rec);
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
        if (listLive != null){
            listLive.clear();
        }
//        for (int i = 0;i<6; i++){
//            listLive.add("推荐课程测试标题"+i);
//        }
        relevantRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BookCourseDetailsAdapter(R.layout.item_course_details_content,getActivity(),listLive);
        relevantRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(getActivity(),"正在开发中"+position));
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
