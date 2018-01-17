package com.jiaoyu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xm8-02 on 2017/10/23.
 */

public class OrganizationEntity {


    /**
     * message : 查询成功
     * success : true
     * entity : {"page":{"totalResultSize":19,"totalPageSize":19,"pageSize":1,"currentPage":1,"startRow":0,"first":false,"last":false},"companyVoList":[{"overduTime":"2018-09-13 09:41:38","courseNum":0,"userNum":0,"id":104,"name":"北京中铸未来教育科技有限公司","logo":"/upload/mavendemo/institutions/image/20170918/1505698702106288528.jpg","culture":"<p style=\"text-indent:24.0pt;\">\r\n\t公司是以中国铸造协会为首的<span>13<\/span>家核心层企业与人员、<span>29<\/span>家紧密层企业与地方协会和50家半紧密层企业与地方协会组成的主要从事铸造职业教育与职业培训、铸造领域教育产品的生产与销售、铸造领域技术管理咨询与服务业务的大型、综合性经济实体；公司拥有职业教育与职业培训方面的资源与资质包括<span>3<\/span>家专业委员会（中国铸造协会教育培训工作委员会，国家开放大学铸造专业学术指导委员会，机械行业铸造职业标准、教程、试题技术委员会）、<span>2<\/span>个中心（国家学习成果认证铸造行业中心、机械工业职业技能鉴定铸造行业分中心）、<span>1<\/span>所大学（国家开放大学铸造学院）和<span>31<\/span>个国内外教育培训基地（略）；公司以国家学习成果认证标准和国家、行业职业资格与能力标准为依据，以信息技术为支撑，以远程教育教学为手段，以建立从业人员终身学习立交体系为目标，在中国铸造行业中广泛推动各级行业组织、各类企事业单位和院校开展铸造专业的学历教育、职业资格证书教育和在职员工岗位技能培训业务；并着力为全行业从业人员提供不断更新的优质学习产品、学习资源和学习支持服务。\r\n<\/p>","address":"北京市海淀区首体南路2号机械科学研究总院办公楼","status":0,"createTime":"2017-09-18 09:41:38","updateTime":"2017-09-18 09:41:38","creditcode":"91110108MA002T3069","applyName":"于子飞","applyMobile":"13619584693","applyEmail":"yuzifei@foundry.com.cn","capacity":200,"landline":"010－68353382","type":"ENTERPRISE","purchaseTime":12,"parentId":0,"institutionsType":"ORGANIZATION","price":6666}]}
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
         * page : {"totalResultSize":19,"totalPageSize":19,"pageSize":1,"currentPage":1,"startRow":0,"first":false,"last":false}
         * companyVoList : [{"overduTime":"2018-09-13 09:41:38","courseNum":0,"userNum":0,"id":104,"name":"北京中铸未来教育科技有限公司","logo":"/upload/mavendemo/institutions/image/20170918/1505698702106288528.jpg","culture":"<p style=\"text-indent:24.0pt;\">\r\n\t公司是以中国铸造协会为首的<span>13<\/span>家核心层企业与人员、<span>29<\/span>家紧密层企业与地方协会和50家半紧密层企业与地方协会组成的主要从事铸造职业教育与职业培训、铸造领域教育产品的生产与销售、铸造领域技术管理咨询与服务业务的大型、综合性经济实体；公司拥有职业教育与职业培训方面的资源与资质包括<span>3<\/span>家专业委员会（中国铸造协会教育培训工作委员会，国家开放大学铸造专业学术指导委员会，机械行业铸造职业标准、教程、试题技术委员会）、<span>2<\/span>个中心（国家学习成果认证铸造行业中心、机械工业职业技能鉴定铸造行业分中心）、<span>1<\/span>所大学（国家开放大学铸造学院）和<span>31<\/span>个国内外教育培训基地（略）；公司以国家学习成果认证标准和国家、行业职业资格与能力标准为依据，以信息技术为支撑，以远程教育教学为手段，以建立从业人员终身学习立交体系为目标，在中国铸造行业中广泛推动各级行业组织、各类企事业单位和院校开展铸造专业的学历教育、职业资格证书教育和在职员工岗位技能培训业务；并着力为全行业从业人员提供不断更新的优质学习产品、学习资源和学习支持服务。\r\n<\/p>","address":"北京市海淀区首体南路2号机械科学研究总院办公楼","status":0,"createTime":"2017-09-18 09:41:38","updateTime":"2017-09-18 09:41:38","creditcode":"91110108MA002T3069","applyName":"于子飞","applyMobile":"13619584693","applyEmail":"yuzifei@foundry.com.cn","capacity":200,"landline":"010－68353382","type":"ENTERPRISE","purchaseTime":12,"parentId":0,"institutionsType":"ORGANIZATION","price":6666}]
         */

        private PageBean page;
        private List<CompanyVoListBean> companyVoList;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<CompanyVoListBean> getCompanyVoList() {
            return companyVoList;
        }

