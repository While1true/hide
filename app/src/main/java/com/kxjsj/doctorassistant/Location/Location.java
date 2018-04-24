package com.kxjsj.doctorassistant.Location;


import com.kxjsj.doctorassistant.Utils.GsonUtils;

/**
 * Created by 不听话的好孩子 on 2018/4/23.
 */

public class Location {
    private String time,locType,locTypeDescription,latitude,lontitude,radius,CountryCode,Country,citycode,city,District,Street,addr,UserIndoorState,Direction,locationdescribe,Poi,describe,other;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getLocTypeDescription() {
        return locTypeDescription;
    }

    public void setLocTypeDescription(String locTypeDescription) {
        this.locTypeDescription = locTypeDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLontitude() {
        return lontitude;
    }

    public void setLontitude(String lontitude) {
        this.lontitude = lontitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUserIndoorState() {
        return UserIndoorState;
    }

    public void setUserIndoorState(String userIndoorState) {
        UserIndoorState = userIndoorState;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getLocationdescribe() {
        return locationdescribe;
    }

    public void setLocationdescribe(String locationdescribe) {
        this.locationdescribe = locationdescribe;
    }

    public String getPoi() {
        return Poi;
    }

    public void setPoi(String poi) {
        Poi = poi;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return GsonUtils.parse2String(this);
    }
}
