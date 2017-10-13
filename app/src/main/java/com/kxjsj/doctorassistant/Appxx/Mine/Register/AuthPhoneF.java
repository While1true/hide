package com.kxjsj.doctorassistant.Appxx.Mine.Register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.InputUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.MobSMS.MessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;

/**
 * Created by vange on 2017/10/9.
 */

public class AuthPhoneF extends BaseFragment implements MessageUtils.MssListener {
    @BindView(R.id.introduce)
    TextView introduce;
    @BindView(R.id.etphone)
    TextInputEditText etphone;
    @BindView(R.id.sendcode)
    TextView sendcode;
    Unbinder unbinder;
    @BindView(R.id.etphonel)
    TextInputLayout etphonel;
    @BindView(R.id.etcode)
    TextInputEditText etcode;
    @BindView(R.id.etcodel)
    TextInputLayout etcodel;
    private MessageUtils.MyMssHandler handler;
    private MaterialDialog dialog;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        handler = new MessageUtils.MyMssHandler(this);
        MessageUtils.registListener(handler);
        etcode.setOnEditorActionListener((v, actionId, event) -> {
            if(EditorInfo.IME_ACTION_DONE==actionId){
                doAuth();
                return true;
            }
            return false;
        });

        RxBus.getDefault()
                .toObservable(Constance.Rxbus.CLOST_INPUT,BaseBean.class)
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        InputUtils.hideKeyboard(etphone);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.auth_phone_fragment;
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
        MessageUtils.unregistListener(handler);

    }

    @OnClick({R.id.auth, R.id.sendcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.auth:
                doAuth();
                break;
            case R.id.sendcode:
                if (InputUtils.validateAccount(etphonel, etphone.getText().toString())) {
                    etphone.setEnabled(false);
                    sendcode.setText("正在请求");
                    sendcode.setEnabled(false);
                    showdialog("正在请求");
                    MessageUtils.sendMessage(etphone.getText().toString());
                }
                break;
        }
    }

    private void doAuth() {
        if (TextUtils.isEmpty(etphone.getText().toString())) {
            K2JUtils.toast("请输入手机号", 1);
            return;
        }
        if (TextUtils.isEmpty(etcode.getText().toString())) {
            K2JUtils.toast("请输入验证码", 1);
            return;
        }
        InputUtils.hideKeyboard(etphone);
        showdialog("正在验证中");
        MessageUtils.authCode(etphone.getText().toString(), etcode.getText().toString());
    }

    private void showdialog(String title) {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(getContext())
                    .title(title)
                    .progress(true, 100)
                    .cancelable(false)
                    .build();
        dialog.setTitle(title);
        dialog.show();
    }

    @Override
    public void onSendSmsError(String msg) {
        etphonel.post(() -> {
            if(dialog!=null)
                dialog.dismiss();
            K2JUtils.toast(msg, 1);
            sendcode.setEnabled(true);
            etphone.setEnabled(true);
        });
    }

    @Override
    public void onSendSmsSuccess() {
        etphonel.post(() -> {
            if(dialog!=null)
                dialog.dismiss();
            etcodel.setVisibility(View.VISIBLE);
            etcodel.requestFocus();
        });

        Observable
                .interval(1, TimeUnit.SECONDS)
                .take(60)
                .compose(RxSchedulers.compose())
                .subscribe(new MyObserver<Long>(this) {
                    @Override
                    public void onNext(Long integer) {
                        super.onNext(integer);
                        sendcode.setText((60 - integer) + "后获取");

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        sendcode.setEnabled(true);
                        etphone.setEnabled(true);
                        sendcode.setText("获取验证码");
                    }
                });

    }

    @Override
    public void onAuthSuccess(HashMap<String, Object> datas) {
        if(dialog!=null)
            dialog.dismiss();
        RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.SIGNNEXT, etphone.getText().toString()));


    }
    @Override
    public void onReceiverSupportCountries(ArrayList<HashMap<String, Object>> countries) {

    }
}
