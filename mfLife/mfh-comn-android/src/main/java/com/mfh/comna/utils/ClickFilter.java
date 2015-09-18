package com.mfh.comna.utils;

/**
 * 判断按钮在规定时间内被连续点击
 * Created by yxm on 2014/10/16.
 */
public class ClickFilter {
    private static long lastClickTime;
    public static boolean filter() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
