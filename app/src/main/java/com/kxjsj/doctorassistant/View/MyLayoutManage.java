package com.kxjsj.doctorassistant.View;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Field;


/**
 * Created by ck on 2017/10/15.
 */

public class MyLayoutManage extends LinearLayoutManager {
    /**
     * 固定浮动View
     */
    private View viewForPosition;

    /**
     * 滚动浮动View
     */
    private View viewForPosition1;

    /**
     * 真实item数量
     */
    private int itemCount;

    /**
     * 反射改变数量让最后一项不显示，
     * 拿来摆放到固定位置
     */
    private Field mItemCount;

    /**
     * 滚动浮动的位置
     */
    private int itemPosition=-1;

    /**
     * 是否可以浮动最后一项
     * @param context
     */
    private boolean canFlow;

    public MyLayoutManage(Context context) {
        super(context);
    }

    public MyLayoutManage(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLayoutManage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void setStateCount(RecyclerView.State state, int count) throws NoSuchFieldException, IllegalAccessException {

        if(!canFlow)
            return;
        if (mItemCount == null) {
            mItemCount = state.getClass().getDeclaredField("mItemCount");
            if (itemCount == 0)
                itemCount = state.getItemCount();
            mItemCount.setAccessible(true);
        }
        mItemCount.setInt(state, count);
    }

    public void setFloatItem(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setGunEnable(boolean canFlow){
        this.canFlow=canFlow;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            setStateCount(state, itemCount - 1);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onLayoutChildren(recycler, state);

        try {
            setStateCount(state, itemCount);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (viewForPosition != null)
            removeView(viewForPosition);
        if(canFlow) {
            viewForPosition = recycler.getViewForPosition(state.getItemCount() - 1);
            addView(viewForPosition);
            viewForPosition.layout(100, 100, 300, 200);
        }

        if (viewForPosition1 != null)
            removeView(viewForPosition1);
        int firstCompletelyVisibleItemPosition = findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisibleItemPosition > itemPosition&&itemPosition!=-1) {
            viewForPosition1 = recycler.getViewForPosition(itemPosition);
            addView(viewForPosition1);
            measureChild(viewForPosition1, 0, 0);
            viewForPosition1.layout(0, 0, viewForPosition1.getMeasuredWidth(), viewForPosition1.getMeasuredHeight());
        }

    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            setStateCount(state, itemCount - 1);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        if (viewForPosition1 != null)
            removeView(viewForPosition1);
        if (viewForPosition != null)
            removeView(viewForPosition);


        int i = super.scrollVerticallyBy(dy, recycler, state);


        try {
            setStateCount(state, itemCount);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        /**
         * 静态浮动 item在最后
         */
        if(canFlow) {
            viewForPosition = recycler.getViewForPosition(itemCount - 1);
            addView(viewForPosition);
            viewForPosition.layout(100, 100, 300, 200);
        }


        /**
         * 浮动
         */
        int firstCompletelyVisibleItemPosition = findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisibleItemPosition > itemPosition&&itemPosition!=-1) {
            viewForPosition1 = recycler.getViewForPosition(itemPosition);
            addView(viewForPosition1);
            measureChild(viewForPosition1, 0, 0);
            viewForPosition1.layout(0, 0, viewForPosition1.getMeasuredWidth(), viewForPosition1.getMeasuredHeight());
        }

        return i;
    }

}
