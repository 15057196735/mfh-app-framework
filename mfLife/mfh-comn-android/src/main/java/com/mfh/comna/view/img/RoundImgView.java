package com.mfh.comna.view.img;

import android.content.Context;
import android.util.AttributeSet;

import com.mfh.comna.R;

/**
 * 椭圆形头像
 * Created by Administrator on 14-5-26.
 */
public class RoundImgView extends FineImgView {

    public RoundImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayOutResource() {
        return R.layout.subview_round_image;
    }
}
