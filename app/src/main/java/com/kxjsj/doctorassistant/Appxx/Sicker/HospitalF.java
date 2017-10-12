package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Holder.MyHolder;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.kxjsj.doctorassistant.View.MoveTextview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/28.
 */

public class HospitalF extends BaseFragment {

    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    int spancount = 3;
    @BindView(R.id.movetext)
    MoveTextview movetext;
    private String[] menus = {"身份信息", "检查报告", "用药信息", "缴费信息", "检查项目及价格", "住院信息"};
    private int[] dres = {R.drawable.ic_id, R.drawable.ic_checkreport, R.drawable.ic_medicine, R.drawable.ic_money_detail, R.drawable.ic_checkprice, R.drawable.ic_roominfo};
    private SBaseMutilAdapter adapter;
    private GridLayoutManager manager;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        spancount = OrentionUtils.calculateSpanCount(getContext());
        manager = new GridLayoutManager(getContext(), spancount);

        ArrayList<String> list = new ArrayList<>(8);
        list.add("");
        list.add("");
        list.add("信息查询");
        for (int i = 0; i < menus.length; i++) {
            list.add(menus[i]);
        }
        adapter = new SBaseMutilAdapter(list)
                .addType(R.layout.sickerinfo, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {

                        GradualButton buttoncallhelp = holder.getView(R.id.callhelp);
                        GradualButton buttonhelp = holder.getView(R.id.help);

                        buttoncallhelp.start(buttoncallhelp.getCurrentTextColor(), 0xff4070);
                        buttonhelp.start(buttonhelp.getCurrentTextColor(), 0x4FB7DD);

                        buttoncallhelp.setOnClickListener(v -> {

                        });
                        buttonhelp.setOnClickListener(v -> {

                        });
                    }

                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return manager.getSpanCount();
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position == 0;
                    }
                })
                .addType(R.layout.doctor_answer_item, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.question, "提醒--10/12 8:41");
                        holder.setTextColor(R.id.question, getResources().getColor(R.color.navi_checked));
                        holder.setText(R.id.answer, "已知悉，正在准备换药");
                    }

                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return manager.getSpanCount();
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position == 1;
                    }
                })
                .addType(R.layout.title_layout, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.title, item);
                    }

                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return manager.getSpanCount();
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position == 2;
                    }
                })
                .addType(R.layout.label_layout, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        Drawable drawable = getResources().getDrawable(dres[position - 3]);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        TextView tv = holder.getView(R.id.bt);
                        tv.setCompoundDrawables(null, drawable, null, null);
                        holder.setText(R.id.bt, item);
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
                            adapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "我是有底线的");
                        }, 800);
                    }
                });
        if (firstLoad) {
            firstLoad = false;
            srecyclerview.setRefreshing();
        } else {
            adapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "我是有底线的");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.hospital;
    }

    @Override
    protected void loadLazy() {

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

    @OnClick({R.id.movetext, R.id.close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.movetext:
                break;
            case R.id.close:
                break;
        }
    }
}
