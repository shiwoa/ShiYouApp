package com.jiaoyu.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.jiaoyu.adapter.MainFragmentPagerAdapter;
import com.jiaoyu.application.DemoApplication;
import com.jiaoyu.base.AppManager;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;

/**
 * description: 主mainActivity类
 * @author bishaung
 * @date 2018/1/5 11:28
 */
public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainFragmentPagerAdapter pagerAdapter;
    private TabLayout.Tab find;
    private TabLayout.Tab book;
    private TabLayout.Tab community;
    private TabLayout.Tab my;
    //时间标记
    private long mExitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();//隐藏掉整个ActionBar
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        //设置tabPage的方法
        setTabPage();
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {

    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /**
     * @date 2018/1/5 16:09
     * @Description: 设置tabPage的方法
     */
    private void setTabPage() {
        //使用适配器将ViewPager与Fragment绑定在一起
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        //将TabLayout与ViewPager绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);
        //指定Tab的位置
        find = mTabLayout.getTabAt(0);
        book = mTabLayout.getTabAt(1);
        community = mTabLayout.getTabAt(2);
        my = mTabLayout.getTabAt(3);

        //设置Tab的图标
        find.setIcon(R.drawable.main_tab_icon_find);
        book.setIcon(R.drawable.main_tab_icon_book);
        community.setIcon(R.drawable.main_tab_icon_community);
        my.setIcon(R.drawable.main_tab_icon_my);
    }

    /** 第一次点击时提示：再按一次退出
     *   如果2s以内则退出程序
     *    如果2s之外则提示：再按一次退出
     */
    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.showNormal(MainActivity.this,"再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit(DemoApplication.getAppContext(),false);
        }
    }

    /**判断是否点击了返回键*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
