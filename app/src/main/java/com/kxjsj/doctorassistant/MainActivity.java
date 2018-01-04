package com.kxjsj.doctorassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.kxjsj.doctorassistant.Appxx.Mine.Login.LoginActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.RadioActivity;
import com.kxjsj.doctorassistant.Appxx.Doctor.RadioActivityD;
import com.kxjsj.doctorassistant.Appxx.Sicker.Remark.RemarkActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.Utils.MyToast;
import com.qihoo360.replugin.RePlugin;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyToast.Companion.init();
//        RePlugin.startActivity(this,RePlugin.createIntent("app-debug","coms.kxjsj.myapplication.MainActivity"));
//startActivity(new Intent(this, RemarkActivity.class));
        Session userInfo = App.getUserInfo();
        String token = userInfo.getToken();
        if(!TextUtils.isEmpty(token)){
            if(userInfo.getType()==0){
                Sicker();
            }else{
                Doctor();
            }
        }else{
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

    }

    public void Sicker() {
        startActivity(new Intent(this, RadioActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    public void Doctor() {
        startActivity(new Intent(this, RadioActivityD.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
