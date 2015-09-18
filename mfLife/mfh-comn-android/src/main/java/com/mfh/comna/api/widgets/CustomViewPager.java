package com.mfh.comna.api.widgets;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager，解决ViewPagger嵌套使用时不滑动问题。
 * Created by Nat.ZZN on 2015/8/12.
 */
public class CustomViewPager extends ViewPager {
    //是否可以滑动
    private boolean scrollEnabled;

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.scrollEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.scrollEnabled){
            //当拦截触摸事件到达此位置的时候，返回true，
            //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
            return super.onInterceptTouchEvent(ev);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.scrollEnabled){
            //每次进行onTouch事件都记录当前的按下的坐标
            curP.x = ev.getX();
            curP.y = ev.getY();

            if(ev.getAction() == MotionEvent.ACTION_DOWN){
                //记录按下时候的坐标
                //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
                downP.x = ev.getX();
                downP.y = ev.getY();
                //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                getParent().requestDisallowInterceptTouchEvent(true);
            }

            if(ev.getAction() == MotionEvent.ACTION_MOVE){
                //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                getParent().requestDisallowInterceptTouchEvent(true);
            }

//        if(ev.getAction() == MotionEvent.ACTION_UP){
//            //在up时判断是否按下和松手的坐标为一个点
//            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
//            if(downP.x==curP.x && downP.y==curP.y){
////                onSingleTouch();
//                return true;
//            }
//        }

            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }
}
