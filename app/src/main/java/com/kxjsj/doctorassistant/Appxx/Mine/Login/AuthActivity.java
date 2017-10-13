package com.kxjsj.doctorassistant.Appxx.Mine.Login;

import android.content.Intent;
import android.os.Bundle;

import com.kxjsj.doctorassistant.Appxx.Mine.Register.AuthPhoneF;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

/**
 * Created by vange on 2017/10/13.
 */

public class AuthActivity extends BaseTitleActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.framelayout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        AuthPhoneF authPhoneF = new AuthPhoneF();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, authPhoneF).commit();

        RxBus.getDefault()
                .toObservable(Constance.Rxbus.SIGNNEXT, BaseBean.class)
                .compose(RxSchedulers.compose())
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        String phone = (String) baseBean.getData();
                        Intent intent = new Intent(AuthActivity.this, ChangePassActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    }
                });

    }
}
