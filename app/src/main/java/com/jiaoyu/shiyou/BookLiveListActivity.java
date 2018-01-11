package com.jiaoyu.shiyou;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.ViewPagerAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.fragment.BookLiveNowFragment;
import com.jiaoyu.fragment.BookPlaybackFragment;

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
 * Created by bishuang on 2018/1/8.
 * 书城-直播课程-直播列表
 */

public class BookLiveListActivity extends BaseActivity{

    private MagicIndicator magicIndicator; //滑动控件
    private LinearLayout back;
    private TextView title,course; //标题 课程
    private String[] tabTitle = new String[]{"直播","回放"};
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private BookLiveNowFragment liveNow; //直播
    private BookPlaybackFragment livePlayBack;//回放

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_live_list;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
       magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
       back = (LinearLayout) findViewById(R.id.public_head_back);
       title = (TextView) findViewById(R.id.public_head_title);
       title.setText(R.string.book_live_list);
       viewPager = (ViewPager) findViewById(R.id.view_pager);
       course = (TextView) findViewById(R.id.book_live_course);
        liveNow = new BookLiveNowFragment();
        livePlayBack = new BookPlaybackFragment();

       fragments.add(liveNow);
       fragments.add(livePlayBack);
       ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
       viewPager.setAdapter(mViewPagerAdapter);
       initMagicIndicator();
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> BookLiveListActivity.this.finish()); //返回
        //课程
        course.setOnClickListener(view -> openActivity(BookCourseActivity.class));
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.parseColor("#ffffff"));//背景色
        CommonNavigator commonNavigator = new CommonNavigator(BookLiveListActivity.this);
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
                simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.color_33));//未选中颜色
                simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_320));//选中的颜色
                simplePagerTitleView.setTextSize(13);//字体大小
                simplePagerTitleView.setOnClickListener(view -> {
                        viewPager.setCurrentItem(index);
                });
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
