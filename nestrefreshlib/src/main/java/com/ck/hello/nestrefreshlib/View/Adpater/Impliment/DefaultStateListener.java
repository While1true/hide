package com.ck.hello.nestrefreshlib.View.Adpater.Impliment;

import android.content.Context;

import com.ck.hello.nestrefreshlib.View.Adpater.Interface.BaseStateListener;

/**
 * Created by ck on 2017/9/9.
 */

public abstract class DefaultStateListener implements BaseStateListener {
    public abstract void netError(Context context);

    public  void showEmpty(Context context) {
    }
}
