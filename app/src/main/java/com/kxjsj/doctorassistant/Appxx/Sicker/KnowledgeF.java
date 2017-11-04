package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/28.
 */

public class KnowledgeF extends BaseFragment {


    private Unbinder unbinder;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
            loadLazy();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_infos;
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

    @OnClick({R.id.id, R.id.checkinfo, R.id.medicalinfo, R.id.money, R.id.checke_price, R.id.roominfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
