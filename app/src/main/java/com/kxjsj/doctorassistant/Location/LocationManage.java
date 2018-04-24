package com.kxjsj.doctorassistant.Location;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;

/**
 * Created by 不听话的好孩子 on 2018/4/23.
 */

public class LocationManage implements LifecycleObserver {
    private static LocationService locationService;
    private BDAbstractLocationListener locationListener;

    public LocationManage(BDAbstractLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public LocationManage get(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
        start();
        return this;
    }

    public static void init(Application application) {
        locationService = new LocationService(application);
//        mVibrator = (Vibrator) application.getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(application);
    }

    public void start() {
        locationService.start();
    }

    public void stop() {
        locationService.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onstart() {
        locationService.registerListener(locationListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        locationService.unregisterListener(locationListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    public static Location parseString(BDLocation bdlocation) {
        Location location = new Location();
        if (null != bdlocation && bdlocation.getLocType() != BDLocation.TypeServerError) {
            StringBuffer sb = new StringBuffer(256);
            /**
             * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
             * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
             */
            location.setTime(bdlocation.getTime());
            location.setLocType(bdlocation.getLocType() + "");
            location.setLocTypeDescription(bdlocation.getLocTypeDescription());
            location.setLatitude(bdlocation.getLatitude() + "");
            location.setLontitude(bdlocation.getLongitude() + "");
            location.setRadius(bdlocation.getRadius() + "");
            location.setCountryCode(bdlocation.getCountryCode() + "");
            location.setCountry(bdlocation.getCountry() + "");
            location.setCitycode(bdlocation.getCityCode() + "");
            location.setCity(bdlocation.getCity() + "");
            location.setDistrict(bdlocation.getDistrict() + "");
            location.setStreet(bdlocation.getStreet() + "");
            location.setAddr(bdlocation.getAddrStr());
            location.setUserIndoorState(bdlocation.getUserIndoorState() + "");
            location.setDirection(bdlocation.getDirection() + "");
            location.setLocationdescribe(bdlocation.getLocationDescribe());
            if (bdlocation.getPoiList() != null && !bdlocation.getPoiList().isEmpty()) {
                for (int i = 0; i < bdlocation.getPoiList().size(); i++) {
                    Poi poi = (Poi) bdlocation.getPoiList().get(i);
                    sb.append(poi.getName() + ";");
                }
            }
            location.setPoi(sb.toString());
            sb = new StringBuffer();
            if (bdlocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(bdlocation.getSpeed());// 速度 单位：km/h
                sb.append("\nsatellite : ");
                sb.append(bdlocation.getSatelliteNumber());// 卫星数目
                sb.append("\nheight : ");
                sb.append(bdlocation.getAltitude());// 海拔高度 单位：米
                sb.append("\ngps status : ");
                sb.append(bdlocation.getGpsAccuracyStatus());// *****gps质量判断*****
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (bdlocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                if (bdlocation.hasAltitude()) {// *****如果有海拔高度*****
                    sb.append("\nheight : ");
                    sb.append(bdlocation.getAltitude());// 单位：米
                }
                sb.append("\noperationers : ");// 运营商信息
                sb.append(bdlocation.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (bdlocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (bdlocation.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (bdlocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bdlocation.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            location.setOther(sb.toString());
        }
        return location;
    }
}
