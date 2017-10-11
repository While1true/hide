package com.kxjsj.doctorassistant.Mi;

import android.content.Context;

import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.xiaomi.market.sdk.UpdateResponse;
import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.xiaomi.market.sdk.XiaomiUpdateListener;

/**
 * Created by vange on 2017/10/10.
 */

public class MiUpdateUtils {
    /**
     * 手动检查更新
     *
     * @param context
     * @param toastNoUpadte
     */
    public static void upgrade(Context context, boolean toastNoUpadte) {
        if (!toastNoUpadte)
            XiaomiUpdateAgent.setCheckUpdateOnlyWifi(true);
        XiaomiUpdateAgent.update(context);
        XiaomiUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            switch (updateStatus) {
                case UpdateStatus.STATUS_UPDATE:
                    // 有更新， UpdateResponse为本次更新的详细信息
                    // 其中包含更新信息，下载地址，MD5校验信息等，可自行处理下载安装
                    // 如果希望 SDK继续接管下载安装事宜，可调用
                    XiaomiUpdateAgent.arrange();
                    break;
                case UpdateStatus.STATUS_NO_UPDATE:
                    // 无更新， UpdateResponse为null
                    if (toastNoUpadte) {
                        K2JUtils.toast("无更新");
                    }
                    break;
                case UpdateStatus.STATUS_NO_WIFI:
                    // 设置了只在WiFi下更新，且WiFi不可用时， UpdateResponse为null
                    break;
                case UpdateStatus.STATUS_NO_NET:
                    // 没有网络， UpdateResponse为null
                    break;
                case UpdateStatus.STATUS_FAILED:
                    // 检查更新与服务器通讯失败，可稍后再试， UpdateResponse为null
                    break;
                case UpdateStatus.STATUS_LOCAL_APP_FAILED:
                    // 检查更新获取本地安装应用信息失败， UpdateResponse为null
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 自动检查更新
     *
     * @param context
     */
    public static void upgrade(Context context) {
        upgrade(context, false);
    }

}
