package com.kxjsj.doctorassistant.Appxx.Mine;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.DialogAndPopWindow.InputDialog;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.GradualButton;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/11.
 */

public class SickerHome extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;

    private String[] infos = {"检查报告", "用药信息"};
    private int[] dres = {R.drawable.ic_checkreport, R.drawable.ic_medicine};
    private SAdapter adapter;
    private boolean isfirst = true;
    PatientBed bean;

    String patientNo;
    private InputDialog inputDialog;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle(patientNo);
        ButterKnife.bind(this);
        if(getIntent()!=null)
            patientNo=getIntent().getStringExtra("patientNo");
        if(savedInstanceState!=null&&patientNo==null)
            patientNo=savedInstanceState.getString("patientNo");

        LinearLayoutManager manager = new LinearLayoutManager(this);

        adapter = new SAdapter()
                .addType(R.layout.sickerinfo, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.number,bean.getPatientNo());
                        holder.setText(R.id.name,bean.getPname());
                        holder.setText(R.id.bennumber,bean.getroomId());
                        holder.setText(R.id.instruction,bean.getRemark());
                        holder.setText(R.id.level,"¥"+bean.getBalance());
                        holder.setText(R.id.date,bean.getIntime());
                        holder.setText(R.id.nurse,bean.getNurname());
                        holder.setText(R.id.callhelp, "事项提醒");
                        holder.setText(R.id.help, "交流沟通");
                        holder.setOnClickListener(R.id.callhelp, (View v) -> {
                                showDialog();
                        });
                        holder.setOnClickListener(R.id.help, v -> {
                            ConversationUtils.startChartSingle(SickerHome.this, bean.getphoneNumber(),bean.getPname());

                        });
                        startButtonAnimator(holder);
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==0;
                    }
                })
                .addType(R.layout.label_layout, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        Drawable drawable = getResources().getDrawable(dres[position - 1]);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        TextView tv = holder.getView(R.id.bt);
                        tv.setCompoundDrawables(null, drawable, null, null);
                        holder.setText(R.id.bt, infos[position - 1]);
                    }

                    @Override
                    public boolean istype(int position) {
                        return position<infos.length+1;
                    }
                })
                .addType(R.layout.title_layout, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.title, "提醒记录");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==infos.length+1;
                    }
                })
                .addType(R.layout.doctor_answer_item, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.question, "提醒--10/12 8:41");
                        holder.setTextColor(R.id.question, getResources().getColor(R.color.navi_checked));
                        holder.setText(R.id.answer, "已知悉，正在准备换药");
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        doNet(patientNo);
                    }
                });
        if (savedInstanceState != null)
            isfirst = savedInstanceState.getBoolean("isfirst", true);
        if (isfirst) {
            isfirst = false;
            srecyclerview.setRefreshing();
        } else {
            adapter.showNomore();
        }

    }

    private void showDialog() {
        if(bean==null)
            return;
        if (inputDialog == null) {
            inputDialog = new InputDialog();
            inputDialog.setToUserid(bean.getPatientId());
        }
        inputDialog.show(getSupportFragmentManager());
    }

    private void doNet(String painentNo) {
        ApiController.getBedInfo(painentNo)
                .subscribe(new DataObserver<PatientBed>(this) {
                    @Override
                    public void OnNEXT(PatientBed beanz) {
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: " + bean);
                        bean = beanz;
                        adapter.setCount(20);
                        adapter.showNomore();
                        srecyclerview.notifyRefreshComplete();
                    }

                    @Override
                    public void OnERROR(String error) {
                        srecyclerview.notifyRefreshComplete();
                        adapter.ShowError();
                        K2JUtils.toast(error);
                    }
                });
    }

    private void startButtonAnimator(SimpleViewHolder holder) {
        GradualButton askButton = holder.getView(R.id.callhelp);
        askButton.start(0xff535353, getResources().getColor(R.color.colorPrimary));

        GradualButton communicateButton = holder.getView(R.id.help);
        communicateButton.start(0xff535353, getResources().getColor(R.color.navi_checked));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isfirst", isfirst);
        if(bean!=null)
        outState.putString("patientNo", patientNo);
    }
}
