package com.kxjsj.doctorassistant.Dagger.Component;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Dagger.Module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vange on 2017/11/14.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    App getApp();

    Session getUserInfo();

}
