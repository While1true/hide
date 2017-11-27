package com.kxjsj.doctorassistant;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.Dagger.Component.AppComponent;
import com.kxjsj.doctorassistant.Dagger.Component.DaggerAppComponent;
import com.kxjsj.doctorassistant.Dagger.Module.AppModule;
import com.kxjsj.doctorassistant.RongYun.RongYunInitialUtils;
import com.kxjsj.doctorassistant.Screen.AdjustUtil;
import com.kxjsj.doctorassistant.MobSMS.MessageUtils;
import com.kxjsj.doctorassistant.Utils.GsonUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;



/**
 * Created by vange on 2017/9/6.
 */
public class App extends Application {
    public static App app;
    private static AppComponent appComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app=this;

        new Thread(() -> init()).start();

        AdjustUtil.adjust(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).onTrimMemory(level);
    }

    private void init() {
        RongYunInitialUtils.init(this);
        MessageUtils.init(this);
//        ZXingLibrary.initDisplayOpinion(this);
    }


    /**
     * 获取用户信息
     * @return
     */
    public static Session getUserInfo(){
        String userinfo=K2JUtils.get("userinfo","");
        Session session = GsonUtils.parse2Bean(userinfo, Session.class);
        return session==null?new Session():session;
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        return getUserInfo().getToken();
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }
}
