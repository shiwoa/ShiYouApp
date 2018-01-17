package com.jiaoyu.utils;

/**
 * Created by bishuang on 2018/1/12.
 */

public class Address {
    // 主域名
    public static String HOST = "http://192.168.1.94:81/app/";
    // 访问图片
    public static String IMAGE_NET = "http://static.268xue.com";
    // 获取sgin
    public static String GET_SGIN = HOST+"getMobileKey";
    // 获取手机验证码
    public static String GET_PHONE_CODE = HOST + "sendMobileMessage";
    // 注册接口
    public static String REGISTER = HOST + "register";
    // 登录接口
    public static String LOGIN = HOST + "login";
    // 图书所有分类
    public static String BOOKTYPE = HOST + "ebook/getEbookTypes";
    // 书城石油专区
    public static String BOOKSYLIST = HOST + "ebook/getEbookList";
    // 阅读详情
    public static String EBOOKDESC = HOST + "ebook/getEbookDesc";
    // 添加到书架
    public static String ADDBOOKLIST = HOST + "ebook/addToEbookShelf";
    //我的书架
    public static String MYBOOKLIST = HOST + "ebook/getMyEbookShelf";
    //课程列表
    public static String COURSE_LIST = HOST + "course/list";

}
