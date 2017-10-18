package com.kxjsj.doctorassistant.Net;


import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;


import io.reactivex.Observable;

/**
 * Created by vange on 2017/9/13.
 */

public class ApiController {

    /**
     * 获取所有床位
     * @return
     */
    public static Observable getAllBeds(){
        return RetrofitHttpManger.create(Api.class)
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
        return RetrofitHttpManger.create(Api.class)
                .authPhone(userid,type)
                .compose(RxSchedulers.compose());
    }

    /**
     *
     * @param userid 手机
     * @param type 0：病人 1：医生
     * @param password 密码md5加密
     * @param medicalcard 就诊卡
     * @return
     */
    public static Observable register(String userid, int type, String password, String medicalcard){
        return RetrofitHttpManger.create(Api.class)
                .register(userid,type,password,medicalcard)
                .compose(RxSchedulers.compose());
    }

}
