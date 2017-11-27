package com.kxjsj.doctorassistant.View;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kxjsj.doctorassistant.Screen.SizeUtils;

/**
 * Created by vange on 2017/11/13.
 */

public class MyItemDecorate extends RecyclerView.ItemDecoration {
    float height=SizeUtils.dp2px(15);
    Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    public MyItemDecorate(int color,float height){
        paint.setColor(color);
        this.height=height;
    }
    public MyItemDecorate(){
        paint.setColor(0xffffffff);
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int bottom = view.getBottom();
            int left = view.getLeft();
            int right = view.getRight();
            c.drawRect(left,bottom,right,bottom+ SizeUtils.dp2px(15),paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int itemCount = adapter.getItemCount();
        outRect.bottom= (int) height;

    }
}
