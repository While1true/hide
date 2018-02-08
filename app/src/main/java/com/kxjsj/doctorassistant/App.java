package com.kxjsj.doctorassistant;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.RongYun.RongYunInitialUtils;
import com.kxjsj.doctorassistant.Screen.AdjustUtil;
import com.kxjsj.doctorassistant.MobSMS.MessageUtils;
import com.kxjsj.doctorassistant.Utils.GsonUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.qihoo360.replugin.RePluginApplication;
import com.tencent.smtt.sdk.QbSdk;


/**
 * Created by vange on 2017/9/6.
 */
public class App extends RePluginApplication {
    public static App app;

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
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
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

}
