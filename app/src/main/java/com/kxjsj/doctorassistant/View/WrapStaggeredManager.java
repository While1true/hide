package com.kxjsj.doctorassistant.View;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自适应高度瀑布流 由于是局部加载
 * 不支持中间插入或删除 ，只支持一直添加数量
 * 流畅的很
 */
public class WrapStaggeredManager extends RecyclerView.LayoutManager {

    private int count;
    int[] offsets;
    int scrolls;
    int maxHeight;
    SparseArray<View> attchedViews=new SparseArray<>();

    Pool<Rect> layouts = new Pool<>(new Pool.Factory<Rect>() {
        @Override
        public Rect get() {
            return new Rect();
        }
    });
    private OrientationHelper helper;
    private OrientationHelper helper2;
    private int eachWidth;

    public int getCount() {
        return count;
    }

    public WrapStaggeredManager setCount(int count) {
        /**
         * 计算大概位置
         */
//        scrolls=scrolls*this.count/count;
        this.count = count;
        offsets = new int[count];

        layouts.getArray().clear();
        requestLayout();
        return this;
    }
    RecyclerView.Adapter newAdapter;
    RecyclerView.Recycler recycler;
    public int getScrolls() {
        return scrolls;
    }

    public WrapStaggeredManager setScrolls(int scrolls) {
        this.scrolls = scrolls;
        return this;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        this.newAdapter = newAdapter;
        System.out.println("onAdapterChanged");
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(eachWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.recycler=recycler;
        if (state.getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }

        if (helper == null) {
            helper = OrientationHelper.createHorizontalHelper(this);
            helper2 = OrientationHelper.createVerticalHelper(this);
        }

        detachAndScrapAttachedViews(recycler);


        /**
         * 预计算位置
         */
        init(recycler, state);

        layout(recycler, state, 0);

    }

    private void init(final RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(offsets==null||layouts.size()==1){
            offsets=new int[count];
            layouts.getArray().clear();
        }
        attchedViews.clear();
        eachWidth = helper.getTotalSpace() / count;

        View scrap = recycler.getViewForPosition(0);
        addView(scrap);
        measureChildWithMargins(scrap, eachWidth*(count-1), 0);
        removeAndRecycleView(scrap, recycler);
    }

    private void caculate(final RecyclerView.Recycler recycler, int dy) {
        long start = System.currentTimeMillis();
        A:
        for (int i = layouts.size(); i < getItemCount(); i++) {
            /**
             * 之测量不同type的大小 计算位置
             */
            View scrap = recycler.getViewForPosition(i);
            addView(scrap);
            measureChildWithMargins(scrap, eachWidth * (count - 1), 0);
            int decoratedMeasuredHeight = getDecoratedMeasuredHeight(scrap);
            removeAndRecycleView(scrap, recycler);
            int rowNumber = getMinIndex();
            Rect rect = layouts.get(i);
            rect.set(rowNumber * eachWidth, offsets[rowNumber], (rowNumber + 1) * eachWidth, offsets[rowNumber] + decoratedMeasuredHeight);
            offsets[rowNumber] = offsets[rowNumber] + rect.height();
            /**
             * 只多加载一屏幕的
             */
            if (offsets[getMinIndex()] > dy + scrolls + getPaddingTop() + helper2.getTotalSpace()) {
                break A;
            }

        }
        maxHeight = getMaxHeight();
    }

    private int  caculate2Position(final RecyclerView.Recycler recycler, final int position) {
        long start = System.currentTimeMillis();
        A:
        for (int i = layouts.size(); i < position&&position<getItemCount(); i++) {
            /**
             * 之测量不同type的大小 计算位置
             */
            View scrap = recycler.getViewForPosition(i);
            addView(scrap);
            measureChildWithMargins(scrap, eachWidth*(count-1), 0);
            int decoratedMeasuredHeight = getDecoratedMeasuredHeight(scrap);
            detachAndScrapView(scrap, recycler);
            int rowNumber = getMinIndex();
            Rect rect = layouts.get(i);
            rect.set(rowNumber * eachWidth, offsets[rowNumber], (rowNumber + 1) * eachWidth, offsets[rowNumber] + decoratedMeasuredHeight);
            offsets[rowNumber] = offsets[rowNumber] + rect.height();
            if (System.currentTimeMillis()-start>600) {
                break A;
            }
        }
        maxHeight = getMaxHeight();
        return layouts.size()-1;
    }
    /**
     * 获取最小的指针位置
     *
     * @return
     */
    private int getMinIndex() {
        int min = 0;
        int minnum = offsets[0];
        for (int i = 1; i < offsets.length; i++) {
            if (minnum > offsets[i]) {
                minnum = offsets[i];
                min = i;
            }
        }
        return min;
    }

    /**
     * 获取最大的高度
     *
     * @return
     */
    private int getMaxHeight() {
        int max = offsets[0];
        for (int i = 1; i < offsets.length; i++) {
            if (offsets[i] > max) {
                max = offsets[i];
            }
        }
        return max;
    }

    public Rect getRect(RecyclerView.Recycler recycler, int position) {
        Rect rectx = layouts.get(position);
        return rectx;
    }


    /**
     * dy 1 上滑 -1 下滑 0出初始
     *
     * @param recycler
     * @param state
     */
    private void layout(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        System.out.println("zzzzzz"+scrolls);
        Rect layoutRange = new Rect(getPaddingLeft(), getPaddingTop() + scrolls, helper.getTotalSpace() + getPaddingLeft(), helper2.getTotalSpace() + getPaddingTop() + scrolls);
        int itemCount = state.getItemCount();
        if (dy >= 0) {
            dolayoutAndRecycler(recycler, layoutRange, itemCount);
        } else {
            dolayoutAndRecyclerDown(recycler, layoutRange, itemCount);
        }


    }

    private void recyclerViews(RecyclerView.Recycler recycler, Rect layoutrect) {
//        detachAndScrapAttachedViews(recycler);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int position = getPosition(childAt);
            Rect rect = layouts.get(position);
            if(!Rect.intersects(rect,layoutrect)){
                attchedViews.remove(position);
                removeAndRecycleView(childAt,recycler);
                childCount--;
            }
        }
    }


