package com.kxjsj.doctorassistant.Appxx.Mine.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vange on 2017/10/13.
 */

public class ChoseRegisterActivity extends BaseTitleActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.register_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("注册账号");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.sick, R.id.doctor})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.sick:
                startActivity(0);
                break;
            case R.id.doctor:
                startActivity(1);
                break;
        }
    }

    private void startActivity(int type) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }
}
