package com.kxjsj.doctorassistant.RongYun;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Holder.CallBack;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * Created by vange on 2017/9/19.
 */

public class ConversationUtils {
    /**
     * 单人会话
     *
     * @param context
     * @param RongUseId
     * @param title
     */
    public static void startChartSingle(Context context, String RongUseId, String title) {
        RongIM.getInstance().startConversation(context,
                Conversation.ConversationType.PRIVATE, RongUseId, title);
    }

    public static void openChartList(Context context) {
        RongIM.getInstance().startConversationList(context);
    }

    public static void creatGroupChart(String title, List<String> RongIds, RongIMClient.CreateDiscussionCallback callback) {
        /**
         *创建讨论组时，mLists为要添加的讨论组成员，创建者一定不能在 mLists 中
         */
        RongIM.getInstance().getRongIMClient().createDiscussion(title, RongIds, callback);
    }


    public static void sendMessage(String targetId, String message, CallBack<Message> callBack) {
        TextMessage myTextMessage = TextMessage.obtain(message);
        Message myMessage = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onAttached: "+message.getTargetId());
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                callBack.onCallBack(message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onError: "+errorCode.getMessage());
            }
        });
    }
}
