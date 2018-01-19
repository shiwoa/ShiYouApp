package com.jiaoyu.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.MyBookListAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.shiyou.BookDetailsActivity;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.LogUtils;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.w3c.dom.Text;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/8.
 * 我的书架的fragment
 */

public class MyBookFragment extends BaseFragment{

   private List<EntityPublic> datalist = new ArrayList<>();
   private MyBookListAdapter adapter;
   private NoScrollGridView dataGrid;
   private LinearLayout no_data; //没有数据
   private int userId,page = 1; //用户id 当前页
   private ScrollView book_scr;
   private TextView no_data_tv;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_my_book;
    }

    @Override
    public void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        getData(userId,page);
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        dataGrid = findViewById(R.id.my_book_grid);
        no_data = findViewById(R.id.no_data);
        book_scr = findViewById(R.id.book_scr);
        no_data.setVisibility(View.VISIBLE);
        no_data_tv = findViewById(R.id.no_data_tv);
//        showStateLoading(book_scr);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {

    }



    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getData(userId,page);
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {
        initData();
    }

    /**
    * @date 2018/1/16 9:34
    * @Description: 获取收藏图书的方法
    */
    public void getData(int userId,int page) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("page.currentPagelong", String.valueOf(page));
        OkHttpUtils.get().params(map).url(Address.MYBOOKLIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                no_data_tv.setText(R.string.on_retry);
                book_scr.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
//                showStateContent();
                if (!TextUtils.isEmpty(response.toString())) {
//                    try {
                        String message = response.getMessage();
                        if (response.isSuccess()){
                            if (response.getEntity().getDataList() != null){
                                if (datalist != null){
                                    datalist.clear();
                                }
                                    List<EntityPublic> list = response.getEntity().getDataList();
                                    if (list.size() != 0){
                                        book_scr.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                        datalist.addAll(list);
                                        adapter = new MyBookListAdapter(getActivity(),datalist);
                                        dataGrid.setAdapter(adapter);
                                    }else{
                                        no_data_tv.setText(R.string.no_data);
                                        book_scr.setVisibility(View.GONE);
                                        no_data.setVisibility(View.VISIBLE);
                                    }
                            }else{
                                book_scr.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);
                                if (userId == -1){
                                    no_data_tv.setText(R.string.no_login);
                                }else {
                                    no_data_tv.setText(R.string.no_data);
                                }
                            }

                        }else{
                            ToastUtil.showWarning(getActivity(),message);
                        }
//                    } catch (Exception e) {
//                    }
                }
            }
        });
    }


}
