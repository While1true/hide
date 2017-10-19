package com.kxjsj.doctorassistant.Appxx.Mine.Push;

import android.os.Bundle;
import android.widget.CompoundButton;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.View.SettingView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/19.
 */

public class PushActivity extends BaseTitleActivity {
    @BindView(R.id.toggle)
    SettingView toggle;

    boolean isOpen;

    @Override
    protected int getContentLayoutId() {
        return R.layout.push_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        toggle.getaSwitch().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
            } else {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}