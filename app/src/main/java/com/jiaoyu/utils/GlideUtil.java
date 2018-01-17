package com.jiaoyu.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiaoyu.application.DemoApplication;
import com.jiaoyu.shiyou.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by admin on 2017/7/4.
 * 加载图片工具类
 */

public class GlideUtil {

    /**
     * 加载图片
     *  @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(DemoApplication.getAppContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher) //设置占位图，在加载之前显示
                .error(R.mipmap.ic_launcher) //在图像加载失败时显示
                .fallback(R.mipmap.ic_launcher)
                .crossFade()
                .into(imageView);
    }




    public static void ImageGetter(){

    }
    /**
     * 加载圆形图片
     *
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(DemoApplication.getAppContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.defined_user_head) //设置占位图，在加载之前显示
                .error(R.drawable.defined_user_head) //在图像加载失败时显示
                .fallback(R.drawable.defined_user_head)
                .bitmapTransform(new CropCircleTransformation(DemoApplication.getAppContext()))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆形头像
     *
     * @param url
     * @param imageView
     */
    public static void loadCircleHeadImage(Context context, String url, ImageView imageView) {
        Glide.with(DemoApplication.getAppContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.defined_user_head) //设置占位图，在加载之前显示
                .error(R.drawable.defined_user_head) //在图像加载失败时显示
                .fallback(R.drawable.defined_user_head)
                .bitmapTransform(new CropCircleTransformation(DemoApplication.getAppContext()))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundedImage(Context context, String url, ImageView imageView) {
        Glide.with(DemoApplication.getAppContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.defined_image) //设置占位图，在加载之前显示
                .error(R.drawable.defined_image) //在图像加载失败时显示
                .fallback(R.drawable.defined_image)
                .bitmapTransform(new RoundedCornersTransformation(DemoApplication.getAppContext(), 80, 0, RoundedCornersTransformation.CornerType.ALL))
                .crossFade()
                .into(imageView);
    }


}
