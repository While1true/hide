package com.kxjsj.doctorassistant.Mi;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
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
        String mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            String mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            String mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            String mUserAccount = message.getUserAccount();
        }
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "onReceivePassThroughMessage: " + mMessage);
    }
}
