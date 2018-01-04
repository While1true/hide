package com.kxjsj.doctorassistant.JavaBean;

/**
 * Created by vange on 2018/1/4.
 */

public class RatingBean {

    /**
     * CODE : 20180104103533446
     * CONTENT : 试试
     * CREATIONDATE : 1515033339000
     * RN : 2
     * SCORE : 2.5
     * TYPE : 0
     * USERID : 13959012996
     */

    private String CODE;
    private String CONTENT;
    private long CREATIONDATE;
    private int RN;
    private double SCORE;
    private String TYPE;
    private String USERID;

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public long getCREATIONDATE() {
        return CREATIONDATE;
    }

    public void setCREATIONDATE(long CREATIONDATE) {
        this.CREATIONDATE = CREATIONDATE;
    }

    public int getRN() {
        return RN;
    }

    public void setRN(int RN) {
        this.RN = RN;
    }

    public double getSCORE() {
        return SCORE;
    }

    public void setSCORE(double SCORE) {
        this.SCORE = SCORE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
}
