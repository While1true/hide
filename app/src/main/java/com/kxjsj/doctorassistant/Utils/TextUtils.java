package com.kxjsj.doctorassistant.Utils;

import android.text.TextPaint;
import android.widget.TextView;

import com.nestrefreshlib.Adpater.Base.Holder;

/**
 * Created by 不听话的好孩子 on 2018/5/2.
 */

public class TextUtils {
    public static void setTextBold(Holder holder, boolean isbold, int... id) {
        for (int i : id) {
            TextView tv = holder.getView(i);
            TextPaint paint = tv.getPaint();
            paint.setFakeBoldText(isbold);
        }
    }
}