        public void setCompanyVoList(List<CompanyVoListBean> companyVoList) {
            this.companyVoList = companyVoList;
        }

        public static class PageBean {
            /**
             * totalResultSize : 19
             * totalPageSize : 19
             * pageSize : 1
             * currentPage : 1
             * startRow : 0
             * first : false
             * last : false
             */

            private int totalResultSize;
            private int totalPageSize;
            private int pageSize;
            private int currentPage;
            private int startRow;
            private boolean first;
            private boolean last;

            public int getTotalResultSize() {
                return totalResultSize;
            }

            public void setTotalResultSize(int totalResultSize) {
                this.totalResultSize = totalResultSize;
            }

            public int getTotalPageSize() {
                return totalPageSize;
            }

            public void setTotalPageSize(int totalPageSize) {
                this.totalPageSize = totalPageSize;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getStartRow() {
                return startRow;
            }

            public void setStartRow(int startRow) {
                this.startRow = startRow;
            }

            public boolean isFirst() {
                return first;
            }

            public void setFirst(boolean first) {
                this.first = first;
            }

            public boolean isLast() {
                return last;
            }

            public void setLast(boolean last) {
                this.last = last;
            }
        }

        public static class CompanyVoListBean implements Serializable {
            /**
             * overduTime : 2018-09-13 09:41:38
             * courseNum : 0
             * userNum : 0
             * id : 104
             * name : 北京中铸未来教育科技有限公司
             * logo : /upload/mavendemo/institutions/image/20170918/1505698702106288528.jpg
             * culture : <p style="text-indent:24.0pt;">
             公司是以中国铸造协会为首的<span>13</span>家核心层企业与人员、<span>29</span>家紧密层企业与地方协会和50家半紧密层企业与地方协会组成的主要从事铸造职业教育与职业培训、铸造领域教育产品的生产与销售、铸造领域技术管理咨询与服务业务的大型、综合性经济实体；公司拥有职业教育与职业培训方面的资源与资质包括<span>3</span>家专业委员会（中国铸造协会教育培训工作委员会，国家开放大学铸造专业学术指导委员会，机械行业铸造职业标准、教程、试题技术委员会）、<span>2</span>个中心（国家学习成果认证铸造行业中心、机械工业职业技能鉴定铸造行业分中心）、<span>1</span>所大学（国家开放大学铸造学院）和<span>31</span>个国内外教育培训基地（略）；公司以国家学习成果认证标准和国家、行业职业资格与能力标准为依据，以信息技术为支撑，以远程教育教学为手段，以建立从业人员终身学习立交体系为目标，在中国铸造行业中广泛推动各级行业组织、各类企事业单位和院校开展铸造专业的学历教育、职业资格证书教育和在职员工岗位技能培训业务；并着力为全行业从业人员提供不断更新的优质学习产品、学习资源和学习支持服务。
             </p>
             * address : 北京市海淀区首体南路2号机械科学研究总院办公楼
             * status : 0
             * createTime : 2017-09-18 09:41:38
             * updateTime : 2017-09-18 09:41:38
             * creditcode : 91110108MA002T3069
             * applyName : 于子飞
             * applyMobile : 13619584693
             * applyEmail : yuzifei@foundry.com.cn
             * capacity : 200
             * landline : 010－68353382
             * type : ENTERPRISE
             * purchaseTime : 12
             * parentId : 0
             * institutionsType : ORGANIZATION
             * price : 6666.0
             */

            private String overduTime;
            private int courseNum;
            private int userNum;
            private int id;
            private String name;
            private String logo;
            private String culture;
            private String address;
            private int status;
            private String createTime;
            private String updateTime;
            private String creditcode;
            private String applyName;
            private String applyMobile;
            private String applyEmail;
            private int capacity;
            private String landline;
            private String type;
            private int purchaseTime;
            private int parentId;
            private String institutionsType;
            private double price;
            private List<EntityPublicType> entity;

            public List<EntityPublicType> getEntity() {
                return entity;
            }

            public void setEntity(List<EntityPublicType> entity) {
                this.entity = entity;
            }

            public String getOverduTime() {
                return overduTime;
            }

            public void setOverduTime(String overduTime) {
                this.overduTime = overduTime;
            }

            public int getCourseNum() {
                return courseNum;
            }

            public void setCourseNum(int courseNum) {
                this.courseNum = courseNum;
            }

            public int getUserNum() {
                return userNum;
            }

            public void setUserNum(int userNum) {
                this.userNum = userNum;
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

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getCulture() {
                return culture;
            }

            public void setCulture(String culture) {
                this.culture = culture;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
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

            public String getCreditcode() {
                return creditcode;
            }

            public void setCreditcode(String creditcode) {
                this.creditcode = creditcode;
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

            public String getLandline() {
                return landline;
            }

            public void setLandline(String landline) {
                this.landline = landline;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }
        }
    }
}
