package com.mfh.comna.api.widgets;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.R;


/**
 * 自定义搜索控件
 * @author ZZN
 * */
public class CustomSearchView extends RelativeLayout {
	private Context     context;
    private EditText    etQueryText;
    private ImageButton ibClear;

    private boolean queryEnabled = true;//是否支持查询


    public interface CustomSearchViewListener{
        void beforeTextChanged(java.lang.CharSequence charSequence, int i, int i1, int i2);
        void onTextChanged(java.lang.CharSequence charSequence, int i, int i1, int i2);
        void afterTextChanged(android.text.Editable editable);
        void doSearch(String queryText);
    }
    private CustomSearchViewListener listener;
    public void setListener(CustomSearchViewListener listener){
        this.listener = listener;
    }

	public CustomSearchView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public CustomSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	private void initView() {
		View.inflate(context, R.layout.custom_search_view, this);
        etQueryText = (EditText) findViewById(R.id.et_query);
        etQueryText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//设置键盘回车类型
        etQueryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (listener != null) {
                        listener.doSearch(getQueryText());
                    }
                    return true;
                }
                return false;
            }
        });
        etQueryText.addTextChangedListener(queryTextWatcher);
        ibClear = (ImageButton) findViewById(R.id.search_clear);
        ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                etQueryText.setText("");
                etQueryText.getText().clear();
            }
        });
	}

    private TextWatcher queryTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (listener != null) {
                listener.beforeTextChanged(charSequence, i, i2, i3);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //1.隐藏/显示删除按键
            if (charSequence != null && charSequence.length() > 0) {
                ibClear.setVisibility(View.VISIBLE);
            } else {
                ibClear.setVisibility(View.INVISIBLE);
            }

            if (listener != null) {
                listener.onTextChanged(charSequence, i, i2, i3);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (listener != null) {
                listener.afterTextChanged(editable);
            }
        }
    };

    public void setHint(String queryHint){
        etQueryText.setHint(queryHint);
    }

    public void setHint(int resId){
        etQueryText.setHint(resId);
    }

    public void setHint(String hint, int hintColor, int backgroundColor){
        etQueryText.setHint(hint);
        etQueryText.setHintTextColor(hintColor);
        etQueryText.setBackgroundColor(backgroundColor);
    }

    public void setHintColor(int hintColor){
        etQueryText.setHintTextColor(hintColor);
    }

    public void setTextColor(int color){
        etQueryText.setTextColor(color);
    }

    /**
     * 设置是否支持输入
     * */
    public void setEnabled(boolean enabled){
        queryEnabled = enabled;
        if(enabled){
            etQueryText.setText("");
//            etQueryText.setFocusable(true);
        }else{
            etQueryText.setText("");
//            etQueryText.setFocusable(false);
        }
//        etQueryText.setClickable(!enabled);
    }

    public String getQueryText(){
        return etQueryText.getText().toString();
    }
}
