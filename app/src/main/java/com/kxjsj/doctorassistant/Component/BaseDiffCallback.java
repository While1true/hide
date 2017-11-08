package com.kxjsj.doctorassistant.Component;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by vange on 2017/11/8.
 */

public abstract class BaseDiffCallback<T> extends DiffUtil.Callback{
    protected List<T>olds=null;
    protected List<T>news=null;
    public BaseDiffCallback(List<T>olds, List<T>news){
        this.olds=olds;
        this.news=news;
    }

    @Override
    public int getOldListSize() {
        return olds==null?0:olds.size();
    }

    @Override
    public int getNewListSize() {
        return news==null?0:news.size();
    }


}
