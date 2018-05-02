package com.kxjsj.doctorassistant.Appxx.Sicker.Knowledge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kxjsj.doctorassistant.Component.BaseTitleActivity
import com.kxjsj.doctorassistant.JavaBean.KotlinBean
import com.kxjsj.doctorassistant.Net.ApiController
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Rx.DataObserver
import com.nestrefreshlib.State.DefaultStateListener
import com.nestrefreshlib.State.StateLayout
import kotlinx.android.synthetic.main.x5web_layout.*

/**
 * Created by vange on 2018/1/11.
 */
class X5WebviewActivity:BaseTitleActivity() {
    var statelayout: StateLayout?=null
    var id:Int=0
    override fun initView(savedInstanceState: Bundle?) {
        var titlex=intent.getStringExtra("title")
        id=intent.getIntExtra("id",0)
        title = titlex
        statelayout= StateLayout(this)
        x5web.settings.apply {
            setSupportZoom(true)
            displayZoomControls=true
            builtInZoomControls=true
        }
        initData(id)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var titlex=intent?.getStringExtra("title")
        id=intent?.getIntExtra("id",0)!!
        title = titlex
        initData(id)
    }

    fun initData(id:Int){
        ApiController.getKnowedge(id)
                .subscribe (object : DataObserver<KotlinBean.Artical>(this){
                    override fun OnNEXT(bean: KotlinBean.Artical?) {
                        x5web.loadData(bean?.content,"text/html; charset=UTF-8", null)
                    }
                })
    }

    override fun getContentLayoutId(): Int {
        return R.layout.x5web_layout
    }

    override fun SetRootView(): View? {
        return statelayout?.setContent(contentLayoutId)?.setStateListener(object : DefaultStateListener(){
            override fun netError(p0: Context?) {
                initData(id)
            }
        })
    }
}