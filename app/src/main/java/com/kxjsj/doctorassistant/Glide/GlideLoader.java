package com.kxjsj.doctorassistant.Glide;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kxjsj.doctorassistant.R;

/**
 * Created by vange on 2017/10/23.
 */

public class GlideLoader {

    public static void loadRound(ImageView imageView,Object url){
        Glide.with(imageView)
                .load(url)
                .apply(MyOptions.getCircleRequestOptions())
                .transition(MyOptions.getTransitionCrossFade())
                .into(imageView);
    }
}
