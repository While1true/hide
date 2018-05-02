package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.Home.SickerHome;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.DialogAndPopWindow.DepartmentDialog;
import com.kxjsj.doctorassistant.JavaBean.PatientHome;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.PositionHolder;
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

public class CommunicateDF extends BaseFragment {
    int spancount;
    @BindView(R.id.department)
    GradualButton department;
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    Unbinder unbinder;
    private StateAdapter adapter;
    private GridLayoutManager manager;
    String departmentStr;
    ArrayList<PatientHome> beans;
    private DepartmentDialog departmentDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);
        department.start(getResources().getColor(R.color.navi_unchecked), getResources().getColor(R.color.colorPrimary));
        department.setOnClickListener(view -> {
            if (departmentDialog == null) {
                departmentDialog = new DepartmentDialog();
                departmentDialog.setCallBack(obj -> {
                    if (Constance.DEBUGTAG)
                        Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "initView: "+obj.toString());
                    departmentStr= (String) obj;
                    refreshLayout.setRefreshing();
                    loadData(departmentStr);
                });
            }
            departmentDialog.show(getActivity().getSupportFragmentManager());
        });
        if (savedInstanceState != null) {
            departmentStr = savedInstanceState.getString("department");
            beans = savedInstanceState.getParcelableArrayList("bean");
        }
        if(departmentStr==null){
            departmentStr=App.getUserInfo().getDepartment();
        }
        loadLazy();
    }

    /**
     * 调到病人主页
     *
     * @param bean
     */
    private void go2SickHome(PatientHome bean) {
        Intent intent = new Intent(getContext(), SickerHome.class);
        intent.putExtra("patientNo", bean.getPatientNo());
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
        return R.layout.hosptial_doctor_layout;
    }

    @Override
    protected void loadLazy() {
        caculateSpanCount();

        manager = new GridLayoutManager(getContext(), spancount);
        adapter = new StateAdapter(beans)
                .addType(R.layout.title_layout, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.setText(R.id.title, departmentStr + "病人");
                    }

                    @Override
                    public int gridSpanSize(Object item, int position) {
                        return manager.getSpanCount();
                    }


                    @Override
                    public boolean istype(int position) {
                        return position == 0;
                    }
                }).addType(R.layout.doctor, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.itemView.setOnClickListener(view -> go2SickHome(beans.get(position - 1)));
                        holder.setText(R.id.doctor, beans.get(position - 1).getPname() + "\n" + beans.get(position - 1).getPhoneNumber());
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        adapter.setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData(departmentStr);
                    }
                });
        RecyclerView recyclerview = refreshLayout.getmScroll();
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);

        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                loadData(departmentStr);
            }

            @Override
            public void Loading() {

            }
        });
        if (beans==null) {
            refreshLayout.setRefreshing();
        } else {
            adapter.showState(StateEnum.SHOW_NOMORE, "无更多内容了");
        }

    }

    private void loadData(String department) {
        ApiController.getPatientByDepartment(App.getToken(), department)
                .subscribe(new DataObserver<ArrayList<PatientHome>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<PatientHome> bean) {
                        refreshLayout.NotifyCompleteRefresh0();
                        beans = bean;
                        if(bean.size()==0)
                            adapter.showState(StateEnum.SHOW_EMPTY, "无更多内容了");
                        else{
                            adapter.setCount(bean.size() + 1);
                            adapter.showState(StateEnum.SHOW_NOMORE, "无更多内容了");
                        }
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        refreshLayout.NotifyCompleteRefresh0();
                        adapter.ShowError();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (beans != null)
            outState.putParcelableArrayList("bean", beans);
        if (departmentStr != null)
            outState.putString("department", departmentStr);
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
