package com.kxjsj.doctorassistant.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by vange on 2017/10/12.
 */

public class MoveTextview extends TextView {
    public MoveTextview(Context context) {
        this(context,null);
    }

    public MoveTextview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MoveTextview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public MoveTextview(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSingleLine();
        requestFocus();
        setMarqueeRepeatLimit(-1);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        post(() -> start());
    }

    private void start() {
        int measuredWidth = getMeasuredWidth();
        TextPaint paint = getPaint();
        String string = getText().toString();
        float measureText = paint.measureText(string);
        /**
         * 前后增加空格达到跑马灯走动字数
         */
        if(measuredWidth>=measureText) {
            float needfill = (measuredWidth - measureText) / 2;
            float v = paint.measureText("  ");
            int add = (int) (0.5f + needfill / v);
            StringBuilder builder = new StringBuilder(20);
            for (int i = 0; i < add; i++) {
                builder.append("  ");
            }
            builder.append(string);
            for (int i = 0; i < add+1; i++) {
                builder.append("  ");
            }
            setText(builder.toString());
        }
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
