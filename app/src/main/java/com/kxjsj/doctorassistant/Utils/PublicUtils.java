package com.kxjsj.doctorassistant.Utils;

import android.content.Context;
import android.content.Intent;

import com.kxjsj.doctorassistant.Appxx.Mine.Login.LoginActivity;
import com.kxjsj.doctorassistant.Net.Api;
import com.kxjsj.doctorassistant.Net.ApiController;

/**
 * Created by vange on 2017/10/23.
 */

public class PublicUtils {
    public static void loginOut(Context context) {
        ActivityUtils.getInstance().removeAll();
        context.startActivity(new Intent(context, LoginActivity.class));
    }

}