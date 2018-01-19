package org.geometerplus.android.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CustomToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {

        if (mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(mContext, text, duration);
            long delayMillis = 0;
            switch (duration) {
                case Toast.LENGTH_SHORT:
                    delayMillis = 1500;
                    break;
                case Toast.LENGTH_LONG:
                    delayMillis = 3000;
                    break;
            }
            mHandler.postDelayed(r, delayMillis);
        }
        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

}