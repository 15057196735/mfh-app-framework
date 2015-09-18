package com.mfh.comna.api.widgets;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.R;


/***
 * 自定义View（组合原有安卓控件或者布局）
 * 样式：(Icon + Title/Name) + Detail
 * TODO:定义样式文件加载
 */
public class SettingsItem extends RelativeLayout {
    private ImageView   ivTag;
    private TextView    tvTitle;
    private TextView    tvDetail;
    private ImageView   ivArrow;
    private View        vSeperateTop;//上分割线
    private View        vSeperateMiddle;//中分割线
    private View        vSeperateBottom;//下分割线

    /**
     * 样式类型
     * */
    public enum ThemeType{
        THEME_IMAGE_TEXT_TEXT_ARROW,
        THEME_TEXT_TEXT_ARROW,
        THEME_TEXT_TEXT
    }
    private ThemeType themeType = ThemeType.THEME_IMAGE_TEXT_TEXT_ARROW;

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

    /**
     * detail text
     * */
    public enum DetailTextTheme{
        RIGHT_SINGLE_LINE,
        LEFT_SINGLE_LINE,
        LEFT_MULTI_LINE,
    }
    private DetailTextTheme detailTextTheme = DetailTextTheme.RIGHT_SINGLE_LINE;

	public SettingsItem(Context context) {
		super(context);
		init();
	}

	public SettingsItem(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

    public SettingsItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

	private void init() {
		View.inflate(getContext(), R.layout.view_settings_item, this);
        ivTag = (ImageView) findViewById(R.id.iv_tag);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        ivArrow = (ImageView) findViewById(R.id.iv_arrow_right);
        vSeperateTop = findViewById(R.id.separate_top);
        vSeperateMiddle = findViewById(R.id.separate_middle);
        vSeperateBottom = findViewById(R.id.separate_bottom);
	}

    public void init(SettingsItemData data){
        ivTag.setImageResource(data.getResId());
        tvTitle.setText(data.getTitle());
        tvDetail.setText(data.getDetail());

        setThemeType(themeType);
        setSeperateType(seperateLineType);
    }

    public void init(SettingsItemData data, SeperateLineType seperateLineType){
        ivTag.setImageResource(data.getResId());
        tvTitle.setText(data.getTitle());
        tvDetail.setText(data.getDetail());

        setThemeType(themeType);
        setSeperateType(seperateLineType);
    }

    /**
     * 设置类型
     * */
    public void setButtonType(ThemeType themeType, SeperateLineType seperateLineType){
        setThemeType(themeType);
        setSeperateType(seperateLineType);
    }

    public void setButtonType(ThemeType themeType, SeperateLineType seperateLineType, DetailTextTheme detailTextTheme){
        setThemeType(themeType);
        setSeperateType(seperateLineType);
        setDetailTextTheme(detailTextTheme);
    }

    /**
     * 设置样式类型
     * */
    public void setThemeType(ThemeType themeType){
        this.themeType = themeType;
        if(themeType == ThemeType.THEME_TEXT_TEXT_ARROW){
            ivTag.setVisibility(View.GONE);
            ivArrow.setVisibility(View.VISIBLE);
        }else if(themeType == ThemeType.THEME_TEXT_TEXT){
            ivTag.setVisibility(View.GONE);
            ivArrow.setVisibility(View.GONE);
        } else{
            ivTag.setVisibility(View.VISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
        }
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

    /**
     * 设置detailText样式
     * */
    public void setDetailTextTheme(DetailTextTheme detailTextTheme){
        this.detailTextTheme = detailTextTheme;

        if(detailTextTheme == DetailTextTheme.LEFT_SINGLE_LINE){
            this.tvDetail.setGravity(Gravity.START);
            this.tvDetail.setSingleLine(true);
//            this.tvDetail.setMaxLines(1);
        }
        else if(detailTextTheme == DetailTextTheme.LEFT_MULTI_LINE){
            this.tvDetail.setGravity(Gravity.START);
            this.tvDetail.setSingleLine(false);
            this.tvDetail.setMaxLines(2);
        }
        else{
            this.tvDetail.setGravity(Gravity.END);
            this.tvDetail.setSingleLine(true);
        }

        this.tvDetail.setEllipsize(TextUtils.TruncateAt.END);
    }

    /**
     * 设置详细描述信息
     * */
    public void setDetailText(String text){
        this.tvDetail.setText(text);
    }

}
