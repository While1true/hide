package com.kxjsj.doctorassistant.Screen;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by vange on 2017/9/28.
 */

public class OrentionUtils {
    /**
     * 是否竖屏
     * @param context
     * @return true 竖屏 false 横屏
     */
    public static boolean isPortrait(Context context){
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 横竖屏设置不同count
     * @param context
     * @return
     */
    public static int calculateSpanCount(Context context){
        if (OrentionUtils.isPortrait(context))
            return 3;
        else
            return 4;
    }
}
