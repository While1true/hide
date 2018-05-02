package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.RemindActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.InputDialog;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Rx.RxBaseBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.kxjsj.doctorassistant.View.MoveTextview;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by vange on 2017/9/28.
 */

public class HospitalF extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.movetext)
    MoveTextview movetext;
    @BindView(R.id.seemore)
    GradualButton seemore;
    @BindView(R.id.sscrollview)
    RefreshLayout sscrollview;
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
    private InputDialog inputDialog;

    PatientBed beans;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        seemore.start(seemore.getCurrentTextColor(), getResources().getColor(R.color.colorecRed), 2000);
        buttoncallhelp.start(buttoncallhelp.getCurrentTextColor(), 0xff4070);
        buttonhelp.start(buttonhelp.getCurrentTextColor(), 0x4FB7DD);
        sscrollview.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                doNet();
            }

            @Override
            public void Loading() {

            }
        });
        if (savedInstanceState != null) {
            beans = (PatientBed) savedInstanceState.getSerializable("beans");
        }
        if (beans != null)
            updateUi(beans);
        if (beans == null)
            sscrollview.setRefreshing();

        RxBus.getDefault().toObservable(
                Constance.Rxbus.CALLHELP, RxBaseBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<RxBaseBean>(this) {
                    @Override
                    public void onNext(RxBaseBean rxBaseBean) {
                        super.onNext(rxBaseBean);
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onNext: " + rxBaseBean);
                        KotlinBean.PushBean bean = (KotlinBean.PushBean) rxBaseBean.getData();
                        movetext.start(bean.getFromName() + ": " + bean.getContent());
                    }
                });

    }

    private void doNet() {
        ApiController.getBedInfo(
                App.getUserInfo().getPatientNo())
                .subscribe(new DataObserver<PatientBed>(this) {
                    @Override
                    public void OnNEXT(PatientBed bean) {
                        sscrollview.NotifyCompleteRefresh0();
                        if (bean == null) {
                            K2JUtils.toast("获取信息失败");
                        }
                        beans = bean;
                        updateUi(bean);
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        sscrollview.NotifyCompleteRefresh0();
                    }
                });

    }

    private void updateUi(PatientBed bean) {
        number.setText(bean.getPatientNo());
        name.setText(bean.getPname());
        bennumber.setText(bean.getroomId());
        instruction.setText(bean.getRemark());
        nurse.setText(bean.getNurname());
        level.setText("¥" + bean.getBalance());
        date.setText(bean.getIntime());
//                      date.setText(bean.get);
//                      level.setText();
//                      nurse.setText();
        sscrollview.NotifyCompleteRefresh0();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("beans", beans);
    }

    @OnClick({R.id.ll, R.id.seemore, R.id.callhelp, R.id.help, R.id.help_nurse, R.id.callhelp_doctor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.help_nurse:
                if (beans == null)
                    return;
                ConversationUtils.startChartSingle(getContext(), beans.getNurid(), beans.getNurname());
                break;
            case R.id.callhelp_doctor:
                if (beans == null)
                    return;
                ConversationUtils.startChartSingle(getContext(), beans.getDocid(), beans.getDocname());
                break;
            case R.id.seemore:
            case R.id.ll:
                startActivity(new Intent(getContext(), RemindActivity.class));
                break;
            case R.id.callhelp:
                if (beans == null)
                    return;
                new MaterialDialog.Builder(getContext())
                        .title("请求紧急呼叫")
                        .content("请确认您真的需要紧急呼叫，以免影响其他人使用")
                        .positiveText("呼叫")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {
                            Session userInfo = App.getUserInfo();
                            ApiController.pushToUser(
                                    beans.getDocid(), userInfo.getToken(), userInfo.getUserid(),
                                    "请求紧急呼叫", userInfo.getType(), 1)
                                    .subscribe(new DataObserver<Object>(this) {
                                            @Override
                                            public void OnNEXT(Object bean) {
                                                K2JUtils.toast("发送成功");
                                            }

                                            @Override
                                            public void OnERROR(String error) {
                                                super.OnERROR(error);
                                                K2JUtils.toast("发送失败");
                                            }
                                    });
                        }).show();

//                ConversationUtils.sendMessage();
                break;
            case R.id.help:
                if (beans == null)
                    return;
                showInputDialog();
                break;
        }
    }

    private void showInputDialog() {
        if (inputDialog == null) {
            inputDialog = new InputDialog();
            inputDialog.setToUserid(beans.getDocid());
        }
        inputDialog.show(getActivity().getSupportFragmentManager());
    }

}
