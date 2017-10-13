package com.kxjsj.doctorassistant.JavaBean

import java.io.File

/**
 * Created by vange on 2017/9/28.
 */
object KotlinBean {

    data class DataBean constructor(var type : Int,var content : String)

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
    data class DoctorInfo constructor(var name: String)

    /**
     * 下载进度封装
     */
    data class ProgressBean constructor(var name : String,var current : Long,var total : Long,var file : File )
}