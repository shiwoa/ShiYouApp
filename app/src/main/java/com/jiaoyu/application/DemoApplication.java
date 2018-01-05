package com.jiaoyu.application;

import android.app.Application;
import android.content.Context;

import com.jiaoyu.utils.SharedPreferencesUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by bishuang on 2017/12/25.
 * application类
 */

public class DemoApplication extends Application {

    private static DemoApplication instance = null;
    private static DemoApplication demoApplication;
    private RefWatcher refWatcher = null;

    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication = this;
        initPrefs();
        //屏幕适配
        AutoLayoutConifg.getInstance().useDeviceSize();
        //检测内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        //BaseFragment检测内存泄漏
        refWatcher = LeakCanary.install(this);
    }

    public static DemoApplication getsInstance() {
        synchronized (DemoApplication.class) {
            if (instance == null) {
                instance = new DemoApplication();
            }
        }
        return instance;
    }
    public static Context getAppContext() {
        return demoApplication;
    }

    //返回BaseFragment检测内存泄漏
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

}
