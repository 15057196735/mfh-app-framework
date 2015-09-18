package com.mfh.comna.api.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.api.widgets.toggleButton.ToggleButton;
import com.mfh.comna.R;

/***
 * 自定义View（组合原有安卓控件或者布局）
 * 样式：(Icon + Title/Name) + Detail
 */
public class ToggleSettingItem extends RelativeLayout {
    private TextView    tvTitle;
    private ToggleButton buttonToggle;
    private View        vSeperateTop;//上分割线
    private View        vSeperateMiddle;//中分割线
    private View        vSeperateBottom;//下分割线

    /**
     * 分割线类型
     * */
    public enum SeperateLineType{
        SEPERATE_LINE_SINGLE,
        SEPERATE_LINE_MULTI_TOP,
        SEPERATE_LINE_MULTI_CENTER,
        SEPERATE_LINE_MULTI_BOTTOM,
    }
    private SeperateLineType seperateLineType = SeperateLineType.SEPERATE_LINE_SINGLE;


    public interface SettingItemLisener{
        void onToggleChanged(boolean on);
    }
    private SettingItemLisener listener;


	public ToggleSettingItem(Context context) {
		super(context);
		init();
	}

	public ToggleSettingItem(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

    public ToggleSettingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

	private void init() {
		View.inflate(getContext(), R.layout.listitem_switch, this);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.buttonToggle = (ToggleButton) findViewById(R.id.button_toggle);
        this.buttonToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (listener != null) {
                    listener.onToggleChanged(on);
                }
            }
        });
        vSeperateTop = findViewById(com.mfh.comna.R.id.separate_top);
        vSeperateMiddle = findViewById(com.mfh.comna.R.id.separate_middle);
        vSeperateBottom = findViewById(com.mfh.comna.R.id.separate_bottom);
	}

    /**
     * 初始化
     * */
    public void init(String title, SeperateLineType seperateLineType, SettingItemLisener listener){
        this.tvTitle.setText(title);
        this.listener = listener;

        setSeperateType(seperateLineType);
    }
    public void init(int resId, SeperateLineType seperateLineType, SettingItemLisener listener){
        this.tvTitle.setText(resId);
        this.listener = listener;

        setSeperateType(seperateLineType);
    }

    /**
     * 设置分割线类型
     * */
    public void setSeperateType(SeperateLineType seperateLineType){
        this.seperateLineType = seperateLineType;

        if(seperateLineType == SeperateLineType.SEPERATE_LINE_MULTI_TOP){
            vSeperateTop.setVisibility(View.VISIBLE);
            vSeperateMiddle.setVisibility(View.VISIBLE);
            vSeperateBottom.setVisibility(View.GONE);
        } else if(seperateLineType == SeperateLineType.SEPERATE_LINE_MULTI_CENTER){
            vSeperateTop.setVisibility(View.GONE);
            vSeperateMiddle.setVisibility(View.VISIBLE);
            vSeperateBottom.setVisibility(View.GONE);
        } else if(seperateLineType == SeperateLineType.SEPERATE_LINE_MULTI_BOTTOM){
            vSeperateTop.setVisibility(View.GONE);
            vSeperateMiddle.setVisibility(View.GONE);
            vSeperateBottom.setVisibility(View.VISIBLE);
        }else{
            vSeperateTop.setVisibility(View.VISIBLE);
            vSeperateMiddle.setVisibility(View.GONE);
            vSeperateBottom.setVisibility(View.VISIBLE);
        }
    }

    public void setToggleEnable(boolean enabled){
        if(enabled){
            this.buttonToggle.setToggleOn();
        }else{
            this.buttonToggle.setToggleOff();
        }
    }
}
