package com.kxjsj.doctorassistant.Appxx

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kxjsj.doctorassistant.Appxx.Mine.Login.LoginActivity
import com.kxjsj.doctorassistant.Appxx.Mine.Push.PushActivity
import com.kxjsj.doctorassistant.Appxx.Mine.UserInfoActivity

import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Utils.ActivityUtils
import com.kxjsj.doctorassistant.Utils.K2JUtils
import com.kxjsj.doctorassistant.View.GradualButton
import io.reactivex.Observable
import kotlinx.android.synthetic.main.hospital.*
import kotlinx.android.synthetic.main.mine_layout.*
import log
import java.util.concurrent.TimeUnit

/**
 * Created by vange on 2017/9/19.
 */

class MineF : BaseFragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.message ->""
            R.id.account ->startActivity(Intent(context,UserInfoActivity::class.java))
            R.id.push ->startActivity(Intent(context,PushActivity::class.java))
            R.id.suggest ->""
            R.id.about -> ""
            R.id.loginOut ->{
                K2JUtils.put("userinfo","")
                ActivityUtils.getInstance().removeAll()
                startActivity(Intent(context,LoginActivity::class.java))
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        log("------------")
        scrollview.setRefreshMode(true,true,false,false)
        retainInstance=true
        message.setOnClickListener(this);
        account.setOnClickListener (this)
        push.setOnClickListener (this)
        suggest.setOnClickListener (this)
        about.setOnClickListener(this)
        loginOut.setOnClickListener(this)
        loginOut.postDelayed({ loginOut.start(loginOut.currentTextColor,resources.getColor(R.color.colorPrimary)) },600)

    }

    override fun getLayoutId(): Int {
        return R.layout.mine_layout
    }

    override fun loadLazy() {

    }
}
