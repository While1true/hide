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
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.InputUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/10/27.
 */

public class InputDialog extends BaseBottomSheetDialog {
    @BindView(R.id.et_input)
    TextInputEditText etInput;
    @BindView(R.id.layout_input)
    TextInputLayout layoutInput;
    Unbinder unbinder;

    String toUserid;
    @Override
    protected int getLayoutId() {
        return R.layout.input_dialog;
    }

    public void setToUserid(String toUserid){
        this.toUserid=toUserid;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("写下您的问题描述");

              if(savedInstanceState!=null){
                  toUserid=savedInstanceState.getString("toUserid");
              }
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
        if(toUserid!=null){
            outState.putString("toUserid",toUserid);
        }
    }

    @OnClick(R.id.send)
    public void onViewClicked() {
        String inputStr = etInput.getText().toString();
        if(TextUtils.isEmpty(inputStr)){
            K2JUtils.toast("请输入内容后在发送");
            return;
        }
        Session userInfo = App.getUserInfo();
        ApiController.pushToUser(new KotlinBean.PushBean(
                toUserid,userInfo.getToken(),userInfo.getUserid(),
                inputStr,userInfo.getType()==0?1:0,0))
                .subscribe(new DataObserver(this) {
                    @Override
                    public void OnNEXT(Object bean) {
                        etInput.setText("");
                        InputUtils.hideKeyboard(etInput);
                        dismiss();
                    }
                });

    }
}
