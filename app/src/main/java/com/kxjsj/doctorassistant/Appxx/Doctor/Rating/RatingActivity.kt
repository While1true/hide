package com.kxjsj.doctorassistant.Appxx.Doctor.Rating

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView
import com.kxjsj.doctorassistant.App
import com.kxjsj.doctorassistant.Component.BaseTitleActivity
import com.kxjsj.doctorassistant.JavaBean.KotlinBean
import com.kxjsj.doctorassistant.JavaBean.RatingBean
import com.kxjsj.doctorassistant.Net.ApiController
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Rx.DataObserver
import kotlinx.android.synthetic.main.hospital.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by vange on 2018/1/4.
 */
class RatingActivity:BaseTitleActivity() {

    var adapter:SAdapter<Any>?=null
    var pager:Int=1
    var isloading:Boolean=false
    val format:SimpleDateFormat= SimpleDateFormat("yyyy/MM/dd")
    var beans:ArrayList<RatingBean>?= ArrayList<RatingBean>()
    override fun getContentLayoutId(): Int {
        return R.layout.srecyclerview
    }

    override fun initView(savedInstanceState: Bundle?) {
       var beanz:ArrayList<RatingBean>?= savedInstanceState?.getSerializable("bean") as ArrayList<RatingBean>?
        if(beanz!=null){
            beans=beanz
        }
        title="评价列表"
        adapter= SAdapter<RatingBean>(beans)
                .addType(R.layout.check_report_item,object :ItemHolder<RatingBean>(){
                    override fun istype(item: RatingBean?, position: Int): Boolean {
                        return true
                    }

                    override fun onBind(holder: SimpleViewHolder?, bean:RatingBean, position: Int) {
                        holder?.setText(R.id.title,"评价人："+bean.userid)
                        holder?.setText(R.id.description,bean.content)
                        holder?.setText(R.id.name,"结账单 ："+bean.code)
                        holder?.setTextColor(R.id.state,resources.getColor(R.color.colorecRed))
                        holder?.setText(R.id.time,"发表时间："+format.format(Date(bean.creationdate)))
                        holder?.setText(R.id.state,"评分："+bean.score.toString())
                    }
                })
        srecyclerview.setPullRate(2)
                .addDefaultHeaderFooter()
                .setAdapter(LinearLayoutManager(this),adapter)
                .setRefreshMode(true,true,true,true)
                .setRefreshingListener(object : SRecyclerView.OnRefreshListener() {
                    override fun Refreshing() {
                        pager=1
                        isloading=true
                       loadData()
                    }

                    override fun Loading() {
                        if(!isloading) {
                            isloading=true
                            pager += 1
                            loadData()
                        }
                    }
                })
                .setRefreshing()
    }

    fun loadData(){
        val userInfo = App.getUserInfo()
        ApiController.selectAverage(userInfo.userid,userInfo.token)
                .subscribe(object : DataObserver<KotlinBean.RatingBeanAverage>(this){
                    override fun OnNEXT(bean: KotlinBean.RatingBeanAverage?) {
                        title="评价列表/平均分："+bean?.AVERAGE
                    }
                })
        ApiController.selectEvaluate(userInfo.userid,userInfo.token,pager)
                .subscribe(object : DataObserver<ArrayList<RatingBean>>(this){
                    override fun OnNEXT(bean: ArrayList<RatingBean>?) {
                        srecyclerview.notifyRefreshComplete()
                        isloading=false
                        if(pager==1)
                            beans!!.clear()
                        if (bean != null) {
                            beans?.addAll(bean)
                        }
                        if(pager==1&&bean?.size==0){
                            adapter?.showEmpty()
                        }else {
                            adapter?.showItem()
                        }
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        isloading=false
                        srecyclerview.notifyRefreshComplete()
                        if(pager==1){
                            adapter?.ShowError()
                        }
                        if(pager>1)
                            pager-=1
                    }
                })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(beans!=null){
            outState?.putSerializable("bean",beans)
        }
    }
}