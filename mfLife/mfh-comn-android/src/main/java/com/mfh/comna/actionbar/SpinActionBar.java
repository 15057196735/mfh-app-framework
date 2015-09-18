package com.mfh.comna.actionbar;

import com.mfh.comna.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * 中间带有下拉菜单的actionbar，替换了title
 * 
 * @author zhangyz created on 2013-4-10
 * @since Framework 1.0
 */
public class SpinActionBar extends BaseActionBar{
    protected Spinner spinTitle = null;
    
    public SpinActionBar(Context context) {
        super(context);
    }

    public SpinActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SpinActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void otherInit (TypedArray a) {
        spinTitle = (Spinner)this.findViewById(R.id.gd_action_bar_spinner);
    }

    @Override
    protected int getLayoutId(TypedArray a) {
        int type = a.getInteger(R.styleable.ActionBar_type, -1);
        int layoutID;
        switch (type) {
            case 0:
            default:
                mType = Type.Normal;
                layoutID = R.layout.gd_action_bar_spin;
                break;
        }
        return layoutID;
    }
    
    /**
     * 获取actionbar中的spin
     * @return
     * @author zhangyz created on 2013-4-10
     */
    public Spinner getSpinTitle() {
        if (spinTitle == null)
            spinTitle = (Spinner)this.findViewById(R.id.gd_action_bar_spinner);
        return spinTitle;
    }
}
