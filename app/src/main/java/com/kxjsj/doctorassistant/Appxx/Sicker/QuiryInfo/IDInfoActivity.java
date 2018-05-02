package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.SettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vange on 2017/10/31.
 */

public class IDInfoActivity extends BaseTitleActivity {
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
        name.setSubText(bean.getPname());
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


    @OnClick({R.id.name, R.id.id, R.id.phone, R.id.sex, R.id.age, R.id.birthday, R.id.address, R.id.sscrollview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.name:
                break;
            case R.id.id:
                break;
            case R.id.phone:
                ReplyDialog replyDialog = new ReplyDialog();;
                replyDialog.setTitleStr("情输入您要修改的联系电话");
                replyDialog.setCallback(obj -> {
                    Session userInfo = App.getUserInfo();
                    if(obj.toString().length()!=11){
                        K2JUtils.toast("请输入正确的手机号");
                        return;
                    }
                    ApiController.updateContactMode(userInfo.getUserid(),userInfo.getToken(),obj)
                            .subscribe(baseBean -> {
                                replyDialog.dismiss();
                                K2JUtils.toast("修改成功");
                                address.setSubText(obj);
                            });
                });
                replyDialog.setInputType(InputType.TYPE_CLASS_PHONE);
                replyDialog.show(getSupportFragmentManager());
                break;
            case R.id.sex:
                break;
            case R.id.age:
                break;
            case R.id.birthday:
                break;
            case R.id.address:
                ReplyDialog replyDialog2 = new ReplyDialog();;
                replyDialog2.setTitleStr("请输入您要修改的新地址");
                replyDialog2.setInputType(InputType.TYPE_CLASS_TEXT);
                replyDialog2.setCallback(obj -> {
                    Session userInfo = App.getUserInfo();
                    ApiController.updateAddress(userInfo.getUserid(),userInfo.getToken(),obj)
                            .subscribe(baseBean -> {
                                replyDialog2.dismiss();
                                K2JUtils.toast("修改成功");
                                address.setSubText(obj);
                            });
                });
                replyDialog2.show(getSupportFragmentManager());
                break;
            case R.id.sscrollview:
                break;
        }
    }
}
