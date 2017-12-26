package com.kxjsj.doctorassistant.Mi;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Mine.Comment.CommentActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Rx.RxBaseBean;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Utils.GsonUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.NotificationUtils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import java.util.List;

import io.rong.push.platform.MiMessageReceiver;

/**
 * Created by vange on 2017/9/21.
 */

public class MiPushReceiver extends MiMessageReceiver {
    //传递给融云处理了，我只拿到resid给服务器来进行自己的推送
    String mRegId;

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onCommandResult: " + command);
        List<String> arguments = message.getCommandArguments();
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = arguments.get(0);
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onCommandResult: " + mRegId);
                K2JUtils.put("xiaomiId", mRegId);
            }
        }
        super.onCommandResult(context, message);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onReceiveRegisterResult: " + command);
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                K2JUtils.put("xiaomiId", mRegId);
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onReceiveRegisterResult: " + mRegId);
            }
        }
        super.onReceiveRegisterResult(context, message);
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        String description = message.getDescription();
        String mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            String mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            String mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            String mUserAccount = message.getUserAccount();
        }
        /**
         * 0:紧急呼叫 1：留言
         */
        if("0".equals(description)) {
            KotlinBean.PushBean pushBean = GsonUtils.parse2Bean(mMessage, KotlinBean.PushBean.class);
            if(pushBean!=null) {
                RxBus.getDefault().post(new RxBaseBean<>(Constance.Rxbus.CALLHELP, pushBean));
//                showPush(context, pushBean);
            }
        }else if("1".equals(description)){
            KotlinBean.PushBean pushBean = GsonUtils.parse2Bean(mMessage, KotlinBean.PushBean.class);
            if(pushBean!=null) {
                RxBus.getDefault().post(new RxBaseBean<>(Constance.Rxbus.COMMENT, pushBean));
                showPush(context, pushBean);
            }
        }
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "onReceivePassThroughMessage: " + mMessage);
    }

    private void showPush(Context context, KotlinBean.PushBean pushBean) {
        boolean showPush = K2JUtils.get("showPush", true);
        if(showPush) {
            NotificationUtils.CreatNotification(context,
                    "病床管理",App.getUserInfo().getType()==0?("@回复"+pushBean.getReply()):(pushBean.getFromName()+"@留言/"+pushBean.getContent()), new Intent(context, CommentActivity.class));
        }
    }


}
