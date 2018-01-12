package com.jiaoyu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.adapter.BookPlaybackAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.BookPlayBackActicity;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/8.
 * 书城-直播的fragment
 */

public class BookPlaybackFragment extends BaseFragment{

    private RecyclerView playbackRec;
    private List<String> playbackList = new ArrayList<>();
    private BookPlaybackAdapter adapter;


    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_book_playback;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        playbackRec = findViewById(R.id.book_playback_rec);

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
        if (playbackList != null){
            playbackList.clear();
        }
        for (int i = 0;i<6; i++){
            playbackList.add("回放测试标题"+i);

        }
        playbackRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BookPlaybackAdapter(R.layout.item_book_playback,getActivity(),playbackList);
        playbackRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> openActivity(BookPlayBackActicity.class));
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
