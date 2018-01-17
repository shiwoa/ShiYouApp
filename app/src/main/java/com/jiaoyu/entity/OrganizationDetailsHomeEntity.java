package com.jiaoyu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xm8-02 on 2017/10/24.
 */

public class OrganizationDetailsHomeEntity  implements Serializable {

    /**
     * message : 查询成功
     * success : true
     * entity : {"userNum":0,"popular":[{"id":58,"name":"平台分公司课程001（公开）","isavaliable":2,"addtime":"2017-08-23 06:14:23","isPay":0,"sourceprice":0,"currentprice":0,"enterprisePrice":554,"title":"fafa","context":"asd","coursetag":"","logo":"/upload/mavendemo/course/20170831/1504143239876437163.jpg","mobileLogo":"/upload/mavendemo/course/20170831/1504143241576842581.jpg","packageLogo":"","updateTime":"2017-08-31 09:34:22","losetype":1,"loseTime":"50","updateuser":"\"admin888\"","pageViewcount":16,"sellType":"COURSE","teacherList":["李燕珍"],"sort":0,"status":0,"studyType":-1,"review":0,"peopleNum":0,"companyId":96,"enrollStatus":0},{"id":60,"name":"平台分公司课程套餐（公开）","isavaliable":2,"addtime":"2017-08-23 21:51:43","isPay":0,"sourceprice":0,"currentprice":0,"enterprisePrice":5,"title":"asdasd","context":"asd","coursetag":"","logo":"/upload/mavendemo/course/20170829/1503989311393836752.jpg","mobileLogo":"/upload/mavendemo/course/20170829/1503989313687370197.jpg","packageLogo":"/upload/mavendemo/course/20170829/1503989315762699691.jpg","updateTime":"2017-08-29 14:48:50","losetype":1,"loseTime":"50","updateuser":"\"admin888\"","pageViewcount":16,"sellType":"PACKAGE","teacherList":["马化腾"],"sort":0,"status":0,"studyType":-1,"review":0,"peopleNum":0,"companyId":96,"enrollStatus":0}],"company":{"id":96,"name":"成都开普锐科技有限公司","status":1,"createTime":"2017-09-14 10:03:14","applyName":"杨梅","applyMobile":"13982254197","applyEmail":"null","capacity":0,"purchaseTime":0,"parentId":0,"institutionsType":"ORGANIZATION"},"newest":[{"id":60,"name":"平台分公司课程套餐（公开）","addtime":"2017-08-23 21:51:43","isPay":0,"enterprisePrice":5,"title":"asdasd","lessionnum":0,"logo":"/upload/mavendemo/course/20170829/1503989311393836752.jpg","losetype":0,"pageBuycount":0,"pageViewcount":16,"sellType":"PACKAGE","status":0,"studyType":0,"review":0,"peopleNum":0,"enrollStatus":0},{"id":58,"name":"平台分公司课程001（公开）","addtime":"2017-08-23 06:14:23","isPay":0,"enterprisePrice":554,"title":"fafa","lessionnum":0,"logo":"/upload/mavendemo/course/20170831/1504143239876437163.jpg","losetype":0,"pageBuycount":0,"pageViewcount":16,"sellType":"COURSE","status":0,"studyType":0,"review":0,"peopleNum":0,"enrollStatus":0}],"teacherNum":1,"courseNum":2}
     */

    private String message;
    private boolean success;
    private EntityBean entity;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public EntityBean getEntity() {
        return entity;
    }

    public void setEntity(EntityBean entity) {
        this.entity = entity;
    }

