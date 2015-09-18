package com.mfh.comna.view.widget;
import android.content.Context;
import android.util.AttributeSet;
import com.mfh.comna.R;

/**
 * Created by Shicy on 14-4-24.
 */
public class SimpleHorizontalField extends FieldLayout {

    public SimpleHorizontalField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comn_field_horizontal_simple;
    }
}
