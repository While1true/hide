package com.kxjsj.doctorassistant.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.View.LazyViewPager.LazyFragmentPagerAdapter;
import com.umeng.analytics.MobclickAgent;

import java.time.temporal.IsoFields;

/**
 * BaseFragment base
 */

public abstract class BaseFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {
    protected Toolbar toolbar;
    protected View view;
    protected boolean viewCreated = false;
    protected boolean firstLoad = true;
    protected boolean isVisable = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = SetRootView();
        if (view == null)
            view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        viewCreated = true;
        if(isVisable&&firstLoad){
            loadLazy();
            firstLoad=false;
        }
    }


    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 内容布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected View SetRootView() {

        return null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisable=isVisibleToUser;
        if (isVisibleToUser && viewCreated && firstLoad) {
            loadLazy();
            firstLoad = false;
        }
    }

    /**
     * 懒加载
     */
    protected abstract void loadLazy();


    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        toolbar = null;
        isVisable=false;
        firstLoad=true;
        viewCreated=false;
        RxLifeUtils.getInstance().remove(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

}
