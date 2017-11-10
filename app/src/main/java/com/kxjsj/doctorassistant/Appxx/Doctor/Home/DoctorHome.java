package com.kxjsj.doctorassistant.Appxx.Doctor.Home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.ReplyDialog;
import com.kxjsj.doctorassistant.Glide.GlideLoader;
import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Screen.AdjustUtil;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.GradualButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/9/29.
 */

public class DoctorHome extends BaseTitleActivity {
    /**
     * 显示内容控件
     */
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    private SAdapter adapter;

    /**
     * 数据包
     */
    ArrayList<KotlinBean.PushBean> datas;
    DoctorBean.ContentBean bean;
    private ReplyDialog replyDialog;

    @Override
    protected int getContentLayoutId() {

        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(savedInstanceState!=null){
            bean= savedInstanceState.getParcelable("data");
            datas= (ArrayList<KotlinBean.PushBean>) savedInstanceState.getSerializable("bean");
        }
        if(intent!=null&&bean==null){
            bean=intent.getParcelableExtra("data");
        }
        if(bean!=null){
            setTitle(bean.getName()+"的个人主页");
        }

        adapter = new SAdapter()
                .addType(R.layout.doctor_info, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {

                        GlideLoader.loadRound(holder.getView(R.id.iv_doctor),bean.getUserimg());
                        holder.setText(R.id.introduction,bean.getRemark());

                        startButtonAnimator(holder);
                        holder.setOnClickListener(R.id.ask, v -> {
                            askQuestion(bean.getUserid());
                        });
                        holder.setOnClickListener(R.id.communicate, view -> ConversationUtils.startChartSingle(DoctorHome.this,bean.getUserid(),bean.getDepartment()+"/"+bean.getName()));
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==0&&OrentionUtils.isPortrait(DoctorHome.this);
                    }
                })
                .addType(R.layout.doctor_info_land, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        GlideLoader.loadRound(holder.getView(R.id.iv_doctor),bean.getUserimg());
                        holder.setText(R.id.introduction,bean.getRemark());

                        startButtonAnimator(holder);
                        holder.setOnClickListener(R.id.ask, v -> {
                            askQuestion(bean.getUserid());
                        });
                        holder.setOnClickListener(R.id.communicate, view ->   {
                            ConversationUtils.startChartSingle(DoctorHome.this,bean.getUserid(),bean.getDepartment()+"/"+bean.getName());
                        });
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==0&&!OrentionUtils.isPortrait(DoctorHome.this);
                    }
                })
                .addType(R.layout.doctor_answer_item, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        if(datas.size()==0){
                            holder.setText(R.id.question,"当前无留言") ;
                            holder.setText(R.id.answer,"来做第一个留言的人吧") ;
                            return;
                        }
                        KotlinBean.PushBean pushBean = datas.get(position - 1);
                        holder.setText(R.id.question, pushBean.getFromName()+":" + pushBean.getContent());
                        holder.setText(R.id.answer,bean.getName()+":"+pushBean.getReply());
                    }

                    @Override
                    public boolean istype(int position) {
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
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                            loadData();

                    }
                }).setAdapter(new LinearLayoutManager(this), adapter)
                .setRefreshing();
    }

    private void startButtonAnimator(SimpleViewHolder holder) {
        GradualButton askButton = holder.getView(R.id.ask);
        askButton.start(0xff535353, getResources().getColor(R.color.colorPrimary));

        GradualButton communicateButton = holder.getView(R.id.communicate);
        communicateButton.start(0xff535353, getResources().getColor(R.color.navi_checked));
    }

    /**
     * 提交留言问题
     *
     * @param userid
     */
    private void askQuestion(String userid) {

        if(replyDialog==null) {
            replyDialog = new ReplyDialog();
            replyDialog.setTitleStr("写下留言的问题");
            replyDialog.setCallback(obj -> {
                Session userInfo = App.getUserInfo();
                ApiController.Comment(userid,userInfo.getUserid(),userInfo.getToken(),obj)
                        .subscribe(new DataObserver(this) {
                            @Override
                            public void OnNEXT(Object bean) {
                                replyDialog.dismiss();
                                K2JUtils.toast("留言成功！");
                            }
                        });
            });
        }
        replyDialog.show(getSupportFragmentManager());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(adapter!=null) {
            /**
             * 为了不重启调整布局，把doctor_info后缀加了个Land
             */
            AdjustUtil.changeTypeValue(this);
            adapter.notifyDataSetChanged();
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
        }
    }

    /**
     * 请求网络获取数据
     */
    private void loadData() {
        Session userInfo = App.getUserInfo();
        ApiController.getReplyComment(userInfo.getUserid(),userInfo.getToken())
                .subscribe(new DataObserver<ArrayList<KotlinBean.PushBean>>(this) {
                    @Override
                    public void OnNEXT(ArrayList<KotlinBean.PushBean> bean) {
                       datas=bean;
                        int size = datas.size();
                        adapter.setCount(size ==0?size+2:size+1);
                       adapter.showItem();
                    }

                    @Override
                    public void OnERROR(String error) {
                        super.OnERROR(error);
                        adapter.ShowError();
                    }
                });
        srecyclerview.notifyRefreshComplete();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onDestroy: ");
        srecyclerview.setRefreshingListener(null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onCreate: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onRestoreInstanceState: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data",bean);
        outState.putSerializable("bean",datas);
    }

}
