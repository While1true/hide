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
    ){
        constructor() : this("")
    }

}