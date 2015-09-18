package com.mfh.comna.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/6/4.
 */
public class LoadingImageView extends ImageView {
    private AnimationDrawable loadingDrawable;

    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);

        //init and start animation
        loadingDrawable = (AnimationDrawable) getBackground();
        if(loadingDrawable != null){
            loadingDrawable.stop();
            loadingDrawable.start();
        }
    }

    /**
     * 设置状态
     * */
    public void toggle(boolean enabled){
        if(enabled){
            setVisibility(View.VISIBLE);
            if(loadingDrawable != null){
                loadingDrawable.stop();
                loadingDrawable.start();
            }
        }else{
            setVisibility(View.GONE);
//            if(loadingDrawable != null){
//                loadingDrawable.stop();
//            }
        }
    }


}
