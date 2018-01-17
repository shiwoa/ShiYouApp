package com.jiaoyu.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jiaoyu.application.DemoApplication;
import com.jiaoyu.utils.NetWorkUtils;
import com.jiaoyu.utils.ToastUtil;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by bishuang on 2018/1/5.
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment implements PageStateManager.OnRetryClickListener,
        View.OnClickListener,AdapterView.OnItemClickListener{
    //view
    protected View mRootView;
    //ButterKnife
    private Unbinder unbinder;
    //页面状态管理器
    private PageStateManager pageStateManager;
    //是否已经有数据
    private boolean isContentAlready;
    private static ProgressDialog dialog = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(initContentView(),container,false);
        //返回一个UnBinder值（进行解绑）
        unbinder = ButterKnife.bind(this, mRootView);
        initComponent(savedInstanceState);
        //初始化方法
        initData();
        //添加点击事件
        initListener();
        return mRootView;
    }
    /**
     * 初始化布局资源文件
     */
    protected abstract int initContentView();

    /**
     * 初始化组件
     */
    protected abstract void initComponent(Bundle savedInstanceState);

    /**
     * 添加监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 数据加载失败重新加载
     */
    protected abstract void onDataReload();

    /**
     * 加载中状态
     * 如果需要在该view上显示
     * 不能是最外层view，必须要有父布局
     * 建议只调用一次，否则会创建多个view { new PageStateManager(container) }
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

    /**
     * 点击重试
     */
    @Override
    public void onRetryClick() {
        if (NetWorkUtils.isNetWorkAvailable(getActivity())) {
            pageStateManager.showLoading();
            onDataReload();
        } else {
            ToastUtil.showError(getActivity(), "网络不见了 :-(");
        }
    }

    protected <T extends View> T findViewById(int id) {
        if (mRootView == null) {
            return null;
        }
        return (T) mRootView.findViewById(id);
    }


    public void showLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
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
        Intent intent = new Intent(getActivity(), targetActivityClass);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void openActivity(Class<?> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), targetActivityClass);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解绑ButterKnife
        unbinder.unbind();
        RefWatcher refWatcher = ((DemoApplication) getActivity().getApplication()).getRefWatcher();
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }

    /**
    * @date 2018/1/5 11:46
    * @Description: item 的点击事件
    */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
    * @date 2018/1/5 11:46
    * @Description: 控件的点击事件
    */
    @Override
    public void onClick(View view) {

    }
}

