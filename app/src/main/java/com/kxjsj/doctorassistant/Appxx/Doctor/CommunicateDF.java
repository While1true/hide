package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
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
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    private SAdapter adapter;
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
                    srecyclerview.setRefreshing();
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
        if (!firstLoad) {
            loadLazy();
        }
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
        adapter = new SAdapter(beans)
                .addType(R.layout.title_layout, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.title, departmentStr + "病人");
                    }

                    @Override
                    public int gridSpanSize(int position) {
                        return manager.getSpanCount();
                    }

                    @Override
                    public boolean istype(int position) {
                        return position == 0;
                    }
                }).addType(R.layout.doctor, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.itemView.setOnClickListener(view -> go2SickHome(beans.get(position - 1)));
                        holder.setText(R.id.doctor, beans.get(position - 1).getPname() + "\n" + beans.get(position - 1).getPhoneNumber());
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData(departmentStr);
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                            loadData(departmentStr);
                    }
                });
        if (firstLoad) {
            srecyclerview.setRefreshing();
        } else {
            adapter.showState(SAdapter.SHOW_NOMORE, "无更多内容了");
        }

    }

    private void loadData(String department) {
        ApiController.getPatientByDepartment(App.getToken(), department)
                .subscribe(new DataObserver<ArrayList<PatientHome>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<PatientHome> bean) {
                        srecyclerview.notifyRefreshComplete();
                        beans = bean;
                        if(bean.size()==0)
                            adapter.showState(SAdapter.SHOW_EMPTY, "无更多内容了");
                        else{
                            adapter.setCount(bean.size() + 1);
                            adapter.showState(SAdapter.SHOW_NOMORE, "无更多内容了");
                        }
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        srecyclerview.notifyRefreshComplete();
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
