package com.kxjsj.doctorassistant.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.Utils.InputUtils;


/**
 * Created by vange on 2017/9/30.
 */

public abstract class BaseBottomSheetDialog extends BottomSheetDialogFragment {

    private TextView title;
    private ImageView close;
    private LinearLayout view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parent = inflater.inflate(R.layout.dialog_base_layout, container, false);
        title = parent.findViewById(R.id.title);
        close = parent.findViewById(R.id.close);
        close.setOnClickListener(view ->dismiss());
        view = parent.findViewById(R.id.content);
        View child = inflater.inflate(getLayoutId(), view, false);
        view.addView(child);
        return parent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view, savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    public void setTitle(CharSequence charSequence) {
        if (title != null) {
            title.setText(charSequence);
        }
    }


    /**
     * show
     *
     * @param manager
     */
    public BaseBottomSheetDialog show(FragmentManager manager) {
        show(manager, getClass().getSimpleName());
        return this;
    }
    @Override
    public void onResume() {
        super.onResume();
        BottomSheetBehavior.from(view.getRootView().findViewById(android.support.design.R.id.design_bottom_sheet)).setState(BottomSheetBehavior.STATE_EXPANDED);

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