    private void dolayoutAndRecyclerDown(RecyclerView.Recycler recycler, Rect layoutRange, int itemCount) {
        int childCount = getChildCount();
        int max = getMax(childCount);
        recyclerViews(recycler,layoutRange);
        int xx = 0;
        for (int i = max; i >= 0; i--) {
            Rect layout = getRect(recycler, i);
            if (Rect.intersects(layout, layoutRange)&&attchedViews.get(i)==null) {
                View viewForPosition = recycler.getViewForPosition(i);
                addView(viewForPosition);
                attchedViews.put(i,viewForPosition);
                measureChildWithMargins(viewForPosition, eachWidth*(count-1), 0);
                layoutDecoratedWithMargins(viewForPosition, layout.left, layout.top - scrolls, layout.right, layout.bottom - scrolls);
            }
            if (layout.bottom <= layoutRange.top) {
                xx++;
                if (xx >= count) {
                    break;
                }
            }
        }


    }

    private int getMax(int childCount) {
        int max = 0;
        if (childCount != 0) {
            max = getPosition(getChildAt(0));
            for (int i = 1; i < childCount; i++) {
                int position = getPosition(getChildAt(i));
                if (position > max) {
                    max = position;
                }
            }
        }
        return max;
    }

    /**
     * 出初始layout
     *
     * @param recycler
     * @param layoutRange
     * @param itemCount
     */
    private void dolayoutAndRecycler(RecyclerView.Recycler recycler, Rect layoutRange, int itemCount) {
        int childCount = getChildCount();
        int min = getMin(childCount);
        recyclerViews(recycler,layoutRange);
        int xx = 0;
        A:
        for (int i = min; i < itemCount; i++) {

            if (getRect(recycler, i).isEmpty()) {
                layouts.getArray().remove(i);
                caculate(recycler, 0);
            }

            final Rect layout = getRect(recycler, i);
            if (Rect.intersects(layout, layoutRange)&&attchedViews.get(i)==null) {
                View viewForPosition = recycler.getViewForPosition(i);
                addView(viewForPosition);
                attchedViews.put(i,viewForPosition);
                measureChildWithMargins(viewForPosition, eachWidth*(count-1), 0);
                layoutDecoratedWithMargins(viewForPosition, layout.left, layout.top - scrolls, layout.right, layout.bottom - scrolls);
            }
            if (layout.top >= layoutRange.bottom) {
                xx++;
                if (xx >= count) {
                    break;
                }
            }
        }
    }


