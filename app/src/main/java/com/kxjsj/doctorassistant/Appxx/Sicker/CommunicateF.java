package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Doctor.Home.DoctorHome;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Glide.GlideLoader;
import com.kxjsj.doctorassistant.Holder.DoctorHolder;
import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.State.DefaultStateListener;
import com.nestrefreshlib.State.Interface.StateEnum;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/19.
 */

public class CommunicateF extends BaseFragment {
    int spancount;
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    Unbinder unbinder;
    private StateAdapter adapter;
    private GridLayoutManager manager;
    ArrayList<DoctorBean> bean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
            bean = savedInstanceState.getParcelableArrayList("bean");
        }
        if(bean==null) {
            loadLazy();
        }
    }

    /**
     * 调到医生主页
     *
     * @param bean
     */
    private void go2DoctorHome(DoctorBean.ContentBean bean) {
        Intent intent = new Intent(getContext(), DoctorHome.class);
        intent.putExtra("data", bean);
        startActivity(intent);
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
        caculateSpanCount();
        manager = new GridLayoutManager(getContext(), spancount);
        adapter = new StateAdapter()
                .addType(R.layout.title_layout, new DoctorHolder() {
                    @Override
                    public void onBind(Holder holder, DoctorBean item, int position) {
                        holder.setText(R.id.title, item.getMessage());
                    }

                    @Override
                    public boolean istype(Object obj, int position) {
                        DoctorBean item = (DoctorBean) obj;
                        return item.getType() == 0;
                    }

                    @Override
                    public int gridSpanSize(DoctorBean item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.doctor, new DoctorHolder() {
                    @Override
                    public void onBind(Holder holder, DoctorBean item, int position) {
                        DoctorBean.ContentBean content = item.getContent();
                        holder.itemView.setOnClickListener(view -> go2DoctorHome(content));
                        GlideLoader.loadRound(holder.getView(R.id.imageview), content.getUserimg());
                        holder.setText(R.id.doctor, content.getName() + "\n" + content.getDepartment() + "\n" + content.getUserid());
                    }

                    @Override
                    public boolean istype(Object obj, int position) {
                        DoctorBean item = (DoctorBean) obj;
                        return item.getType() == 1;
                    }
                });
        adapter.setStateListener(new DefaultStateListener() {
            @Override
            public void netError(Context context) {
                loadData();
            }
        });
        RecyclerView recyclerView = refreshLayout.getmScroll();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                loadData();
            }

            @Override
            public void Loading() {

            }
        });
        if (firstLoad) {
            refreshLayout.setRefreshing();
        } else {
            if (bean != null && !bean.isEmpty()) {
                adapter.setList(bean);
                adapter.showState(StateEnum.SHOW_NOMORE, "无更多内容了");
            } else {
                adapter.showEmpty();
            }
        }

    }

    private void loadData() {
        ApiController.getAllDocotr(App.getToken())
                .subscribe(new DataObserver<ArrayList<DoctorBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<DoctorBean> bean) {
                        if (bean.isEmpty()) {
                            adapter.showEmpty();
                        } else {
                            CommunicateF.this.bean = bean;
                            adapter.setList(bean);
                            adapter.showState(StateEnum.SHOW_NOMORE, "无更多内容了");
                        }
                        refreshLayout.NotifyCompleteRefresh0();
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        refreshLayout.NotifyCompleteRefresh0();
                        adapter.showState(StateEnum.SHOW_ERROR, "发生错误了哦");
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bean != null) {
            outState.putParcelableArrayList("bean", bean);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
