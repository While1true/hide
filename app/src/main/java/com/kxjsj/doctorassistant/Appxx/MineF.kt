package com.kxjsj.doctorassistant.Appxx

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kxjsj.doctorassistant.App
import com.kxjsj.doctorassistant.Appxx.Doctor.Rating.RatingActivity
import com.kxjsj.doctorassistant.Appxx.Mine.Comment.CommentActivity
import com.kxjsj.doctorassistant.Appxx.Mine.Push.PushActivity
import com.kxjsj.doctorassistant.Appxx.Mine.UserInfoActivity
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.RemindActivity

import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.Glide.GlideLoader
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Utils.K2JUtils
import com.kxjsj.doctorassistant.Utils.PublicUtils
import kotlinx.android.synthetic.main.mine_layout.*
import log
import toast

/**
 * Created by vange on 2017/9/19.
 */

class MineF : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> startActivity(Intent(context, CommentActivity::class.java))
            R.id.account -> startActivity(Intent(context, UserInfoActivity::class.java))
            R.id.push -> startActivity(Intent(context, PushActivity::class.java))
            R.id.suggest -> "说出您想说的".toast()
            R.id.about -> "网格科技有限公司杰作".toast()
            R.id.remind -> {
                var i = Intent(context, RemindActivity::class.java)
                i.putExtra("type", 1)
                startActivity(i)
            }
            R.id.loginOut -> {
                K2JUtils.put("userinfo", "")
                PublicUtils.loginOut(context)
            }
            R.id.rating -> startActivity(Intent(context, RatingActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        log("------------")
        scrollview.setRefreshMode(true, true, false, false)
        retainInstance = true
        val userInfo = App.getUserInfo()
        GlideLoader.loadRound(imageview, userInfo.imgUrl)
        info.text = userInfo.username + "\n" + userInfo.userid

        if (userInfo.type == 1) {
            message.setText("收到的留言")
            rating.visibility=View.VISIBLE
        }
        message.setOnClickListener(this)
        account.setOnClickListener(this)
        push.setOnClickListener(this)
        suggest.setOnClickListener(this)
        about.setOnClickListener(this)
        loginOut.setOnClickListener(this)
        remind.setOnClickListener(this)
        rating.setOnClickListener(this)
        loginOut.postDelayed({ loginOut.start(loginOut.currentTextColor, resources.getColor(R.color.colorPrimary)) }, 600)

    }

    override fun getLayoutId(): Int {
        return R.layout.mine_layout
    }

    override fun loadLazy() {

    }
}
