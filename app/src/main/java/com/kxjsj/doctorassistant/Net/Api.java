package com.kxjsj.doctorassistant.Net;




import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean.BaseBean;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.JavaBean.PatientHome;
import com.kxjsj.doctorassistant.JavaBean.SickBed;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
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
     *
     * @return
     */
    @GET("login/authphone")
    Observable<BaseBean> authPhone(@Query("userid") String userid, @Query("type") int type);

    @FormUrlEncoded
    @POST("login/register")
    Observable<BaseBean<Session>> register(@Field("userid") String userid, @Field("type") int type
            , @Field("password") String password, @Field("medicalcard") String medicalcard);

    @FormUrlEncoded
    @POST("login/login")
    Observable<BaseBean<Session>> login(@Field("userid") String userid
            , @Field("password") String password, @Field("type") int type);

    @FormUrlEncoded
    @POST("login/modifyPassword")
    Observable<BaseBean> modifypassword(@Field("userid") String userid
            , @Field("password") String password, @Field("type") int type);

    @GET("patient/getPatient")
    Observable<BaseBean<PatientBed>> getBedInfo(@Query("patientNo") String patientNo);

    @GET("doctor/getAllDoctor")
    Observable<BaseBean<ArrayList<DoctorBean>>> getAllDoctor(@Query("token") String token);

    @GET("patient/getDepartment")
    Observable<BaseBean<ArrayList<String>>> getDepartment();

    @GET("patient/getPatientByDepartment")
    Observable<BaseBean<ArrayList<PatientHome>>> getPatientByDepartment(@Query("token") String token, @Query("department") String department);

    @FormUrlEncoded
    @POST("login/bindXiaomi")
    Observable<BaseBean> bindXiaomiId(@Field("userid") String userid, @Field("id") String xiaomiID
            , @Field("token") String token, @Field("type") int type);

    @FormUrlEncoded
    @POST("login/getUserInfo")
    Observable<BaseBean<KotlinBean.UserInfoBean>> getUserInfo(@Field("userid") String userid, @Field("token") String token, @Field("type") int type);

    @FormUrlEncoded
    @POST("push/pushToUser")
    Observable<BaseBean> pushToUser( @Field("userid")String userid ,@Field("token")String token, @Field("fromid")String fromid,
                                     @Field("content")String content ,
                                     @Field("type")int type ,
                                     @Field("message_type")int message_type );

    /**
     * 获取所有收到的提醒通知
     * @return
     */
    @FormUrlEncoded
    @POST("push/getAllPush")
    Observable<BaseBean<ArrayList<KotlinBean.PushBean>>> getAllPush(
            @Field("userid")String userid,
            @Field("token")String token,
            @Field("type")int type);


    /**
     * 回复通知
     * @return
     */
    @FormUrlEncoded
    @POST("push/replyPush")
    Observable<BaseBean> replyPush(@Field("id")String id,@Field("userid")String userid,
                                   @Field("fromid")String fromid, @Field("type")int type,
                                   @Field("token")String token,@Field("reply")String reply);

}