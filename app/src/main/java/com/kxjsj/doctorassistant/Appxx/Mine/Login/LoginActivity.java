package com.kxjsj.doctorassistant.Appxx.Mine.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.kxjsj.doctorassistant.Appxx.Mine.Register.RegisterActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vange on 2017/10/13.
 */

public class LoginActivity extends BaseTitleActivity {
    @BindView(R.id.et_account)
    TextInputEditText etAccount;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.account_Layout)
    TextInputLayout accountLayout;
    @BindView(R.id.forget)
    Button forget;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.login)
    Button login;

    @Override
    protected int getContentLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.forget, R.id.register, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget:
                startActivity(new Intent(this,AuthActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login:
                break;
        }
    }
}
