package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Intent;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Appxx.Sicker.Hospital.RemindActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Holder.MyHolder;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.kxjsj.doctorassistant.View.MoveTextview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by vange on 2017/9/28.
 */

public class HospitalDF extends BaseFragment {
    @BindView(R.id.movetext)
    MoveTextview movetext;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    @BindView(R.id.seemore)
    GradualButton seemore;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        if (!firstLoad) {
            loadLazy();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.hospital;
    }

    @Override
    protected void loadLazy() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        seemore.start(seemore.getCurrentTextColor(),getResources().getColor(R.color.colorecRed),2000);

        ArrayList list = new ArrayList(15);
        for (int i = 0; i < 15; i++) {
            if (i < 6)
                list.add("xxM" + i + "请求紧急帮助");
            else
                list.add("xxm" + i + "\n换药时间到了，麻烦快来换药");
        }

        RxBus.getDefault().toObservable(
                Constance.Rxbus.CALLHELP, BaseBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onNext: "+baseBean);
                        KotlinBean.PushBean bean= (KotlinBean.PushBean) baseBean.getData();
                        movetext.start(bean.getFromName()+": "+bean.getContent());
                    }
                });
        SBaseMutilAdapter adapter = new SBaseMutilAdapter(list)
                .addType(R.layout.title_layout, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.title, position == 0 ? "紧急事项" : "待处理事项");
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position == 0 || position == 6;
                    }
                })
                .addType(R.layout.doctor_answer_item, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.question, "来自xxx的提醒--10/12 8:41");
                        holder.setTextColor(R.id.question, getResources().getColor(position < 6 ? R.color.colorecRed : R.color.navi_checked));
                        holder.setText(R.id.answer, position < 6 ? "待处理" : item);

                        holder.itemView.setOnClickListener(view -> {
//                            Intent intent = new Intent(view.getContext(), SickerHome.class);
//                            intent.putExtra("")
//                            startActivity(intent);
                        });
                        holder.itemView.setOnLongClickListener(v -> {
                            new MaterialDialog.Builder(v.getContext())
                                    .title("标记为已处理")
                                    .positiveText("确认")
                                    .negativeText("取消")
                                    .onPositive((dialog, which) -> {

                                    })
                                    .build()
                                    .show();

                            return true;
                        });

                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return true;
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(linearLayoutManager, adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        srecyclerview.postDelayed(() -> {
                            srecyclerview.notifyRefreshComplete();
                            adapter.showNomore();
                        }, 500);
                    }
                });
        if (firstLoad) {
            srecyclerview.setRefreshing();
        } else {
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

    @OnClick({R.id.ll,R.id.seemore})
    public void onViewClicked() {
        startActivity(new Intent(getContext(), RemindActivity.class));
    }
}
