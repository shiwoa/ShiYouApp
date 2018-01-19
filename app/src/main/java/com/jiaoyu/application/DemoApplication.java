package com.jiaoyu.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.jiaoyu.utils.CacheIntercptor;
import com.jiaoyu.utils.LogInterceptor;
import com.jiaoyu.utils.SharedPreferencesUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.geometerplus.android.fbreader.FBReaderApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by bishuang on 2017/12/25.
 * application类
 */

public class DemoApplication extends FBReaderApplication {

    private static DemoApplication instance = null;
    private static DemoApplication demoApplication;
    private RefWatcher refWatcher = null;
    //上下文
    public static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication = this;
        mContext = getApplicationContext();// 获取全局上下文
        initPrefs();
        //屏幕适配
        AutoLayoutConifg.getInstance().useDeviceSize();
        //检测内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        //BaseFragment检测内存泄漏
        refWatcher = LeakCanary.install(this);
        initLogger();
        //网络框架
        initOkHttpUtils();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
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

    private void initLogger() {

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                //.methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("logok268")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
    }


    /** 
    * @date 2018/1/14 10:29 
    * @Description: 网络框架 
    */
    private void initOkHttpUtils() {
        File httpCacheDirectory = new File(mContext.getCacheDir(), "shoyoucache");
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LogInterceptor(false))
                .cache(cache)
                .addInterceptor(new CacheIntercptor(mContext))
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }

}
