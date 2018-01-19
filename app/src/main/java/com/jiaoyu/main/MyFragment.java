package com.jiaoyu.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.ViewPagerAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.fragment.MyArticleFragment;
import com.jiaoyu.fragment.MyBookFragment;
import com.jiaoyu.fragment.MyCollectionFragment;
import com.jiaoyu.fragment.MyCommunityFragment;
import com.jiaoyu.fragment.MyPurchasedFragment;
import com.jiaoyu.login.LoginActivity;
import com.jiaoyu.shiyou.MyInformationActivity;
import com.jiaoyu.shiyou.MySettingActivity;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.SharedPreferencesUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/5.
 * 我的fragment
 */

public class MyFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    private static MyFragment myFragment;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private TextView title;
    private MyBookFragment myBook; //我的书架
    private MyPurchasedFragment myPurchase; //已购买
    private MyCollectionFragment myCollection; //收藏
    private MyArticleFragment myArticle; //文章
    private MyCommunityFragment myCommunity; //社区
    private ImageView userIcon; //用户头像
    private int userId; //用户id
    private LinearLayout setting; //设置
    private LinearLayout book_lin,yigou_lin,shoucang_lin,wenzhang_lin,shequ_lin;
    private TextView book_tv,yigou_tv,shoucang_tv,wenzhang_tv,shequ_tv;
    private View book_view,yigou_view,shoucang_view,wenzhang_view,shequ_view;
    private ViewPagerAdapter viewPagerAdapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_main_my;
    }

    /**
     * 获取本类对象的方法
     */
    public static MyFragment getInstance() {
        if (myFragment == null) {
            myFragment = new MyFragment();
        }
        return myFragment;
    }




    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        title = (TextView) findViewById(R.id.my_title);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        userIcon = findViewById(R.id.my_user_logo);
        setting = findViewById(R.id.my_setting);
        book_lin = findViewById(R.id.book_lin);
        yigou_lin = findViewById(R.id.yigou_lin);
        shoucang_lin = findViewById(R.id.shoucang_lin);
        wenzhang_lin = findViewById(R.id.wenzhang_lin);
        shequ_lin = findViewById(R.id.shequ_lin);
        book_tv = findViewById(R.id.book_tv);
        yigou_tv = findViewById(R.id.yigou_tv);
        shoucang_tv = findViewById(R.id.shoucang_tv);
        wenzhang_tv = findViewById(R.id.wenzhang_tv);
        shequ_tv = findViewById(R.id.shequ_tv);
        yigou_view = findViewById(R.id.yigou_view);
        book_view = findViewById(R.id.book_view);
        shoucang_view = findViewById(R.id.shoucang_view);
        wenzhang_view = findViewById(R.id.wenzhang_view);
        shequ_view = findViewById(R.id.shequ_view);

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        fragments = new ArrayList<>(); // 存放fragment的集合
        if (viewPagerAdapter == null){
            viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
            viewPager.setAdapter(viewPagerAdapter);
            initFragments();
            viewPagerAdapter.notifyDataSetChanged();
        }
