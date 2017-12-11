package com.kxjsj.doctorassistant.Constant;

import com.kxjsj.doctorassistant.Utils.GsonUtils;

/**
 * Created by vange on 2017/9/27.
 */

public class Session {
    String userid;
    String username;
    String imgUrl; //头像地址
    String token;//用户token
    String patientNo;//病号
    String rongToken;//融云token
    String department;//科室
    int type;//0:病人 1：医生 2；护士

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRongToken() {
        return rongToken;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }

    @Override
    public String toString() {
        return GsonUtils.parse2String(this);
    }
}
