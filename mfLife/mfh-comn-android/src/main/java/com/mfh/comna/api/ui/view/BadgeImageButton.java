package com.mfh.comna.api.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfh.comna.R;


/**
 * @author NAT.ZZN (bingshanguxue)
 */
public class BadgeImageButton extends RelativeLayout {
    private static final int MIN_NUM = 0;
    private static final int MAX_NUM = 99;
    private static final String TEXT_MAX_NUM = "99+";


    private View rootView;
    private ImageView 	ivButtonImage;
	private TextView    tvButtonText;
    private TextView    tvBadgeNumber;

	public BadgeImageButton(Context context) {
		super(context);
        init();
	}

	public BadgeImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

	private void init() {
        rootView = View.inflate(getContext(), R.layout.view_badge_imagebutton, this);
        ivButtonImage = (ImageView) this.findViewById(R.id.iv_buttonImage);
		tvButtonText = (TextView) this.findViewById(R.id.tv_buttonText);
        tvBadgeNumber = (TextView) this.findViewById(R.id.tv_badgeNumber);
	}

    public void init(int resId, String text){
        this.ivButtonImage.setImageResource(resId);
        this.tvButtonText.setText(text);
        this.tvBadgeNumber.setVisibility(View.GONE);
    }
    public void init(int imageResId, int titleResId){
        this.ivButtonImage.setImageResource(imageResId);
        this.tvButtonText.setText(titleResId);
        this.tvBadgeNumber.setVisibility(View.GONE);
    }

    public void init(int imageResId, String text, int textColor){
        this.ivButtonImage.setImageResource(imageResId);
        this.tvButtonText.setText(text);
        this.tvButtonText.setTextColor(textColor);
        this.tvBadgeNumber.setVisibility(View.GONE);
    }

    public void init(int imageResId, int titleResId, int textColor){
        this.ivButtonImage.setImageResource(imageResId);
        this.tvButtonText.setText(titleResId);
        this.tvButtonText.setTextColor(textColor);
        this.tvBadgeNumber.setVisibility(View.GONE);
    }
    public void setBadgeNumber(int badgeNumber){
        if(badgeNumber > MIN_NUM){
            if(badgeNumber > MAX_NUM){
                this.tvBadgeNumber.setText(TEXT_MAX_NUM);
            }else{
                this.tvBadgeNumber.setText(String.valueOf(badgeNumber));
            }

            this.tvBadgeNumber.setVisibility(View.VISIBLE);
        }
        else{
            this.tvBadgeNumber.setVisibility(View.GONE);
        }
    }

    public void setTvButtonText(int color){
        this.tvButtonText.setTextColor(color);
    }

    public void setSelected(boolean selected){
        this.tvButtonText.setSelected(selected);
        this.ivButtonImage.setSelected(selected);
    }


}
