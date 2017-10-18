package com.kxjsj.doctorassistant.Appxx.Mine;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Holder.MyHolder;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.AdjustUtil;
import com.kxjsj.doctorassistant.View.GradualButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/11.
 */

public class SickerHome extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;

    private String[] infos = {"检查报告", "用药信息"};
    private int[] dres = {R.drawable.ic_checkreport, R.drawable.ic_medicine};
    private SBaseMutilAdapter adapter;
    private boolean isfirst = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("SickerHome");
        ButterKnife.bind(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        ArrayList list = new ArrayList(18);
        for (int i = 0; i < 18; i++) {
            list.add(i + "");
        }
        adapter = new SBaseMutilAdapter(list)
                .addType(R.layout.sickerinfo, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {

                        holder.setButtonText(R.id.callhelp, "事项提醒");
                        holder.setButtonText(R.id.help, "交流沟通");
                        startButtonAnimator(holder);
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position == 0;
                    }
                })
                .addType(R.layout.label_layout, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        Drawable drawable = getResources().getDrawable(dres[position - 1]);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        TextView tv = holder.getView(R.id.bt);
                        tv.setCompoundDrawables(null, drawable, null, null);
                        holder.setText(R.id.bt, infos[position - 1]);
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position < infos.length + 1;
                    }
                })
                .addType(R.layout.title_layout, new MyHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.title,"提醒记录");
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position==infos.length+1;
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
                            adapter.showNomore();
                            srecyclerview.notifyRefreshComplete();
                        }, 500);
                    }
                });
        if(savedInstanceState!=null)
            isfirst=savedInstanceState.getBoolean("isfirst",true);
        if (isfirst) {
            isfirst = false;
            srecyclerview.setRefreshing();
        } else {
            adapter.showNomore();
        }

    }

    private void startButtonAnimator(SimpleViewHolder holder) {
        GradualButton askButton = holder.getView(R.id.callhelp);
        askButton.start(askButton.getCurrentTextColor(), getResources().getColor(R.color.colorPrimary));

        GradualButton communicateButton = holder.getView(R.id.help);
        communicateButton.start(communicateButton.getCurrentTextColor(), getResources().getColor(R.color.navi_checked));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isfirst",isfirst);
    }
}
