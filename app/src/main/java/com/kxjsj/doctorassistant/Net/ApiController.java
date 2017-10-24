package com.kxjsj.doctorassistant.Net;


import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.PatientHome;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by vange on 2017/9/13.
 */

public class ApiController {

    private static class InstanceHolder{
        private static Api api = RetrofitHttpManger.create(Api.class);
    }



    /**
     * 获取所有床位
     * @return
     */
    public static Observable getAllBeds(){
        return InstanceHolder.api
                .getAllBeds()
                .compose(RxSchedulers.compose());

    }

    /**
     * 判断手机号是否注册
     * @param  userid 手机号
     * @param  type 0:病人 1：医生
     * @return
     */
    public static Observable authPhone(String userid,int type){
        return InstanceHolder.api
                .authPhone(userid,type)
                .compose(RxSchedulers.compose());
    }

    /**
     *注册
     * @param userid 手机
     * @param type 0：病人 1：医生
     * @param password 密码md5加密
     * @param medicalcard 就诊卡
     * @return
     */
    public static Observable register(String userid, int type, String password, String medicalcard){
        return InstanceHolder.api
                .register(userid,type,password,medicalcard)
                .compose(RxSchedulers.compose());
    }

    /**
     *登录
     * @param userid 手机
     * @param type 0：病人 1：医生
     * @param password 密码md5加密
     * @return
     */
    public static Observable login(String userid, int type, String password){
        return InstanceHolder.api
                .login(userid,password,type)
                .compose(RxSchedulers.compose());
    }

    /**
     *修改密码
     * @param userid 手机
     * @param type 0：病人 1：医生
     * @param newPassword 密码md5加密
     * @return
     */
    public static Observable modifypassword(String userid,  String newPassword,int type){
        return InstanceHolder.api
                .modifypassword(userid,newPassword,type)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取病床信息
     * @param painentid
     * @return
     */
    public static Observable getBedInfo(String painentid){
        return InstanceHolder.api
                .getBedInfo(painentid)
                .compose(RxSchedulers.compose());
    }

    /**
     * 绑定小米Id
     * @param userid
     * @param xiaomiId
     * @param token
     * @return
     */
    public static Observable bindXiaomi(String userid,String xiaomiId,String token){
        return InstanceHolder.api
                .bindXiaomi(userid,xiaomiId,token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取病病人
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<DoctorBean>>> getAllDocotr(String token){
        return InstanceHolder.api
                .getAllDoctor(token)
                .compose(RxSchedulers.compose());
    }
    /**
     * 获取科室类目
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<String>>>getDepartment(){
        return InstanceHolder.api
                .getDepartment()
                .compose(RxSchedulers.compose());
    }
    /**
     * 获取科室类目下的病人
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<PatientHome>>>getAgetPatientByDepartment(String token, String department){
        return InstanceHolder.api
                .getPatientByDepartment(token,department)
                .compose(RxSchedulers.compose());
    }
}
