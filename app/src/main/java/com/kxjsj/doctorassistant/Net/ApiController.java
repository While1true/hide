package com.kxjsj.doctorassistant.Net;


import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.JavaBean.PatientHome;
import com.kxjsj.doctorassistant.JavaBean.RatingBean;
import com.kxjsj.doctorassistant.JavaBean.SickBed;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by vange on 2017/9/13.
 */

public class ApiController {

    private static class InstanceHolder {
        private static Api api = RetrofitHttpManger.create(Api.class);
    }




    /**
     * 获取所有床位
     *
     * @return
     */
    public static Observable<KotlinBean.BaseBean<List<SickBed>>> getAllBeds() {
        return InstanceHolder.api
                .getAllBeds()
                .compose(RxSchedulers.compose());
    }
    /**
     * 判断手机号是否注册
     *
     * @param userid 手机号
     * @param type   0:病人 1：医生
     * @return
     */

    public static Observable<KotlinBean.BaseBean> authPhone(String userid, int type) {
        return InstanceHolder.api
                .authPhone(userid, type)
                .compose(RxSchedulers.compose());
    }



    /**
     * 注册
     *
     * @param userid      手机
     * @param type        0：病人 1：医生
     * @param password    密码md5加密
     * @param medicalcard 就诊卡
     * @return
     */
    public static Observable register(String userid, int type, String password, String medicalcard) {
        return InstanceHolder.api
                .register(userid, type, password, medicalcard)
                .compose(RxSchedulers.compose());
    }

    /**
     * 登录
     *
     * @param userid   手机
     * @param type     0：病人 1：医生
     * @param password 密码md5加密
     * @return
     */
    public static Observable login(String userid, int type, String password) {
        return InstanceHolder.api
                .login(userid, password, type)
                .compose(RxSchedulers.compose());
    }

