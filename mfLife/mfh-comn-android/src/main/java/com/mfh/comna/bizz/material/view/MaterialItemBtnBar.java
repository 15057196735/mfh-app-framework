package com.mfh.comna.bizz.material.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.mfh.comna.R;
import com.mfh.comna.bizz.msg.MsgConstants;

/**
 * Created by Shicy on 14-4-25.
 */
public class MaterialItemBtnBar extends FrameLayout implements View.OnClickListener {
    private View sendBtn;
    private View deleteBtn;
    private OnClickListener listener;
    private Object tag;

    public MaterialItemBtnBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /*public void setListener(OnClickListener listener) {
        this.listener = listener;
        if (listener != null) {
            if (sendBtn != null)
                sendBtn.setOnClickListener(listener);
            if (deleteBtn != null)
                deleteBtn.setOnClickListener(listener);
        }
    }*/

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.subview_material_btnbar, this, true);
        sendBtn = view.findViewById(R.id.mat_msgSendBtn);
       // deleteBtn = view.findViewById(R.id.mat_msgDeleteBtn);
        sendBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Activity activity = (Activity)this.getContext();
        if (v.getId() == R.id.mat_msgSendBtn) {
            Intent intent = new Intent(MsgConstants.ACTION_SEND_MSG);
            Object tag = getTag();
            if (tag instanceof  String) {
                intent.putExtra("content", (String)tag);
            }
            else {
                intent.putExtra("content", Long.parseLong(tag.toString()));
            }
            activity.sendBroadcast(intent);
            activity.setResult(MsgConstants.CODE_REQUEST_CYY, intent);
            activity.finish();
        }
        /*else if (v.getId() == R.id.mat_msgDeleteBtn) {
            DialogUtil.showHint(activity, "暂不支持!");
        }*/
    }
}
