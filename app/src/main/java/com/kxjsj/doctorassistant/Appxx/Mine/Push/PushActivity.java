package com.kxjsj.doctorassistant.Appxx.Mine.Push;

import android.os.Bundle;
import android.util.Log;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.SettingView;
import com.xiaomi.mipush.sdk.MiPushClient;

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
        ButterKnife.bind(this);
        boolean showPush = K2JUtils.get("showPush", true);
        if(showPush){
            toggle.getaSwitch().setChecked(true);
        }
        toggle.getaSwitch().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                K2JUtils.put("showPush",true);
                K2JUtils.toast("已开启推送提醒");
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "isChecked: ");
            } else {
                K2JUtils.put("showPush",false);
                K2JUtils.toast("已关闭推送提醒");
            }
        });
    }
}
