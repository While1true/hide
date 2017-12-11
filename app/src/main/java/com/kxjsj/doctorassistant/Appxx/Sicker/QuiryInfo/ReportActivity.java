package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.kxjsj.doctorassistant.View.WrapStaggeredManager;

import java.util.ArrayList;
import java.util.Random;

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
    String patientNo;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("检查报告");
        ButterKnife.bind(this);
        if(savedInstanceState!=null){
            beans= (ArrayList<KotlinBean.CheckReportBean>) savedInstanceState.getSerializable("bean");
            patientNo=savedInstanceState.getString("patientNo");
        }
        if(patientNo==null)
        patientNo= getIntent().getStringExtra("patientNo");

        adapter = new SAdapter()
                .addType(R.layout.check_report_item, new ItemHolder<KotlinBean.CheckReportBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.CheckReportBean item, int position) {
                      holder.setText(R.id.title,item.getName()+"/"+item.getPart()+"\n￥"+item.getPrice());
                      holder.setText(R.id.state,getStateString(item.getStatus()));
//                      holder.setText(R.id.description,item.getResult_description());
                      holder.setText(R.id.description,getResources().getString(R.string.test).substring(30*position%getResources().getString(R.string.test).length()));
//                      holder.setText(R.id.name,item.getDoctorName());
                      holder.setText(R.id.name,"最好的医生在这里");
                      holder.setText(R.id.time,item.getCheck_date());

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
        if(beans==null) {
            loadData();
        }else {
            if(beans.size()>0){
                adapter.setBeanList(beans);
                adapter.showItem();
            }else{
                adapter.showEmpty();
            }
        }

        int i = OrentionUtils.isPortrait(this) ? 2 : 3;
        sRecyclerView.setAdapter(new StaggeredGridLayoutManager(i,StaggeredGridLayoutManager.VERTICAL),adapter)
                .setRefreshMode(true,true,false,false)
                .setPullRate(2);
    }

    /**
     * 检查状态 0：未检查（未报到）
     * 1：已报到未检查（已报到）
     * 2：已检查未诊断（已检查）
     * 3：已诊断（完成报告）
     * 4：报告审核通过（已签片）
     * 5：报告审核退回（可编辑）
     * @param state
     * @return
     */
    private String getStateString(int state){
        switch (state){
            case 0:
                return "未报到";
            case 1:
                return "已报到";
            case 2:
                return "已检查";
            case 3:
                return "完成报告";
            case 4:
                return "已签片";
            case 5:
                return "可编辑";
                default:
                    return null;
        }
    }

    private void loadData() {
        Session userInfo = App.getUserInfo();
        ApiController.getCheckReport(patientNo,userInfo.getToken())
                .subscribe(new DataObserver<ArrayList<KotlinBean.CheckReportBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.CheckReportBean> bean) {
                        beans=bean;
                        beans.addAll(bean);
                        beans.addAll(bean);
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
            outState.putString("patientNo",patientNo);
        }
    }
}
