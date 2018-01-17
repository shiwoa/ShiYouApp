package com.jiaoyu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiangyao on 2017/10/24.
 */

public class ExpertEntity implements Serializable {

    /**
     * message : 查询成功
     * success : true
     * entity : {"teacher":{"id":67,"name":"刘轶","education":" 宁夏青年拔尖人才","career":"       刘轶，男，高级工程师，2005年毕业于中北大学电气工程自动化专业，、现任国家智能铸造产业创新中心副总经理，共享装备智能制造研究院院长，共享集团技术中心技术专家委员会专家。<br />       作为自治区铸造3D打印及智能工厂产业应用创新团队第二带头人，重点负责3D打印技术在铸造领域的产业化应用和技术难点攻关，主持建立了快速成形中心及大型关键铸件先进制造技术国家地方联合工程实验室，成功攻克了3D打印技术工艺设计、材料、软件及关键零部件等产业应用的技术难题，其中\u201c五大\u201d核心控制软件在同一平台综合集成，\u201c十二大\u201d系统关键技术优化创新，承担国际级、省部级科研项目10余项，拥有授权专利10余项，自治区科技成果3项，形成技术标准2项，2016年获得自治区青年拔尖人才、自治区青年科技奖，2017年获得自治区科技创新争先奖，为公司、行业的转型发展做出了突出贡献。","isStar":0,"picPath":"/upload/mavendemo/teacher/20170913/1505294991340670238.jpg","status":0,"createTime":"2017-09-11 14:11:22","updateTime":"2017-09-18 18:11:17","subjectId":104,"sort":16,"countSub":0,"idCardNo":"140107198312034515","idCardPic":"/upload/mavendemo/teacher/20170913/1505294846872690314.jpg","idCardPicBack":"/upload/mavendemo/teacher/20170918/1505729475383511816.jpg","email":"ksf.gcsbb.ly@kocel.com","phone":"13469502586","account":"浦发银行","accountNumber":"6217924174709029","auditStatus":1,"registerFrom":0,"gradeAndTeacherViewPlus":0,"showIndexPage":1,"proportion":0,"clearingForm":0,"teacherSubscribePriceList":[{"id":36,"teacherId":67,"subscribeType":0,"subscribePrice":400},{"id":35,"teacherId":67,"subscribeType":1,"subscribePrice":5000}]}}
     */

