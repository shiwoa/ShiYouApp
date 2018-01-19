package com.jiaoyu.read;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;
import com.jiaoyu.shiyou.R;

import org.geometerplus.android.fbreader.FBReader;

import java.io.File;

public class Main2Activity extends Activity {
    //图书的本地路径
    public static String localBookPath = "";

    public static double mCurrentRate = 0.0f;
    //远程图书路径
//    public static String serverBookPath = "http://file.dzzgsw.com/9/f4016a1c73c743008a582b3baf0e2252/1a231fa28d9e40778b1cf632e7ca32ad/book.zip";
    private ProgressDialog progressDialog;
    private Context mContext;
    private boolean isCanReader = false;
    public Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    Toast.makeText(mContext, "写入完成", Toast.LENGTH_SHORT).show();
                    isCanReader = true;
                    break;
                case 1:
                    isCanReader = true;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localBookPath = getCacheDir().getPath();
        progressDialog = ProgressDialog.show(this, "DownLoading...", "Please wait...", true, false);
        progressDialog.hide();
        setContentView(R.layout.activity_main2);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new WriteToSD(mContext, msgHandler);
            }
        }).start();
    }

    public void btnClick(View view) {
        if (isCanReader) {
            int btnId = view.getId();
            String localPath = localBookPath + "/book.epub";
            switch (btnId) {
                case R.id.btnNative:
                    if (!new File(localPath).exists()) {
                        Toast.makeText(Main2Activity.this, "图书不存在,先下载", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent readerActivity = new Intent(this, FBReader.class);
                    FBReader.bookPath = localPath;
                    startActivityForResult(readerActivity, 0);
                    break;
            }
        } else {
            Toast.makeText(mContext, "请等待数据写入完成", Toast.LENGTH_SHORT).show();
        }
    }
}
