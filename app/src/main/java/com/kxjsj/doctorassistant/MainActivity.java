package com.kxjsj.doctorassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kxjsj.doctorassistant.Appxx.Sicker.RadioActivity;
import com.kxjsj.doctorassistant.Appxx.Doctor.RadioActivityD;
import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.Utils.MyToast;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose);
        MyToast.Companion.init();




    }

    public void Sicker(View v) {
        startActivity(new Intent(this, RadioActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    public void Doctor(View v) {
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
