package com.kxjsj.doctorassistant.Appxx.Mine.Login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vange on 2017/10/13.
 */

public class ChangePassActivity extends BaseTitleActivity {


    @BindView(R.id.et)
    TextInputEditText et;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.bt)
    Button bt;

    @Override
    protected int getContentLayoutId() {
        return R.layout.changepass_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("修改密码");
        String phone=getIntent().getStringExtra("phone");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
    }
}
