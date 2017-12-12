package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.CheckPartActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.IDInfoActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.MedicalInfo;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.ReportActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.RoomInfoActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.SelfPayActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.JavaBean.PatientBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.AlphaTransformer;
import com.kxjsj.doctorassistant.View.LoopFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/28.
 */

public class QuiryInfoF extends BaseFragment {

    int[] buttonIds = {R.id.id, R.id.medicalinfo, R.id.checkinfo, R.id.roominfo, R.id.money, R.id.checke_price};
    @BindView(R.id.imageView)
    ViewPager mViewPager;
    private Unbinder unbinder;
    public static PatientBed beans;
    public static String patientNo;

    int selected;


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        patientNo = App.getUserInfo().getPatientNo();
        if (savedInstanceState != null) {
            beans = (PatientBed) savedInstanceState.getSerializable("beans");
            setAndStartAnimation();
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_infos;
    }


    @Override
    protected void loadLazy() {
        ApiController.getBedInfo(
                App.getUserInfo().getPatientNo())
                .subscribe(new DataObserver<PatientBed>(this) {
                    @Override
                    public void OnNEXT(PatientBed bean) {
                        if (bean == null) {
                            K2JUtils.toast("获取信息失败");
                        }
                        beans = bean;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
        setAndStartAnimation();
    }

    private void setAndStartAnimation() {
        AlphaTransformer alphaTransformer = new AlphaTransformer();
        mViewPager.setPageTransformer(false,alphaTransformer);
        mViewPager.setAdapter(new LoopFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getActualCount() {
                return 6;
            }

            @Override
            public Fragment getActualItem(int position) {
                Bundle args = new Bundle();
                args.putInt("position", position);
                MyFragment myFragment = new MyFragment();
                myFragment.setArguments(args);
                return myFragment;
            }

            @Override
            public CharSequence getActualPagerTitle(int position) {
                return null;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                view.findViewById(buttonIds[selected]).setBackgroundResource(R.drawable.stoken_background_selector);
                selected = position%6;
                view.findViewById(buttonIds[selected]).setBackgroundResource(R.drawable.stoken_round_background_checked);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("beans", beans);
    }

    @OnClick({R.id.id, R.id.checkinfo, R.id.medicalinfo, R.id.money, R.id.checke_price, R.id.roominfo})
    public void onViewClicked(View view) {
        switch (null == view.getTag() ? view.getId() : (int) view.getTag()) {
            case R.id.id:
                if (beans == null)
                    return;
                Intent intent = new Intent(getContext(), IDInfoActivity.class);
                intent.putExtra("bean", beans);
                startActivity(intent);
                break;
            case R.id.checkinfo:
                Intent intent1 = new Intent(getContext(), ReportActivity.class);
                intent1.putExtra("patientNo", patientNo);
                startActivity(intent1);
                break;
            case R.id.medicalinfo:
                Intent intent2 = new Intent(getContext(), MedicalInfo.class);
                intent2.putExtra("patientNo", patientNo);
                startActivity(intent2);
                break;
            case R.id.money:
                startActivity(new Intent(getContext(), SelfPayActivity.class));
                break;
            case R.id.checke_price:
                startActivity(new Intent(getContext(), CheckPartActivity.class));
                break;
            case R.id.roominfo:
                startActivity(new Intent(getContext(), RoomInfoActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beans = null;
        patientNo=null;
    }
}
