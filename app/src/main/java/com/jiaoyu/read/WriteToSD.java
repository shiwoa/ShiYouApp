package com.jiaoyu.read;

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*将assets文件夹下的数据库写入SD卡中
* @author Dave */
public class WriteToSD {
    private String filePath = "";
    private Context context;

    private Handler handler;

    public WriteToSD(Context context, Handler handler) {
        this.context = context;
        filePath = context.getCacheDir().getPath();
        this.handler = handler;
        if (!isExist()) {
            write();
        } else {
            handler.sendEmptyMessage(1);
        }
    }

    private void write() {
        InputStream inputStream;
        try {
            inputStream = context.getResources().getAssets().open("book.epub");
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/book.epub");
            byte[] buffer = new byte[5 * 1024];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handler.sendEmptyMessage(0);
        }
    }

    private boolean isExist() {
        File file = new File(filePath + "/book.epub");
        return file.exists();
    }
}