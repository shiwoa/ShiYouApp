package com.jiaoyu.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jiaoyu.utils.NetWorkUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

/**
 * Created by bishuang on 2018/1/3.
 * Activit基类
 */
public abstract class BaseActivity extends AutoLayoutActivity implements PageStateManager.OnRetryClickListener
        ,View.OnClickListener,AdapterView.OnItemClickListener{

    //页面状态管理器
    private PageStateManager pageStateManager;
    //是否已经有数据
    private boolean isContentAlready;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        setContentView(initContentView());
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        //初始化方法
        initData();
        //点击事件方法
        addListener();
    }

    /**
     * 初始化布局资源文件
     */
    protected abstract int initContentView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听事件
     */
    protected abstract void addListener();

    /**
     * 数据加载失败重新加载
     */
    protected abstract void onDataReload();

    /**
   * @date 2018/1/3 16:07
   * @Description: 重试点击事件
   */
    @Override
    public void onRetryClick() {
        if (NetWorkUtils.isNetWorkAvailable(BaseActivity.this)) {
            pageStateManager.showLoading();
            onDataReload();
        } else {
            Toast.makeText(BaseActivity.this, "网络不见了:-(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
    * @date 2018/1/3 16:17
    * @Description: 关闭的方法
    */
    public void finishBase() {
        //移出Activity栈
        AppManager.getAppManager().finishActivity(this);
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 加载中状态
     * 如果需要在该view上显示
     * 不能是最外层view，必须要有父布局
     */
    public void showStateLoading(View container) {
        if (container != null) {
            pageStateManager = new PageStateManager(container);
            pageStateManager.showLoading();
        }
    }

    /**
     * 加载完成，显示内容
     */
    public void showStateContent() {
        if (pageStateManager != null) {
            pageStateManager.showContent();
            isContentAlready = true;
        }
    }

    /**
     * 加载失败状态
     */
    public void showStateError() {
        if (pageStateManager != null && !isContentAlready) {
            pageStateManager.showError();
            pageStateManager.setOnRetryClickListener(this);
        }
    }

    /**
     * 加载完成，没有数据状态
     */
    public void showStateEmpty() {
        if (pageStateManager != null) {
            pageStateManager.showEmpty();
            pageStateManager.setOnRetryClickListener(this);
        }
    }

    public void showLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(BaseActivity.this);
            dialog.setMessage("请稍后...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            dialog.show();
        }
    }

    public void cancelLoading() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void openActivity(Class<?> targetActivityClass) {
        Intent intent = new Intent(this, targetActivityClass);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void openActivity(Class<?> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(this, targetActivityClass);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /** 
    * @date 2018/1/5 11:31 
    * @Description: item的点击事件 
    */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        
    }

    /** 
    * @date 2018/1/5 11:32 
    * @Description: 控件的点击事件
    */
    @Override
    public void onClick(View view) {
        
    }
}
