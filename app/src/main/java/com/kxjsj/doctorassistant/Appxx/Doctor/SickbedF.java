package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Appxx.Mine.SickerHome;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.SickBed;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

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
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    private SBaseMutilAdapter baseMutilAdapter;
    private GridLayoutManager manager;
    List<SickBed> list = new ArrayList<>(10);

    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);

        caculateSpanCount();


        manager = new GridLayoutManager(getContext(), spancount);
        baseMutilAdapter = new SBaseMutilAdapter(list)
                .addType(R.layout.title_layout, new SBaseMutilAdapter.ITEMHOLDER<SickBed>() {

                    @Override
                    public void onBind(SimpleViewHolder holder, SickBed item, int position) {
                        holder.setText(R.id.title, item.getFloorid() + "楼"+item.getRoomid()+"房间");
                    }

                    @Override
                    public boolean istype(SickBed item, int position) {
                        return item.getStatus() == 0;
                    }

                    @Override
                    protected int gridSpanSize(SickBed item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.sickbed_item_bed, new SBaseMutilAdapter.ITEMHOLDER<SickBed>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, SickBed item, int position) {
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
                    public boolean istype(SickBed item, int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        K2JUtils.toast("cuole", 1);
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, baseMutilAdapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        onRefresh();
                    }
                });
        if (firstLoad) {
            firstLoad = false;
            srecyclerview.setRefreshing();
        } else {
            baseMutilAdapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "无更多内容了");
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
                srecyclerview.notifyRefreshComplete();
                baseMutilAdapter.setBeanList(list);
                baseMutilAdapter.notifyDataSetChanged();
                if (list.size() != 0) {
                    baseMutilAdapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "我是有底线的");
                } else {
                    baseMutilAdapter.showState(SBaseMutilAdapter.SHOW_EMPTY, "没有相关数据");
                }
            }

            @Override
            public void OnERROR(String error) {
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
