package com.kxjsj.doctorassistant.JavaBean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.File
import java.io.Serializable
import java.util.*

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
     * 医生个人页数据
     */
    data class DoctorInfo constructor(var name: String):Serializable

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
    data class CheckBean constructor(var check_notes_no: String,
                                     var check_notes :String ,
                                     var check_description:String,
                                     var price : String)

    /**
     * 检查报告
     */
    data class CheckReportBean constructor(var checkup_no:String,
                                           var check_type:String,
                                           var check_part:String,
                                           var check_date:String,
                                           var check_description:String,
                                           var doctor_no:String,
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
                                        var reliablenurse:String)

    /**
     * 用药信息
     */
    data class MedicineBean constructor(var medicationtime:String,
                                        var medicinename:String,
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
     * 推送bean
     */
    @Entity(tableName = "pushBean")
    data class PushBean constructor(
            @ColumnInfo(name = "userid")
            var userid : String?=null,
            @ColumnInfo(name = "id")
            @PrimaryKey var id : Int=0,
            @ColumnInfo(name = "token") var token : String?=null,
            @ColumnInfo(name = "fromid") var fromid : String?=null,
            @ColumnInfo(name = "fromName") var fromName : String?=null,
            @ColumnInfo(name = "content") var content : String?=null,
            @ColumnInfo(name = "type") var type : Int=0,
            @ColumnInfo(name = "message_type") var message_type :Int=0,
            @ColumnInfo(name = "creatorTime") var creatorTime :String?=null,
            @ColumnInfo(name = "reply")  var reply :String?=null
    ): Serializable{
        constructor() : this("")
    }

}