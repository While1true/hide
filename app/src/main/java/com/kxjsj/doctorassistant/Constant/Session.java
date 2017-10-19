package com.kxjsj.doctorassistant.Constant;

import com.kxjsj.doctorassistant.Utils.GsonUtils;

/**
 * Created by vange on 2017/9/27.
 */

public class Session {
    String userid;
    String username;
    String imgurl;
    String token;
    String medicalcard;
    String rongtoken;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMedicalcard() {
        return medicalcard;
    }

    public void setMedicalcard(String medicalcard) {
        this.medicalcard = medicalcard;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRongtoken() {
        return rongtoken;
    }

    public void setRongtoken(String rongtoken) {
        this.rongtoken = rongtoken;
    }

    @Override
    public String toString() {
        return GsonUtils.parse2String(this);
    }
}
