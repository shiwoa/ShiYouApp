package com.jiaoyu.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ming on 2017/1/11 11:45.
 * Desc:
 */

public class NetWorkUtils {

    /**
     * 当前有无网络
     *
     * @param context context
     * @return boolean
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info == null) {
                return false;
            } else {
                if (info.isAvailable()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取链接的WiFi Sid;
     *
     * @param context context
     * @return Str
     */
    public static String getConnectWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager != null ? wifiManager.getConnectionInfo() : null;
        return wifiInfo != null ? wifiInfo.getSSID() : null;
    }

    /**
     * getMacAddress
     *
     * @param context context
     * @return MacAddress
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager != null ? wifiManager.getConnectionInfo() : null;
        //6.0系统getMacAddress返回固定值"02:00:00:00:00:00",存在问题暂时没有解决
        @SuppressLint("HardwareIds") String wifiMac = wifiInfo != null ? wifiInfo.getMacAddress() : null;
        if (!TextUtils.isEmpty(wifiMac)) {
            return wifiMac.replace(":", "");
        }
        return null;
    }

    private static ConnectivityManager connectivityManager; // 判断网络的对象

    // 实例话判断网络的类
    public static void initConnectivityManager(Context context){
        if(connectivityManager == null){
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }
    // 判断网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        initConnectivityManager(context);
        //得到网络的信息
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
