package com.kxjsj.doctorassistant.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kxjsj.doctorassistant.App;


/**
 * Created by vange on 2017/10/9.
 * 输入法的隐藏
 * TextInputLayout相关操作
 */

public class InputUtils {
    /**
     * 隐藏输入法
     * @param editText
     */
    public static void hideKeyboard(EditText editText) {
        InputMethodManager service = (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        service.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    /**
     * 隐藏输入法
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 隐藏输入法
     * @param dialog
     */
    public static void hideKeyboard(Dialog dialog) {
        InputMethodManager manager= (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 验证用户名
     * @param account
     * @return
     */
    public static boolean validateAccount(TextInputLayout textInputLayout, String account) {
        if (TextUtils.isEmpty(account)) {
            K2JUtils.toast("手机号不能为空");
            textInputLayout.setError("手机号不能为空");
            return false;
        }
        if (!RegularUtils.isMobile(account)) {
            textInputLayout.setError("请输入正确的手机号");
            K2JUtils.toast("请输入正确的手机号");
            return false;
        }
        return true;
    }

    /**
     * 验证密码
     * @param password
     * @return
     */
    public static boolean validatePassword(TextInputLayout textInputLayout, String password) {
        if (TextUtils.isEmpty(password)) {
            K2JUtils.toast("密码不能为空");
            textInputLayout.setError("密码不能为空");
            return false;
        }
        if (password.length() < 6 || password.length() > 18) {
            textInputLayout.setError("密码长度为6-18位");
            K2JUtils.toast("密码长度为6-18位");
            return false;
        }
        return true;
    }

}
