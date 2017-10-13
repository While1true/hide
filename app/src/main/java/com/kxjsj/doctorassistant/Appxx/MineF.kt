package com.kxjsj.doctorassistant.Appxx

import android.os.Bundle
import android.view.View

import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.View.GradualButton
import io.reactivex.Observable
import kotlinx.android.synthetic.main.mine_layout.*
import log
import java.util.concurrent.TimeUnit

/**
 * Created by vange on 2017/9/19.
 */

class MineF : BaseFragment() {
    override fun initView(savedInstanceState: Bundle?) {
        log("------------")
        retainInstance=true
        loginOut.postDelayed({ loginOut.start(loginOut.currentTextColor,resources.getColor(R.color.colorPrimary)) },600)

    }

    override fun getLayoutId(): Int {
        return R.layout.mine_layout
    }

    override fun loadLazy() {

    }
}
