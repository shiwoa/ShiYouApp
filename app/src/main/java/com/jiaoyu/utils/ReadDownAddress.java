package com.jiaoyu.utils;

import android.os.Environment;

/**
 * Created by bishuang on 2018/1/22.
 * 图书下载地址
 */

public interface ReadDownAddress {
    //电子书下载本地地址
    String READ_DOWN_PATH = Environment.getExternalStorageDirectory().getPath() + "/fbReader/";
}
