package com.kxjsj.doctorassistant.Appxx.Mine.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ChargeDialog;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.kxjsj.doctorassistant.View.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vange on 2017/10/23.
 */

public class WalletActivity extends BaseTitleActivity {
    @BindView(R.id.waveline)
    WaveView waveline;
    @BindView(R.id.charge)
    GradualButton charge;
    @BindView(R.id.detail)
    Button detail;
    private ChargeDialog dialog;

    @Override
    protected int getContentLayoutId() {
        return R.layout.charge_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setTitle("钱包");
        waveline.setMoney("正在获取数据")
                .setTitle("余额");
        loaddata();
        charge.start(getResources().getColor(R.color.navi_unchecked), getResources().getColor(R.color.colorecRed));
    }

    private void loaddata() {
        ApiController.getBedInfo(App.getUserInfo().getPatientNo())
                .subscribe(new DataObserver<PatientBed>(this) {
                    @Override
                    public void OnNEXT(PatientBed bean) {
                        waveline.setMoney(bean.getBalance())
                                .setTitle("余额");
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        waveline.setMoney("0.00")
                                .setTitle("余额");
                    }
                });
    }

    private void charge(String ammount) {
        Session userInfo = App.getUserInfo();
        ApiController.charge(userInfo.getPatientNo(),userInfo.getToken(),ammount)
                .subscribe(new DataObserver<KotlinBean.ChargeResult>(this) {
                    @Override
                    public void OnNEXT(KotlinBean.ChargeResult bean) {
                        waveline.setMoney(bean.getBalance())
                                .setTitle("余额");
                        String pay = bean.getPay();
                        if(pay!=null&&!pay.equals("0")){
                            K2JUtils.toast("充值"+pay+"成功！");
                        }
                    }
                });
    }

    @OnClick({R.id.charge, R.id.detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.charge:
                if (dialog == null) {
                    dialog = new ChargeDialog();
                }
                dialog.SetCallBack(obj -> {
                    charge(obj);
                });
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.detail:
                Intent intent=new Intent(this,ChargeDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
