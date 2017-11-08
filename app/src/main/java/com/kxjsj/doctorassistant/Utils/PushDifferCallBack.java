package com.kxjsj.doctorassistant.Utils;

import com.kxjsj.doctorassistant.Component.BaseDiffCallback;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;

import java.util.List;

/**
 * Created by vange on 2017/11/8.
 */

public class PushDifferCallBack extends BaseDiffCallback<KotlinBean.PushBean> {
    public PushDifferCallBack(List<KotlinBean.PushBean> olds, List<KotlinBean.PushBean> news) {
        super(olds, news);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if(olds==null)
            return false;
        KotlinBean.PushBean pushBean = olds.get(oldItemPosition);
        KotlinBean.PushBean pushBean1 = news.get(newItemPosition);

        return pushBean.getId()==pushBean1.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(olds==null)
            return false;
        KotlinBean.PushBean pushBean = olds.get(oldItemPosition);
        KotlinBean.PushBean pushBean1 = news.get(newItemPosition);
        return pushBean.getId()==pushBean1.getId();
    }
}