    private int getMin(int childCount) {
        int min = 0;
        if (childCount != 0) {
            min = getPosition(getChildAt(0));
            for (int i = 1; i < childCount; i++) {
                int position = getPosition(getChildAt(i));
                if (position < min) {
                    min = position;
                }
            }
        }
        return min;
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean canScrollHorizontally() {
        //返回true表示可以横向滑动
        return false;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        System.out.println(getChildCount() + "------------");
        if (layouts.size() < getItemCount() && maxHeight <= dy + scrolls + getPaddingTop() + helper2.getTotalSpace()) {
            caculate(recycler, dy);
        }

        if (maxHeight < helper2.getTotalSpace()) {
            return 0;
        }
        if (scrolls + dy > maxHeight - helper2.getTotalSpace()) {
            dy = maxHeight - helper2.getTotalSpace() - scrolls;
        }

        if (scrolls + dy < 0) {
            dy = -scrolls;
        }
        offsetChildrenVertical(-dy);
        scrolls += dy;
        if (dy > 0) {
            layout(recycler, state, 1);
        } else if (dy < 0) {
            layout(recycler, state, -1);
        }
        return dy;
    }

    @Override
    public void scrollToPosition(int position) {
        int temp = position;
        if (position > getItemCount()) {
            temp = getItemCount();
        } else if (position < 0) {
            temp = 0;
        }
        if(layouts.size()<position+1){
            temp=caculate2Position(recycler,position);
        }
        int top = layouts.get(temp).top;
        if (top > maxHeight - helper2.getTotalSpace()) {
            top = maxHeight - helper2.getTotalSpace();
        }
        scrolls = top;
        requestLayout();
    }

    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, RecyclerView.State state, int position) {//平滑的移动到某一项
        if(layouts.size()<position+1){
            position = caculate2Position(recycler, position);
        }
            run2Position(recyclerView, position);
    }
    private void run2Position(RecyclerView recyclerView, int position){
        int top = layouts.get(position).top;
        int needscroll = top - scrolls;
        recyclerView.smoothScrollBy(0, needscroll);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        Mystate mystate = new Mystate(parcelable).setPosition(scrolls,count);
        return mystate;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        Mystate state1 = (Mystate) state;
        super.onRestoreInstanceState(state1.getSuperState());
        scrolls=state1.getPosition();
        count=state1.getCount();

    }

    private static class Mystate implements Parcelable {
        int position;
        int count;
        Parcelable superState;
        public Mystate setPosition(int position,int countz){
            this.position=position;
            this.count=countz;
            return this;
        }

        public int getCount() {
            return count;
        }

        public Parcelable getSuperState() {
            return superState;
        }

        public void setSuperState(Parcelable superState) {
            this.superState = superState;
        }

        public int getPosition() {
            return position;
        }

        public Mystate(Parcelable superState) {
            this.superState=superState;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.position);
            dest.writeInt(this.count);
            dest.writeParcelable(this.superState, flags);
        }

        protected Mystate(Parcel in) {
            this.position = in.readInt();
            this.count = in.readInt();
            this.superState = in.readParcelable(Parcelable.class.getClassLoader());
        }

        public static final Creator<Mystate> CREATOR = new Creator<Mystate>() {
            @Override
            public Mystate createFromParcel(Parcel source) {
                return new Mystate(source);
            }

            @Override
            public Mystate[] newArray(int size) {
                return new Mystate[size];
            }
        };
    }

    private static class Pool<T> {
        SparseArray<T> array;
        Factory<T> tnew;

        public Pool(Factory<T> tnew) {
            array = new SparseArray<>();
            this.tnew = tnew;
        }

        public int size() {
            return array.size();
        }

        public SparseArray<T> getArray() {
            return array;
        }

        public T get(int key) {
            T t = array.get(key);
            if (t == null) {
                t = tnew.get();
                array.put(key, t);
            }
            return t;
        }

        public interface Factory<T> {
            T get();
        }
    }


}
