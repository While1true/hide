package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.MoneyDetailsActivity;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.MoneyToPayDetailsActivity;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.WalletActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.RxBaseBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.SettingView;

import org.json.JSONException;
import org.json.JSONObject;

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

        /**
         * 付款成功后清0
         */
        RxBus.getDefault()
                .toObservable(Constance.Rxbus.PAY_COMPLETE, RxBaseBean.class)
                .subscribe(new MyObserver<RxBaseBean>(this) {
                    @Override
                    public void onNext(RxBaseBean rxBaseBean) {
                        super.onNext(rxBaseBean);
                        loadData();
                    }
                });

        loadData();

    }

    private void loadData() {
        Session userInfo = App.getUserInfo();
        ApiController.unpaidTotalAmount(userInfo.getPatientNo(),userInfo.getToken())
                .subscribe(new DataObserver<Object>(this) {
                    @Override
                    public void OnNEXT(Object bean) {
                        String value =null;
                        try {
                            JSONObject object=new JSONObject(bean.toString());
                            value=object.getString("price");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        needPay.setText(value==null?"0.00":value);
                    }
                });
    }

    @OnClick({R.id.pay, R.id.detail, R.id.money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay:
                if(TextUtils.isEmpty(needPay.getText())||"0.00".equals(needPay.getText().toString())){
                    K2JUtils.toast("暂没有待缴款项");
                    return;
                }
                startActivity(new Intent(this, MoneyToPayDetailsActivity.class));
//                new MaterialDialog.Builder(this)
//                        .title("确认立即缴费？")
//                        .positiveText("确认")
//                        .onPositive((dialog, which) -> {
//                            doPay("100");
//                        }).build().show();
                break;
            case R.id.detail:
                startActivity(new Intent(this, MoneyDetailsActivity.class));
                break;
            case R.id.money:
                startActivity(new Intent(this, WalletActivity.class));
                break;
        }
    }
}

