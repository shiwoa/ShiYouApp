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

public class MyFragment extends BaseFragment{

    private static MyFragment myFragment;
    private String[] tabTitle = new String[]{"我的书架","已购专区","关注收藏","文章笔记","社区互动"};
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private TextView title;
    private MagicIndicator magicIndicator; //滑动控件
    private MyBookFragment myBook; //我的书架
    private MyPurchasedFragment myPurchase; //已购买
    private MyCollectionFragment myCollection; //收藏
    private MyArticleFragment myArticle; //文章
    private MyCommunityFragment myCommunity; //社区
    private ImageView userIcon; //用户头像
    private int userId; //用户id
    private LinearLayout setting; //设置
    
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
        magicIndicator = (MagicIndicator) findViewById(R.id.my_magic_indicator);
        title = (TextView) findViewById(R.id.my_title);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        userIcon = findViewById(R.id.my_user_logo);
        setting = findViewById(R.id.my_setting);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {
        //设置
        setting.setOnClickListener(view -> openActivity(MySettingActivity.class));
        userIcon.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (userId == -1){
            openActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()){
            case R.id.my_user_logo: //用户头像
                openActivity(MyInformationActivity.class);
                break;
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
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
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(mViewPagerAdapter);
        initMagicIndicator();
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.parseColor("#ffffff"));//背景色
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.25f);
        if (tabTitle.length <= 6 ) commonNavigator.setAdjustMode(true);//充满屏幕宽度
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(tabTitle[index]);
                simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.color_44));//未选中颜色
                simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_320));//选中的颜色
                simplePagerTitleView.setTextSize(13);//字体大小
                simplePagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);//等同标题长度
                indicator.setYOffset(UIUtil.dip2px(context, 2));
                indicator.setLineHeight(UIUtil.dip2px(context, 2));//指示器宽度
                indicator.setColors(context.getResources().getColor(R.color.color_320));//指示器颜色
                indicator.setStartInterpolator(new AccelerateInterpolator());//开始动画
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));//结束动画
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}
