package com.kxjsj.doctorassistant.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by vange on 2017/10/10.
 */

public class ZXingUtils {
    /**
     * 打开镜头识别二维码
     * @param context
     * @param observer
     */
    public static void startCapture(Context context, MyObserver observer){
        RxBus.getDefault()
                .toObservable(Constance.Rxbus.QRCODE,BaseBean.class)
                .compose(RxSchedulers.compose())
                .subscribe(observer);
        context.startActivity(new Intent(context,MyCaptureActivity.class));

    }

    public static class MyCaptureActivity extends BaseTitleActivity{
        /**
         * 二维码解析回调函数
         */
        CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.QRCODE,result));
            }

            @Override
            public void onAnalyzeFailed() {
                RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.QRCODE,null));
            }
        };
        @Override
        protected int getContentLayoutId() {
            return com.uuzuche.lib_zxing.R.layout.camera;
        }

        @Override
        protected void initView(Bundle savedInstanceState) {
            CaptureFragment captureFragment = new CaptureFragment();
            captureFragment.setAnalyzeCallback(analyzeCallback);
            getSupportFragmentManager().beginTransaction().replace(com.uuzuche.lib_zxing.R.id.fl_zxing_container, captureFragment).commit();
            captureFragment.setCameraInitCallBack(e -> {
                if (e == null) {

                } else {
                    Log.e("TAG", "callBack: ", e);
                }
            });
        }
    }
}
