package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseBottomSheetDialog;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/10/31.
 */

public class ChargeDialog extends BaseBottomSheetDialog {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    int[]moneys={10,20,30,50,100,200,500,1000,2000,5000};

    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("请选择金额");
        int count= OrentionUtils.isPortrait(getContext())?3:4;
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),count);
        SAdapter sAdapter=new SAdapter(10)
                .addType(R.layout.label_layout, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.bt,moneys[position]+"元");
                        holder.setOnClickListener(R.id.bt,v -> {
                            K2JUtils.toast("已充值:"+moneys[position]+"元");
                            dismiss();
                        });
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        srecyclerview.setAdapter(gridLayoutManager,sAdapter);
        sAdapter.showItem();

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
