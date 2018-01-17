package com.jiaoyu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.adapter.MyPurchasedAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/16.
 * 我的已购专区-图书
 */

public class MyPurchasedBookFragment extends BaseFragment{

    private RecyclerView liveNowRec;
    private List<String> listLive = new ArrayList<>();
    private MyPurchasedAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_my_purchase_book;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        liveNowRec = findViewById(R.id.book_rec);
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
            listLive.add("已购图书测试标题"+i);
        }
        liveNowRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyPurchasedAdapter(R.layout.item_my_purchased,getActivity(),listLive);
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
