package com.kxjsj.doctorassistant.Holder;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;
import com.kxjsj.doctorassistant.Utils.ActivityUtils;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.AdapterScrollListener;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler;
import com.nestrefreshlib.State.DefaultStateListener;
import com.nestrefreshlib.State.Interface.StateEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by 不听话的好孩子 on 2018/3/22.
 */

public abstract class RefreshLayoutPageLoading<T> extends DataObserver<List<T>> implements AdapterScrollListener.Callback {
    private int pagenum = 1;
    private int pagesize = 20;
    private boolean nomore = false;
    private boolean loading = false;
    private List<T> list = new ArrayList<>();
    private RefreshLayout refreshLayout;
    private StateAdapter stateAdapter = new StateAdapter(list);
    private RefreshAdapterHandler handler;

    public RefreshLayoutPageLoading(RefreshLayout refreshLayout) {
        this(refreshLayout, new LinearLayoutManager(refreshLayout.getContext()), false);
    }

    public RefreshLayoutPageLoading(RefreshLayout refreshLayout, boolean isinner) {
        this(refreshLayout, new LinearLayoutManager(refreshLayout.getContext()), isinner);
    }


    public RefreshLayoutPageLoading(RefreshLayout refreshLayout, RecyclerView.LayoutManager layoutManager, boolean isinner, RecyclerView.ItemDecoration... itemDecorations) {
        super(ActivityUtils.getInstance().getTopActivity());
        this.refreshLayout = refreshLayout;
        RecyclerView scroll = refreshLayout.getmScroll();
        if (!(scroll instanceof RecyclerView)) {
            throw new UnsupportedOperationException("子view必須是Recyclerview");
        }
        stateAdapter.setStateListener(new DefaultStateListener() {
            @Override
            public void netError(Context context) {
                stateAdapter.showLoading();
                pagenum = 1;
                Go();
            }
        });


        if (itemDecorations != null) {
            for (RecyclerView.ItemDecoration itemDecoration : itemDecorations) {
                scroll.addItemDecoration(itemDecoration);
            }
        }
        stateAdapter.showLoading();
        if (!isinner) {
            scroll.setLayoutManager(layoutManager);
            scroll.addOnScrollListener(new AdapterScrollListener(this));
            scroll.setAdapter(stateAdapter);
        } else {
            handler = new RefreshAdapterHandler();
            handler.attachRefreshLayout(refreshLayout, stateAdapter, layoutManager);
            handler.stopLoading("");
        }
        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                pagenum = 1;
                nomore = false;
                Go();
            }

            @Override
            public void Loading() {
                RefreshLayoutPageLoading.this.call();
            }
        });
    }

    @Override
    public void call() {
        if (!nomore && !loading) {
            if (handler != null) {
                handler.startLoading("正在加载中...");
            }
            pagenum++;
            Go();
        }
    }

    public RefreshLayoutPageLoading<T> addType(ItemHolder<?> holder) {
        stateAdapter.addType(holder);
        return this;
    }

    public RefreshLayoutPageLoading<T> AddLifeOwner(LifecycleOwner owner) {
        stateAdapter.addLifeOwener(owner);
        return this;
    }

    public RefreshLayoutPageLoading<T> Go() {
        loading = true;
        getObservable().subscribe(this);
        return this;
    }

    public RefreshLayoutPageLoading<T> RestAndGo() {
        nomore = false;
        loading = false;
        pagenum = 1;
        Go();
        return this;
    }

    @Override
    public void OnNEXT(List<T> bean) {
        if (pagenum == 1) {
            list.clear();
        }
        list.addAll(bean);
        if (pagenum == 1 && bean.isEmpty()) {
            nomore = true;
            if (handler != null) {
                handler.stopLoading("不听话的好孩子 技术实现");
            }
            stateAdapter.showEmpty();
        } else {
            if (bean.size() < pagesize) {
                nomore = true;
                if (handler != null) {
                    handler.stopLoading((pagenum == 1) ? "不听话的好孩子 技术实现" : "不听话的好孩子 技术实现");
                    stateAdapter.showItem();
                } else {
                    stateAdapter.showState(StateEnum.SHOW_NOMORE, (pagenum == 1) ? "不听话的好孩子 技术实现" : "不听话的好孩子 技术实现");
                }
            } else {
                if (handler != null) {
                    handler.stopLoading((pagenum == 1) ? "" : "正在加载中...");
                    stateAdapter.showItem();
                } else {
                    stateAdapter.showState(StateEnum.SHOW_NOMORE, "正在加载中...");
                }
            }
        }
        refreshLayout.NotifyCompleteRefresh0();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        loading = false;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("pagenum", pagenum);
        outState.putInt("pagenum", pagenum);
        outState.putInt("pagesize", pagesize);
        try {
            if(list instanceof Serializable) {
                outState.putSerializable("list", (Serializable) list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        outState.putBoolean("nomore", nomore);
    }
    public void onGetInstanceState(Bundle savedInstanceState) {
        savedInstanceState.getInt("pagenum", pagenum);
        savedInstanceState.getInt("pagesize", pagesize);
        savedInstanceState.getBoolean("nomore", nomore);
        Serializable list = savedInstanceState.getSerializable("list");
        if(list!=null){
            this.list= (List<T>) list;
        }
    }

    @Override
    public void OnERROR(String error) {
        super.OnERROR(error);
        if (pagenum == 1) {
            stateAdapter.ShowError();
        }
        if (pagenum > 1) {
            handler.stopLoading("这是底线了");
            pagenum--;
        }
        refreshLayout.NotifyCompleteRefresh0();
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public StateAdapter getStateAdapter() {
        return stateAdapter;
    }

    public abstract Observable<KotlinBean.BaseBean<List<T>>> getObservable();
}
