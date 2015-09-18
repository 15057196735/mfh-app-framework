package com.mfh.comna.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mfh.comna.R;

/**
 *
 * Created by Shicy on 14-4-25.
 */
public abstract class FieldLayout extends FrameLayout {

    protected View currentView;
    protected String name;
    protected String value;
    protected boolean showSeparate;
    protected int paddingLeft = 0;
    protected int paddingRight = 0;

    //private transient TextView

    public FieldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.getAttrs(context, attrs);
        currentView = LayoutInflater.from(context).inflate(getLayoutId(), this, true);
        this.setView(currentView);
    }

    protected void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Field);
        name = typedArray.getString(R.styleable.Field_fieldName);
        value = typedArray.getString(R.styleable.Field_fieldValue);
        showSeparate = typedArray.getBoolean(R.styleable.Field_fieldSeparate, true);
        paddingLeft = (int)typedArray.getDimension(R.styleable.Field_fieldPaddingLeft, 0);
        paddingRight = (int)typedArray.getDimension(R.styleable.Field_fieldPaddingRight, 0);
        typedArray.recycle();
    }

    protected abstract int getLayoutId();

    protected void setView(View view) {
        TextView textView1 = (TextView)view.findViewById(android.R.id.text1);
        if (textView1 != null)
            textView1.setText(name == null ? "" : name);

        TextView textView2 = (TextView)view.findViewById(android.R.id.text2);
        if (textView2 != null)
            textView2.setText(value == null ? "" : value);

        View separate = view.findViewById(R.id.field_separate);
        if (separate != null)
            separate.setVisibility(showSeparate ? VISIBLE : GONE);

        View container = view.findViewById(R.id.field_container);
        if (container != null) {
            container.setPadding(paddingLeft, 0, paddingRight, 0);
        }
    }

    /**
     * 设置字段值
     * @param value
     */
    public void setValue(String value) {
        TextView textView2 = (TextView)currentView.findViewById(android.R.id.text2);
        textView2.setText(value);
    }
    /**
     * 获取字段值
     * */
    public String getValue() {
        TextView textView2 = (TextView)currentView.findViewById(android.R.id.text2);
        return textView2.getText().toString();
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        if (currentView == null)
            return ;
        View field = currentView.findViewById(R.id.field);
        if (field == null)
            return ;
        if (listener == null)
            field.setOnClickListener(null);
        else {
            field.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(FieldLayout.this);
                }
            });
        }
    }

}
