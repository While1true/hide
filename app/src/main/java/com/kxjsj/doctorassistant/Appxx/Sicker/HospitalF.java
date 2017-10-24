package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.RefreshViews.SScrollview;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.kxjsj.doctorassistant.View.MoveTextview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/28.
 */

public class HospitalF extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.movetext)
    MoveTextview movetext;
    @BindView(R.id.id)
    Button id;
    @BindView(R.id.checkinfo)
    Button checkinfo;
    @BindView(R.id.medicalinfo)
    Button medicalinfo;
    @BindView(R.id.money)
    Button money;
    @BindView(R.id.checke_price)
    Button checkePrice;
    @BindView(R.id.roominfo)
    Button roominfo;
    @BindView(R.id.seemore)
    GradualButton seemore;
    @BindView(R.id.sscrollview)
    SScrollview sscrollview;
    @BindView(R.id.help)
    GradualButton buttonhelp;
    @BindView(R.id.callhelp)
    GradualButton buttoncallhelp;
    @BindView(R.id.instruction)
    TextView instruction;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.bennumber)
    TextView bennumber;
    @BindView(R.id.nurse)
    TextView nurse;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.level)
    TextView level;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        seemore.start(seemore.getCurrentTextColor(), getResources().getColor(R.color.colorecRed),2000);
        buttoncallhelp.start(buttoncallhelp.getCurrentTextColor(), 0xff4070);
        buttonhelp.start(buttonhelp.getCurrentTextColor(), 0x4FB7DD);
        sscrollview.addDefaultHeaderFooter()
                .setRefreshingListener(new SScrollview.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        sscrollview.postDelayed(() -> {
                            sscrollview.notifyRefreshComplete();
                        }, 500);
                    }
                });
        if (firstLoad)
            sscrollview.setRefreshing();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.hospital_layout;
    }

    @Override
    protected void loadLazy() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll, R.id.id, R.id.checkinfo, R.id.medicalinfo, R.id.money, R.id.checke_price, R.id.roominfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll:
                break;
            case R.id.id:
                break;
            case R.id.checkinfo:
                break;
            case R.id.medicalinfo:
                break;
            case R.id.money:
                break;
            case R.id.checke_price:
                break;
            case R.id.roominfo:
                break;
        }
    }
}