    private String message;
    private boolean success;
    private Entity entity;

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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public static class Entity {
        /**
         * teacher : {"id":67,"name":"刘轶","education":" 宁夏青年拔尖人才","career":"       刘轶，男，高级工程师，2005年毕业于中北大学电气工程自动化专业，、现任国家智能铸造产业创新中心副总经理，共享装备智能制造研究院院长，共享集团技术中心技术专家委员会专家。<br />       作为自治区铸造3D打印及智能工厂产业应用创新团队第二带头人，重点负责3D打印技术在铸造领域的产业化应用和技术难点攻关，主持建立了快速成形中心及大型关键铸件先进制造技术国家地方联合工程实验室，成功攻克了3D打印技术工艺设计、材料、软件及关键零部件等产业应用的技术难题，其中\u201c五大\u201d核心控制软件在同一平台综合集成，\u201c十二大\u201d系统关键技术优化创新，承担国际级、省部级科研项目10余项，拥有授权专利10余项，自治区科技成果3项，形成技术标准2项，2016年获得自治区青年拔尖人才、自治区青年科技奖，2017年获得自治区科技创新争先奖，为公司、行业的转型发展做出了突出贡献。","isStar":0,"picPath":"/upload/mavendemo/teacher/20170913/1505294991340670238.jpg","status":0,"createTime":"2017-09-11 14:11:22","updateTime":"2017-09-18 18:11:17","subjectId":104,"sort":16,"countSub":0,"idCardNo":"140107198312034515","idCardPic":"/upload/mavendemo/teacher/20170913/1505294846872690314.jpg","idCardPicBack":"/upload/mavendemo/teacher/20170918/1505729475383511816.jpg","email":"ksf.gcsbb.ly@kocel.com","phone":"13469502586","account":"浦发银行","accountNumber":"6217924174709029","auditStatus":1,"registerFrom":0,"gradeAndTeacherViewPlus":0,"showIndexPage":1,"proportion":0,"clearingForm":0,"teacherSubscribePriceList":[{"id":36,"teacherId":67,"subscribeType":0,"subscribePrice":400},{"id":35,"teacherId":67,"subscribeType":1,"subscribePrice":5000}]}
         */

        private Teacher teacher;

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        public static class Teacher {
            /**
             * id : 67
             * name : 刘轶
             * education :  宁夏青年拔尖人才
             * career :        刘轶，男，高级工程师，2005年毕业于中北大学电气工程自动化专业，、现任国家智能铸造产业创新中心副总经理，共享装备智能制造研究院院长，共享集团技术中心技术专家委员会专家。<br />       作为自治区铸造3D打印及智能工厂产业应用创新团队第二带头人，重点负责3D打印技术在铸造领域的产业化应用和技术难点攻关，主持建立了快速成形中心及大型关键铸件先进制造技术国家地方联合工程实验室，成功攻克了3D打印技术工艺设计、材料、软件及关键零部件等产业应用的技术难题，其中“五大”核心控制软件在同一平台综合集成，“十二大”系统关键技术优化创新，承担国际级、省部级科研项目10余项，拥有授权专利10余项，自治区科技成果3项，形成技术标准2项，2016年获得自治区青年拔尖人才、自治区青年科技奖，2017年获得自治区科技创新争先奖，为公司、行业的转型发展做出了突出贡献。
             * isStar : 0
             * picPath : /upload/mavendemo/teacher/20170913/1505294991340670238.jpg
             * status : 0
             * createTime : 2017-09-11 14:11:22
             * updateTime : 2017-09-18 18:11:17
             * subjectId : 104
             * sort : 16
             * countSub : 0
             * idCardNo : 140107198312034515
             * idCardPic : /upload/mavendemo/teacher/20170913/1505294846872690314.jpg
             * idCardPicBack : /upload/mavendemo/teacher/20170918/1505729475383511816.jpg
             * email : ksf.gcsbb.ly@kocel.com
             * phone : 13469502586
             * account : 浦发银行
             * accountNumber : 6217924174709029
             * auditStatus : 1
             * registerFrom : 0
             * gradeAndTeacherViewPlus : 0
             * showIndexPage : 1
             * proportion : 0
             * clearingForm : 0
             * teacherSubscribePriceList : [{"id":36,"teacherId":67,"subscribeType":0,"subscribePrice":400},{"id":35,"teacherId":67,"subscribeType":1,"subscribePrice":5000}]
             */

            private int id;
            private String name;
            private String education;
            private String career;
            private int isStar;
            private String picPath;
            private int status;
            private String createTime;
            private String updateTime;
            private int subjectId;
            private int sort;
            private int countSub;
            private String idCardNo;
            private String idCardPic;
            private String idCardPicBack;
            private String email;
            private String phone;
            private String account;
            private String accountNumber;
            private int auditStatus;
            private int registerFrom;
            private int gradeAndTeacherViewPlus;
            private int teacherQualitilieCount;
            private int showIndexPage;
            private int proportion;
            private int clearingForm;
            private float avgGrade;
            private float faceSubscribePrice;
            private float videoSubscribePrice;
            private int userId;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public float getFaceSubscribePrice() {
                return faceSubscribePrice;
            }

            public void setFaceSubscribePrice(float faceSubscribePrice) {
                this.faceSubscribePrice = faceSubscribePrice;
            }

            public float getVideoSubscribePrice() {
                return videoSubscribePrice;
            }

            public void setVideoSubscribePrice(float videoSubscribePrice) {
                this.videoSubscribePrice = videoSubscribePrice;
            }

            public int getTeacherQualitilieCount() {
                return teacherQualitilieCount;
            }

            public void setTeacherQualitilieCount(int teacherQualitilieCount) {
                this.teacherQualitilieCount = teacherQualitilieCount;
            }

            private List<TeacherSubscribePriceList> teacherSubscribePriceList;

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

            public int getSubjectId() {
                return subjectId;
            }

            public void setSubjectId(int subjectId) {
                this.subjectId = subjectId;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getCountSub() {
                return countSub;
            }

            public void setCountSub(int countSub) {
                this.countSub = countSub;
            }

            public String getIdCardNo() {
                return idCardNo;
            }

            public void setIdCardNo(String idCardNo) {
                this.idCardNo = idCardNo;
            }

            public String getIdCardPic() {
                return idCardPic;
            }

            public void setIdCardPic(String idCardPic) {
                this.idCardPic = idCardPic;
            }

            public String getIdCardPicBack() {
                return idCardPicBack;
            }

            public void setIdCardPicBack(String idCardPicBack) {
                this.idCardPicBack = idCardPicBack;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getAccountNumber() {
                return accountNumber;
            }

            public void setAccountNumber(String accountNumber) {
                this.accountNumber = accountNumber;
            }

            public int getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(int auditStatus) {
                this.auditStatus = auditStatus;
            }

            public int getRegisterFrom() {
                return registerFrom;
            }

            public void setRegisterFrom(int registerFrom) {
                this.registerFrom = registerFrom;
            }

            public int getGradeAndTeacherViewPlus() {
                return gradeAndTeacherViewPlus;
            }

            public void setGradeAndTeacherViewPlus(int gradeAndTeacherViewPlus) {
                this.gradeAndTeacherViewPlus = gradeAndTeacherViewPlus;
            }

            public int getShowIndexPage() {
                return showIndexPage;
            }

            public void setShowIndexPage(int showIndexPage) {
                this.showIndexPage = showIndexPage;
            }

            public int getProportion() {
                return proportion;
            }

            public void setProportion(int proportion) {
                this.proportion = proportion;
            }

            public int getClearingForm() {
                return clearingForm;
            }

            public void setClearingForm(int clearingForm) {
                this.clearingForm = clearingForm;
            }

            public float getAvgGrade() {
                return avgGrade;
            }

            public void setAvgGrade(float avgGrade) {
                this.avgGrade = avgGrade;
            }

            public List<TeacherSubscribePriceList> getTeacherSubscribePriceList() {
                return teacherSubscribePriceList;
            }

            public void setTeacherSubscribePriceList(List<TeacherSubscribePriceList> teacherSubscribePriceList) {
                this.teacherSubscribePriceList = teacherSubscribePriceList;
            }

            public static class TeacherSubscribePriceList {
                /**
                 * id : 36
                 * teacherId : 67
                 * subscribeType : 0
                 * subscribePrice : 400
                 */

                private int id;
                private int teacherId;
                private int subscribeType;
                private int subscribePrice;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getTeacherId() {
                    return teacherId;
                }

                public void setTeacherId(int teacherId) {
                    this.teacherId = teacherId;
                }

                public int getSubscribeType() {
                    return subscribeType;
                }

                public void setSubscribeType(int subscribeType) {
                    this.subscribeType = subscribeType;
                }

                public int getSubscribePrice() {
                    return subscribePrice;
                }

                public void setSubscribePrice(int subscribePrice) {
                    this.subscribePrice = subscribePrice;
                }
            }
        }
    }
}
