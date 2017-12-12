package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.ChargeDetailActivity;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.MoneyDetailsActivity;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.WalletActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.Remark.RemarkActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
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
        ButterKnife.bind(this);
        setTitle("自助缴费");

    }

    @OnClick({R.id.pay, R.id.detail, R.id.money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay:
                new MaterialDialog.Builder(this)
                        .title("确认立即缴费？")
                        .positiveText("确认")
                        .onPositive((dialog, which) -> {
                            doPay("100");
                        }).build().show();
                break;
            case R.id.detail:
                startActivity(new Intent(this, MoneyDetailsActivity.class));
                break;
            case R.id.money:
                startActivity(new Intent(this, WalletActivity.class));
                break;
        }
    }

    private void doPay(String ammount) {
        Session userInfo = App.getUserInfo();
        ApiController.pay(userInfo.getPatientNo(),userInfo.getToken(),ammount)
                .subscribe(new DataObserver<KotlinBean.ChargeResult>(this) {
                    @Override
                    public void OnNEXT(KotlinBean.ChargeResult bean) {
                        needPay.setText("0");
                        K2JUtils.toast("缴费成功！"+bean.getPay());
                        startActivity(new Intent(SelfPayActivity.this,RemarkActivity.class));
                    }
                });
    }
}

