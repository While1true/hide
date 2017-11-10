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
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Screen.SizeUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/11/9.
 */

public class ReportActivity extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView sRecyclerView;
    private SAdapter adapter;
    ArrayList<KotlinBean.CheckReportBean> beans;
    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("检查报告");
        ButterKnife.bind(this);
        if(savedInstanceState!=null){
            savedInstanceState.getSerializable("bean");
        }
        if(beans==null) {
            loadData();
        }
        int padding = SizeUtils.dp2px(16);
        sRecyclerView.setPadding(padding,padding,padding,padding);
        adapter = new SAdapter()
                .addType(R.layout.check_report_item, new ItemHolder<KotlinBean.CheckReportBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.CheckReportBean item, int position) {
                    }

                    @Override
                    public boolean istype(KotlinBean.CheckReportBean item, int position) {
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
        Session userInfo = App.getUserInfo();
        ApiController.getCheckReport(userInfo.getPatientNo(),userInfo.getToken())
                .subscribe(new DataObserver<ArrayList<KotlinBean.CheckReportBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.CheckReportBean> bean) {
                        beans=bean;
                        if(beans.size()>0){
                            adapter.setBeanList(beans);
                            adapter.showItem();
                        }else{
                            adapter.showEmpty();
                        }
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: "+bean);
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
