package com.kxjsj.doctorassistant.Appxx.Sicker.Hospital;

import android.os.Bundle;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;

/**
 * Created by vange on 2017/11/1.
 */

public class MedicalInfo extends BaseTitleActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("用药信息");

    }
}
