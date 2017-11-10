package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.WalletActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.View.SettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vange on 2017/10/31.
 */

public class SelfPayActivity extends BaseTitleActivity {
    @BindView(R.id.detail)
    SettingView detail;
    @BindView(R.id.money)
    SettingView money;
    @BindView(R.id.needPay)
    TextView needPay;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getContentLayoutId() {
        return R.layout.selfpay_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("自助缴费");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pay, R.id.detail, R.id.money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay:
                break;
            case R.id.detail:

                break;
            case R.id.money:
                startActivity(new Intent(this, WalletActivity.class));
                break;
        }
    }
}
