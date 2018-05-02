package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.Component.BaseBottomSheetDialog;
import com.kxjsj.doctorassistant.Holder.CallBack;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.PositionHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/10/31.
 */

public class ChargeDialog extends BaseBottomSheetDialog {
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    Unbinder unbinder;
    int[]moneys={10,20,30,50,100,200,500,1000,2000,5000};
    CallBack<String> callback;
    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("请选择金额");
        int count= OrentionUtils.isPortrait(getContext())?3:4;
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),count);
        StateAdapter sAdapter=new StateAdapter(10)
                .addType(R.layout.label_layout, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.setText(R.id.bt,moneys[position]+"元");
                        holder.setOnClickListener(R.id.bt,v -> {
                            if(callback!=null){
                                callback.onCallBack(moneys[position]+"");
                            }
                            dismiss();
                        });
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        RecyclerView recyclerView=refreshLayout.getmScroll();
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        sAdapter.showItem();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void SetCallBack(CallBack<String> callback){
        this.callback=callback;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
