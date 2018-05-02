package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Intent;
import android.os.Bundle;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.View.SettingView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/11/13.
 */

public class RoomDetailActivity extends BaseTitleActivity {
    @BindView(R.id.entertime)
    SettingView entertime;
    @BindView(R.id.outtime)
    SettingView outtime;
    @BindView(R.id.recordtime)
    SettingView recordtime;
    @BindView(R.id.department)
    SettingView department;
    @BindView(R.id.reliableDoctor)
    SettingView reliableDoctor;
    @BindView(R.id.reliableNurse)
    SettingView reliableNurse;
    @BindView(R.id.diagnose)
    SettingView diagnose;
    KotlinBean.HospitalBean hospitalBean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.room_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setTitle("住院详情");
        Intent intent = getIntent();
        if(intent!=null) {
            hospitalBean = (KotlinBean.HospitalBean) intent.getSerializableExtra("bean");
        }
        if(savedInstanceState!=null){
            hospitalBean= (KotlinBean.HospitalBean) savedInstanceState.getSerializable("bean");
        }

        entertime.setSubText(hospitalBean.getIntime());
        outtime.setSubText(hospitalBean.getOuttime());
        recordtime.setSubText(hospitalBean.getRecorddate());
        department.setSubText(hospitalBean.getDepartment());
        reliableDoctor.setSubText(hospitalBean.getReliabledoctor());
        reliableNurse.setSubText(hospitalBean.getReliablenurse());
        diagnose.setSubText("住院诊断："+hospitalBean.getDiagnose());
        diagnose.setTitleText("");


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(hospitalBean!=null){
            outState.putSerializable("bean",hospitalBean);
        }
    }
}
