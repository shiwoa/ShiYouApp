package com.mouee.fbreader.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ContainerLayout extends RelativeLayout {
    private boolean interceptState = false;

    public ContainerLayout(Context context) {
        super(context);
    }

    public ContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInterceptState(boolean state) {
        interceptState = state;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (interceptState) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
