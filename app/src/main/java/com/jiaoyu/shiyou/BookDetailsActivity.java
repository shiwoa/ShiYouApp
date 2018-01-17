package com.jiaoyu.shiyou;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.login.LoginActivity;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.GlideUtil;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/9.
 * 图书详情的类
 */

public class BookDetailsActivity extends BaseActivity{

    private TextView title,recommend; //标题 推荐
    private LinearLayout back,down_book_list; //返回 下载到书架
    private int ebookId,userId; //图书id
    private TextView bookTitle,num,price,member,bookInfo,bookAuthor;//图书标题 观看量 价格 会员 简介 作者
    private ImageView bookImg; //图书图片

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
        getDate(userId,ebookId);
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
        recommend.setOnClickListener(view -> openActivity(BookRecommendActivity.class));
        //返回
        back.setOnClickListener(view -> BookDetailsActivity.this.finish());
        //添加到书架
        down_book_list.setOnClickListener(view -> {
            getDownBook(userId,ebookId);
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
        map.put("ebookIdlong", String.valueOf(bookId));
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
                    try {
                        String message = response.getMessage();
                        ToastUtil.showWarning(BookDetailsActivity.this,message);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
