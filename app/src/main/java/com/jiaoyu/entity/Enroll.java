package com.jiaoyu.entity;

import java.io.Serializable;

/**
 * Created by xiangyao on 2017/11/01.
 */

public class Enroll implements Serializable {

    /**
     * id : 265
     * userId : 4735
     * courseId : 47
     * enrollType : 0
     * name : 是打电话
     * mobile : 15141755385
     * position : 姐姐
     * unit : sjskq
     * description : 你说你是孟母三迁
     * createTime : 2017-11-01 20:07:46
     * price : 1500
     */

    private int id;
    private int userId;
    private int courseId;
    private int enrollType;
    private String name;
    private String mobile;
    private String position;
    private String unit;
    private String description;
    private String createTime;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getEnrollType() {
        return enrollType;
    }

    public void setEnrollType(int enrollType) {
        this.enrollType = enrollType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
