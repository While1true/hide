package com.kxjsj.doctorassistant.Appxx.Mine.Register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.InputUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/10/9.
 */

public class SignF extends BaseFragment {
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
                .toObservable(Constance.Rxbus.CLOST_INPUT,BaseBean.class)
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        InputUtils.hideKeyboard(etaccount);
                    }
                });

       if(SignActivity.type==0)
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
        if(!InputUtils.validatePassword(layoutPassword,etpassword.getText().toString()))
            return;

        if(SignActivity.type==0){
            if(!InputUtils.validateAccount(layoutAccount,etpassword.getText().toString()))
                return;
        }


        InputUtils.hideKeyboard(etpassword);


        //TODO 注册
        Session session=new Session();
        RxBus.getDefault().post(new BaseBean<Session>(Constance.Rxbus.LOGIN_SUCCESS,session));
        getActivity().finish();
    }
}
