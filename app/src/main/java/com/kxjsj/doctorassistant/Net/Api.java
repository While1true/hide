package com.kxjsj.doctorassistant.Net;




import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean.BaseBean;
import com.kxjsj.doctorassistant.JavaBean.SickBed;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by vange on 2017/9/13.
 */

public interface Api {


    /**
     * bed/getallbeds
     * 获取所有床位信息
     */
    @GET("bed/getallbeds")
    Observable<BaseBean<List<SickBed>>> getAllBeds();

    /**
     * 判断该手机号是否注册
     * @return
     */
    @GET("login/authphone")
    Observable<BaseBean> authPhone(@Query("userid")String userid,@Query("type") int type);

    @FormUrlEncoded
    @POST("login/register")
    Observable<BaseBean<Session>>register(@Field("userid") String userid, @Field("type")int type
            , @Field("password") String password, @Field("medicalcard") String medicalcard);
}
