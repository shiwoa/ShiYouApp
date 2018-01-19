package com.jiaoyu.shiyou;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jiaoyu.adapter.BookCourseDetailsAdapter;
import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.EntityCourse;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.GlideUtil;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/10.
 * 图书课程详情的类
 */

public class BookCourseDetailsActivity extends BaseActivity{

    private TextView title,recommend; //标题 推荐
    private LinearLayout back,rightLin,teacher; //返回 右侧布局 讲师
    private ImageView rightBtn;
    private RecyclerView contentRec;
    private List<EntityPublic> listLive = new ArrayList<>();
    private BookCourseDetailsAdapter adapter;
    private ScrollView detailsScr;
    private int userId,courseId; //用户id 课程id
    private TextView teacher_tv,teacher_tab,course_desc,people_num
            ,time_num,zhang_num,course_title,price;//专家 专家标签 课程简介 人数 课时 章节数 课程标题 价格
    private ImageView course_icon; //课程图片

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_book_course_details;
    }

    /**
    * @date 2018/1/17 10:14
    * @Description: 获取courseId的方法
    */
    public void getMessageIntent(){
        courseId = getIntent().getIntExtra("courseId",courseId);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getMessageIntent();
        userId = (int) SharedPreferencesUtils.getParam(BookCourseDetailsActivity.this, "userId", -1);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.course_details);
        teacher = (LinearLayout) findViewById(R.id.teacher);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        contentRec = (RecyclerView) findViewById(R.id.content_rec);
        rightLin = (LinearLayout) findViewById(R.id.public_rigthBtn);
        rightLin.setVisibility(View.VISIBLE);
        rightBtn= (ImageView) findViewById(R.id.public_rigth_Btn);
        rightBtn.setBackgroundResource(R.drawable.share);
        detailsScr = (ScrollView) findViewById(R.id.course_details_res);
        showStateLoading(detailsScr);
        teacher_tv = (TextView) findViewById(R.id.teacher_tv);
        teacher_tab = (TextView) findViewById(R.id.teacher_tab);
        course_desc = (TextView) findViewById(R.id.course_desc);
        people_num = (TextView) findViewById(R.id.course_people_num);
        time_num = (TextView) findViewById(R.id.course_time_num);
        zhang_num = (TextView) findViewById(R.id.course_zhang_num);
        course_title = (TextView) findViewById(R.id.course_title);
        price = (TextView) findViewById(R.id.course_price);
        course_icon = (ImageView) findViewById(R.id.course_icon);
        //获取课程详情的方法
        getDate(userId,courseId);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //专家
        teacher.setOnClickListener(view -> openActivity(BookCourseTeacherActivity.class));
        //返回
        back.setOnClickListener(view -> BookCourseDetailsActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /**
    * @date 2018/1/17 10:00
    * @Description: 获取课程详情的方法
    */
    public void getDate(int userId,int courseId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("courseId", String.valueOf(courseId));
        OkHttpUtils.get().params(map).url(Address.COURSE_INFO).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showStateError();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                showStateContent();
                if (!TextUtils.isEmpty(response.toString())) {
//                    try {
                        String message = response.getMessage();
                        if (response.isSuccess()){
                            EntityCourse course = response.getEntity().getCourse();
                            if (!TextUtils.isEmpty(course.getOwnerName())){
                                teacher_tv.setText("专家："+course.getOwnerName());
                            }else{
                                teacher_tv.setText(R.string.nodata);
                            }
                            if (!TextUtils.isEmpty(course.getColTagNames())){
                                teacher_tab.setText(course.getColTagNames());
                            }else{
                                teacher_tab.setText(R.string.nodata);
                            }
                            course_desc.setText(course.getTitle());
                            people_num.setText(course.getBuycount()+"人");
                            time_num.setText(course.getLessionnum()+"");
                            course_title.setText(course.getName());
                            price.setText("购买￥ "+course.getCurrentprice());
                            GlideUtil.loadImage(BookCourseDetailsActivity.this,Address.IMAGE_NET+
                            course.getMobileLogo(),course_icon);
                            if (listLive != null){
                                listLive.clear();
                            }
                            List<EntityPublic> courseKpoints = response.getEntity().getCourseKpoints();
                            zhang_num.setText("共"+courseKpoints.size()+"条");
                            if (courseKpoints.size() != 0 ){
                                listLive.addAll(courseKpoints);
                                contentRec.setLayoutManager(new LinearLayoutManager(BookCourseDetailsActivity.this));
                                contentRec.setNestedScrollingEnabled(false);
                                adapter = new BookCourseDetailsAdapter(R.layout.item_course_details_content,BookCourseDetailsActivity.this,listLive);
                                contentRec.setAdapter(adapter);
                                adapter.setOnItemClickListener((adapter1, view, position) -> openActivity(BookCoursePlayActivity.class));
                            }else{
                                showStateEmpty();
                            }
                        }else{
                            showStateError();
                            ToastUtil.showWarning(BookCourseDetailsActivity.this,message);
                        }

//                    } catch (Exception e) {
//                    }
                }
            }
        });
    }

}
