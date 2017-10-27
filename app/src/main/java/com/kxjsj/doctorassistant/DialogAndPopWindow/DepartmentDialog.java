package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.StringHolder;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseBottomSheetDialog;
import com.kxjsj.doctorassistant.Holder.CallBack;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/10/24.
 */

public class DepartmentDialog extends BaseBottomSheetDialog {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    private SAdapter adapter;
    ArrayList<String> bean;
    CallBack callback;

    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    public void setCallBack(CallBack callback) {
        this.callback = callback;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("选择要查看的科室");
       if(savedInstanceState!=null){
           bean=savedInstanceState.getStringArrayList("list");
       }
        if(bean==null)
           loadData();
        else
            setdata(bean);

    }

    private void loadData() {
        ApiController.getDepartment()
                .subscribe(new DataObserver<ArrayList<String>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<String> beans) {
                        bean = beans;
                        setdata(beans);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        adapter.showState(SAdapter.SHOW_ERROR, e.getMessage());
                    }
                });
    }

    private void setdata(ArrayList<String> beans) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        adapter = new SAdapter(bean)
                .addType(R.layout.label_layout, new StringHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.bt,item);
                        holder.setOnClickListener(R.id.bt,view -> {
                            K2JUtils.toast(item);
                            if(callback!=null) {
                                callback.onCallBack(item);
                                dismiss();
                            }
                        });
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });
        if(bean!=null){
            if(bean.size()==0)
                adapter.showEmpty();
            else
                adapter.showItem();
        }
        srecyclerview
                .setRefreshMode(false,false,false,false)
                .setAdapter(manager, adapter);
        adapter.setBeanList(beans);
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
        if(bean!=null)
        outState.putStringArrayList("list",bean);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
