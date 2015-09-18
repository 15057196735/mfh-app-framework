package com.mfh.comna.api.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.mfh.comna.api.web.HybridWebView;

/**
 * 自定义ViewPager
 * Created by Nat.ZZN on 15/8/12.
 */
public class WebviewViewPager extends ViewPager {
    public WebviewViewPager(Context context) {
        super(context);
    }

    public WebviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof HybridWebView){
            return ((HybridWebView) v).canScrollHor(-dx);
        }
        return super.canScroll(v, checkV, dx, x, y);
    }


    //    public boolean canScrollHor(int direction) {
//        final int offset = computeHorizontalScrollOffset();
//        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
//        if (range == 0) return false;
//        if (direction < 0) {
//            return offset > 0;
//        } else {
//            return offset < range - 1;
//        }
//    }
}
