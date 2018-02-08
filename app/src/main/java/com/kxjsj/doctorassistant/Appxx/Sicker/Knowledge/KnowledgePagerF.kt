package com.kxjsj.doctorassistant.Appxx.Sicker.Knowledge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView
import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.JavaBean.KotlinBean
import com.kxjsj.doctorassistant.Net.ApiController
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Rx.DataObserver
import kotlinx.android.synthetic.main.srecyclerview.*
import java.util.ArrayList

/**
 * Created by vange on 2018/1/10.
 */
class KnowledgePagerF : BaseFragment() {
    var pager: KotlinBean.Title? = null
    var sAdapter: SAdapter<Any>? = null
    var beans: ArrayList<KotlinBean.Knowledge>? = null;
    override fun initView(savedInstanceState: Bundle?) {
        pager = arguments.getSerializable("pager") as KotlinBean.Title?
        val serializable = savedInstanceState?.getSerializable("beans")
        if (serializable != null) {
            beans = serializable as ArrayList<KotlinBean.Knowledge>
        }
//        if(beans!=null){
//            initAdapter()
//        }
    }

    private fun loaddata() {
        ApiController.getKnowedgesByType(pager?.type.toString())
                .subscribe(object : DataObserver<ArrayList<KotlinBean.Knowledge>>(context) {
                    override fun OnNEXT(bean: ArrayList<KotlinBean.Knowledge>?) {

                        if (beans == null) {
                            beans = bean
                        } else {
                            beans?.addAll(bean!!)
                        }
                        setdata()
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        sAdapter?.ShowError()
                    }
                })
    }

    private fun setdata() {
        sAdapter?.setBeanList(beans as List<Any>?)
        sAdapter?.showItem()
    }

    private fun initAdapter() {
        sAdapter = SAdapter<KotlinBean.Knowledge>()
                .addType(R.layout.knowledge_item, object : ItemHolder<KotlinBean.Knowledge>() {
                    override fun onBind(p0: SimpleViewHolder?, p1: KotlinBean.Knowledge?, p2: Int) {
                        p0?.apply {
                            setText(R.id.title, p1?.title)
                            setText(R.id.time, p1?.updatedate)
                            setText(R.id.type, p1?.type)
                            itemView.setOnClickListener {
                                var intent=Intent(context,X5WebviewActivity::class.java)
                                intent.putExtra("id",p1?.id)
                                intent.putExtra("title",p1?.title)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun istype(p0: KotlinBean.Knowledge?, p1: Int): Boolean {
                        return true
                    }
                }).setStateListener(object : DefaultStateListener() {
            override fun netError(p0: Context?) {
                loaddata()
            }
        })

        srecyclerview
                .setRefreshingListener(object : SRecyclerView.OnRefreshListener() {
                    override fun Refreshing() {
                    }

                    override fun Loading() {
                        super.Loading()

                        srecyclerview.notifyRefreshComplete()
                    }
                }).setRefreshMode(true, true, false, true)
        srecyclerview.setAdapter(LinearLayoutManager(context), sAdapter)
        srecyclerview.addItemDecorate(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

    }

    override fun getLayoutId(): Int {
        return R.layout.srecyclerview
    }

    override fun loadLazy() {
        initAdapter()
        if (beans == null)
            loaddata()
        else
            setdata()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("beans", beans)
    }

}