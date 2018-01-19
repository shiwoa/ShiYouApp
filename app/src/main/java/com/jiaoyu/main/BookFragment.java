package com.jiaoyu.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sliderlibrary.SliderLayout;
import com.example.sliderlibrary.SliderTypes.BaseSliderView;
import com.example.sliderlibrary.SliderTypes.TextSliderView;
import com.jiaoyu.adapter.BookAdapter;
import com.jiaoyu.adapter.BookClassAdapter;
import com.jiaoyu.base.BaseFragment;
import com.jiaoyu.entity.EntityPublic;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.shiyou.BookAllClassActivity;
import com.jiaoyu.shiyou.BookClassificationActivity;
import com.jiaoyu.shiyou.BookDetailsActivity;
import com.jiaoyu.shiyou.BookLiveListActivity;
import com.jiaoyu.shiyou.BookSearchActivity;
import com.jiaoyu.shiyou.R;
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
 * Created by bishuang on 2018/1/5.
 * 图书的fragment
 */

public class BookFragment extends BaseFragment{

    private static BookFragment bookFragment;
    private NoScrollGridView shiyouGrid,shekeGrid; //石油列表 社科列表
    private List<EntityPublic> datalist = new ArrayList<>();
    private List<EntityPublic> datashekelist = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>(); //bannerList
    private BookAdapter shiyouAdapter,shekeAdapter;
    private SliderLayout homeBannerLayout; //banner图
    private LinearLayout liveList,classList,tencentList; //直播课程 本社图书 腾讯阅读
    private Dialog dialog;
    private TextView search_tv,nodata_sy,nodata_sq; //搜索 无数据石油 无数据社区
    private int userId; //用户名
    private LinearLayout all_shequ,all_shiyou; //全部社区 全部石油

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.fra_main_book;
    }

    /**
     * 获取本类对象的方法
     */
    public static BookFragment getInstance() {
        if (bookFragment == null) {
            bookFragment = new BookFragment();
        }
        return bookFragment;
    }

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initComponent(Bundle savedInstanceState) {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        shiyouGrid = findViewById(R.id.shiyou_grid);
        shekeGrid = findViewById(R.id.sheke_grid);
        homeBannerLayout = findViewById(R.id.home_banner_layout);
        liveList = findViewById(R.id.book_live_list);
        classList = findViewById(R.id.book_class_list);
        tencentList = findViewById(R.id.book_tencent_list);
        search_tv = findViewById(R.id.search_tv);
        nodata_sy = findViewById(R.id.nodata_sy);
        nodata_sq = findViewById(R.id.nodata_sq);
        all_shequ = findViewById(R.id.all_shequ);
        all_shiyou = findViewById(R.id.all_shiyou);
    }

    /**
     * 添加监听
     */
    @Override
    protected void initListener() {
        Intent intent = new Intent();
        //石油查看全部
        all_shiyou.setOnClickListener(view -> {
            intent.setClass(getActivity(), BookAllClassActivity.class);
            intent.putExtra("type","shiyou");
            startActivity(intent);
        });
        //石油查看全部
        all_shequ.setOnClickListener(view -> {
            intent.setClass(getActivity(), BookAllClassActivity.class);
            intent.putExtra("type","shequ");
            startActivity(intent);
        });
        //搜索
        search_tv.setOnClickListener(view -> openActivity(BookSearchActivity.class));
        //知道啦
        tencentList.setOnClickListener(view -> confirmDiaLog());
        //本社图书
        classList.setOnClickListener(view -> openActivity(BookClassificationActivity.class));
        //直播课程
        liveList.setOnClickListener(view->openActivity(BookLiveListActivity.class));
        //石油列表
        shiyouGrid.setOnItemClickListener((adapterView, view, i, l) -> {
            intent.setClass(getActivity(),BookDetailsActivity.class);
            intent.putExtra("ebookId",datalist.get(i).getId());
            startActivity(intent);
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (datalist != null){
            datalist.clear();
            datashekelist.clear();
            bannerList.clear();
        }

        bannerList.add("http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468745418218035.jpg");
        bannerList.add("http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468735497415340.jpg");
        bannerList.add("http://static.268xue.com/upload/eduplat/bannerImages/20150721/1437468754570856573.jpg");

//        for (int i = 0; i < 6; i++){
//            datalist.add("石油测试标题"+i);
//        }
//        shiyouAdapter = new BookAdapter(getActivity(),datalist);
//        shiyouGrid.setAdapter(shiyouAdapter);

        //获取石油专区列表的方法
        getSYList(userId);
//        for (int i = 0; i < 2; i++){
//            datashekelist.add("社科测试标题"+i);
//        }
//        shekeAdapter = new BookAdapter(getActivity(),datashekelist);
//        shekeGrid.setAdapter(shekeAdapter);
        initBannerLayout();
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }


    /**
     * 初始化轮播图
     */
    private void initBannerLayout() {
        for (int i = 0; i < bannerList.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.image(bannerList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

                        @Override
                        public void onSliderClick(BaseSliderView slider) {
//                            Intent intent = new Intent();
//                            intent.setClass(getActivity(), CourseDetails96kActivity.class);
//                            intent.putExtra("courseId", bannerList.get(homeBannerLayout.getCurrentPosition()).getCourseId());
//                            startActivity(intent);
                            ToastUtil.showNormal(getActivity(),"点击"+homeBannerLayout.getCurrentPosition());
                        }
                    });
            homeBannerLayout.addSlider(textSliderView);
        }
        homeBannerLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        homeBannerLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        homeBannerLayout.setDuration(3000);
    }

    /**
    * @date 2018/1/14 15:19
    * @Description: 获取石油专区列表的方法
    */
    public void getSYList(int userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        showLoading();
        OkHttpUtils.get().params(map).url(Address.BOOKSYLIST).build().execute(new PublicEntityCallback() {
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
                        EntityPublic entity = response.getEntity();
                        if (response.isSuccess()) {
                            List<EntityPublic> shiyouList = entity.getShiyouList();
                            if (shiyouList!= null){
                                datalist.addAll(shiyouList);
                                shiyouAdapter = new BookAdapter(getActivity(),datalist);
                                shiyouGrid.setAdapter(shiyouAdapter);
                            }else{
                                shiyouGrid.setVisibility(View.GONE);
                                nodata_sy.setVisibility(View.VISIBLE);
                            }
                            List<EntityPublic> shekeList = entity.getShekeList();
                            if (shekeList != null){
                                datashekelist.addAll(shekeList);
                                shekeAdapter = new BookAdapter(getActivity(),datashekelist);
                                shekeGrid.setAdapter(shekeAdapter);
                            }else{
                                shekeGrid.setVisibility(View.GONE);
                                nodata_sq.setVisibility(View.VISIBLE);
                            }

                        } else {
                            shiyouGrid.setVisibility(View.GONE);
                            nodata_sy.setVisibility(View.VISIBLE);
                            shekeGrid.setVisibility(View.GONE);
                            nodata_sq.setVisibility(View.VISIBLE);
                            ToastUtil.showWarning(getActivity(),message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    // 确认时显示的diaLog
    public void confirmDiaLog() {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_tencent_show, null);
        WindowManager manager = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        //确认
        TextView know = (TextView) view.findViewById(R.id.know);
        know.setOnClickListener(view1 -> dialog.cancel());
    }

}
