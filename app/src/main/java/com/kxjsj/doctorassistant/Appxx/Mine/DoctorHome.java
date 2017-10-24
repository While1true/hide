package com.kxjsj.doctorassistant.Appxx.Mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Glide.GlideLoader;
import com.kxjsj.doctorassistant.JavaBean.DoctorBean;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
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
    private SAdapter sBaseMutilAdapter;

    /**
     * 数据包
     */
    private ArrayList<String> datas;
    DoctorBean.ContentBean bean;
    @Override
    protected int getContentLayoutId() {

        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if(savedInstanceState!=null){
            bean= savedInstanceState.getParcelable("data");
            setTitle(bean.getName()+"的个人主页");
            datas= (ArrayList<String>) savedInstanceState.getSerializable("bean");
        }
        if(intent!=null&&bean==null){
            bean=intent.getParcelableExtra("data");
        }

        ButterKnife.bind(this);
        sBaseMutilAdapter = new SAdapter(30)
                .addType(R.layout.doctor_info, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {

                        GlideLoader.loadRound(holder.getView(R.id.iv_doctor),bean.getUserimg());
                        holder.setText(R.id.introduction,bean.getRemark());

                        startButtonAnimator(holder);
                        holder.setOnClickListener(R.id.ask, v -> {
                            askQuestion(bean.getUserid());
                        });
                        holder.setOnClickListener(R.id.communicate, view -> ConversationUtils.startChartSingle(DoctorHome.this,bean.getDepartment()+"/"+bean.getName(),bean.getUserid()));
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
                            ConversationUtils.startChartSingle(DoctorHome.this,bean.getDepartment()+"/"+bean.getName(),bean.getUserid());
                        });
                        holder.setOnClickListener(R.id.communicate, view -> askQuestion(bean.getUserid()));
                    }

                    @Override
                    public boolean istype(int position) {
                        return position==0&&!OrentionUtils.isPortrait(DoctorHome.this);
                    }
                })
                .addType(R.layout.doctor_answer_item, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {

                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
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
                }).setAdapter(new LinearLayoutManager(this), sBaseMutilAdapter)
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

        new MaterialDialog.Builder(this)
                .title("留言提问")
                .content("留言可能不能及时收到回复，请谅解\n您可以试着先看看别人的提问，也许可以找到你想要的答案")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("提交")
                .negativeText("取消")
                .input("请输入您的问题", "", false, (dialog, input) -> {

                }).onPositive((dialog, which) -> {
            String string = dialog.getInputEditText().getText().toString();
            K2JUtils.toast(string, 1);
        }).build()
                .show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(sBaseMutilAdapter!=null) {
            /**
             * 为了不重启调整布局，把doctor_info后缀加了个Land
             */
            AdjustUtil.changeTypeValue(this);
            sBaseMutilAdapter.notifyDataSetChanged();
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
        }
    }

    /**
     * 请求网络获取数据
     */
    private void loadData() {




        sBaseMutilAdapter.showState(SAdapter.SHOW_NOMORE, "无更多内容了");
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
