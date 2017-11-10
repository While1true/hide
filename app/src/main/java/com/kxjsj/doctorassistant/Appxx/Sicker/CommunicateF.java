package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/19.
 */

public class CommunicateF extends BaseFragment {
    int spancount;
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    private SAdapter baseMutilAdapter;
    private GridLayoutManager manager;
    ArrayList<DoctorBean> bean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
            bean=savedInstanceState.getParcelableArrayList("bean");
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
        intent.putExtra("data",bean);
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
        baseMutilAdapter = new SAdapter()
                .addType(R.layout.title_layout, new DoctorHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, DoctorBean item, int position) {
                        holder.setText(R.id.title,item.getMessage());
                    }

                    @Override
                    public boolean istype(DoctorBean item, int position) {
                        return item.getType()==0;
                    }

                    @Override
                    public int gridSpanSize(DoctorBean item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.doctor, new DoctorHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, DoctorBean item, int position) {
                        DoctorBean.ContentBean content = item.getContent();
                        holder.itemView.setOnClickListener(view -> go2DoctorHome(content));
                        GlideLoader.loadRound(holder.getView(R.id.imageview),content.getUserimg());
                        holder.setText(R.id.doctor, content.getName()+"\n"+content.getDepartment()+"\n"+content.getUserid());
                    }

                    @Override
                    public boolean istype(DoctorBean item, int position) {
                        return item.getType()==1;
                    }
                }) .setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, baseMutilAdapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                     loadData();
                    }
                });
                if(firstLoad) {
                    srecyclerview.setRefreshing();
                }else{
                    baseMutilAdapter.setBeanList(bean);
                    baseMutilAdapter.showState(SAdapter.SHOW_NOMORE, "无更多内容了");
                }

    }

    private void loadData() {
        ApiController.getAllDocotr(App.getToken())
                .subscribe(new DataObserver<ArrayList<DoctorBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<DoctorBean> bean) {
                        CommunicateF.this.bean=bean;
                        baseMutilAdapter.setBeanList(bean);
                        baseMutilAdapter.showState(SAdapter.SHOW_NOMORE, "无更多内容了");
                        srecyclerview.notifyRefreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        srecyclerview.notifyRefreshComplete();
                        baseMutilAdapter.showState(SAdapter.SHOW_ERROR, "发生错误了哦");
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
        if(bean!=null){
            outState.putParcelableArrayList("bean",bean);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
