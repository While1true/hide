package com.kxjsj.doctorassistant.Appxx.Mine.Comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Doctor.Home.DoctorHome;
import com.kxjsj.doctorassistant.Appxx.Sicker.Home.SickerHome;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.Holder.CallBack;
import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/11/8.
 */

public class CommentActivity extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    private SAdapter adapter;
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
        if (savedInstanceState != null) {
            beans = (ArrayList<KotlinBean.PushBean>) savedInstanceState.getSerializable("bean");
        }
        adapter = new SAdapter()
                .addType(R.layout.doctor_answer_item, new ItemHolder<KotlinBean.PushBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.PushBean item, int position) {
                        if(userInfo.getType()==0) {
                            holder.setText(R.id.question, "我:" + item.getContent());
                            holder.setTextColor(R.id.question,(null==item.getReply())?0xff535353:getResources().getColor(R.color.navi_checked));
                            if(null==item.getReply()){
                                holder.setText(R.id.answer,"（未回复）");
                            }else{
                                holder.setText(R.id.answer,item.getReply());
                            }
                            holder.itemView.setOnClickListener(v -> goDoctorHome(item.getUserid()));
                        }else{
                            holder.setText(R.id.question, item.getFromName()+":" + item.getContent());
                            holder.setTextColor(R.id.question,(null==item.getReply())?0xff535353:getResources().getColor(R.color.navi_checked));
                            if(null==item.getReply()){
                                holder.setText(R.id.answer,"（待回复）");
                            }else{
                                holder.setText(R.id.answer,"@回复/"+item.getReply());
                            }


                            holder.itemView.setOnLongClickListener(v -> {
                                goSickerHome(item.getFromid());
                                return true;
                            });

                            holder.itemView.setOnClickListener(v -> {
                                if(null==item.getReply()){
                                    showDialogs(item);
                                }else{
                                    goSickerHome(item.getFromid());
                                }
                            });
                        }
                    }

                    @Override
                    public boolean istype(KotlinBean.PushBean item, int position) {
                        return true;
                    }
                })
                .setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });

        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(new LinearLayoutManager(this), adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        loadData();
                    }
                });
        if (beans == null) {
            srecyclerview.setRefreshing();
        } else {
            adapter.setBeanList(beans);
            adapter.showItem();
        }


    }

    private void goDoctorHome(String userid) {
        ApiController.getCurrentDoc(userid,App.getToken())
                .subscribe(new DataObserver<DoctorBean>(this) {
                    @Override
                    public void OnNEXT(DoctorBean bean) {
                        if(bean==null){
                            return;
                        }
                        DoctorBean.ContentBean content = bean.getContent();
                        if (Constance.DEBUGTAG)
                            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "OnNEXT: "+content);
                        Intent intent=new Intent(CommentActivity.this,DoctorHome.class);
                        intent.putExtra("data",bean.getContent());
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
                    ApiController.answerComment(bean.getId()+"",userInfo.getToken(),obj,bean.getUserid(),bean.getFromid())
                            .subscribe(new DataObserver(this) {
                                @Override
                                public void OnNEXT(Object objs) {
                                    bean.setReply(obj);
                                    adapter.notifyDataSetChanged();
                                }
                            });

                }
            });
        dialog.show(getSupportFragmentManager());
    }

    private void goSickerHome(String userid) {
        ApiController.getUserInfo(userid,userInfo.getToken(),0)
                .compose(RxSchedulers.compose())
                .subscribe(new DataObserver<KotlinBean.UserInfoBean>(this) {
                    @Override
                    public void OnNEXT(KotlinBean.UserInfoBean bean) {
                        String patientNo = bean.getPatientNo();
                        Intent intent=new Intent(CommentActivity.this, SickerHome.class);
                        intent.putExtra("patientNo",patientNo);
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
                            adapter.setBeanList(beans);
                            adapter.showItem();
                        } else {
                            adapter.showEmpty();
                        }

                        srecyclerview.notifyRefreshComplete();
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        adapter.ShowError();
                        srecyclerview.notifyRefreshComplete();
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
