package com.jiaoyu.shiyou;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookLiveNowAdapter;
import com.jiaoyu.adapter.BookRecommendAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/9.
 * 选择推荐的人的类
 */

public class BookRecommendActivity extends BaseActivity{

    private TextView title,rightTv; //标题 右侧文字
    private LinearLayout back,rightLin; //返回 右侧布局
    private RecyclerView recommendRec;
    private BookRecommendAdapter adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Boolean> isSelectList = new ArrayList<>();
    private Dialog dialog;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_recommend;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.recommend_select);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        recommendRec = (RecyclerView) findViewById(R.id.recommend_rec);
        rightLin = (LinearLayout) findViewById(R.id.public_rigthTv);
        rightLin.setVisibility(View.VISIBLE);
        rightTv = (TextView) findViewById(R.id.public_rigth_Tv);
        rightTv.setText(R.string.confirm);
        for (int i = 0; i<8; i++){
            dataList.add("测试推荐姓名"+i);
            isSelectList.add(i,false);
        }
        recommendRec.setLayoutManager(new LinearLayoutManager(BookRecommendActivity.this));
        recommendRec.setNestedScrollingEnabled(false);
        adapter = new BookRecommendAdapter(R.layout.item_book_commend,dataList,isSelectList);
        recommendRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->{
                    if (isSelectList.get(position)){
                        isSelectList.set(position,false);
                    }else{
                        isSelectList.set(position,true);
                    }
                    adapter.setIsSelect(isSelectList);
                    adapter.notifyDataSetChanged();
                });
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //确认
        rightLin.setOnClickListener(view -> confirmDiaLog());
        //返回
        back.setOnClickListener(view -> BookRecommendActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    // 确认时显示的diaLog
    public void confirmDiaLog() {
        View view = LayoutInflater.from(BookRecommendActivity.this).inflate(
                R.layout.recommend_show, null);
        WindowManager manager = (WindowManager) BookRecommendActivity.this.getSystemService(
                Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(BookRecommendActivity.this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        //确认
        TextView linbtnsure = (TextView) view.findViewById(R.id.recommend_show_confirm);
        linbtnsure.setOnClickListener(view1 -> dialog.cancel());
    }

}
