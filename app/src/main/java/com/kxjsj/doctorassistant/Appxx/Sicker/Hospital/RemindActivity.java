package com.kxjsj.doctorassistant.Appxx.Sicker.Hospital;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/10/31.
 */

public class RemindActivity extends BaseTitleActivity {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    private SAdapter sAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setTitle("提醒信息");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        sAdapter = new SAdapter(50)
                .addType(R.layout.doctor_answer_item, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {
                        holder.setText(R.id.question, "来自xxx的提醒--10/12 8:41");
                        holder.setTextColor(R.id.question, getResources().getColor(position < 6 ? R.color.colorecRed : R.color.navi_checked));
                        holder.setText(R.id.answer, position < 6 ? "待处理" : "看看再说");
                    }

                    @Override
                    public boolean istype(int position) {
                        return true;
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
        if(savedInstanceState==null)
            srecyclerview.setRefreshing();

    }

    private void loadData() {
        srecyclerview.postDelayed(() ->{
            srecyclerview.notifyRefreshComplete();
            sAdapter.showNomore();
            } ,500);
    }
}
