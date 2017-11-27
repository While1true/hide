package com.kxjsj.doctorassistant.Dagger.Module;

import android.content.Context;

import com.kxjsj.doctorassistant.Dagger.ActivityScope;


import dagger.Module;
import dagger.Provides;

/**
 * Created by vange on 2017/11/14.
 */

@ActivityScope
@Module
public class ActivityModule {
    Context context;
    public ActivityModule(Context context){
        this.context=context;
    }

    @Provides
    public Context provideContext(){
        return context;
    }
}
