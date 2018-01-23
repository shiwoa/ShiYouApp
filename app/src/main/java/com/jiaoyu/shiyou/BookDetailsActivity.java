package com.jiaoyu.shiyou;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.read.book.ReadDownTask;
import com.jiaoyu.read.book.ReadWriteToSD;
import com.jiaoyu.read.test.BookDownLoadTask;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.GlideUtil;
import com.jiaoyu.utils.ReadDownAddress;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.geometerplus.android.fbreader.FBReader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/9.
 * 图书详情的类
 */

public class BookDetailsActivity extends BaseActivity implements ReadDownAddress{

    private TextView title,recommend; //标题 推荐
    private LinearLayout back,down_book_list; //返回 下载到书架
    private int ebookId,userId; //图书id
    private TextView bookTitle,num,price,member,bookInfo,bookAuthor;//图书标题 观看量 价格 会员 简介 作者
    private ImageView bookImg; //图书图片
    private TextView downBookTv; //下载图书文字
    private boolean isCanReader = false;
    public static double mCurrentRate = 0.0f;
    private ReadDownTask readTask; //阅读的下载Task
    private boolean isok = false; //下载handler完成的标记
    //图书的本地路径
    public static String localBookPath = "";
    //远程图书路径
    public static String serverBookPath = "http://file.dzzgsw.com/9/f4016a1c73c743008a582b3baf0e2252/1a231fa28d9e40778b1cf632e7ca32ad/book.zip";
    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_details;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getMessage();
        localBookPath = Environment.getExternalStorageDirectory().getPath() + "/fbReader/";
        userId = (int) SharedPreferencesUtils.getParam(BookDetailsActivity.this, "userId", -1);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.read);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        recommend = (TextView) findViewById(R.id.book_det_reccommend);
        bookTitle = (TextView) findViewById(R.id.title);
        num = (TextView) findViewById(R.id.num);
        price = (TextView) findViewById(R.id.price);
        member = (TextView) findViewById(R.id.member);
        bookImg = (ImageView) findViewById(R.id.bookImg);
        bookInfo = (TextView) findViewById(R.id.bookInfo);
        bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        down_book_list = (LinearLayout) findViewById(R.id.down_book_list);
        downBookTv = findViewById(R.id.down_book_tv);
        getDate(userId,ebookId);
    }

    public MyHandler msgHandler = new MyHandler(BookDetailsActivity.this);
    public class MyHandler extends Handler {
        // 弱引用 ，防止内存泄露
        private WeakReference<BookDetailsActivity> weakReference;

        public MyHandler(BookDetailsActivity handlerMemoryActivity) {
            weakReference = new WeakReference<BookDetailsActivity>(handlerMemoryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 通过  软引用  看能否得到activity示例
            BookDetailsActivity handlerMemoryActivity = weakReference.get();
            // 防止内存泄露
            if (handlerMemoryActivity != null) { // 如果当前Activity，进行UI的更新
                int what = msg.what;
                switch (what) {
                    case 0:
                        Toast.makeText(BookDetailsActivity.this, "写入完成", Toast.LENGTH_SHORT).show();
                        isCanReader = true;
                        break;
                    case 1:
                        isCanReader = true;
                        break;
                    case ReadDownTask.MSG_PROGRESS:
                        downBookTv.setText((int)(mCurrentRate * 100)+"");
                        Log.i("ceshi",(int) (mCurrentRate * 100)+"---------下载中---------");
                        break;
                    case ReadDownTask.MSG_DOWN_OVER:
                        if (!isok){
                            isok = true;
                            Log.i("ceshi",(int) (mCurrentRate * 100)+"---------下载完成---------");
                            getDownBook(userId,ebookId);
                        }
                        break;
                }
            }else {
             // 没有实例不进行操作
            }
        }
    }


/**
* @date 2018/1/15 17:20
* @Description: 获取从 BookFragment 传递过来的数据
*/
    public void getMessage(){
        Intent intent = getIntent();
        ebookId = intent.getIntExtra("ebookId",0);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //推荐
        recommend.setOnClickListener(view -> {
//            openActivity(BookRecommendActivity.class);
            if (isCanReader) {
                int btnId = view.getId();
                String localPath = localBookPath + "book.epub";
                switch (btnId) {
                    case R.id.btnNative:
                        if (!new File(localPath).exists()) {
                            Toast.makeText(BookDetailsActivity.this, "图书不存在,先下载", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent readerActivity = new Intent(this, FBReader.class);
                        FBReader.bookPath = localPath;
                        startActivityForResult(readerActivity, 0);
                        break;
                }
            } else {
                Toast.makeText(BookDetailsActivity.this, "请等待数据写入完成", Toast.LENGTH_SHORT).show();
            }
        });
        //返回
        back.setOnClickListener(view -> BookDetailsActivity.this.finish());
        //添加到书架
        down_book_list.setOnClickListener(view -> {
            if (userId == -1){
                ToastUtil.showWarning(BookDetailsActivity.this,"请先登录");
                return;
            }
            boolean isClick = false;
            if (!isClick){
                isClick = true;
                readTask = new ReadDownTask(BookDetailsActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ReadWriteToSD(BookDetailsActivity.this, msgHandler,readTask);
                    }
                }).start();
            }else{
                ToastUtil.showWarning(BookDetailsActivity.this,"请勿重复点击");
            }
        });
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /** 
    * @date 2018/1/15 17:23 
    * @Description: 获取阅读详情的方法 
    */
    public void getDate(int userId,int bookId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("ebookId", String.valueOf(bookId));
        showLoading();
        OkHttpUtils.get().params(map).url(Address.EBOOKDESC).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        EntityPublic dataOne = response.getEntity().getDataOne();
                        bookTitle.setText(dataOne.getEbookName());
                        price.setText(dataOne.getNowPrice());
                        num.setText(dataOne.getSaleCount()+"");
                        member.setText("正在开发...");
                        GlideUtil.loadImage(BookDetailsActivity.this,
                                Address.IMAGE_NET+dataOne.getEbookImg(), bookImg);
                        bookInfo.setText(dataOne.getEbookInfo());
                        bookAuthor.setText(dataOne.getAuthor());

                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
    * @date 2018/1/15 19:00
    * @Description: 下载到书架的方法
    */
    public void getDownBook(int userId,int bookId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("ebookId", String.valueOf(bookId));
        showLoading();
        OkHttpUtils.get().params(map).url(Address.ADDBOOKLIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                if (!TextUtils.isEmpty(response.toString())) {
//                    try {
                        String message = response.getMessage();
//                        ToastUtil.showWarning(BookDetailsActivity.this,message);
                        isCanReader = true;
                        Toast.makeText(BookDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                    }
                }
            }
        });
//        OkHttpUtils.post().params(map).url(Address.ADDBOOKLIST).build().execute(new PublicEntityCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                ToastUtil.showWarning(BookDetailsActivity.this,"添加出错");
//                cancelLoading();
//            }
//
//            @Override
//            public void onResponse(PublicEntity response, int id) {
//                cancelLoading();
//                if (!TextUtils.isEmpty(response.toString())) {
////                    try {
//                        String message = response.getMessage();
////                        ToastUtil.showWarning(BookDetailsActivity.this,message);
//                        isCanReader = true;
//                        Toast.makeText(BookDetailsActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
////                    } catch (Exception e) {
////                    }
//                }
//            }
//        });
    }
}
