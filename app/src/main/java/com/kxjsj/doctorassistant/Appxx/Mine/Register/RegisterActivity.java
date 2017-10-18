package com.kxjsj.doctorassistant.Appxx.Mine.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.NoScrollViewPager;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/9.
 */

public class RegisterActivity extends BaseTitleActivity {
    public static String phone;
    @BindView(R.id.vp)
    NoScrollViewPager vp;

    /**
     * 0:病人 1:医生
     */
    public static int type=0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.noscrollviewpager;
    }

    @Override
    protected void onNavigationClicked() {
        RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.CLOST_INPUT,""));
        super.onNavigationClicked();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /**
         * 获取用户选择的注册类型
         */
        Intent intent = getIntent();
        if(intent!=null){
            type=intent.getIntExtra("type",0);
        }
        /**
         * 屏幕旋转恢复记录的类型
         */
        if(savedInstanceState!=null){
            type=savedInstanceState.getInt("type");
        }


        ButterKnife.bind(this);
        setTitle("注册账号");
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position==0)
                    return new AuthPhoneF();
                return new RegisterF();
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


        RxBus.getDefault()
                .toObservable(Constance.Rxbus.SIGNNEXT, BaseBean.class)
                .compose(RxSchedulers.compose())
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        phone = (String) baseBean.getData();
                        vp.setCurrentItem(1);
                        setTitle("输入密码");
                        K2JUtils.toast(phone);
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type",type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}