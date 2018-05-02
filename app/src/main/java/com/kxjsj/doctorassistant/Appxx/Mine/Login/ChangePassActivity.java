package com.kxjsj.doctorassistant.Appxx.Mine.Login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.RadioGroup;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.EncryptUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

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
    @BindView(R.id.group)
    RadioGroup group;
    private String phone;

    @Override
    protected int getContentLayoutId() {
        return R.layout.changepass_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("修改密码");
        phone = getIntent().getStringExtra("phone");
        if (savedInstanceState != null) {
            phone = savedInstanceState.getString("phone");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("phone", phone);
    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        ApiController.modifypassword(phone, EncryptUtils.md5(et.getText().toString()),
                group.getCheckedRadioButtonId() == R.id.user ? 0 : 1)
                .subscribe(new DataObserver(this) {
                    @Override
                    public void OnNEXT(Object o) {
                        finish();
                    }

                    @Override
                    public void OnERROR(String error) {
                        K2JUtils.toast(error);
                    }
                });
    }
}
