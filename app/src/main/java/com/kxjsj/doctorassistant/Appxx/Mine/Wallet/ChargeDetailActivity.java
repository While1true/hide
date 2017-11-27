package com.kxjsj.doctorassistant.Appxx.Mine.Wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.RoomDetailActivity;
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

public class ChargeDetailActivity extends BaseTitleActivity {
    ArrayList<KotlinBean.BankDetail>roomBeans;
    private SAdapter adapter;
    private SRecyclerView sRecyclerView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("缴费明细");
        sRecyclerView = findViewById(R.id.srecyclerview);
        if(savedInstanceState!=null){
            roomBeans= (ArrayList<KotlinBean.BankDetail>) savedInstanceState.getSerializable("bean");
        }

        adapter = new SAdapter()
                .addType(R.layout.room_info_item_title, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setTextBold(true,R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4);
                        holder.setText(R.id.tv1,"充值号");
                        holder.setText(R.id.tv2,"充值金额");
                        holder.setText(R.id.tv3,"余额");
                        holder.setText(R.id.tv4,"日期");
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
                        KotlinBean.BankDetail bankDetail = roomBeans.get(position - 1);
                        holder.setText(R.id.tv1,bankDetail.getPaymentNo());
                        holder.setText(R.id.tv2,bankDetail.getPay());
                        holder.setText(R.id.tv3,bankDetail.getBalance());
                        holder.setText(R.id.tv4,bankDetail.getPayTime());
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
        ApiController.payDetail(userInfo.getPatientNo(),userInfo.getToken(),null,null)
                .subscribe(new DataObserver<ArrayList<KotlinBean.BankDetail>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.BankDetail> bean) {
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
