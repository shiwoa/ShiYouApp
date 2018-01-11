package com.jiaoyu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 书城-直播的fragment
 */

public class BookLiveNowFragment extends BaseFragment{

    private RecyclerView liveNowRec;
    private List<String> listLive = new ArrayList<>();
    private BookLiveNowAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_book_live_now;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        liveNowRec = findViewById(R.id.live_now_rec);

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
        for (int i = 0;i<6; i++){
                listLive.add("直播测试标题"+i);

        }
        liveNowRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        liveNowRec.setNestedScrollingEnabled(false);
        adapter = new BookLiveNowAdapter(R.layout.item_book_live_now,getActivity(),listLive);
        liveNowRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(getActivity(),"努力开发中"+position));
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
