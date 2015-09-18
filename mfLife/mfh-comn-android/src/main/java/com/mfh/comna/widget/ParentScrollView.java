package com.mfh.comna.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 解决ScrollView嵌套ListView显示不全问题
 * Created by Administrator on 2015/4/22.
 */
public class ParentScrollView extends ScrollView{

    public ParentScrollView(Context context) {
        super(context);
    }

    public ParentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
