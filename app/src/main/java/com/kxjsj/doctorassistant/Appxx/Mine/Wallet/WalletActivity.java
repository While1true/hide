package com.kxjsj.doctorassistant.Appxx.Mine.Wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ChargeDialog;
import com.kxjsj.doctorassistant.R;
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
        waveline.setMoney("324.00")
                .setTitle("余额");
        charge.start(getResources().getColor(R.color.navi_unchecked), getResources().getColor(R.color.colorecRed));
    }

    @OnClick({R.id.charge, R.id.detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.charge:
                if (dialog == null)
                    dialog = new ChargeDialog();

                dialog.show(getSupportFragmentManager());
                break;
            case R.id.detail:
                break;
        }
    }
}
