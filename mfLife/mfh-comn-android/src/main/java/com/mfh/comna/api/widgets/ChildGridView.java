package com.mfh.comna.api.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView，解决ScrollView嵌套GridView，GridView显示不全问题。
 * Created by Administrator on 2015/4/18.
 */
public class ChildGridView extends GridView {
    public ChildGridView(Context context) {
        super(context);
    }

    public ChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
