package com.kxjsj.doctorassistant.Appxx.Mine.Comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Doctor.Home.DoctorHome;
import com.kxjsj.doctorassistant.Appxx.Sicker.Home.SickerHome;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.Holder.CallBack;
import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.BaseHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.State.DefaultStateListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/11/8.
 */

public class CommentActivity extends BaseTitleActivity {
    @BindView(R.id.refreshlayout)
    RefreshLayout refreshLayout;
    private StateAdapter adapter;
    ArrayList<KotlinBean.PushBean> beans;
    private Session userInfo;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        userInfo = App.getUserInfo();
        ButterKnife.bind(this);
        setTitle("收到的留言");
        if (savedInstanceState != null) {
            beans = (ArrayList<KotlinBean.PushBean>) savedInstanceState.getSerializable("bean");
        }
        adapter = new StateAdapter()
                .addType(R.layout.doctor_answer_item, new BaseHolder<KotlinBean.PushBean>() {
                    @Override
                    public void onViewBind(Holder holder, KotlinBean.PushBean item, int position) {
                        if (userInfo.getType() == 0) {
                            holder.setText(R.id.question, "我:" + item.getContent());
                            holder.setTextColor(R.id.question, (null == item.getReply()) ? 0xff535353 : getResources().getColor(R.color.navi_checked));
                            if (null == item.getReply()) {
                                holder.setText(R.id.answer, "（未回复）");
                            } else {
                                holder.setText(R.id.answer, item.getReply());
                            }
                            holder.itemView.setOnClickListener(v -> goDoctorHome(item.getUserid()));
                        } else {
                            holder.setText(R.id.question, item.getFromName() + ":" + item.getContent());
                            holder.setTextColor(R.id.question, (null == item.getReply()) ? 0xff535353 : getResources().getColor(R.color.navi_checked));
                            if (null == item.getReply()) {
                                holder.setText(R.id.answer, "（待回复）");
                            } else {
                                holder.setText(R.id.answer, "@回复/" + item.getReply());
                            }


                            holder.itemView.setOnLongClickListener(v -> {
                                goSickerHome(item.getFromid());
                                return true;
                            });

                            holder.itemView.setOnClickListener(v -> {
                                if (null == item.getReply()) {
                                    showDialogs(item);
                                } else {
                                    goSickerHome(item.getFromid());
                                }
                            });
                        }
                    }

                    @Override
                    public boolean istype(Object item, int position) {
                        return true;
                    }
                });
        adapter.setStateListener(new DefaultStateListener() {
            @Override
            public void netError(Context context) {
                loadData();
            }
        });

        RecyclerView recyclerView=refreshLayout.getmScroll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
      refreshLayout.setListener(new RefreshListener() {
          @Override
          public void Refreshing() {
              loadData();
          }

          @Override
          public void Loading() {

          }
      });
        if (beans == null) {
            refreshLayout.setRefreshing();
        } else {
            adapter.setList(beans);
            adapter.showItem();
        }


    }

    private void goDoctorHome(String userid) {
        ApiController.getCurrentDoc(userid, App.getToken())
                .subscribe(new DataObserver<DoctorBean.ContentBean>(this) {
                    @Override
                    public void OnNEXT(DoctorBean.ContentBean bean) {
                        if (bean == null) {
                            return;
                        }
                        Intent intent = new Intent(CommentActivity.this, DoctorHome.class);
                        intent.putExtra("data", bean);
                        startActivity(intent);
                    }
                });
    }

    private void showDialogs(KotlinBean.PushBean bean) {
        ReplyDialog dialog = new ReplyDialog();
        dialog.setTitleStr("情输入回复内容");
        dialog.setCallback(new CallBack<String>() {
            @Override
            public void onCallBack(String obj) {
                ApiController.answerComment(bean.getId() + "", userInfo.getToken(), obj, bean.getUserid(), bean.getFromid())
                        .subscribe(new DataObserver<Object>(this) {
                            @Override
                            public void OnNEXT(Object objs) {
                                bean.setReply(obj);
                                dialog.dismiss();
                                adapter.notifyDataSetChanged();
                            }
                        });

            }
        });
        dialog.show(getSupportFragmentManager());
    }

    private void goSickerHome(String userid) {
        ApiController.getUserInfo(userid, userInfo.getToken(), 0)
                .compose(RxSchedulers.compose())
                .subscribe(new DataObserver<KotlinBean.UserInfoBean>(this) {
                    @Override
                    public void OnNEXT(KotlinBean.UserInfoBean bean) {
                        String patientNo = bean.getPatientNo();
                        Intent intent = new Intent(CommentActivity.this, SickerHome.class);
                        intent.putExtra("patientNo", patientNo);
                        startActivity(intent);
                    }
                });

    }


    private void loadData() {
        Session userInfo = App.getUserInfo();
        ApiController.getComment(userInfo.getUserid(), userInfo.getToken())
                .subscribe(new DataObserver<ArrayList<KotlinBean.PushBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.PushBean> bean) {
                        beans = bean;
                        if (beans.size() > 0) {
                            adapter.setList(beans);
                            adapter.showItem();
                        } else {
                            adapter.showEmpty();
                        }

                        refreshLayout.NotifyCompleteRefresh0();
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        adapter.ShowError();
                        refreshLayout.NotifyCompleteRefresh0();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (beans != null) {
            outState.putSerializable("bean", beans);
        }
    }
}
