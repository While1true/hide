package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;

/**
 * Created by vange on 2017/11/1.
 */

public class RoomInfoActivity extends BaseTitleActivity {
    ArrayList<KotlinBean.HospitalBean>roomBeans;
    private SAdapter adapter;
    private SRecyclerView sRecyclerView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("住院信息");
        sRecyclerView = findViewById(R.id.srecyclerview);
        if(savedInstanceState!=null){
            roomBeans= (ArrayList<KotlinBean.HospitalBean>) savedInstanceState.getSerializable("bean");
        }

        adapter = new SAdapter()
                .addType(R.layout.room_info_item_title, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setTextBold(true,R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4);
                        holder.setText(R.id.tv1,"科室");
                        holder.setText(R.id.tv2,"入院时间");
                        holder.setText(R.id.tv3,"出院时间");
                        holder.setText(R.id.tv4,"责任医生");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==0;
                    }
                })
                .addType(R.layout.room_info_item, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setTextBold(false,R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4);
                        KotlinBean.HospitalBean hospitalBean = roomBeans.get(position - 1);
                        holder.setText(R.id.tv1,hospitalBean.getDepartment());
                        holder.setText(R.id.tv2,hospitalBean.getIntime());
                        holder.setText(R.id.tv3,hospitalBean.getOuttime());
                        holder.setText(R.id.tv4,hospitalBean.getReliabledoctor());
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent=new Intent(RoomInfoActivity.this,RoomDetailActivity.class);
                            intent.putExtra("bean",hospitalBean);
                            startActivity(intent);
                        });
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });
        if(roomBeans==null){
            loadData();
        }else{
            if(roomBeans.size()>0){
                adapter.setCount(roomBeans.size()+1);
                adapter.showItem();
            }else{
                adapter.showEmpty();
            }
        }
        sRecyclerView.setPullRate(2)
//                .addItemDecorate(new MyItemDecorate())
                .setAdapter(new LinearLayoutManager(this),adapter)
                .setRefreshMode(true,true,false,false);

    }

    private void loadData() {
        Session userInfo = App.getUserInfo();
        ApiController.getHospitalizationInfo(userInfo.getPatientNo(),userInfo.getToken())
                .subscribe(new DataObserver<ArrayList<KotlinBean.HospitalBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.HospitalBean> bean) {
                        roomBeans=bean;

                        if(roomBeans.size()>0){
                            adapter.setCount(roomBeans.size()+1);
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
            outState.putSerializable("bean",roomBeans);
        }
    }
}