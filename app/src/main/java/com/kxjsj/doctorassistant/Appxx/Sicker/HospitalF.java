package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ck.hello.nestrefreshlib.View.RefreshViews.SScrollview;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.InputDialog;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.InputUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
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
    private InputDialog inputDialog;

    PatientBed beans;
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        seemore.start(seemore.getCurrentTextColor(), getResources().getColor(R.color.colorecRed), 2000);
        buttoncallhelp.start(buttoncallhelp.getCurrentTextColor(), 0xff4070);
        buttonhelp.start(buttonhelp.getCurrentTextColor(), 0x4FB7DD);
        sscrollview.addDefaultHeaderFooter()
                .setRefreshingListener(new SScrollview.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                    doNet();
                    }
                });
        if(savedInstanceState!=null){
            beans= (PatientBed) savedInstanceState.getSerializable("beans");
        }
        if(beans!=null)
            updateUi(beans);
        if (firstLoad||beans==null)
            sscrollview.setRefreshing();

    }

    private void doNet() {
        ApiController.getBedInfo(
                App.getUserInfo().getPatientNo())
                .subscribe(new DataObserver<PatientBed>(this) {
                    @Override
                    public void OnNEXT(PatientBed bean) {
                        if(bean==null){
                            K2JUtils.toast("获取信息失败");
                        }
                        beans=bean;
                        updateUi(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        sscrollview.notifyRefreshComplete();
                    }
                });

    }

    private void updateUi(PatientBed bean) {
        number.setText(bean.getPatientNo());
        name.setText(bean.getPname());
        bennumber.setText(bean.getroomId());
        instruction.setText(bean.getRemark());
        roominfo.setText(bean.getroomId());
        nurse.setText(bean.getNurname());
        level.setText("¥"+bean.getBalance());
        date.setText("¥"+bean.getIntime());
//                      date.setText(bean.get);
//                      level.setText();
//                      nurse.setText();
        sscrollview.notifyRefreshComplete();
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
        outState.putSerializable("beans",beans);
    }

    @OnClick({R.id.ll, R.id.id, R.id.checkinfo, R.id.medicalinfo, R.id.money, R.id.checke_price, R.id.roominfo,R.id.callhelp,R.id.help})
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
            case R.id.callhelp:
                Session userInfo = App.getUserInfo();
                ApiController.pushToUser(new KotlinBean.PushBean(
                        "12580",userInfo.getToken(),userInfo.getUserid(),
                        null,userInfo.getType()==0?1:0,1))
                        .subscribe(new DataObserver(this) {
                            @Override
                            public void OnNEXT(Object bean) {

                            }
                        });
//                ConversationUtils.sendMessage();
                break;
            case R.id.help:
                showInputDialog();
                break;
        }
    }

    private void showInputDialog() {
        if (inputDialog == null) {
            inputDialog = new InputDialog();
            inputDialog.setToUserid("12580");
        }
        inputDialog.show(getActivity().getSupportFragmentManager());
    }

}
