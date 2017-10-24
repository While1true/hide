package com.kxjsj.doctorassistant.JavaBean;

import java.io.Serializable;

import com.kxjsj.doctorassistant.Utils.GsonUtils;


public class Patient implements Serializable {
    private String patientNo;

    private String pname;

    private String psex;

    private String adress;

    private String phoneNumber;

    private String floorId;
    
    private String roomId;
    
    private String bedId;

    private String identity;

    private String page;

    private String birthday;

    private String status;

    private String remark;

    private String balance;

    private static final long serialVersionUID = 1L;

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }

    public void setphoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getfloorId() {
        return floorId;
    }
    
    public String getroomId() {
        return roomId;
    }
    
    public String getbedId() {
        return bedId;
    }

    public String setfloorId(String floorId) {
        return floorId;
    }
    
    public void setroomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String setbedId(String bedId) {
        return bedId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return GsonUtils.parse2String(this);
    }

}