package com.kxjsj.doctorassistant.Architecture;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;

import com.kxjsj.doctorassistant.Architecture.ViewModel.PushViewModel;

/**
 * Created by vange on 2017/11/8.
 */

public class Test implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void getdata(AppCompatActivity activity){
        PushViewModel pushViewModel = ViewModelProviders.of(activity).get(PushViewModel.class);
        activity.getLifecycle().addObserver(Test.this);
    }
}
