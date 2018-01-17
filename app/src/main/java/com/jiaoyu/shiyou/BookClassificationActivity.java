package com.jiaoyu.shiyou;

import android.text.TextUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.BookClassAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.login.LoginActivity;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.LogUtils;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/10.
 * 书城-本社图书的类
 */

public class BookClassificationActivity extends BaseActivity{

    private TextView title; //标题
    private LinearLayout back; //返回
    private GridView classGrud;
    private List<EntityPublic> datalist = new ArrayList<>();
    private BookClassAdapter adapter;
    private int userId;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_classification;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.classification);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        classGrud = (GridView) findViewById(R.id.book_class_grid);
        //获取图书分类的方法
        getData(userId);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> BookClassificationActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {
        //获取图书分类的方法
        getData(userId);
    }

    /** 
    * @date 2018/1/14 14:17 
    * @Description: 获取电子书分类的方法
    */
    public void getData(int userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        showLoading();
        OkHttpUtils.get().params(map).url(Address.BOOKTYPE).build().execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        cancelLoading();
                        if (!TextUtils.isEmpty(response.toString())) {
                            try {
                                String message = response.getMessage();
                                if (response.isSuccess()) {

                                    List<EntityPublic> subjectList = response.getEntity().getSubjectList();
                                        datalist.addAll(subjectList);
                                      adapter = new BookClassAdapter(BookClassificationActivity.this,datalist);
                                      classGrud.setAdapter(adapter);
                                } else {
                                    ToastUtil.showWarning(BookClassificationActivity.this,message);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                });
    }
}