    public static class EntityBean {
        /**
         * userNum : 0
         * popular : [{"id":58,"name":"平台分公司课程001（公开）","isavaliable":2,"addtime":"2017-08-23 06:14:23","isPay":0,"sourceprice":0,"currentprice":0,"enterprisePrice":554,"title":"fafa","context":"asd","coursetag":"","logo":"/upload/mavendemo/course/20170831/1504143239876437163.jpg","mobileLogo":"/upload/mavendemo/course/20170831/1504143241576842581.jpg","packageLogo":"","updateTime":"2017-08-31 09:34:22","losetype":1,"loseTime":"50","updateuser":"\"admin888\"","pageViewcount":16,"sellType":"COURSE","teacherList":["李燕珍"],"sort":0,"status":0,"studyType":-1,"review":0,"peopleNum":0,"companyId":96,"enrollStatus":0},{"id":60,"name":"平台分公司课程套餐（公开）","isavaliable":2,"addtime":"2017-08-23 21:51:43","isPay":0,"sourceprice":0,"currentprice":0,"enterprisePrice":5,"title":"asdasd","context":"asd","coursetag":"","logo":"/upload/mavendemo/course/20170829/1503989311393836752.jpg","mobileLogo":"/upload/mavendemo/course/20170829/1503989313687370197.jpg","packageLogo":"/upload/mavendemo/course/20170829/1503989315762699691.jpg","updateTime":"2017-08-29 14:48:50","losetype":1,"loseTime":"50","updateuser":"\"admin888\"","pageViewcount":16,"sellType":"PACKAGE","teacherList":["马化腾"],"sort":0,"status":0,"studyType":-1,"review":0,"peopleNum":0,"companyId":96,"enrollStatus":0}]
         * company : {"id":96,"name":"成都开普锐科技有限公司","status":1,"createTime":"2017-09-14 10:03:14","applyName":"杨梅","applyMobile":"13982254197","applyEmail":"null","capacity":0,"purchaseTime":0,"parentId":0,"institutionsType":"ORGANIZATION"}
         * newest : [{"id":60,"name":"平台分公司课程套餐（公开）","addtime":"2017-08-23 21:51:43","isPay":0,"enterprisePrice":5,"title":"asdasd","lessionnum":0,"logo":"/upload/mavendemo/course/20170829/1503989311393836752.jpg","losetype":0,"pageBuycount":0,"pageViewcount":16,"sellType":"PACKAGE","status":0,"studyType":0,"review":0,"peopleNum":0,"enrollStatus":0},{"id":58,"name":"平台分公司课程001（公开）","addtime":"2017-08-23 06:14:23","isPay":0,"enterprisePrice":554,"title":"fafa","lessionnum":0,"logo":"/upload/mavendemo/course/20170831/1504143239876437163.jpg","losetype":0,"pageBuycount":0,"pageViewcount":16,"sellType":"COURSE","status":0,"studyType":0,"review":0,"peopleNum":0,"enrollStatus":0}]
         * teacherNum : 1
         * courseNum : 2
         */

        private int userNum;
        private CompanyBean company;
        private int teacherNum;
        private int courseNum;
        private List<PopularBean> popular;
        private List<PopularBean> newest;

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }

        public CompanyBean getCompany() {
            return company;
        }

        public void setCompany(CompanyBean company) {
            this.company = company;
        }

        public int getTeacherNum() {
            return teacherNum;
        }

        public void setTeacherNum(int teacherNum) {
            this.teacherNum = teacherNum;
        }

        public int getCourseNum() {
            return courseNum;
        }

        public void setCourseNum(int courseNum) {
            this.courseNum = courseNum;
        }

        public List<PopularBean> getPopular() {
            return popular;
        }

        public void setPopular(List<PopularBean> popular) {
            this.popular = popular;
        }

        public List<PopularBean> getNewest() {
            return newest;
        }

        public void setNewest(List<PopularBean> newest) {
            this.newest = newest;
        }

        public static class CompanyBean {
            /**
             * id : 96
             * name : 成都开普锐科技有限公司
             * status : 1
             * createTime : 2017-09-14 10:03:14
             * applyName : 杨梅
             * applyMobile : 13982254197
             * applyEmail : null
             * capacity : 0
             * purchaseTime : 0
             * parentId : 0
             * institutionsType : ORGANIZATION
             */

