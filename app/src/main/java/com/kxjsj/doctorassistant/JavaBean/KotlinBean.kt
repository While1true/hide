package com.kxjsj.doctorassistant.JavaBean

import java.io.File
import java.io.Serializable

/**
 * Created by vange on 2017/9/28.
 */
object KotlinBean {

    /**
     * baseDataBean
     */
    data class DataBean constructor(var type: Int = 0,var content : String,var message : String)

    /**
     * baeBean
     */
    data class BaseBean<T> constructor(var message :String,var error_code : Int, var data :T)

    /**
     * 首页病床的数据
     */
    data class SickBedBean constructor(var type : Int=0,var title:String="第几号楼", var name : String ="几号病床")

    /**
     * 首页医患交流 医生数据
     */
    data class Doctor constructor(var type : Int=0,var title:String="第几号楼", var name : String ="几号病床")


    /**
     * 下载进度封装
     */
    data class ProgressBean constructor(var name : String,var current : Long,var total : Long,var file : File )

    /**
     * 获取用户基本信息
     */
    data class UserInfoBean constructor(var name : String,var userid : String,var imgUrl :String, var userName : String,var patientNo :String)

    /**
     * 检查项目
     */
    data class CheckBean constructor(var notes: String,
                                     var remark :String ,
                                     var part:String,
                                     var name:String,
                                     var check_no:String,
                                     var description:String,
                                     var price : String)

    /**
     * 检查报告
     */
    data class CheckReportBean constructor(var checkup_num:String,
                                           var name:String,
                                           var price:String,
                                           var check_date:String,
                                           var part:String,
                                           var result_description:String,
                                           var doctorName:String,
                                           var status:Int)

    /**
     * 住院信息
     */
    data class HospitalBean constructor(var recorddate:String,
                                        var intime:String,
                                        var outtime:String,
                                        var diagnose:String,
                                        var department:String,
                                        var reliabledoctor:String,
                                        var reliablenurse:String):Serializable

    /**
     * 用药信息
     */
    data class MedicineBean constructor(var medicationTime:String,
                                        var name:String,
                                        var payornot:String,
                                        var doctorName:String,
                                        var specifications:String,
                                        var unit:String,
                                        var num :Int,
                                        var price:String)

    /**
     * 充值明细
     */
    data class BankDetail constructor(var paymentNo:String,
                                      var pay :String,
                                      var payTime:String,
                                      var balance:String)
    /**
     * 付费明细
     */
    data class DebitDetail constructor(var check_time:String,
                                      var name :String,
                                      var price:String,
                                      var payornot:String)

    data class ChargeResult constructor(var balance:String,var pay:String,var code:String)


    /**
     * 推送bean
     */
    data class PushBean constructor(
            var userid : String?=null,
            var id : Int=0,
            var token : String?=null,
            var fromid : String?=null,
            var fromName : String?=null,
            var content : String?=null,
            var type : Int=0,
            var message_type :Int=0,
            var creatorTime :String?=null,
            var reply :String?=null
    ): Serializable

    data class RatingBeanAverage constructor(var AVERAGE:String, var TOTALITY:String):Serializable

    data class Knowledge constructor(var id:Int,var type: String ,var title:String,var updatedate:String):Serializable
    data class Artical constructor(var id:Int,var type: String,var title:String,var updatedate:String,var content: String ):Serializable
    data class Title constructor(var type: Int,var name:String):Serializable
}