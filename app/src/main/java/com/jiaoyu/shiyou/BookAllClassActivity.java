package com.jiaoyu.shiyou;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.BookAllClassAdapter;
import com.jiaoyu.adapter.BookAllClassListAdapter;
import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/18.
 * 书城-专区—全部-分类
 */

public class BookAllClassActivity extends BaseActivity{

    private BookAllClassAdapter adapter;
    private RecyclerView recyclerView;
    private List<EntityPublic> listLive = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();
    private String type; //shiyou石油 shequ社区

    private TextView title,no_data; //标题 暂无数据
    private LinearLayout back; //返回
    private int userId; //用户名
    private NoScrollGridView gridView;
    private BookAllClassListAdapter bookAdapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_all_class;
    }

    public void getMessageIntent(){
        type = getIntent().getStringExtra("type");
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getMessageIntent();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        recyclerView = findViewById(R.id.class_rel);
        title = (TextView) findViewById(R.id.public_head_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        no_data = findViewById(R.id.no_data);
        gridView = findViewById(R.id.grid);
        if (type.equals("shiyou")){
            getData(userId,1);
        }else{
            getData(userId,2);
        }

        if (dataList != null){
            dataList.clear();
        }
        for (int i = 0;i<16; i++){
            if (type.equals("shiyou")){
                dataList.add("石油列表测试标题"+i);
            }else{
                dataList.add("社区列表测试标题" + i);
            }

        }
        bookAdapter = new BookAllClassListAdapter(BookAllClassActivity.this,dataList);
        gridView.setAdapter(bookAdapter);
    }


    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> BookAllClassActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /**
     * @date 2018/1/14 14:17
     * @Description: 获取电子书分类的方法
     */
    public void getData(int userId,int level) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("level", String.valueOf(level));
        OkHttpUtils.get().params(map).url(Address.BOOKTYPE).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            if (listLive != null){
                                listLive.clear();
                            }
                            List<EntityPublic> subjectList = response.getEntity().getSubjectList();
                            if (subjectList.size() != 0){
                                recyclerView.setVisibility(View.VISIBLE);
                                no_data.setVisibility(View.GONE);
                                listLive.addAll(subjectList);
                                if (type.equals("shiyou")){
                                    title.setText("石油专区");
                                }else{
                                    title.setText("社科专区");
                                }
                                LinearLayoutManager ms= new LinearLayoutManager(BookAllClassActivity.this);
                                recyclerView.setLayoutManager(ms);
                                ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
                                adapter = new BookAllClassAdapter(R.layout.item_all_class,BookAllClassActivity.this,listLive);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener((adapter1, view, position) ->{
                                            adapter.getSelect(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                );
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);
                            }

                        } else {
                            ToastUtil.showWarning(BookAllClassActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
