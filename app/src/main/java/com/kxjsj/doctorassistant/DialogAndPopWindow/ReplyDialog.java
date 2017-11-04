package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseBottomSheetDialog;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Holder.CallBack;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.InputUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/10/27.
 */

public class ReplyDialog extends BaseBottomSheetDialog {
    @BindView(R.id.et_input)
    TextInputEditText etInput;
    Unbinder unbinder;

    CallBack<String> callback;
    @Override
    protected int getLayoutId() {
        return R.layout.input_dialog;
    }

    public void setCallback(CallBack<String> callback){
        this.callback=callback;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("写下您的问题描述");
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


    @OnClick(R.id.send)
    public void onViewClicked() {
        String inputStr = etInput.getText().toString();
        if(TextUtils.isEmpty(inputStr)){
            K2JUtils.toast("请输入内容后在发送");
            return;
        }
        if(callback!=null){
            callback.onCallBack(inputStr);
        }

    }
}
