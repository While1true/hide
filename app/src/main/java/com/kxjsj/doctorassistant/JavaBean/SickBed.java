package com.kxjsj.doctorassistant.JavaBean;

/**
 * Created by vange on 2017/10/16.
 */

public class SickBed {

    public SickBed(int floorid,int bedid, int roomid) {
        this.bedid = bedid;
        this.floorid = floorid;
        this.roomid = roomid;
        this.status = 0;
    }

    /**
     * bedid : 0
     * floorid : 0
     * id : 1
     * isfree : 0
     * patient_no : 854617
     * roomid : 0
     * status : 1
     */

    private int bedid;
    private int floorid;
    private int id;
    private int isfree;
    private int patient_no;
    private int roomid;
    private int status;

    public int getBedid() {
        return bedid;
    }

    public void setBedid(int bedid) {
        this.bedid = bedid;
    }

    public int getFloorid() {
        return floorid;
    }

    public void setFloorid(int floorid) {
        this.floorid = floorid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsfree() {
        return isfree;
    }

    public void setIsfree(int isfree) {
        this.isfree = isfree;
    }

    public int getPatient_no() {
        return patient_no;
    }

    public void setPatient_no(int patient_no) {
        this.patient_no = patient_no;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
