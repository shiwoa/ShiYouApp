package com.jiaoyu.shiyou;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.adapter.BookCourseDetailsAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bishuang on 2018/1/10.
 * 图书课程详情的类
 */

public class BookCourseDetailsActivity extends BaseActivity{

    private TextView title,recommend; //标题 推荐
    private LinearLayout back,rightLin; //返回 右侧布局
    private ImageView rightBtn;
    private RecyclerView contentRec;
    private List<String> listLive = new ArrayList<>();
    private BookCourseDetailsAdapter adapter;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_course_details;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.course_details);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        contentRec = (RecyclerView) findViewById(R.id.content_rec);
        rightLin = (LinearLayout) findViewById(R.id.public_rigthBtn);
        rightLin.setVisibility(View.VISIBLE);
        rightBtn= (ImageView) findViewById(R.id.public_rigth_Btn);
        rightBtn.setBackgroundResource(R.drawable.share);
        if (listLive != null){
            listLive.clear();
        }
        for (int i = 0;i<6; i++){
            listLive.add("章节测试标题"+i);
        }
        contentRec.setLayoutManager(new LinearLayoutManager(BookCourseDetailsActivity.this));
        contentRec.setNestedScrollingEnabled(false);
        adapter = new BookCourseDetailsAdapter(R.layout.item_course_details_content,BookCourseDetailsActivity.this,listLive);
        contentRec.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->
                ToastUtil.showNormal(BookCourseDetailsActivity.this,"努力开发中"+position));
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //返回
        back.setOnClickListener(view -> BookCourseDetailsActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }
}
