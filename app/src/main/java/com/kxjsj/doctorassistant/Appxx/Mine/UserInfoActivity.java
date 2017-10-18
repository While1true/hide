package com.kxjsj.doctorassistant.Appxx.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kxjsj.doctorassistant.Appxx.Mine.Login.AuthActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.DialogAndPopWindow.PicDialog;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author bugaosuni
 *         Created by vange on 2017/10/18.
 */
public class UserInfoActivity extends BaseTitleActivity {

    private PicDialog dialog;

    @Override
    protected int getContentLayoutId() {
        return R.layout.userinfo_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("用户信息");
        RxBus.getDefault().
                toObservable(Constance.Rxbus.PIC, BaseBean.class)
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onNext: "+ baseBean.getCode());

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img, R.id.nickname, R.id.phone, R.id.sickcard, R.id.textView8, R.id.password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img:
                if(dialog==null) {
                    dialog = new PicDialog();
                }
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.nickname:
                break;
            case R.id.phone:
                break;
            case R.id.sickcard:
                break;
            case R.id.textView8:
                break;
            case R.id.password:
                startActivity(new Intent(this,AuthActivity.class));
                break;
        }
    }
}
