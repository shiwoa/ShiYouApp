package com.jiaoyu.entity;

import java.io.Serializable;

/**
 * @author bin
 *         修改人:
 *         时间:2015-10-12 上午10:33:30
 *         类说明:教师的实体类
 */
public class TeacherEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String education;
    private int isStar;
    private String picPath;
    private int status;
    private String createTime;
    private String updateTime;

    /**
     * 面谈价格
     */
    private int faceSubscribePrice;

    /**
     * 视频价格
     */
    private int videoSubscribePrice;
    /**
     * 评分
     */
    private float avgGrade;
    /**
     * 认证数
     */
    private int certificationCount;
    /**
     * 预约数
     */
    private int countSub;
    /**
     * 生涯
     */
    private String career;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getFaceSubscribePrice() {
        return faceSubscribePrice;
    }

    public void setFaceSubscribePrice(int faceSubscribePrice) {
        this.faceSubscribePrice = faceSubscribePrice;
    }

    public int getVideoSubscribePrice() {
        return videoSubscribePrice;
    }

    public void setVideoSubscribePrice(int videoSubscribePrice) {
        this.videoSubscribePrice = videoSubscribePrice;
    }

    public float getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(float avgGrade) {
        this.avgGrade = avgGrade;
    }

    public int getCertificationCount() {
        return certificationCount;
    }

    public void setCertificationCount(int certificationCount) {
        this.certificationCount = certificationCount;
    }

    public int getCountSub() {
        return countSub;
    }

    public void setCountSub(int countSub) {
        this.countSub = countSub;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
