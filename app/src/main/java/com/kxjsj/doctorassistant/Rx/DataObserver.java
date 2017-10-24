package com.kxjsj.doctorassistant.Rx;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.PublicUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class DataObserver<T> implements Observer<KotlinBean.BaseBean<T>> {
    private static final String TAG = "DataObserver";
    private Object tag = null;
    Disposable d;

    protected DataObserver(Object tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        RxLifeUtils.getInstance().add(tag, d);
    }


    @Override
    public void onNext(KotlinBean.BaseBean<T> t) {
        if (t.getError_code()==200) {
            OnNEXT(t.getData());
        }else if(t.getError_code()==100){
            OnERROR(t.getMessage());
        }else{
            OnLOGIN();
        }
    }

    @Override
    public void onError(Throwable e) {
        OnERROR(e.getMessage());
    }

    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed())
            d.isDisposed();
    }

    public abstract void OnNEXT(T bean);
    public void OnERROR(String error){
        K2JUtils.toast(error);
    }
    public void OnLOGIN(){
        PublicUtils.loginOut(App.app);}

}

