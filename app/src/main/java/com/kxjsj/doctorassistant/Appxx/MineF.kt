package com.kxjsj.doctorassistant.Appxx

import android.os.Bundle
import android.view.View

import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.R
import kotlinx.android.synthetic.main.mine_layout.*

/**
 * Created by vange on 2017/9/19.
 */

class MineF : BaseFragment() {
    override fun initView(savedInstanceState: Bundle?) {
        retainInstance = true
        loginOut.start(loginOut.currentTextColor,resources.getColor(R.color.colorPrimary))
    }

    override fun getLayoutId(): Int {
        return R.layout.mine_layout
    }

    override fun loadLazy() {

    }
}
