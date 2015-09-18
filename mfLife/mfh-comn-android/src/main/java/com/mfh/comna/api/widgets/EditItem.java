package com.mfh.comna.api.widgets;


import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.R;

import butterknife.Bind;
import butterknife.ButterKnife;


/***
 * 自定义编辑控件
 */
public class EditItem extends RelativeLayout {
    private View        rootView;
    private TextView    tvTitle;
    private TextView    tvContent;
    private EditText    etContent;
    private ImageView   ivArrow;
    private View        vSeperateTop;//上分割线
    private View        vSeperateBottom;//下分割线

    /**
     * 样式类型
     * */
    public enum ThemeType{
        THEME_TEXT_EDIT,
        THEME_TEXT_EDIT_NUMBER,
        THEME_TEXT_EDIT_PHONE,
        THEME_TEXT_TEXT_ARROW
    }
    private ThemeType themeType = ThemeType.THEME_TEXT_EDIT;

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

	public EditItem(Context context) {
		super(context);
		init();
	}

	public EditItem(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

    public EditItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

	private void init() {
        rootView = View.inflate(getContext(), R.layout.view_edit_item, this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        etContent = (EditText) findViewById(R.id.et_content);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivArrow = (ImageView) findViewById(R.id.iv_arrow_right);
        vSeperateTop = findViewById(R.id.separate_top);
        vSeperateBottom = findViewById(R.id.separate_bottom);
	}

    public void init(String title, String content, ThemeType themeType){
        tvTitle.setText(title);
        etContent.setText(content);

        setThemeType(themeType);
        setSeperateType(seperateLineType);
    }

    public void init(String title, String content, ThemeType themeType, SeperateLineType seperateLineType){
        tvTitle.setText(title);
        etContent.setText(content);

        setThemeType(themeType);
        setSeperateType(seperateLineType);
    }

    /**
     * 设置样式类型
     * */
    public void setThemeType(ThemeType themeType){
        this.themeType = themeType;
        if(themeType == ThemeType.THEME_TEXT_EDIT){
            etContent.setVisibility(VISIBLE);
            tvContent.setVisibility(GONE);
            ivArrow.setVisibility(View.GONE);
        }
        else if(themeType == ThemeType.THEME_TEXT_EDIT_NUMBER){
            etContent.setVisibility(VISIBLE);
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvContent.setVisibility(GONE);
            ivArrow.setVisibility(View.GONE);
        }
        else if(themeType == ThemeType.THEME_TEXT_EDIT_PHONE){
            etContent.setVisibility(VISIBLE);
            etContent.setInputType(InputType.TYPE_CLASS_PHONE);
            tvContent.setVisibility(GONE);
            ivArrow.setVisibility(View.GONE);
        }
        else if(themeType == ThemeType.THEME_TEXT_TEXT_ARROW){
            etContent.setVisibility(View.GONE);
            tvContent.setVisibility(VISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
        } else{
            etContent.setVisibility(VISIBLE);
            tvContent.setVisibility(GONE);
            ivArrow.setVisibility(View.GONE);
        }
    }

    /**
     * 设置分割线类型
     * */
    public void setSeperateType(SeperateLineType seperateLineType){
        this.seperateLineType = seperateLineType;

        if(seperateLineType == SeperateLineType.SEPERATE_LINE_MULTI_TOP){
            vSeperateTop.setVisibility(View.VISIBLE);
            vSeperateBottom.setVisibility(View.GONE);
        } else if(seperateLineType == SeperateLineType.SEPERATE_LINE_MULTI_CENTER){
            vSeperateTop.setVisibility(View.GONE);
            vSeperateBottom.setVisibility(View.GONE);
        } else if(seperateLineType == SeperateLineType.SEPERATE_LINE_MULTI_BOTTOM){
            vSeperateTop.setVisibility(View.GONE);
            vSeperateBottom.setVisibility(View.VISIBLE);
        }else{
            vSeperateTop.setVisibility(View.VISIBLE);
            vSeperateBottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置详细描述信息
     * */
    public void setDetailText(String text){
        this.etContent.setText(text);
        this.tvContent.setText(text);
    }

    public String getDetailText(){
        return this.etContent.getText().toString();
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }
}
