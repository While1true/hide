package com.kxjsj.doctorassistant.Dagger.Module;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Utils.GsonUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vange on 2017/11/14.
 */

@Module
public class AppModule {
    private App app;
    public AppModule(App app){
        this.app=app;
    }

    @Singleton
    @Provides
    public App provideApp(){
        return app;
    }

    @Provides
    public Session provideUserInfo(){
        String userinfo= K2JUtils.get("userinfo","");
        Session session = GsonUtils.parse2Bean(userinfo, Session.class);
        return session==null?new Session():session;
    }
}
