package com.kxjsj.doctorassistant.Appxx.Mine.Register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Appxx.Doctor.RadioActivityD;
import com.kxjsj.doctorassistant.Appxx.Sicker.RadioActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.ActivityUtils;
import com.kxjsj.doctorassistant.Utils.EncryptUtils;
import com.kxjsj.doctorassistant.Utils.InputUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by vange on 2017/10/9.
 */

public class RegisterF extends BaseFragment {
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.etpassword)
    TextInputEditText etpassword;
    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;
    @BindView(R.id.etaccount)
    TextInputEditText etaccount;
    @BindView(R.id.layout_account)
    TextInputLayout layoutAccount;
    Unbinder unbinder;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        RxBus.getDefault()
                .toObservable(Constance.Rxbus.CLOST_INPUT, BaseBean.class)
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        InputUtils.hideKeyboard(etaccount);
                    }
                });

        if (RegisterActivity.type == 0)
            layoutAccount.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.signf;
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
        InputUtils.hideKeyboard(etpassword);
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.signnow)
    public void onViewClicked() {
        if (!InputUtils.validatePassword(layoutPassword, etpassword.getText().toString()))
            return;

        String cardnum = etaccount.getText().toString();
        if (RegisterActivity.type == 0) {
            if (TextUtils.isEmpty(cardnum) || cardnum.length() < 6) {
                K2JUtils.toast("请输入正确的就诊卡账号");
                return;
            }
        }


        InputUtils.hideKeyboard(etpassword);

        ApiController.register(RegisterActivity.phone, RegisterActivity.type
                , EncryptUtils.md5(etpassword.getText().toString()),cardnum)
                .subscribe(new DataObserver<Session>(this) {

                    @Override
                    public void OnNEXT(Session session) {
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: "+session.toString());
                        session.setType(RegisterActivity.type);
                        K2JUtils.put("userinfo",session.toString());
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(session.getUserid(), session.getUsername(), Uri.parse(session.getImgUrl())));
//                        RxBus.getDefault().post(new BaseBean<Session>(Constance.Rxbus.LOGIN_SUCCESS, session));
                        if(RegisterActivity.type==0){
                            startActivity(new Intent(getContext(), RadioActivity.class));
                        }else{
                            startActivity(new Intent(getContext(), RadioActivityD.class));
                        }
                        ActivityUtils.getInstance().removeAll();

                    }

                    @Override
                    public void OnERROR(String error) {
                     K2JUtils.toast(error);
                    }
                });



    }
}
