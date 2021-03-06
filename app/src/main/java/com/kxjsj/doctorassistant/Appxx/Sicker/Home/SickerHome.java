package com.kxjsj.doctorassistant.Appxx.Sicker.Home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.MedicalInfo;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.ReportActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.DialogAndPopWindow.InputDialog;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.PositionHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.State.Interface.StateEnum;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/11.
 */
public class SickerHome extends BaseTitleActivity {
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;

    private String[] infos = {"检查报告", "用药信息"};
    private int[] dres = {R.drawable.ic_checkreport, R.drawable.ic_medicine};
    private StateAdapter adapter;
    private boolean isfirst = true;
    PatientBed bean;

    String patientNo;
    private InputDialog inputDialog;
    ArrayList<KotlinBean.PushBean> beans;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (getIntent() != null)
            patientNo = getIntent().getStringExtra("patientNo");
        if (savedInstanceState != null && patientNo == null)
            patientNo = savedInstanceState.getString("patientNo");
        setTitle(patientNo);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        adapter = new StateAdapter()
                .addType(R.layout.sickerhome, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.setText(R.id.number, bean.getPatientNo());
                        holder.setText(R.id.name, bean.getPname());
                        holder.setText(R.id.bennumber, bean.getroomId());
                        holder.setText(R.id.instruction, bean.getRemark());
                        holder.setText(R.id.level, "¥" + bean.getBalance());
                        holder.setText(R.id.date, bean.getIntime());
                        holder.setText(R.id.nurse, bean.getNurname());
                        holder.setText(R.id.callhelp, "事项提醒");
                        holder.setText(R.id.help, "交流沟通");
                        holder.setOnClickListener(R.id.callhelp, (View v) -> {
                            showDialog();
                        });
                        holder.setOnClickListener(R.id.help, v -> {
                            ConversationUtils.startChartSingle(SickerHome.this, bean.getphoneNumber(), bean.getPname());

                        });
                        startButtonAnimator(holder);
                    }

                    @Override
                    public boolean istype(int position) {
                        return position == 0;
                    }
                })
                .addType(R.layout.label_layout, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        Drawable drawable = getResources().getDrawable(dres[position - 1]);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        TextView tv = holder.getView(R.id.bt);
                        tv.setCompoundDrawables(null, drawable, null, null);
                        holder.setText(R.id.bt, infos[position - 1]);
                        holder.setOnClickListener(R.id.bt, (View v) -> {
                            if (position == 1) {
                                Intent intent = new Intent(SickerHome.this, ReportActivity.class);
                                intent.putExtra("patientNo", bean.getPatientNo());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SickerHome.this, MedicalInfo.class);
                                intent.putExtra("patientNo", bean.getPatientNo());
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public boolean istype(int position) {
                        return position == 1 || position == 2;
                    }
                })
                .addType(R.layout.title_layout, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.setText(R.id.title, "提醒记录");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position == infos.length + 1;
                    }
                })
                .addType(R.layout.doctor_answer_item, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        int i = position - 4;
                        KotlinBean.PushBean pushBean = beans.get(i);

                        holder.setText(R.id.question, "来自" + pushBean.getFromName() + "的提醒/" + pushBean.getCreatorTime());
                        holder.setTextColor(R.id.question, getResources().getColor(R.color.navi_checked));
                        holder.setText(R.id.answer, pushBean.getContent());
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        RecyclerView recyclerView = refreshLayout.getmScroll();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                doNet(patientNo);
            }

            @Override
            public void Loading() {

            }
        });
        if (savedInstanceState != null)
            isfirst = savedInstanceState.getBoolean("isfirst", true);
        if (isfirst) {
            isfirst = false;
            refreshLayout.setRefreshing();
        } else {
            adapter.showNomore();
        }

    }

    private void showDialog() {
        if (bean == null)
            return;
        if (inputDialog == null) {
            inputDialog = new InputDialog();
            inputDialog.setToUserid(bean.getPatientId());
        }
        inputDialog.show(getSupportFragmentManager());
    }

    private void doNet(String patientNo) {
        ApiController.getBedInfo(patientNo).flatMap(patientBedBaseBean -> {
            bean = patientBedBaseBean.getData();
            return ApiController.getAllUnhandlerPush(patientBedBaseBean.getData().getPatientId(), App.getToken(), 0);
        }).subscribe(new DataObserver<ArrayList<KotlinBean.PushBean>>(this) {
            @Override
            public void OnNEXT(ArrayList<KotlinBean.PushBean> bean) {
                beans = bean;
                adapter.setCount(4 + beans.size());
                adapter.showState(StateEnum.SHOW_NOMORE, "没有更多了");
                refreshLayout.NotifyCompleteRefresh0();
            }

            @Override
            public void OnERROR(String error) {
                refreshLayout.NotifyCompleteRefresh0();
                adapter.ShowError();
                K2JUtils.toast(error);
            }
        });
//        ;

    }

    private void startButtonAnimator(Holder holder) {
        GradualButton askButton = holder.getView(R.id.callhelp);
        askButton.start(0xff535353, getResources().getColor(R.color.colorPrimary));

        GradualButton communicateButton = holder.getView(R.id.help);
        communicateButton.start(0xff535353, getResources().getColor(R.color.navi_checked));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isfirst", isfirst);
        if (bean != null)
            outState.putString("patientNo", patientNo);
    }
}
