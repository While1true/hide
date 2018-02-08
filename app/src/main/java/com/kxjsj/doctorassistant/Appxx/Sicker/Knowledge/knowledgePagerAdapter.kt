package com.kxjsj.doctorassistant.Appxx.Sicker.Knowledge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.kxjsj.doctorassistant.JavaBean.KotlinBean


/**
 * Created by vange on 2018/1/10.
 */
class knowledgePagerAdapter : FragmentStatePagerAdapter {
    var datas: ArrayList<KotlinBean.Title>
    constructor(fm: FragmentManager, datas: ArrayList<KotlinBean.Title>) : super(fm) {
        this.datas = datas
    }

    override fun getItem(position: Int): Fragment? {
            var fragment = KnowledgePagerF()
            var bund = Bundle()
            bund.putSerializable("pager", datas?.get(position))
            fragment.arguments = bund
        return fragment
    }

    override fun getCount(): Int {
        return datas.size
    }

//    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
//        super.restoreState(Bundle(), loader)
//    }
//
//    override fun saveState(): Parcelable? {
//        return null
//    }

    override fun getPageTitle(position: Int): CharSequence {
        return datas.get(position)?.name
    }
}