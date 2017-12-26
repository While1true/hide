package com.kxjsj.doctorassistant.Appxx.Sicker.Remark;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Appxx.Sicker.RadioActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.RxBaseBean;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/12/11.
 */

public class RatingFragment extends BaseFragment {
    @BindView(R.id.notes)
    TextView notes;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.etText)
    TextInputEditText etText;
    @BindView(R.id.et_layout)
    TextInputLayout etLayout;
    @BindView(R.id.commit)
    Button commit;
    Unbinder unbinder;
    private int type;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        notes.setText("来给您的责任"+(type==0?"医生":"护士")+"做个评价吧\n以便我们做出更好的服务");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.rating_item;
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

    @OnClick(R.id.commit)
    public void onViewClicked() {
        float rating = this.rating.getRating();
        String commit = etText.getText().toString();



        if(type==0){
            RxBus.getDefault().post(new RxBaseBean<String>(Constance.Rxbus.DOCTOR_NURSE,""));
        }else {
            startActivity(new Intent(getContext(), RadioActivity.class));
        }
    }
}
