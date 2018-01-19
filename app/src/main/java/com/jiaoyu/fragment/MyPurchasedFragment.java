package com.jiaoyu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jiaoyu.adapter.MyBookListAdapter;
import com.jiaoyu.adapter.ViewPagerAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.widget.NoScrollGridView;
import com.jiaoyu.widget.NoScrollViewPager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/8.
 * 我的已购买fragment
 */

public class MyPurchasedFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    private List<Fragment> fragments;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MyPurchasedBookFragment bookFragment; // 图书
    private MyPurchasedCourseFragment courseFragment; // 课程
    private TextView bookBtn,courseBtn; // 图书 课程

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_my_purchased;
    }


    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        viewPager = findViewById(R.id.view_pager);
//        viewPager.setScanScroll(false);
        bookBtn = findViewById(R.id.book);
        courseBtn = findViewById(R.id.course);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {
        viewPager.setOnPageChangeListener(this);
       bookBtn.setOnClickListener(this);
       courseBtn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        fragments = new ArrayList<>(); // 存放fragment的集合
        initFragments();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    private void initFragments() {
        bookFragment = new MyPurchasedBookFragment();
        courseFragment = new MyPurchasedCourseFragment();
        fragments.add(bookFragment); // 图书
        fragments.add(courseFragment); // 课程
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.book:
                setTextViewBackGround();
                viewPager.setCurrentItem(0);
                bookBtn.setTextColor(getResources().getColor(R.color.color_320));
                bookBtn.setBackgroundResource(R.drawable.line_main);
                break;
            case R.id.course:
                setTextViewBackGround();
                viewPager.setCurrentItem(1);
                courseBtn.setTextColor(getResources().getColor(R.color.color_320));
                courseBtn.setBackgroundResource(R.drawable.line_main);
                break;
        }
    }

    //  设置文字默认样式
    private void setTextViewBackGround() {
        bookBtn.setTextColor(getResources().getColor(R.color.color_99));
        bookBtn.setBackgroundColor(getResources().getColor(R.color.White));
        courseBtn.setTextColor(getResources().getColor(R.color.color_99));
        courseBtn.setBackgroundColor(getResources().getColor(R.color.White));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position ==0){
            setTextViewBackGround();
            viewPager.setCurrentItem(0);
            bookBtn.setTextColor(getResources().getColor(R.color.color_320));
            bookBtn.setBackgroundResource(R.drawable.line_main);
        }else if (position == 1){
            setTextViewBackGround();
            viewPager.setCurrentItem(1);
            courseBtn.setTextColor(getResources().getColor(R.color.color_320));
            courseBtn.setBackgroundResource(R.drawable.line_main);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
