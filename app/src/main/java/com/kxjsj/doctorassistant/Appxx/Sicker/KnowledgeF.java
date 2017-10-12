package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Screen.SizeUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/28.
 */

public class KnowledgeF extends BaseFragment {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;

    int spancount;


    String[] categorys = {"最新推送", "饮食知识", "用药须知", "心理健康", "医学常识"};
    private GridLayoutManager manager;
    private SBaseMutilAdapter adapter;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
                loadLazy();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    private void caculateSpanCount() {
        if (OrentionUtils.isPortrait(getContext()))
            spancount = 3;
        else
            spancount = 4;
    }

    @Override
    protected void loadLazy() {
        srecyclerview.setPadding(0, SizeUtils.dp2px(15),0,0);
        caculateSpanCount();
        manager = new GridLayoutManager(getContext(), spancount);
        ArrayList<String> list = new ArrayList<>(30);
        for (String category : categorys) {
            list.add(category);
        }
        for (int i = 0; i < 20; i++) {
            list.add(i + "");
        }
        adapter = new SBaseMutilAdapter<String>(list)
                .addType(R.layout.label_layout, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.bt, item);
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position < categorys.length;
                    }
                })
                .addType(R.layout.title_layout, new SBaseMutilAdapter.ITEMHOLDER() {
                    @Override
                    public void onBind(SimpleViewHolder holder, Object item, int position) {
                        holder.setText(R.id.title, "浏览热门");
                    }

                    @Override
                    protected int gridSpanSize(Object item, int position) {
                        return manager.getSpanCount();
                    }

                    @Override
                    public boolean istype(Object item, int position) {
                        return position == categorys.length;
                    }
                })
                .addType(R.layout.doctor_answer_item, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {

                    }

                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return manager.getSpanCount();
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return true;
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        srecyclerview.postDelayed(() -> {
                            srecyclerview.notifyRefreshComplete();
                            adapter.showNomore();
                        },1000);
                    }
                });

                if(firstLoad) {
                    srecyclerview.setRefreshing();
                }else{
                    adapter.showNomore();
                }
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
