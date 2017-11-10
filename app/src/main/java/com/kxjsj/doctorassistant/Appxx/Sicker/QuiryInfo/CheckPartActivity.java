package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;

import org.apache.http.impl.cookie.BestMatchSpec;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/11/9.
 */

public class CheckPartActivity extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView sRecyclerView;
    private SAdapter adapter;
    ArrayList<KotlinBean.CheckBean> beans;
    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("检查项目及价格");
        ButterKnife.bind(this);

        if(savedInstanceState!=null){
            beans= (ArrayList<KotlinBean.CheckBean>) savedInstanceState.getSerializable("bean");
        }
        if(beans==null) {
            loadData();
        }
        adapter = new SAdapter()
                .addType(R.layout.check_programmer, new ItemHolder<KotlinBean.CheckBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.CheckBean item, int position) {
                          holder.setText(R.id.title,item.getCheck_description());
                          holder.setText(R.id.price,"￥"+item.getPrice());
                          holder.setText(R.id.number,"项目编号："+item.getCheck_notes_no());
                          holder.setText(R.id.decscription,item.getCheck_description());
                    }

                    @Override
                    public boolean istype(KotlinBean.CheckBean item, int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });


        sRecyclerView.setAdapter(new GridLayoutManager(this, OrentionUtils.isPortrait(this)?2:3), adapter)
                .setRefreshMode(true,true,false,false)
                .setPullRate(2);
    }

    private void loadData() {
        ApiController.getCheckupPro()
                .subscribe(new DataObserver<ArrayList<KotlinBean.CheckBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.CheckBean> bean) {
                        beans=bean;
                        if(beans.size()>0) {
                            adapter.setBeanList(beans);
                            adapter.showItem();
                        }else{
                            adapter.showEmpty();
                        }
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: "+bean.toString());
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        adapter.ShowError();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(beans!=null){
            outState.putSerializable("bean",beans);
        }
    }
}
