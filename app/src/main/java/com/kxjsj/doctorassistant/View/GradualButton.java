package com.kxjsj.doctorassistant.View;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

/**
 * Created by vange on 2017/10/12.
 */

public class GradualButton extends Button {


    private ObjectAnimator objectAnimator;

    public GradualButton(Context context) {
        this(context,null);
    }

    public GradualButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradualButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public GradualButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setGravity(Gravity.CENTER);
        setClickable(true);
        setSoundEffectsEnabled(true);
    }

    public void start(int startColor,int endColor){
        if(objectAnimator==null) {
            objectAnimator = ObjectAnimator.ofInt(this, "textColor", startColor, endColor);
            objectAnimator.setEvaluator(new HsvEvaluator());
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.setDuration(1000);
            objectAnimator.setRepeatCount(-1);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }
        objectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(objectAnimator!=null)
            objectAnimator.cancel();
        objectAnimator=null;
    }

}
