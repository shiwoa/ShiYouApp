package com.jiaoyu.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.adapter.FindGaoduanAdapter;
import com.jiaoyu.adapter.FindHuodongAdapter;
import com.jiaoyu.adapter.FindJingpinAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/5.
 * 发现的fragment
 */

public class FindFragment extends BaseFragment{

    private static FindFragment findFragment;
    private NoScrollGridView jingpinGrid; //精品列表
    private RecyclerView huodongRec,liveRec,gaoduanRec; //活動列表 直播列表 高端列表
    private LinearLayout zixunLin,bookLin,liveLin,dingyueLin; //资讯 图书 直播 订阅
    private List<String> huodongList = new ArrayList<>(); //活动集合
    private List<String> jingpinList = new ArrayList<>(); //精品集合
    private List<String> liveList = new ArrayList<>(); //直播集合
    private List<String> gaoduanList = new ArrayList<>(); //高端集合
    private FindHuodongAdapter huodongAdapter;//活动的adapter
    private FindJingpinAdapter jingpinAdapter;//精品的adapter
    private BookLiveNowAdapter liveAdapter;//直播的adapter
    private FindGaoduanAdapter gaoduanAdapter;//高端的adapter
    private LinearLayout back;
    private TextView title;


    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_main_find;
    }

    /**
     * 获取本类对象的方法
     */
    public static FindFragment getInstance() {
        if (findFragment == null) {
            findFragment = new FindFragment();
        }
        return findFragment;
    }

    /**
     * 初始化组件
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        huodongRec = findViewById(R.id.find_huodong_rec);
        jingpinGrid = findViewById(R.id.find_jingpin_grid);
        liveRec = findViewById(R.id.find_live_Rec);
        gaoduanRec = findViewById(R.id.find_gaoduan_Rec);
        zixunLin = findViewById(R.id.find_zixun_lin);
        bookLin = findViewById(R.id.find_book_lin);
        liveLin = findViewById(R.id.find_live_lin);
        dingyueLin = findViewById(R.id.find_dingyue_lin);
        back = findViewById(R.id.public_head_back);
        back.setVisibility(View.GONE);
        title = findViewById(R.id.public_head_title);
        title.setText(R.string.find_title);
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
        if(huodongList != null){
            huodongList.clear();
            jingpinList.clear();
            liveList.clear();
            gaoduanList.clear();
        }
//      ----------------------------------------精彩活动-------------------------------------
        for (int i = 0; i < 1; i++){
            huodongList.add("活动测试标题"+i);
        }
        huodongRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        huodongRec.setNestedScrollingEnabled(false);
        huodongAdapter = new FindHuodongAdapter(R.layout.item_find_huodong,huodongList);
        huodongRec.setAdapter(huodongAdapter);
        huodongAdapter.setOnItemChildClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(getActivity(),"点击活动"+position));

//      ----------------------------------------精品必读-------------------------------------
        for (int i = 0; i < 3; i++){
            jingpinList.add("精品测试标题"+i);
        }
        jingpinAdapter = new FindJingpinAdapter(getActivity(),jingpinList);
        jingpinGrid.setAdapter(jingpinAdapter);
//      ----------------------------------------热门直播-------------------------------------
        for (int i = 0; i < 1; i++){
            liveList.add("直播测试标题"+i);
        }
        liveRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        liveRec.setNestedScrollingEnabled(false);
        liveAdapter = new BookLiveNowAdapter(R.layout.item_book_live_now,getActivity(),liveList);
        liveRec.setAdapter(liveAdapter);
        liveAdapter.setOnItemChildClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(getActivity(),"点击热门直播"+position));
//      ----------------------------------------高端专栏-------------------------------------
        for (int i = 0; i < 2; i++){
            gaoduanList.add("高端测试标题"+i);
        }
        gaoduanRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        gaoduanRec.setNestedScrollingEnabled(false);
        gaoduanAdapter = new FindGaoduanAdapter(R.layout.item_find_gaoduan,getActivity(),gaoduanList);
        gaoduanRec.setAdapter(gaoduanAdapter);
        gaoduanAdapter.setOnItemChildClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(getActivity(),"点击高端专栏"+position));

    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