    /**
     * 修改密码
     *
     * @param userid      手机
     * @param type        0：病人 1：医生
     * @param newPassword 密码md5加密
     * @return
     */
    public static Observable modifypassword(String userid, String newPassword, int type) {
        return InstanceHolder.api
                .modifypassword(userid, newPassword, type)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取病床信息
     *
     * @param painentNo
     * @return
     */
    public static Observable<KotlinBean.BaseBean<PatientBed>> getBedInfo(String painentNo) {
        return InstanceHolder.api
                .getBedInfo(painentNo)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取病病人
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<DoctorBean>>> getAllDocotr(String token) {
        return InstanceHolder.api
                .getAllDoctor(token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取科室类目
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<String>>> getDepartment() {
        return InstanceHolder.api
                .getDepartment()
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取科室类目下的病人
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<PatientHome>>> getPatientByDepartment(String token, String department) {
        return InstanceHolder.api
                .getPatientByDepartment(token, department)
                .compose(RxSchedulers.compose());
    }

    /**
     * 绑定小米id
     */
    public static Observable<KotlinBean.BaseBean> bindXiaomiId(String userid, String xiaomiID, String token, int type) {
        return InstanceHolder.api
                .bindXiaomiId(userid, xiaomiID, token, type)
                .compose(RxSchedulers.compose());
    }

    /**
     * 根据id获取用户图像
     *
     * @param userid
     * @param token
     * @param type
     * @return
     */
    public static Observable<KotlinBean.BaseBean<KotlinBean.UserInfoBean>> getUserInfo(String userid, String token, int type) {
        return InstanceHolder.api
                .getUserInfo(userid, token, type);
//                .compose(RxSchedulers.compose());
    }

    /**
     * 发送push请求
     *
     *
     * @return
     */
    public static Observable<KotlinBean.BaseBean> pushToUser(String userid, String token, String fromid,
                                                             String content,
                                                             int type,
                                                             int message_type) {
        return InstanceHolder.api
                .pushToUser(userid, token, fromid, content, type, message_type)
                .compose(RxSchedulers.compose());
    }
    /**
     * 获取所有消息提醒
     * @param userid
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.PushBean>>> getAllPush(String userid, String token,int type) {
        return InstanceHolder.api
                .getAllPush(userid,token,type)
                .compose(RxSchedulers.compose());
    }
  /**
     * 获取所有未处理消息提醒
     * @param userid
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.PushBean>>> getAllUnhandlerPush(String userid, String token,int type) {
        return InstanceHolder.api
                .getAllUnhandlerPush(userid,token,type)
                .compose(RxSchedulers.compose());
    }

    /**
     * 回复提醒
     * @param id
     * @param userid
     * @param token
     * @param reply
     * @return
     */
    public static Observable<KotlinBean.BaseBean> replyPush(String id, String userid, String fromid,int type,String token, String reply) {
        return InstanceHolder.api
                .replyPush(id,userid,fromid,type,token,reply)
                .compose(RxSchedulers.compose());
    }

    /**
     * 留言
     * @return
     */
    public static Observable<KotlinBean.BaseBean> Comment(String userid,String fromid,
                                                          String token,
                                                         String content) {
        return InstanceHolder.api
                .comment(userid,token,fromid,content)
                .compose(RxSchedulers.compose());
    }
    /**
     * 获取留言
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.PushBean>>> getComment(String userid,
                                                         String content) {
        return InstanceHolder.api
                .getComment(userid,content)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取未回复留言
     * @param userid
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.PushBean>>>getReplyComment(String userid,
                                                       String token){
        return InstanceHolder.api
                .getReplyComment(userid,token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 回复留言
     * @param
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean>answerComment(String id,String token,String reply,
                                                  String userid,String fromid){
        return InstanceHolder.api
                .answerComment(id,token,1+"",reply,userid,fromid)
                .compose(RxSchedulers.compose());
    }

    /**
     * 根据userid获取医生信息
     * @param userid
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<DoctorBean.ContentBean>> getCurrentDoc(String userid, String token){
        return InstanceHolder.api
                .getCurrentDoc(userid,token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取检查项目
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.CheckBean>>>getCheckupPro(){
        return InstanceHolder.api
                .getCheckupPro()
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取检查报告
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.CheckReportBean>>>getCheckReport(String patientNo,String token){
        return InstanceHolder.api
                .getCheckupReport(patientNo,token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 用药信息
     * @param patientNo
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.MedicineBean>>>getMedicationInfo(String patientNo,String token){
        return InstanceHolder.api
                .getMedicationInfo(patientNo,token)
                .compose(RxSchedulers.compose());
    }
    /**
     * 住院信息
     * @param patientNo
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.HospitalBean>>>getHospitalizationInfo(String patientNo,String token){
        return InstanceHolder.api
                .getHospitalizationInfo(patientNo,token)
                .compose(RxSchedulers.compose());
    }

    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.DebitDetail>>>selectPaymentDetails(String patientNo,String token){
        return InstanceHolder.api
                .selectPaymentDetails(patientNo,token)
                .compose(RxSchedulers.compose());
    }
    
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.DebitDetail>>>selectUnpaidDetails(String patientNo,String token){
        return InstanceHolder.api
                .selectUnpaidDetails(patientNo,token)
                .compose(RxSchedulers.compose());
    }

    public static Observable<KotlinBean.BaseBean> updateContactMode(String userid, String token, String contactMode) {
        return InstanceHolder.api
                .updateContactMode(userid,token,contactMode)
                .compose(RxSchedulers.compose());
    }

    public static Observable<KotlinBean.BaseBean> updateAddress(String userid, String token, String address) {
        return InstanceHolder.api
                .updateAddress(userid,token,address)
                .compose(RxSchedulers.compose());
    }

    /**
     * 充值
     * @param patientNo
     * @param token
     * @param ammount
     * @return
     */
    public static Observable<KotlinBean.BaseBean<KotlinBean.ChargeResult>> charge(String patientNo, String token, String ammount){
        return InstanceHolder.api
                .recharge(patientNo,token,ammount)
                .compose(RxSchedulers.compose());
    }

    /**
     * 付费
     * @param patientNo
     * @param token
     * @param
     * @return
     */
    public static Observable<KotlinBean.BaseBean<KotlinBean.ChargeResult>> pay(String patientNo,String token){
        return InstanceHolder.api
                .debit(patientNo,token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 明细
     * @param patientNo
     * @param token
     * @param startTime
     * @param endTime
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<KotlinBean.BankDetail>>> payDetail(String patientNo,String token,String startTime,String endTime){
        return InstanceHolder.api
                .selectDetails(patientNo,token,startTime,endTime)
                .compose(RxSchedulers.compose());
    }
    /**
     * 待付款总金额
     * @param patientNo
     * @param token
     */
    public static Observable<KotlinBean.BaseBean<Object>> unpaidTotalAmount(String patientNo,String token){
        return InstanceHolder.api
                .unpaidTotalAmount(patientNo,token)
                .compose(RxSchedulers.compose());
    }

    /**
     * 评分
     */
    public static Observable<KotlinBean.BaseBean<Object>> saveEvaluate(
            String userid,
            String token,
            String code,
            String commentid,
            int type,
            float rank,
            String comment){
        return InstanceHolder.api
                .saveEvaluate(userid,token,code,commentid,type,rank,comment)
                .compose(RxSchedulers.compose());
    }

    /**
     * 获取平均分
     * @param userid
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<ArrayList<RatingBean>>> selectEvaluate(String userid, String token, int pagerNo){
            return InstanceHolder.api
                    .selectEvaluate(userid, token,pagerNo,20)
                    .compose(RxSchedulers.compose());
    }

    /**
     * 获取所有评价
     * @param userid
     * @param token
     * @return
     */
    public static Observable<KotlinBean.BaseBean<KotlinBean.RatingBeanAverage>> selectAverage(String userid, String token){
        return InstanceHolder.api
                .selectAverage(userid, token)
                .compose(RxSchedulers.compose());
    }
}
