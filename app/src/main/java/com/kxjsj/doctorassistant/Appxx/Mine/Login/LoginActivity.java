package com.kxjsj.doctorassistant.Appxx.Mine.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.kxjsj.doctorassistant.Appxx.Doctor.RadioActivityD;
import com.kxjsj.doctorassistant.Appxx.Mine.Register.ChoseRegisterActivity;
import com.kxjsj.doctorassistant.Appxx.Mine.Register.RegisterActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.RadioActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.EncryptUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.RegularUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

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
    @BindView(R.id.group)
    RadioGroup group;

    int checked=0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("登录");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            if(checkedId==R.id.doctor){
                checked=1;
            }else if(checkedId==R.id.user){
                checked=0;
            }
        });
    }

    @OnClick({R.id.forget, R.id.register, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget:
                startActivity(new Intent(this, AuthActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(this, ChoseRegisterActivity.class));
                break;
            case R.id.login:
                dologin();
                break;
        }
    }

    private void dologin() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString().trim();
        if(!RegularUtils.isMobile(account)){
            K2JUtils.toast("请输入正确的手机号码");
            return;
        }
        if(TextUtils.isEmpty(password)||password.length()<6||password.length()>18){
            K2JUtils.toast("请输入6-18位的密码");
            return;
        }
        ApiController.login(account,checked, EncryptUtils.md5(password))
                .subscribe(new DataObserver<Session>(this) {
                    @Override
                    public void OnNEXT(Session o) {
                       o.setType(checked);
                        K2JUtils.put("userinfo",o.toString());
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: "+o.toString());
                        if(checked==0){
                            startActivity(new Intent(LoginActivity.this, RadioActivity.class));
                        }else{
                            startActivity(new Intent(LoginActivity.this, RadioActivityD.class));
                        }
                        finish();
                    }

                    @Override
                    public void OnERROR(String error) {
                       K2JUtils.toast(error);
                    }
                });

    }
}
