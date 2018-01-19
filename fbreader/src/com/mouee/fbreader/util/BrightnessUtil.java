package com.mouee.fbreader.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.WindowManager;

public class BrightnessUtil {
    private static final String TAG = BrightnessUtil.class.getSimpleName();

    /**
     * 判断是否开启了自动亮度调节
     *
     * @param aContext
     * @return
     */
    public static boolean isAutoBrightness(Activity activity) {
        boolean autoBrightness = false;
        try {
            autoBrightness = Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (autoBrightness) {
            Log.d(TAG, "当前状态为自动调节");
        } else {
            Log.d(TAG, "当前状态为手动调节");
        }

        return false;
    }

    /**
     * 设置指定activity的亮度
     *
     * @param activity
     * @param value(0~1)
     */
    public static void setBrightness(Activity activity, float value) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = value;
        activity.getWindow().setAttributes(layoutParams);
        Log.d(TAG, "设置亮度值：" + value);
    }

    /**
     * 获取系统的亮度
     *
     * @param activity
     * @return
     */
    public static float getSystemBrightness(Activity activity) {
        float nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        nowBrightnessValue = (float) (nowBrightnessValue / 255);
        Log.d(TAG, "当前系统亮度为: " + nowBrightnessValue);
        return nowBrightnessValue;
    }

    /**
     * 开关亮度自动调节
     *
     * @param activity
     */
    public static void toggleAutoBrightness(Activity activity, boolean value) {
        int mode = 0;
        if (value) {
            mode = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            Log.d(TAG, "设为自动调节");
        } else {
            mode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
            Log.d(TAG, "设为手动调节");
        }
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                mode);
    }
}
