package com.kxjsj.doctorassistant.Appxx.Doctor;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.ZXingUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/19.
 */

/**
 * setsetRetainInstance(true)
 * ondestory不会调用
 * initView中view不用重新初始化
 * 由于横竖屏个数要改一下，就通知更新
 * Srecyclerview中onDetachFromWindow销毁了一部分View 所以要重新初始化头布局
 * <p>
 * 控件不懂使用的参考我的简书页
 * **************  http://www.jianshu.com/u/07d24a532308  *******************
 */
public class SickbedF extends BaseFragment {
    int spancount;
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    Unbinder unbinder;
    private SBaseMutilAdapter baseMutilAdapter;
    private GridLayoutManager manager;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);

        caculateSpanCount();

        List<KotlinBean.SickBedBean> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                KotlinBean.SickBedBean bean1 = new KotlinBean.SickBedBean(0, "第" + (i / 10) + "号楼", "第" + i % 10 + "床");
                list.add(bean1);
            }
            KotlinBean.SickBedBean bean = new KotlinBean.SickBedBean(1, "第" + (i / 10) + "号楼", "第" + i % 10 + "床");
            list.add(bean);
        }


        manager = new GridLayoutManager(getContext(), spancount);
        baseMutilAdapter = new SBaseMutilAdapter(list)
                .addType(R.layout.title_layout, new SBaseMutilAdapter.ITEMHOLDER<KotlinBean.SickBedBean>() {

                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.SickBedBean item, int position) {
                        holder.setText(R.id.title, item.getTitle());
                    }

                    @Override
                    public boolean istype(KotlinBean.SickBedBean item, int position) {
                        return item.getType() == 0;
                    }

                    @Override
                    protected int gridSpanSize(KotlinBean.SickBedBean item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.sickbed_item_bed, new SBaseMutilAdapter.ITEMHOLDER<KotlinBean.SickBedBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.SickBedBean item, int position) {
                        ImageView iv=holder.getView(R.id.iv);
                        TextView tv=holder.getView(R.id.tv);
                        if(position%3==2){
                            iv.setImageResource(R.drawable.ic_avaliable);
                            tv.setText(item.getName());
                        }else{
                            iv.setImageResource(R.drawable.ic_lived);
                            tv.setText("病号：xm12596\n姓名：zx12"+position);
                        }

                        holder.itemView.setOnClickListener(view -> {
                            ZXingUtils.startCapture(view.getContext(), new MyObserver<BaseBean<String>>(SickbedF.this) {
                                @Override
                                public void onNext(BaseBean<String> o) {
                                    super.onNext(o);
                                    new MaterialDialog.Builder(getContext())
                                            .title(o.getData())
                                            .positiveText("取消")
                                            .build()
                                            .show();
                                    onComplete();
                                }

                            });
                        });

                    }

                    @Override
                    public boolean istype(KotlinBean.SickBedBean item, int position) {
                        return item.getType() == 1;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        K2JUtils.toast("cuole", 1);
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, baseMutilAdapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        srecyclerview.postDelayed(() -> {
                            baseMutilAdapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "无更多内容了");
                            srecyclerview.notifyRefreshComplete();
                        }, 1000);

                    }
                });
        if(firstLoad) {
            firstLoad=false;
            srecyclerview.setRefreshing();
        }else{
            baseMutilAdapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "无更多内容了");
        }

    }

    private void caculateSpanCount() {
        if (OrentionUtils.isPortrait(getContext()))
            spancount = 3;
        else
            spancount = 4;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void loadLazy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onSaveInstanceState: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
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
}
