package com.jiaoyu.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jiaoyu.shiyou.R;
import com.jiaoyu.view.PageStateView;

public class PageStateManager implements View.OnClickListener {

    private OnRetryClickListener onRetryClickListener;

    private PageStateView pageStateLayout;

    public void showLoading() {
        pageStateLayout.showLoading();
    }

    public void showError() {
        pageStateLayout.showError();
    }

    public void showContent() {
        pageStateLayout.showContent();
    }

    public void showEmpty() {
        pageStateLayout.showEmpty();
    }

    public PageStateManager(View container) {
        ViewGroup contentParent = (ViewGroup) (container.getParent());
        Context context = container.getContext();

        int childCount = contentParent.getChildCount();
        //get contentParent
        int index = 0;
        for (int i = 0; i < childCount; i++) {
            if (contentParent.getChildAt(i) == container) {
                index = i;
                break;
            }
        }
        contentParent.removeView(container);
        //set content layout
        PageStateView stateLayout = new PageStateView(context);

        ViewGroup.LayoutParams lp = container.getLayoutParams();
        contentParent.addView(stateLayout, index, lp);
        stateLayout.setContentView(container);
        //set loading,error,empty layout
        setLoadingLayout(stateLayout);
        setEmptyLayout(stateLayout);
        setErrorLayout(stateLayout);

        pageStateLayout = stateLayout;

        pageStateLayout.getErrorView().setOnClickListener(this);

        pageStateLayout.getEmptyView().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_error_state:
                if (onRetryClickListener != null) {
                    onRetryClickListener.onRetryClick();
                }
                break;
            case R.id.base_empty_state:
                if (onRetryClickListener != null) {
                    onRetryClickListener.onRetryClick();
                }
                break;
        }
    }

    private void setLoadingLayout(PageStateView stateLayout) {
        stateLayout.setLoadingView(R.layout.base_state_loading_layout);
    }

    private void setEmptyLayout(PageStateView stateLayout) {
        stateLayout.setEmptyView(R.layout.base_state_empty_layout);
    }

    private void setErrorLayout(PageStateView stateLayout) {
        stateLayout.setErrorView(R.layout.base_state_error_layout);
    }

    public interface OnRetryClickListener {
        void onRetryClick();
    }

    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.onRetryClickListener = listener;
    }

}
