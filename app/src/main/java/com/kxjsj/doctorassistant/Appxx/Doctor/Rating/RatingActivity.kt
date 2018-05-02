package com.kxjsj.doctorassistant.Appxx.Doctor.Rating

import android.os.Bundle
import com.kxjsj.doctorassistant.App
import com.kxjsj.doctorassistant.Component.BaseTitleActivity
import com.kxjsj.doctorassistant.Holder.RefreshLayoutPageLoading
import com.kxjsj.doctorassistant.JavaBean.KotlinBean
import com.kxjsj.doctorassistant.JavaBean.RatingBean
import com.kxjsj.doctorassistant.Net.ApiController
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Rx.DataObserver
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import kotlinx.android.synthetic.main.srecyclerview.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by vange on 2018/1/4.
 */
class RatingActivity : BaseTitleActivity() {

    val format: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
//    var beans: ArrayList<RatingBean>? = ArrayList<RatingBean>()
    var pagerloading:RefreshLayoutPageLoading<RatingBean>?=null
    override fun getContentLayoutId(): Int {
        return R.layout.srecyclerview
    }

    override fun initView(savedInstanceState: Bundle?) {
        var beanz: ArrayList<RatingBean>? = savedInstanceState?.getSerializable("bean") as ArrayList<RatingBean>?
        if (beanz != null) {
//            beans = beanz
            if(pagerloading!=null){
                pagerloading?.onGetInstanceState(savedInstanceState)
            }
        }
        title = "评价列表"

        pagerloading=object : RefreshLayoutPageLoading<RatingBean>(refreshlayout) {
            val userInfo = App.getUserInfo()
            override fun getObservable() = ApiController.selectEvaluate(userInfo.userid, userInfo.token, pagenum)

        }.addType(object : BaseHolder<RatingBean>(R.layout.check_report_item) {
            override fun onViewBind(holder: Holder?, bean: RatingBean, p2: Int) {
                holder?.setText(R.id.title, "评价人：" + bean.userid)
                holder?.setText(R.id.description, bean.content)
                holder?.setText(R.id.name, "结账单 ：" + bean.code)
                holder?.setTextColor(R.id.state, resources.getColor(R.color.colorecRed))
                holder?.setText(R.id.time, "发表时间：" + format.format(Date(bean.creationdate)))
                holder?.setText(R.id.state, "评分：" + bean.score.toString())

            }
        }).AddLifeOwner(this)
                .Go()
        loadData()

    }

    fun loadData() {
        val userInfo = App.getUserInfo()
        ApiController.selectAverage(userInfo.userid, userInfo.token)
                .subscribe(object : DataObserver<KotlinBean.RatingBeanAverage>(this) {
                    override fun OnNEXT(bean: KotlinBean.RatingBeanAverage?) {
                        title = "评价列表/平均分：" + bean?.AVERAGE
                    }
                })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
//        if (beans != null) {
//            outState?.putSerializable("bean", beans)
//        }
        pagerloading?.onSaveInstanceState(outState)
    }
}