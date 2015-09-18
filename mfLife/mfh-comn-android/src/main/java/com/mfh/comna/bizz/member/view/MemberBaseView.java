package com.mfh.comna.bizz.member.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mfh.comna.R;
import com.mfh.comna.utils.DensityUtil;
import com.mfh.comna.view.img.FineImgView;


/**
 * Created by Shicy on 14-4-14.
 */
public class MemberBaseView extends FrameLayout {
    private TextView nameTv;
    private TextView addrTv;
    private FineImgView headImg;

    public MemberBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.subview_member_base, this, true);
        nameTv = (TextView)findViewById(R.id.base_name);
        addrTv = (TextView)findViewById(R.id.base_address);
        headImg = (FineImgView)findViewById(R.id.head_image_base);

        //头像设置
        headImg.setFao(FineImgView.getHeadImgFao());
        headImg.setNeedSample(true);
        headImg.setMaxWidth(DensityUtil.dip2px(context, 48));
    }

    /**
     * 设置姓名
     * @param humanName
     */
    public void setHumanName(String humanName) {
        nameTv.setText(humanName);
    }

    /**
     * 设置地址
     * @param addrName
     */
    public void setAddrName(String addrName) {
        addrTv.setText(addrName);
    }

    /**
     * 设置头像
     * @param imgSrc
     */
    public void setHeadImg(String imgSrc) {
        headImg.setSrc(imgSrc);
    }

}