            private int id;
            private String name;
            private int status;
            private String createTime;
            private String applyName;
            private String applyMobile;
            private String applyEmail;
            private int capacity;
            private int purchaseTime;
            private int parentId;
            private String institutionsType;

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

            public String getApplyName() {
                return applyName;
            }

            public void setApplyName(String applyName) {
                this.applyName = applyName;
            }

            public String getApplyMobile() {
                return applyMobile;
            }

            public void setApplyMobile(String applyMobile) {
                this.applyMobile = applyMobile;
            }

            public String getApplyEmail() {
                return applyEmail;
            }

            public void setApplyEmail(String applyEmail) {
                this.applyEmail = applyEmail;
            }

            public int getCapacity() {
                return capacity;
            }

            public void setCapacity(int capacity) {
                this.capacity = capacity;
            }

            public int getPurchaseTime() {
                return purchaseTime;
            }

            public void setPurchaseTime(int purchaseTime) {
                this.purchaseTime = purchaseTime;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getInstitutionsType() {
                return institutionsType;
            }

            public void setInstitutionsType(String institutionsType) {
                this.institutionsType = institutionsType;
            }
        }

        public static class PopularBean {
            /**
             * id : 58
             * name : 平台分公司课程001（公开）
             * isavaliable : 2
             * addtime : 2017-08-23 06:14:23
             * isPay : 0
             * sourceprice : 0.0
             * currentprice : 0.0
             * enterprisePrice : 554.0
             * title : fafa
             * context : asd
             * coursetag :
             * logo : /upload/mavendemo/course/20170831/1504143239876437163.jpg
             * mobileLogo : /upload/mavendemo/course/20170831/1504143241576842581.jpg
             * packageLogo :
             * updateTime : 2017-08-31 09:34:22
             * losetype : 1
             * loseTime : 50
             * updateuser : "admin888"
             * pageViewcount : 16
             * sellType : COURSE
             * teacherList : ["李燕珍"]
             * sort : 0
             * status : 0
             * studyType : -1
             * review : 0
             * peopleNum : 0
             * companyId : 96
             * enrollStatus : 0
             */

            private int id;
            private String name;
            private int isavaliable;
            private String addtime;
            private int isPay;
            private double sourceprice;
            private double currentprice;
            private double enterprisePrice;
            private String title;
            private String context;
            private String coursetag;
            private String logo;
            private String mobileLogo;
            private String packageLogo;
            private String updateTime;
            private int losetype;
            private String loseTime;
            private String updateuser;
            private int pageViewcount;
            private String sellType;
            private int sort;
            private int status;
            private int studyType;
            private int review;
            private int peopleNum;
            private int companyId;
            private int enrollStatus;
            private List<String> teacherList;

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

            public int getIsavaliable() {
                return isavaliable;
            }

