package com.kxjsj.doctorassistant.Appxx.Sicker.Remark;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.View.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/12/11.
 */

public class RemarkActivity extends BaseTitleActivity {
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.remark_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setTitle("给医生评价");
        RxBus.getDefault().toObservable(Constance.Rxbus.DOCTOR_NURSE, BaseBean.class)
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        viewpager.setCurrentItem(1);
                        setTitle("给护士评价");
                    }
                });

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("type",position);
                RatingFragment ratingFragment = new RatingFragment();
                ratingFragment.setArguments(bundle);
                return ratingFragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

    }
}
