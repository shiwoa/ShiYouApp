package com.jiaoyu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sliderlibrary.SliderLayout;
import com.example.sliderlibrary.SliderTypes.BaseSliderView;
import com.example.sliderlibrary.SliderTypes.TextSliderView;
import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.shiyou.BookClassificationActivity;
import com.jiaoyu.shiyou.BookDetailsActivity;
import com.jiaoyu.shiyou.BookLiveListActivity;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/5.
 * 图书的fragment
 */

public class BookFragment extends BaseFragment{

    private static BookFragment bookFragment;
    private NoScrollGridView shiyouGrid,shekeGrid; //石油列表 社科列表
    private List<String> datalist = new ArrayList<>();
    private List<String> datashekelist = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>(); //bannerList
    private BookAdapter shiyouAdapter,shekeAdapter;
    private SliderLayout homeBannerLayout; //banner图
    private LinearLayout liveList,classList; //直播课程 本社图书

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_main_book;
    }

    /**
     * 获取本类对象的方法
     */
    public static BookFragment getInstance() {
        if (bookFragment == null) {
            bookFragment = new BookFragment();
        }
        return bookFragment;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        shiyouGrid = findViewById(R.id.shiyou_grid);
        shekeGrid = findViewById(R.id.sheke_grid);
        homeBannerLayout = findViewById(R.id.home_banner_layout);
        liveList = findViewById(R.id.book_live_list);
        classList = findViewById(R.id.book_class_list);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {
        //本社图书
        classList.setOnClickListener(view -> openActivity(BookClassificationActivity.class));
        //直播课程
        liveList.setOnClickListener(view->openActivity(BookLiveListActivity.class));
        //石油列表
        shiyouGrid.setOnItemClickListener((adapterView, view, i, l) -> openActivity(BookDetailsActivity.class));
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (datalist != null){
            datalist.clear();
            datashekelist.clear();
            bannerList.clear();
        }

        bannerList.add("http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468745418218035.jpg");
        bannerList.add("http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468735497415340.jpg");
        bannerList.add("http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468754570856573.jpg");

        for (int i = 0; i < 6; i++){
            datalist.add("石油测试标题"+i);
        }
        shiyouAdapter = new BookAdapter(getActivity(),datalist);
        shiyouGrid.setAdapter(shiyouAdapter);
        for (int i = 0; i < 2; i++){
            datashekelist.add("社科测试标题"+i);
        }
        shekeAdapter = new BookAdapter(getActivity(),datashekelist);
        shekeGrid.setAdapter(shekeAdapter);
        initBannerLayout();
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /**
     * 初始化轮播图
     */
    private void initBannerLayout() {
        for (int i = 0; i < bannerList.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.image(bannerList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

                        @Override
                        public void onSliderClick(BaseSliderView slider) {
//                            Intent intent = new Intent();
//                            intent.setClass(getActivity(), CourseDetails96kActivity.class);
//                            intent.putExtra("courseId", bannerList.get(homeBannerLayout.getCurrentPosition()).getCourseId());
//                            startActivity(intent);
                            ToastUtil.showNormal(getActivity(),"点击"+homeBannerLayout.getCurrentPosition());
                        }
                    });
            homeBannerLayout.addSlider(textSliderView);
        }
        homeBannerLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        homeBannerLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        homeBannerLayout.setDuration(3000);
    }

}
