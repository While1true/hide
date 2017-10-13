package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Appxx.Mine.Register.SignActivity;
import com.kxjsj.doctorassistant.Component.BaseDialogFragment;
import com.kxjsj.doctorassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/30.
 */

public class LoginDialog extends BaseDialogFragment {
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.sign)
    TextView sign;
    Unbinder unbinder;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_dialog;
    }

    @Override
    public LoginDialog show(FragmentManager manager) {
        return (LoginDialog) super.show(manager);
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



    @OnClick({R.id.sign, R.id.confirm, R.id.close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign:
                dismiss();
                startActivity(new Intent(getContext(), SignActivity.class));
                break;
            case R.id.confirm:
                dismiss();
                break;
            case R.id.close:
                dismiss();
                break;
        }
    }
}