//        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
//        viewPager.setAdapter(mViewPagerAdapter);
    }

    private void initFragments() {
        myBook = new MyBookFragment();
        myPurchase= new MyPurchasedFragment();
        myCollection = new MyCollectionFragment();
        myArticle = new MyArticleFragment();
        myCommunity = new MyCommunityFragment();
        fragments.add(myBook);
        fragments.add(myPurchase);
        fragments.add(myCollection);
        fragments.add(myArticle);
        fragments.add(myCommunity);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {
        viewPager.setOnPageChangeListener(this);
        //我的书架
        book_lin.setOnClickListener(this);
        //已购专区
        yigou_lin.setOnClickListener(this);
        //关注收藏
        shoucang_lin.setOnClickListener(this);
        //文章笔记
        wenzhang_lin.setOnClickListener(this);
        //社区互动
        shequ_lin.setOnClickListener(this);
        //设置
        setting.setOnClickListener(view -> openActivity(MySettingActivity.class));
        //用户头像
        userIcon.setOnClickListener(view -> {
            if (userId == -1){
                openActivity(LoginActivity.class);
                return;
            }
            openActivity(MyInformationActivity.class);
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.book_lin: //我的书架
                setTextViewBackGround();
                viewPager.setCurrentItem(0);
                book_tv.setTextColor(getResources().getColor(R.color.color_320));
                book_view.setBackgroundColor(getResources().getColor(R.color.color_320));
                break;
            case R.id.yigou_lin: //已购专区
                setTextViewBackGround();
                viewPager.setCurrentItem(1);
                yigou_tv.setTextColor(getResources().getColor(R.color.color_320));
                yigou_view.setBackgroundColor(getResources().getColor(R.color.color_320));
                break;
            case R.id.shoucang_lin: //关注收藏
                setTextViewBackGround();
                viewPager.setCurrentItem(2);
                shoucang_tv.setTextColor(getResources().getColor(R.color.color_320));
                shoucang_view.setBackgroundColor(getResources().getColor(R.color.color_320));
                break;
            case R.id.wenzhang_lin: //文章笔记
                setTextViewBackGround();
                viewPager.setCurrentItem(3);
                wenzhang_tv.setTextColor(getResources().getColor(R.color.color_320));
                wenzhang_view.setBackgroundColor(getResources().getColor(R.color.color_320));
                break;
            case R.id.shequ_lin: //社区互动
                setTextViewBackGround();
                viewPager.setCurrentItem(4);
                shequ_tv.setTextColor(getResources().getColor(R.color.color_320));
                shequ_view.setBackgroundColor(getResources().getColor(R.color.color_320));
                break;
        }
    }





    //  设置文字默认样式
    private void setTextViewBackGround() {
        book_tv.setTextColor(getResources().getColor(R.color.color_44));
        book_view.setBackgroundColor(getResources().getColor(R.color.White));
        yigou_tv.setTextColor(getResources().getColor(R.color.color_44));
        yigou_view.setBackgroundColor(getResources().getColor(R.color.White));
        shoucang_tv.setTextColor(getResources().getColor(R.color.color_44));
        shoucang_view.setBackgroundColor(getResources().getColor(R.color.White));
        wenzhang_tv.setTextColor(getResources().getColor(R.color.color_44));
        wenzhang_view.setBackgroundColor(getResources().getColor(R.color.White));
        shequ_tv.setTextColor(getResources().getColor(R.color.color_44));
        shequ_view.setBackgroundColor(getResources().getColor(R.color.White));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position ==0){
            setTextViewBackGround();
            viewPager.setCurrentItem(0);
            book_tv.setTextColor(getResources().getColor(R.color.color_320));
            book_view.setBackgroundColor(getResources().getColor(R.color.color_320));
        }else if (position == 1){
            setTextViewBackGround();
            viewPager.setCurrentItem(1);
            yigou_tv.setTextColor(getResources().getColor(R.color.color_320));
            yigou_view.setBackgroundColor(getResources().getColor(R.color.color_320));
        }else if (position == 2){
            setTextViewBackGround();
            viewPager.setCurrentItem(2);
            shoucang_tv.setTextColor(getResources().getColor(R.color.color_320));
            shoucang_view.setBackgroundColor(getResources().getColor(R.color.color_320));
        }else if (position == 3){
            setTextViewBackGround();
            viewPager.setCurrentItem(3);
            wenzhang_tv.setTextColor(getResources().getColor(R.color.color_320));
            wenzhang_view.setBackgroundColor(getResources().getColor(R.color.color_320));
        }else if (position == 4){
            setTextViewBackGround();
            viewPager.setCurrentItem(4);
            shequ_tv.setTextColor(getResources().getColor(R.color.color_320));
            shequ_view.setBackgroundColor(getResources().getColor(R.color.color_320));
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
