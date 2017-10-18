package com.kxjsj.doctorassistant.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.Utils.InputUtils;


/**
 * Created by vange on 2017/9/30.
 */

public abstract class BaseBottomSheetDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view,savedInstanceState);
    }

    protected  abstract int getLayoutId();

    protected abstract void initView(View view,Bundle savedInstanceState);


    /**
     * show
     * @param manager
     */
    public BaseBottomSheetDialog show(FragmentManager manager){
        show(manager,getClass().getSimpleName());
        return this;
    }

    @Override
    public void dismiss() {
        InputUtils.hideKeyboard(getDialog());
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);

    }
}
