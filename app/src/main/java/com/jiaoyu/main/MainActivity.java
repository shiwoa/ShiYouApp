package com.jiaoyu.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jiaoyu.application.DemoApplication;
import com.jiaoyu.base.AppManager;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.fragment.FragmentControllerNew;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.ToastUtil;

/**
 * description: 主mainActivity类
 * @author bishaung
 * @date 2018/1/5 11:28
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private FragmentControllerNew controller; //Fragment控制器
    //时间标记
    private long mExitTime;
    private RadioGroup radioGroup;
    private RadioButton findBtn,bookBtn,communityBtn,myBtn; //发现 图书 社区 我的

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        controller = FragmentControllerNew.getInstance(this, R.id.fragment_layout);
        controller.showFragment(0);
        radioGroup = (RadioGroup) findViewById(R.id.main_radio_group);
        findBtn = (RadioButton) findViewById(R.id.main_find_btn);
        bookBtn = (RadioButton) findViewById(R.id.main_book_btn);
        communityBtn = (RadioButton) findViewById(R.id.main_community_btn);
        myBtn = (RadioButton) findViewById(R.id.main_my_btn);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

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

    /** 
    * @date 2018/1/9 14:47 
    * @Description: 点击radiobtn的监听
    */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.main_find_btn: //发现
                controller.showFragment(0);
                break;
            case R.id.main_book_btn: //书城
                controller.showFragment(1);
                break;
            case R.id.main_community_btn: //社区
                controller.showFragment(2);
                break;
            case R.id.main_my_btn: //我的
                controller.showFragment(3);
                break;
            default:
                break;
        }
    }
}
