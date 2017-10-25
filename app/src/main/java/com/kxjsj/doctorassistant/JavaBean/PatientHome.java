package com.kxjsj.doctorassistant.JavaBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vange on 2017/10/23.
 */

public class PatientHome implements Parcelable {


    /**
     * adress : 福州
     * birthday : 1504108800000
     * department : 鼻科
     * diagnose : 鼻塞、打喷嚏5年余，加重15天。现病史：患者于5年前晨起打喷嚏、流清水样鼻涕、鼻塞，伴鼻痒、咽痒、耳痒，口服抗感冒药治疗无效，之后就诊于陆军总院诊断为过敏性鼻炎，口服氯雷他定、扑尔敏等抗过敏药时轻时重，每遇秋季加重，为求中医药治疗近日来我院门诊，诊断为鼻炎。
     * identity : 350124
     * intime : 1504195200000
     * page : 22
     * patientNo : 12580
     * phoneNumber : 13959012996
     * pname : 程
     * psex : 1
     * userid : 13959012996
     */

    private String adress;
    private String birthday;
    private String department;
    private String diagnose;
    private String identity;
    private String intime;
    private String page;
    private String patientNo;
    private String phoneNumber;
    private String pname;
    private String psex;
    private String userid;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPsex() {
        return psex;
    }

    public void setPsex(String psex) {
        this.psex = psex;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adress);
        dest.writeString(this.birthday);
        dest.writeString(this.department);
        dest.writeString(this.diagnose);
        dest.writeString(this.identity);
        dest.writeString(this.intime);
        dest.writeString(this.page);
        dest.writeString(this.patientNo);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.pname);
        dest.writeString(this.psex);
        dest.writeString(this.userid);
    }

    public PatientHome() {
    }

    protected PatientHome(Parcel in) {
        this.adress = in.readString();
        this.birthday = in.readString();
        this.department = in.readString();
        this.diagnose = in.readString();
        this.identity = in.readString();
        this.intime = in.readString();
        this.page = in.readString();
        this.patientNo = in.readString();
        this.phoneNumber = in.readString();
        this.pname = in.readString();
        this.psex = in.readString();
        this.userid = in.readString();
    }

    public static final Parcelable.Creator<PatientHome> CREATOR = new Parcelable.Creator<PatientHome>() {
        @Override
        public PatientHome createFromParcel(Parcel source) {
            return new PatientHome(source);
        }

        @Override
        public PatientHome[] newArray(int size) {
            return new PatientHome[size];
        }
    };
}
