package com.mfh.comna.api.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/4/22.
 */
public class YScrollView extends ScrollView{
    private GestureDetector mGestureDetector;

    public YScrollView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    public YScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);//default
        //当拦截触摸事件到达此位置的时候，返回true，
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
//        return true;
        //接近水平滑动时子控件处理该事件，否则交给父控件处理
//        return mGestureDetector.onTouchEvent(ev);
    }


    public YScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            return super.onScroll(e1, e2, distanceX, distanceY);

            //接近水平滑动时子控件处理该事件，否则交给父控件处理
            return (Math.abs(distanceY) > Math.abs(distanceY));
        }
    }
}
