package com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Sicker.Home.SickerHome;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.NotificationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/31.
 */

public class RemindActivity extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    private SAdapter sAdapter;
    ArrayList<KotlinBean.PushBean> bean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        NotificationManagerCompat.from(this).cancel(NotificationUtils.NOTIFICATION_REQUESTID);
        setTitle("提醒信息");
        if (savedInstanceState != null) {
            bean = (ArrayList<KotlinBean.PushBean>) savedInstanceState.getSerializable("bean");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sAdapter = new SAdapter()
                .addType(R.layout.doctor_answer_item, new ItemHolder<KotlinBean.PushBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, final KotlinBean.PushBean item, int position) {
                        holder.setText(R.id.question, item.getFromName() + "/" + item.getCreatorTime());
                        holder.setText(R.id.answer, (item.getMessage_type() == 0 ? (item.getContent()+"\n回复：" +item.getReply()): "请求紧急呼叫")+(null==item.getReply()?"（待处理）":"（已处理）"));
                        holder.setTextColor(R.id.answer, item.getMessage_type() == 0 ? 0xff535353 : getResources().getColor(R.color.navi_checked));
                        holder.setTextColor(R.id.question, item.getMessage_type() == 0 ? getResources().getColor(R.color.navi_checked) : getResources().getColor(R.color.colorecRed));
                        holder.itemView.setOnClickListener(v -> {
                            ApiController.getUserInfo(item.getFromid(), App.getToken(), item.getType())
                                    .compose(RxSchedulers.compose())
                                    .subscribe(new DataObserver<KotlinBean.UserInfoBean>(RemindActivity.this) {
                                        @Override
                                        public void OnNEXT(KotlinBean.UserInfoBean bean) {
                                            if (bean != null) {
                                                Intent intent = new Intent(v.getContext(), SickerHome.class);
                                                intent.putExtra("patientNo", bean.getPatientNo());
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        });

                            holder.itemView.setOnLongClickListener(v -> {
                                if(null==item.getReply()) {
                                    ReplyDialog replyDialog = new ReplyDialog();
                                    replyDialog.setTitleStr("写下提醒的内容");
                                    replyDialog.setCallback(obj -> {
                                        K2JUtils.toast(obj);
                                        Session userInfo = App.getUserInfo();
                                        ApiController.replyPush(item.getId()+"", userInfo.getUserid(), item.getFromid(), userInfo.getType(), userInfo.getToken(), obj)
                                                .subscribe(new DataObserver(RemindActivity.this) {
                                                    @Override
                                                    public void OnNEXT(Object bean) {
                                                        replyDialog.dismiss();
                                                        K2JUtils.toast("成功");
                                                        loadData();
                                                    }
                                                });
                                    });
                                    replyDialog.show(getSupportFragmentManager());
                                }else{
                                    K2JUtils.toast("已处理");
                                }
                                    return true;
                            });
                    }

                    @Override
                    public boolean istype(KotlinBean.PushBean item, int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(linearLayoutManager, sAdapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        loadData();
                    }
                });
        if (bean == null)
            srecyclerview.setRefreshing();

    }

    private void loadData() {
        srecyclerview.postDelayed(() -> {
            Session userInfo = App.getUserInfo();
            ApiController.getAllPush(userInfo.getUserid(), userInfo.getToken(), userInfo.getType())
                    .subscribe(new DataObserver<ArrayList<KotlinBean.PushBean>>(this) {
                        @Override
                        public void OnNEXT(ArrayList<KotlinBean.PushBean> beand) {
                            bean = beand;
                            if (Constance.DEBUGTAG)
                                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: " + beand);
                            sAdapter.setBeanList(beand);
                            srecyclerview.notifyRefreshComplete();
                            sAdapter.showItem();
                        }

                        @Override
                        public void OnERROR(String error) {
                            super.OnERROR(error);
                            srecyclerview.notifyRefreshComplete();
                            sAdapter.ShowError();
                        }
                    });

        }, 500);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bean != null) {
            outState.putSerializable("bean", bean);
        }
    }
}
