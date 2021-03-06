package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.Home.SickerHome;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.RemindActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.RxBaseBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.GradualButton;
import com.kxjsj.doctorassistant.View.MoveTextview;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.PositionHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.State.DefaultStateListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vange on 2017/9/28.
 */

public class HospitalDF extends BaseFragment {
    @BindView(R.id.movetext)
    MoveTextview movetext;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.seemore)
    GradualButton seemore;
    private StateAdapter adapter;
    ArrayList<KotlinBean.PushBean> bean = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        if (savedInstanceState != null) {
            bean = (ArrayList<KotlinBean.PushBean>) savedInstanceState.getSerializable("bean");
        }
        loadLazy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.hospital;
    }

    @Override
    protected void loadLazy() {
        seemore.start(seemore.getCurrentTextColor(), getResources().getColor(R.color.colorecRed), 2000);

        Session userInfo = App.getUserInfo();
        acqurePush();
        initAdapter();
        RecyclerView recyclerView = refreshLayout.getmScroll();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                getAllUnhandlerPush(userInfo);
            }

            @Override
            public void Loading() {

            }
        });
        if (!bean.isEmpty()) {
            adapter.showNomore();
        } else {
            refreshLayout.setRefreshing();
        }

    }

    private void initAdapter() {
        adapter = new StateAdapter()
                .addType(R.layout.title_layout, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.setText(R.id.title, "待处理事项");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position == 0;
                    }
                })
                .addType(R.layout.doctor_answer_item_reply, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onBind: " + position);
                        KotlinBean.PushBean pushBean = bean.get(position - 1);
                        holder.setText(R.id.question, "来自" + pushBean.getFromName() + "的提醒--" + pushBean.getCreatorTime());
                        holder.setTextColor(R.id.question, getResources().getColor(pushBean.getMessage_type() == 1 ? R.color.colorecRed : R.color.navi_checked));
                        holder.setText(R.id.answer, null == pushBean.getReply() ? "（待处理）" : "（已处理）" + pushBean.getContent());

                        holder.itemView.setOnClickListener(view -> {
                            ApiController.getUserInfo(pushBean.getFromid(), App.getToken(), 0)
                                    .compose(RxSchedulers.compose())
                                    .subscribe(new DataObserver<KotlinBean.UserInfoBean>(HospitalDF.this) {
                                        @Override
                                        public void OnNEXT(KotlinBean.UserInfoBean bean) {
                                            if (Constance.DEBUGTAG)
                                                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: " + bean.getPatientNo());
                                            Intent intent = new Intent(view.getContext(), SickerHome.class);
                                            intent.putExtra("patientNo", bean.getPatientNo());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void OnERROR(String error) {
                                            super.OnERROR(error);
                                        }
                                    });

                        });
                        if (null == pushBean.getReply()) {
                            holder.setVisible(R.id.mark, true);
                        } else {
                            holder.setVisible(R.id.mark, false);
                        }
                        holder.setOnClickListener(R.id.mark, v -> {
                            Session userInfo = App.getUserInfo();
                            ApiController.replyPush(pushBean.getId() + "", userInfo.getUserid(), pushBean.getFromid(), userInfo.getType(), userInfo.getToken(), "已处理")
                                    .subscribe(new DataObserver<Object>(getContext()) {
                                        @Override
                                        public void OnNEXT(Object beans) {
                                            K2JUtils.toast("成功");
                                            if (bean != null && bean.contains(pushBean)) {
                                                bean.remove(pushBean);
                                                adapter.setCount(bean.size() + 1);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        });
                        holder.itemView.setOnLongClickListener(v -> {
                            ReplyDialog replyDialog = new ReplyDialog();
                            replyDialog.setTitleStr("写下您的问题描述");
                            replyDialog.setCallback(obj -> {
                                replyDialog.dismiss();
                                K2JUtils.toast(obj);
                                Session userInfo = App.getUserInfo();
                                ApiController.replyPush(pushBean.getId() + "", userInfo.getUserid(), pushBean.getFromid(), userInfo.getType(), userInfo.getToken(), obj)
                                        .subscribe(new DataObserver<Object>(getContext()) {
                                            @Override
                                            public void OnNEXT(Object beans) {
                                                K2JUtils.toast("成功");
                                                if (bean != null && bean.contains(pushBean)) {
                                                    bean.remove(pushBean);
                                                    adapter.setCount(bean.size() + 1);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            });
                            replyDialog.show(getActivity().getSupportFragmentManager());
                            return true;
                        });
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                });
        adapter.setStateListener(new DefaultStateListener() {
            @Override
            public void netError(Context context) {
                getAllUnhandlerPush(App.getUserInfo());
            }
        });
    }

    private void acqurePush() {
        RxBus.getDefault().toObservable(
                Constance.Rxbus.CALLHELP, RxBaseBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<RxBaseBean>(this) {
                    @Override
                    public void onNext(RxBaseBean rxBaseBean) {
                        super.onNext(rxBaseBean);
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onNext: " + rxBaseBean);
                        KotlinBean.PushBean beanz = (KotlinBean.PushBean) rxBaseBean.getData();
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onNext: " + beanz);
                        bean.add(beanz);
                        sortList();
                        adapter.notifyDataSetChanged();
                        movetext.start(beanz.getFromName() + ": " + beanz.getContent());
                    }
                });
    }

    /**
     * 按紧急排序 按时间排序
     */
    private void sortList() {
        Collections.sort(bean, (o1, o2) -> {
            int type = o1.getType();
            int type1 = o2.getType();
            if (type > type1) {
                return 1;
            } else if (type == type1) {
                try {
                    Date parse = format.parse(o1.getCreatorTime());
                    Date parse2 = format.parse(o2.getCreatorTime());
                    if (parse.before(parse2)) {
                        return 1;
                    } else {
                        return -1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            return -1;
        });
    }

    private void getAllUnhandlerPush(Session userInfo) {
        ApiController.getAllUnhandlerPush(userInfo.getUserid(), userInfo.getToken(), userInfo.getType())
                .subscribe(new DataObserver<ArrayList<KotlinBean.PushBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.PushBean> beans) {
                        bean = beans;
                        setdata(beans);
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        refreshLayout.NotifyCompleteRefresh0();
                        adapter.ShowError();
                    }
                });
    }

    private void setdata(ArrayList<KotlinBean.PushBean> beans) {
        if (beans.size() > 0) {
            adapter.setCount(beans.size() + 1);
            adapter.showItem();
        } else {
            adapter.showEmpty();
        }
        refreshLayout.NotifyCompleteRefresh0();
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

    @OnClick({R.id.ll, R.id.seemore})
    public void onViewClicked() {
        startActivity(new Intent(getContext(), RemindActivity.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bean != null) {
            outState.putSerializable("bean", bean);
        }
    }
}
