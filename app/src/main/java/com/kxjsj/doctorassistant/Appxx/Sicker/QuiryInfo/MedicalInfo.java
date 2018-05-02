package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.TextUtils;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.PositionHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.State.DefaultStateListener;

import java.util.ArrayList;

/**
 * Created by vange on 2017/11/1.
 */

public class MedicalInfo extends BaseTitleActivity {
    ArrayList<KotlinBean.MedicineBean>medicineBeans;
    private StateAdapter adapter;
    private RefreshLayout refreshLayout;
    String patientNo;
    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("用药信息");
        refreshLayout = findViewById(R.id.refreshlayout);
        if(savedInstanceState!=null){
            medicineBeans= (ArrayList<KotlinBean.MedicineBean>) savedInstanceState.getSerializable("bean");
            patientNo=savedInstanceState.getString("patientNo");
        }
        if(patientNo==null)
            patientNo= getIntent().getStringExtra("patientNo");

        adapter = new StateAdapter()
                .addType(R.layout.medical_info_item, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        TextUtils.setTextBold(holder,true,R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4);
                        holder.setText(R.id.tv1,"药品名称");
                        holder.setText(R.id.tv2,"单价");
                        holder.setText(R.id.tv3,"数量");
                        holder.setText(R.id.tv4,"用药时间");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==0;
                    }
                })
                .addType(R.layout.medical_info_item, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        KotlinBean.MedicineBean medicineBean = medicineBeans.get(position - 1);
                        holder.setText(R.id.tv1,medicineBean.getName());
                        holder.setText(R.id.tv2,"￥"+medicineBean.getPrice());
                        holder.setText(R.id.tv3,"x"+medicineBean.getNum()+" "+medicineBean.getUnit()+" "+medicineBean.getSpecifications());
                        holder.setText(R.id.tv4,medicineBean.getMedicationTime());
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        adapter.setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });
        if(medicineBeans==null){
            loadData();
        }else{
            if(medicineBeans.size()>0){
                adapter.setCount(medicineBeans.size()+1);
                adapter.showItem();
            }else{
                adapter.showEmpty();
            }
        }
        RecyclerView recyclerView = refreshLayout.getmScroll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    private void loadData() {
        Session userInfo = App.getUserInfo();
        ApiController.getMedicationInfo(patientNo,userInfo.getToken())
                .subscribe(new DataObserver<ArrayList<KotlinBean.MedicineBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.MedicineBean> bean) {
                        medicineBeans=bean;

                        if(medicineBeans.size()>0){
                            adapter.setCount(medicineBeans.size()+1);
                            adapter.showItem();
                        }else{
                            adapter.showEmpty();
                        }
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        K2JUtils.toast(error);
                        adapter.ShowError();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState!=null){
            outState.putSerializable("bean",medicineBeans);
            outState.putString("patientNo",patientNo);
        }
    }
}
