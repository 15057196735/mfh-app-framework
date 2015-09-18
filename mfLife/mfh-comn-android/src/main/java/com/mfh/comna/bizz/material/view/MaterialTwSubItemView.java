package com.mfh.comna.bizz.material.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfh.comna.bizz.material.ResourceUtil;
import com.mfh.comna.bizz.material.entity.MsgAttr;
import com.mfh.comna.R;
import com.mfh.comna.view.img.FineImgView;

import org.apache.commons.lang3.StringUtils;

/**
 * 图文消息中的一个子ItemView展现
 * Created by Shicy on 14-4-25.
 */
public class MaterialTwSubItemView extends FrameLayout {
    private MsgAttr msgAttr;
    private View rootView;

    public MsgAttr getMsgAttr() {
        return msgAttr;
    }

    public void setMsgAttr(MsgAttr msgAttr) {
        this.msgAttr = msgAttr;
        fillView();
    }

    public MaterialTwSubItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    private void initView(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.subview_material_tw_subitem, this, true);
    }

    private void fillView() {
        TextView view = (TextView)rootView.findViewById(R.id.mat_tw_item_title);
        view.setText(msgAttr.getTitle());
        FineImgView iv = (FineImgView)rootView.findViewById(R.id.mat_tw_item_img);
        iv.setFao(ResourceUtil.getMatImgFao());
        //iv.setNeedSample(true);
        //iv.setMaxWidth(96);
        iv.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setSrc(msgAttr.getPicurl());

        if (StringUtils.isNotBlank(msgAttr.getUrl())) {
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到该url
                }
            });
        }
    }

}
