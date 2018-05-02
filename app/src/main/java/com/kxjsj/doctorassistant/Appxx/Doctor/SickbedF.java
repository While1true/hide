package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kxjsj.doctorassistant.Appxx.Sicker.Home.SickerHome;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.SickBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.State.DefaultStateListener;
import com.nestrefreshlib.State.Interface.StateEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/19.
 */

/**
 * setsetRetainInstance(true)
 * ondestory不会调用
 *
 * 控件不懂使用的参考我的简书页
 * **************  http://www.jianshu.com/u/07d24a532308  *******************
 */
public class SickbedF extends BaseFragment {
    int spancount;
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    Unbinder unbinder;
    private StateAdapter stateAdapter;
    private GridLayoutManager manager;
    List<SickBed> list = new ArrayList<>(10);

    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);

        caculateSpanCount();


        manager = new GridLayoutManager(getContext(), spancount);
        stateAdapter = new StateAdapter(list)
                .addType(R.layout.title_layout, new ItemHolder<SickBed>() {

                    @Override
                    public void onBind(Holder holder, SickBed item, int position) {
                        holder.setText(R.id.title, item.getFloorid() + "楼"+item.getRoomid()+"房间");
                    }


                    @Override
                    public boolean istype(Object obj, int position) {
                        SickBed item= (SickBed) obj;
                        return item.getStatus() == 0;
                    }

                    @Override
                    public int gridSpanSize(SickBed item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.sickbed_item_bed, new ItemHolder<SickBed>() {
                    @Override
                    public void onBind(Holder holder, SickBed item, int position) {
                        ImageView iv = holder.getView(R.id.iv);
                        TextView tv = holder.getView(R.id.tv);
                        if (item.getIsfree() == 0) {
                            iv.setImageResource(R.drawable.ic_avaliable);
                            tv.setText("病号" + item.getPatientNo());
                        } else {
                            iv.setImageResource(R.drawable.ic_lived);
                            tv.setText(item.getFloorid() + "楼" + item.getBedid() + item.getRoomid());
                        }

                        holder.itemView.setOnClickListener(view -> {
                            if (Constance.DEBUGTAG)
                                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onBind: "+item.getPatientNo());
                            Intent intent = new Intent(view.getContext(), SickerHome.class);
                            intent.putExtra("patientNo",item.getPatientNo());
                            startActivity(intent);
                        });

                    }

                    @Override
                    public boolean istype(Object item, int position) {
                        return true;
                    }
                });
        stateAdapter.setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                       onRefresh();
                    }
                });
        RecyclerView recyclerView=refreshLayout.getmScroll();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(stateAdapter);
        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                onRefresh();
            }

            @Override
            public void Loading() {

            }
        });
        if (firstLoad) {
            firstLoad = false;
            refreshLayout.setRefreshing();
        } else {
            stateAdapter.showState(StateEnum.SHOW_NOMORE, "无更多内容了");
        }

    }

    private void onRefresh() {
        ApiController.getAllBeds().subscribe(new DataObserver<List<SickBed>>(this) {
            @Override
            public void OnNEXT(List<SickBed> sickBeds) {
                System.out.println(sickBeds);
                int lastid = -1;
                list.clear();
                list.addAll(sickBeds);
                Collections.copy(list, sickBeds);
                for (int i = 0; i < sickBeds.size(); i++) {
                    SickBed sickBed = sickBeds.get(i);
                    int roomid = sickBed.getRoomid();
                    if (roomid != lastid) {
                        list.add(list.indexOf(sickBed),new SickBed(sickBed.getFloorid(), sickBed.getBedid(), sickBed.getRoomid()));
                        lastid = roomid;
                    }
                }
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: " + list.size());
                refreshLayout.NotifyCompleteRefresh0();
                stateAdapter.setList(list);
                stateAdapter.notifyDataSetChanged();
                if (list.size() != 0) {
                    stateAdapter.showState(StateEnum.SHOW_NOMORE, "我是有底线的");
                } else {
                    stateAdapter.showState(StateEnum.SHOW_EMPTY, "没有相关数据");
                }
            }

            @Override
            public void OnERROR(String error) {
                refreshLayout.NotifyCompleteRefresh0();
                stateAdapter.ShowError();
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnERROR: " + error);

            }
        });
    }

    private void caculateSpanCount() {
        if (OrentionUtils.isPortrait(getContext()))
            spancount = 3;
        else
            spancount = 4;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void loadLazy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onSaveInstanceState: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
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
}
