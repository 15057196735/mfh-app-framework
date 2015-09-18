package com.mfh.comna.bizz.material.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfh.comna.bizz.material.ResourceUtil;
import com.mfh.comna.bizz.material.entity.MsgAttr;
import com.mfh.comna.R;
import com.mfh.comna.view.img.FineImgView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 图文消息块展现view
 * Created by Shicy on 14-4-25.
 */
public class MaterialTwItemView extends FrameLayout {
    private List<MsgAttr> msgAttrs;
    private View topView;
    private LinearLayout normalListView;

    public MaterialTwItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public List<MsgAttr> getMsgAttrs() {
        return msgAttrs;
    }

    public void setMsgAttrs(List<MsgAttr> msgAttrs) {
        this.msgAttrs = msgAttrs;
        fillView();
    }

    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.subview_material_tw, this, true);
        topView = view.findViewById(R.id.mat_tw_topItemView);
        normalListView = (LinearLayout)view.findViewById(R.id.mat_tw_normalItemList);
    }

    private void fillView() {
        //List<View> subViews = new ArrayList<View>()();
        normalListView.removeAllViews();
        for (int ii = 0; ii < msgAttrs.size(); ii++) {
            if (ii == 0) {
                final MsgAttr attr = msgAttrs.get(0);
                TextView view = (TextView)topView.findViewById(R.id.mat_tw_topItemText);
                //显示标题，
                if (msgAttrs.size() > 0) {
                    //只显示标题
                    view.setText(attr.getTitle());
                }
                else {
                    //还需要显示标题,目前没有位置
                    //view.setText(attr.getTitle());
                    //显示摘要
                    view.setText(attr.getDescription());
                }
                //显示图片
                FineImgView img = (FineImgView)topView.findViewById(R.id.material_top_image_view);
                //img.setNeedSample(true);
                //img.setMaxWidth(ImageUtil.getScreenWidth((Activity) this.getContext()));
                img.setFao(ResourceUtil.getMatImgFao());
                img.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
                img.setSrc(attr.getPicurl());

                if (StringUtils.isNotBlank(attr.getUrl())) {
                    img.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(attr.getUrl());
                            intent.setData(content_url);
                            getContext().startActivity(intent);
                        }
                    });
                }
            }
            else {
                MsgAttr attr = msgAttrs.get(ii);
                MaterialTwSubItemView view = new MaterialTwSubItemView(this.getContext(), null);
                view.setMsgAttr(attr);
                //subViews.add(view);
                normalListView.addView(view);
            }
        }
    }

}
