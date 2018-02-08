package com.kxjsj.doctorassistant.Constant;

/**
 * Created by vange on 2017/8/31.
 */

public class Constance {

    public static final int REQUEST_SUCCESS = 200;
    public static final int REQUEST_FAILED = 100;
    public static final int REQUEST_LOGIN = 300;


    public static final String DEBUG = "DEBUG";
    public static final boolean DEBUGTAG = true;

    public static class Rxbus {
        /**
         * 注册验证验证码成功，通知调到下一页
         */
        public static final int SIGNNEXT = 0;

        /**
         * 登录成功
         */
        public static final int LOGIN_SUCCESS = 1;

        /**
         * 关闭时关闭输入法
         */
        public static final int CLOST_INPUT = 2;

        /**
         * 扫描二维码
         */
        public static final int QRCODE = 3;

        /**
         * 选择了照片
         */
        public static final int PIC=4;

        /**
         * 呼叫
         */
        public static final int CALLHELP=5;

        /**
         * 留言
         */
        public static final int COMMENT=6;

        /**
         * 评价 医生跳护士
         */
        public static final int DOCTOR_NURSE=7;

        /**
         * 缴费完成
         */
        public static final int PAY_COMPLETE=8;

        /**
         * 知识服务推送
         */
        public static final int KNOWLEDGE=9;
    }

}
