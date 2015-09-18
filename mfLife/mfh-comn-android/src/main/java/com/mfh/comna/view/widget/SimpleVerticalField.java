package com.mfh.comna.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mfh.comna.R;

/**
 * Created by Shicy on 14-4-24.
 */
public class SimpleVerticalField extends FieldLayout {

    public SimpleVerticalField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comn_field_vertical_simple;
    }

    public void setValue(String value) {
        TextView textView2 = (TextView)currentView.findViewById(android.R.id.text2);
        if (textView2 != null)
            textView2.setText(value == null ? "" : value);
    }

}
