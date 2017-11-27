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

public class MoneyDetailsActivity extends BaseTitleActivity {
    ArrayList<KotlinBean.DebitDetail>debitDetails;
    private SAdapter adapter;
    private SRecyclerView sRecyclerView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("费用明细");
        sRecyclerView = findViewById(R.id.srecyclerview);
        if(savedInstanceState!=null){
            debitDetails= (ArrayList<KotlinBean.DebitDetail>) savedInstanceState.getSerializable("bean");
        }

        adapter = new SAdapter()
                .addType(R.layout.room_info_item_title, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setTextBold(true,R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4);
                        holder.setText(R.id.tv1,"描述");
                        holder.setText(R.id.tv2,"日期");
                        holder.setText(R.id.tv3,"价格");
                        holder.setText(R.id.tv4,"状态");
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
                        KotlinBean.DebitDetail hospitalBean = debitDetails.get(position - 1);
                        holder.setText(R.id.tv1,hospitalBean.getName());
                        holder.setText(R.id.tv2,hospitalBean.getCheck_time());
                        holder.setText(R.id.tv3,hospitalBean.getPrice());
                        holder.setText(R.id.tv4,hospitalBean.getPayornot());
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
        if(debitDetails==null){
            loadData();
        }else{
            if(debitDetails.size()>0){
                adapter.setCount(debitDetails.size()+1);
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
//        Session userInfo = App.getUserInfo();
//        ApiController.getHospitalizationInfo(userInfo.getPatientNo(),userInfo.getToken())
//                .subscribe(new DataObserver<ArrayList<KotlinBean.DebitDetail>>(this) {
//                    @Override
//                    public void OnNEXT(ArrayList<KotlinBean.DebitDetail> bean) {
//                        debitDetails=bean;
//
//                        if(debitDetails.size()>0){
//                            adapter.setCount(debitDetails.size()+1);
//                            adapter.showItem();
//                        }else{
//                            adapter.showEmpty();
//                        }
//                    }
//
//                    @Override
//                    public void OnERROR(String error) {
//                        super.OnERROR(error);
//                        K2JUtils.toast(error);
//                        adapter.ShowError();
//                    }
//                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState!=null){
            outState.putSerializable("bean",debitDetails);
        }
    }
}
