package com.ck.hello.nestrefreshlib.View.Adpater.Interface;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;

/**
 * Created by ck on 2017/9/9.
 */

public interface StateHandlerInterface<T> {


    /**
     * 用于传递相应的监听 在Adapter设置监听
     * @param listener 继承BaseStateListener 定义自己想要的监听方法
     * @return
     */
    StateHandlerInterface setStateClickListener(BaseStateListener listener);
    BaseStateListener getStateClickListener();

    /**
     * BindView时调用
     * @param holder
     * @param t 由Adapter showState(int showstate, E e);传递过来的数据
     *          默认定义为String，如果你有不同需求，可改为任意类型
     */
    void BindEmptyHolder(SimpleViewHolder holder, T t);
    void BindErrorHolder(SimpleViewHolder holder,T t);
    void BindLoadingHolder(SimpleViewHolder holder,T t);
    void BindNomoreHolder(SimpleViewHolder holder,T t);

    /**
     * detachFromWindow时调用 销毁一些持有的引用
     */
    void destory();

    /**
     * 切换状态时调用，可以用来关闭一些之前状态的动画
     */
    void switchState(int state);
}
