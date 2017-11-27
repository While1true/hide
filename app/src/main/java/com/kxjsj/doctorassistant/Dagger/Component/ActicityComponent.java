package com.kxjsj.doctorassistant.Dagger.Component;

import android.app.Activity;

import com.kxjsj.doctorassistant.Dagger.ActivityScope;
import com.kxjsj.doctorassistant.Dagger.Module.ActivityModule;

import dagger.Component;

/**
 * Created by vange on 2017/11/14.
 */

@ActivityScope
@Component(modules = {ActivityModule.class},dependencies = {AppComponent.class})
public interface ActicityComponent {
    void inject(Activity activity);
}
