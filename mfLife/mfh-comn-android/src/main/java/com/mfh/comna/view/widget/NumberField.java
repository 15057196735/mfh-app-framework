package com.mfh.comna.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mfh.comna.R;

/**
 * Created by Shicy on 14-4-24.
 */
public class NumberField extends FieldLayout {

    private boolean isNew = false;

    public NumberField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comn_field_number;
    }

    @Override
    protected void setView(View view) {
        super.setView(view);

        TextView numberText = (TextView)view.findViewById(android.R.id.text2);
        if (numberText != null) {
            if (value == null || "".equals(value) || "0".equals(value))
                numberText.setVisibility(GONE);
            else
                numberText.setVisibility(VISIBLE);
        }

        View newFlag = view.findViewById(R.id.field_is_new_flag);
        if (newFlag != null)
            newFlag.setVisibility(isNew ? VISIBLE : GONE);
    }

}
