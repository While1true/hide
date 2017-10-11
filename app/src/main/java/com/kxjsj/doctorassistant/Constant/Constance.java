package com.kxjsj.doctorassistant.Constant;

/**
 * Created by vange on 2017/8/31.
 */

public class Constance {
    public static final String DEBUG="DEBUG";
    public static final boolean DEBUGTAG=true;
    public static class Rxbus{
        /**
         * 注册验证验证码成功，通知调到下一页
         */
        public static final int SIGNNEXT=0;

        /**
         * 登录成功
         */
        public static final int LOGIN_SUCCESS=1;

        /**
         * 关闭时关闭输入法
         */
        public static final int CLOST_INPUT=2;

        /**
         * 扫描二维码
         */
        public static final int QRCODE=3;
    }

}
