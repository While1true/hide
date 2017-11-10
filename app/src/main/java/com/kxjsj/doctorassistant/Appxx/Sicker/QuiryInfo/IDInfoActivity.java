package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Intent;
import android.os.Bundle;

import com.ck.hello.nestrefreshlib.View.RefreshViews.SScrollview;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.View.SettingView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/31.
 */

public class IDInfoActivity extends BaseTitleActivity {
    @BindView(R.id.sscrollview)
    SScrollview sScrollview;
    @BindView(R.id.name)
    SettingView name;
    @BindView(R.id.id)
    SettingView id;
    @BindView(R.id.phone)
    SettingView phone;
    @BindView(R.id.sex)
    SettingView sex;
    @BindView(R.id.age)
    SettingView age;
    @BindView(R.id.birthday)
    SettingView birthday;
    @BindView(R.id.address)
    SettingView address;
    PatientBed bean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.id_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        sScrollview.setRefreshMode(true,true,false,false);
        setTitle("身份信息");
        Intent intent = getIntent();
        if (intent != null) {
            bean = (PatientBed) intent.getSerializableExtra("bean");
        }
        if (bean == null && savedInstanceState != null) {
            bean = (PatientBed) savedInstanceState.getSerializable("bean");
        }
        if (bean == null)
            return;
        name.setSubText(bean.getPage());
        id.setSubText(bean.getIdentity());
        phone.setSubText(bean.getphoneNumber());
        sex.setSubText(bean.getPsex());
        age.setSubText(bean.getPage());
        birthday.setSubText(bean.getBirthday());
        address.setSubText(bean.getAdress());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bean != null) {
            outState.putSerializable("bean", bean);
        }
    }
}
