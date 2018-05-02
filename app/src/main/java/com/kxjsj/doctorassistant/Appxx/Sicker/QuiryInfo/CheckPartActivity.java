package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.State.DefaultStateListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/11/9.
 */

public class CheckPartActivity extends BaseTitleActivity {
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    private StateAdapter stateAdapter;
    ArrayList<KotlinBean.CheckBean> beans;
    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("检查项目及价格");
        ButterKnife.bind(this);

        if(savedInstanceState!=null){
            beans= (ArrayList<KotlinBean.CheckBean>) savedInstanceState.getSerializable("bean");
        }

        stateAdapter = new StateAdapter()
                .addType(R.layout.check_programmer, new ItemHolder<KotlinBean.CheckBean>() {
                    @Override
                    public void onBind(Holder holder, KotlinBean.CheckBean item, int position) {
                          holder.setText(R.id.title,item.getName()+"/"+item.getPart());
                          holder.setText(R.id.price,"￥"+item.getPrice());
                          holder.setText(R.id.number,"z："+item.getCheck_no());
                          holder.setText(R.id.description,item.getDescription()+"\n"+item.getNotes()+"\n"+item.getRemark());
                    }

                    @Override
                    public boolean istype(Object item, int position) {
                        return true;
                    }
                });
        stateAdapter.setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });

        if(beans==null) {
            loadData();
        }else{
            if(beans.size()>0) {
                stateAdapter.setList(beans);
                stateAdapter.showItem();
            }else{
                stateAdapter.showEmpty();
            }
        }
        refreshLayout.getAttrsUtils().setOVERSCROLL_ELASTIC(true);
        RecyclerView recyclerView = refreshLayout.getmScroll();
        recyclerView.setLayoutManager(new GridLayoutManager(this, OrentionUtils.isPortrait(this)?2:3));
        recyclerView.setAdapter(stateAdapter);
    }

    private void loadData() {
        ApiController.getCheckupPro()
                .subscribe(new DataObserver<ArrayList<KotlinBean.CheckBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.CheckBean> bean) {
                        beans=bean;
                        refreshLayout.NotifyCompleteRefresh0();
                        if(beans.size()>0) {
                            stateAdapter.setList(beans);
                            stateAdapter.showItem();
                        }else{
                            stateAdapter.showEmpty();
                        }
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: "+bean.toString());
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        refreshLayout.NotifyCompleteRefresh0();
                        stateAdapter.ShowError();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(beans!=null){
            outState.putSerializable("bean",beans);
        }
    }
}
