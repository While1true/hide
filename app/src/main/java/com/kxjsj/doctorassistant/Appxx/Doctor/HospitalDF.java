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
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Mine.SickerHome;
import com.kxjsj.doctorassistant.Appxx.Sicker.Hospital.RemindActivity;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.Holder.MyHolder;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
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
    private SAdapter adapter;
    ArrayList<KotlinBean.PushBean> bean;

    /**
     * 两个title的位置
     */
    int lastIndex1 = -1;
    int lastIndex2 = -1;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        if (savedInstanceState != null) {
            lastIndex1 = savedInstanceState.getInt("index1", -1);
            lastIndex2 = savedInstanceState.getInt("index2", -1);
            bean = (ArrayList<KotlinBean.PushBean>) savedInstanceState.getSerializable("bean");
        }
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
        seemore.start(seemore.getCurrentTextColor(), getResources().getColor(R.color.colorecRed), 2000);

        Session userInfo = App.getUserInfo();
        RxBus.getDefault().toObservable(
                Constance.Rxbus.CALLHELP, BaseBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onNext: " + baseBean);
                        KotlinBean.PushBean bean = (KotlinBean.PushBean) baseBean.getData();
                        movetext.start(bean.getFromName() + ": " + bean.getContent());
                    }
                });
        //                    Intent intent = new Intent(view.getContext(), SickerHome.class);
//                            intent.putExtra("")
//                            startActivity(intent);
        adapter = new SAdapter()
                .addType(R.layout.title_layout, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.title, position == lastIndex1 ? "紧急事项" : "其他事项");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position == lastIndex1 || position == lastIndex2+1;
                    }
                })
                .addType(R.layout.doctor_answer_item, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onBind: " + position);
                        /**
                         * 除去标题所占的位置
                         */
                        if (lastIndex1 == -1) {
                            position = position - 1;
                        } else {
                            if(lastIndex2==-1){
                                position = position - 1;
                            }else {
                                if(position<=lastIndex2){
                                    position = position - 1;
                                }else{
                                    position = position - 2;
                                }
                            }
                        }
                        KotlinBean.PushBean pushBean = bean.get(position);
                        holder.setText(R.id.question, "来自" + pushBean.getFromName() + "的提醒--" + pushBean.getCreatorTime());
                        holder.setTextColor(R.id.question, getResources().getColor(pushBean.getMessage_type() == 1 ? R.color.colorecRed : R.color.navi_checked));
                        holder.setText(R.id.answer, null==pushBean.getReply()?"（待处理）":"（已处理）"+pushBean.getContent());

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

                        holder.itemView.setOnLongClickListener(v -> {
                            ReplyDialog replyDialog = new ReplyDialog();
                            replyDialog.setCallback(obj -> {
                                K2JUtils.toast(obj);
                                Session userInfo = App.getUserInfo();
                                ApiController.replyPush(pushBean.getId()+"", userInfo.getUserid(), pushBean.getFromid(), userInfo.getType(), userInfo.getToken(), obj)
                                        .subscribe(new DataObserver(getContext()) {
                                            @Override
                                            public void OnNEXT(Object beans) {
                                                replyDialog.dismiss();
                                                K2JUtils.toast("成功");
                                            getAllUnhandlerPush(App.getUserInfo());
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
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(linearLayoutManager, adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        getAllUnhandlerPush(userInfo);
                    }
                });
        if(bean == null) {
            getAllUnhandlerPush(userInfo);
        }else{
            setdata(bean);
        }
        if (firstLoad) {
            srecyclerview.setRefreshing();
        } else {
            adapter.showNomore();
        }

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
                        srecyclerview.notifyRefreshComplete();
                        adapter.ShowError();
                    }
                });
    }

    private void setdata(ArrayList<KotlinBean.PushBean> beans) {
        /**
         * 找出标题位置
         */
        lastIndex1=-1;
        lastIndex2=-1;
        for (int i = 0; i < beans.size() - 1; i++) {
            KotlinBean.PushBean pushBean = beans.get(i);
            int message_type = pushBean.getMessage_type();
            if (message_type == 1) {
                if (lastIndex1 == -1)
                    lastIndex1 = i;
            } else {
                lastIndex2 = i;
                break;
            }
        }
        if (bean.size() > 0) {
            int count = 0;
            if (lastIndex1 == -1) {
                count += 1;
            } else {
                if (lastIndex2 == -1) {
                    count = 1;
                } else {
                    count = 2;
                }
            }
            adapter.setCount(bean.size()+count);
            adapter.showItem();
            srecyclerview.notifyRefreshComplete();
        } else {
            adapter.showEmpty();
            srecyclerview.notifyRefreshComplete();
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

    @OnClick({R.id.ll, R.id.seemore})
    public void onViewClicked() {
        startActivity(new Intent(getContext(), RemindActivity.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bean != null) {
            outState.putInt("index1", lastIndex1);
            outState.putInt("index2", lastIndex2);
            outState.putSerializable("bean", bean);
        }
    }
}