            public void setIsavaliable(int isavaliable) {
                this.isavaliable = isavaliable;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public int getIsPay() {
                return isPay;
            }

            public void setIsPay(int isPay) {
                this.isPay = isPay;
            }

            public double getSourceprice() {
                return sourceprice;
            }

            public void setSourceprice(double sourceprice) {
                this.sourceprice = sourceprice;
            }

            public double getCurrentprice() {
                return currentprice;
            }

            public void setCurrentprice(double currentprice) {
                this.currentprice = currentprice;
            }

            public double getEnterprisePrice() {
                return enterprisePrice;
            }

            public void setEnterprisePrice(double enterprisePrice) {
                this.enterprisePrice = enterprisePrice;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getCoursetag() {
                return coursetag;
            }

            public void setCoursetag(String coursetag) {
                this.coursetag = coursetag;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getMobileLogo() {
                return mobileLogo;
            }

            public void setMobileLogo(String mobileLogo) {
                this.mobileLogo = mobileLogo;
            }

            public String getPackageLogo() {
                return packageLogo;
            }

            public void setPackageLogo(String packageLogo) {
                this.packageLogo = packageLogo;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getLosetype() {
                return losetype;
            }

            public void setLosetype(int losetype) {
                this.losetype = losetype;
            }

            public String getLoseTime() {
                return loseTime;
            }

            public void setLoseTime(String loseTime) {
                this.loseTime = loseTime;
            }

            public String getUpdateuser() {
                return updateuser;
            }

            public void setUpdateuser(String updateuser) {
                this.updateuser = updateuser;
            }

            public int getPageViewcount() {
                return pageViewcount;
            }

            public void setPageViewcount(int pageViewcount) {
                this.pageViewcount = pageViewcount;
            }

            public String getSellType() {
                return sellType;
            }

            public void setSellType(String sellType) {
                this.sellType = sellType;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getStudyType() {
                return studyType;
            }

            public void setStudyType(int studyType) {
                this.studyType = studyType;
            }

            public int getReview() {
                return review;
            }

            public void setReview(int review) {
                this.review = review;
            }

            public int getPeopleNum() {
                return peopleNum;
            }

            public void setPeopleNum(int peopleNum) {
                this.peopleNum = peopleNum;
            }

            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public int getEnrollStatus() {
                return enrollStatus;
            }

            public void setEnrollStatus(int enrollStatus) {
                this.enrollStatus = enrollStatus;
            }

            public List<String> getTeacherList() {
                return teacherList;
            }

            public void setTeacherList(List<String> teacherList) {
                this.teacherList = teacherList;
            }
        }

        public static class NewestBean {
            /**
             * id : 60
             * name : 平台分公司课程套餐（公开）
             * addtime : 2017-08-23 21:51:43
             * isPay : 0
             * enterprisePrice : 5.0
             * title : asdasd
             * lessionnum : 0
             * logo : /upload/mavendemo/course/20170829/1503989311393836752.jpg
             * losetype : 0
             * pageBuycount : 0
             * pageViewcount : 16
             * sellType : PACKAGE
             * status : 0
             * studyType : 0
             * review : 0
             * peopleNum : 0
             * enrollStatus : 0
             */

            private int id;
            private String name;
            private String addtime;
            private int isPay;
            private double enterprisePrice;
            private String title;
            private int lessionnum;
            private String logo;
            private int losetype;
            private int pageBuycount;
            private int pageViewcount;
            private String sellType;
            private int status;
            private int studyType;
            private int review;
            private int peopleNum;
            private int enrollStatus;

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

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public int getIsPay() {
                return isPay;
            }

            public void setIsPay(int isPay) {
                this.isPay = isPay;
            }

            public double getEnterprisePrice() {
                return enterprisePrice;
            }

            public void setEnterprisePrice(double enterprisePrice) {
                this.enterprisePrice = enterprisePrice;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getLessionnum() {
                return lessionnum;
            }

            public void setLessionnum(int lessionnum) {
                this.lessionnum = lessionnum;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getLosetype() {
                return losetype;
            }

            public void setLosetype(int losetype) {
                this.losetype = losetype;
            }

            public int getPageBuycount() {
                return pageBuycount;
            }

            public void setPageBuycount(int pageBuycount) {
                this.pageBuycount = pageBuycount;
            }

            public int getPageViewcount() {
                return pageViewcount;
            }

            public void setPageViewcount(int pageViewcount) {
                this.pageViewcount = pageViewcount;
            }

            public String getSellType() {
                return sellType;
            }

            public void setSellType(String sellType) {
                this.sellType = sellType;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getStudyType() {
                return studyType;
            }

            public void setStudyType(int studyType) {
                this.studyType = studyType;
            }

            public int getReview() {
                return review;
            }

            public void setReview(int review) {
                this.review = review;
            }

            public int getPeopleNum() {
                return peopleNum;
            }

            public void setPeopleNum(int peopleNum) {
                this.peopleNum = peopleNum;
            }

            public int getEnrollStatus() {
                return enrollStatus;
            }

            public void setEnrollStatus(int enrollStatus) {
                this.enrollStatus = enrollStatus;
            }
        }
    }
}
