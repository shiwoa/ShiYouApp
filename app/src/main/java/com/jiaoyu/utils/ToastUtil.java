package com.jiaoyu.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaoyu.shiyou.R;


/**
 * Created by ming on 2017/3/29 15:50.
 * Description:Toast工具
 */
public class ToastUtil {

    private static final int NORMAL = 0;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private static final int WARNING = 3;

    private static Toast toast;
    private static TextView toast_text;
    private static ImageView type_msg;

    private static void show(Context context, String message, int status) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
        View toast_layout = inflater.inflate(R.layout.utils_toast_layout, null);
        if (toast == null) {
            toast = new Toast(context.getApplicationContext());
            toast_text = (TextView) toast_layout.findViewById(R.id.toast_type_msg);
            type_msg = (ImageView) toast_layout.findViewById(R.id.toast_type_img);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(toast_layout);
            toast_text.setText(message);
            if (status == 0) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_normal);
            } else if (status == 1) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_success);
            } else if (status == 2) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_failed);
            } else if (status == 3) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_wraning);
            }
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast_text.setText(message);
            if (status == 0) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_normal);
            } else if (status == 1) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_success);
            } else if (status == 2) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_failed);
            } else if (status == 3) {
                type_msg.setBackgroundResource(R.drawable.ic_toast_wraning);
            }
        }
        toast.show();
    }

    //系统默认样式
    public static void showNormal(Context context, String message) {
        show(context, message, NORMAL);
    }

    //成功
    public static void showSuccess(Context context, String message) {
        show(context, message, SUCCESS);
    }

    //失败
    public static void showError(Context context, String message) {
        show(context, message, ERROR);
    }

    //警告
    public static void showWarning(Context context, String message) {
        show(context, message, WARNING);
    }

}
