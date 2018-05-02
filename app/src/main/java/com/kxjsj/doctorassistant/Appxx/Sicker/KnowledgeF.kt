package com.kxjsj.doctorassistant.Appxx.Sicker

import android.content.Context
import android.os.Bundle
import android.view.View
import com.kxjsj.doctorassistant.Appxx.Sicker.Knowledge.knowledgePagerAdapter
import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.JavaBean.KotlinBean
import com.kxjsj.doctorassistant.Net.ApiController
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Rx.DataObserver
import com.nestrefreshlib.State.DefaultStateListener
import com.nestrefreshlib.State.StateLayout
import kotlinx.android.synthetic.main.knowledge_layout.*

/**
 * Created by vange on 2018/1/10.
 */
class KnowledgeF : BaseFragment() {
    var stateLayout: StateLayout? = null
    var adapter: knowledgePagerAdapter? = null
    var beanx: ArrayList<KotlinBean.Title>? = null
    override fun initView(savedInstanceState: Bundle?) {
        retainInstance = true
        val serializable = savedInstanceState?.getSerializable("beanx")
        if (serializable != null)
            beanx = serializable as ArrayList<KotlinBean.Title>?
        if (beanx != null) {
            initAdapter(beanx)
        }
        if(beanx==null)
        getData()
    }

    override fun getLayoutId(): Int {

        return R.layout.knowledge_layout
    }

    private fun getData() {
        ApiController.getknowledgeBaseType()
                .subscribe(object : DataObserver<ArrayList<KotlinBean.Title>>(context) {
                    override fun OnNEXT(beans: ArrayList<KotlinBean.Title>?) {
                        if (beans?.size == 0)
                            stateLayout?.showEmpty()
                        else
                            stateLayout?.showItem()
                        if (beanx == null) {
                            beanx = beans
                        } else {
                            beanx?.addAll(beans!!)
                        }
                        initAdapter(beanx)
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        stateLayout?.ShowError()
                    }
                })

    }

    private fun initAdapter(beans: ArrayList<KotlinBean.Title>?) {
        adapter = knowledgePagerAdapter(childFragmentManager, beans!!)
        adapter?.datas=beans!!
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)
    }

    override fun SetRootView(): View? {
        if (view != null)
            return view
        stateLayout = StateLayout(context)
        return stateLayout
                ?.setContent(layoutId)
                ?.setStateListener(object : DefaultStateListener() {
                    override fun netError(p0: Context?) {
                        getData()
                    }
                })
    }

    override fun loadLazy() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("beanx", beanx)
    }
}