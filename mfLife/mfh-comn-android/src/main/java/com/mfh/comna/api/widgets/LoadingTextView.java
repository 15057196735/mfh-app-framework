package com.mfh.comna.api.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfh.comna.R;


/***
 *
 *
 *
 */
public class LoadingTextView extends LinearLayout {
    private TextView   tvLoading;

	public LoadingTextView(Context context) {
		super(context);
		init();
	}

	public LoadingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

    public LoadingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

	private void init() {
		View.inflate(getContext(), R.layout.loading_text_view, this);
        this.tvLoading = (TextView) findViewById(R.id.tv_loading);
	}

    /**
     *
     * */
    public void setLoadingText(String text){
        this.tvLoading.setText(text);
    }

    public void show(String text){
        this.tvLoading.setText(text);
        this.setVisibility(View.VISIBLE);
    }

    public void hide(){
        this.setVisibility(View.GONE);
    }

}
