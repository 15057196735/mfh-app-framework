package com.mfh.comna.bizz.msg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.mfh.comna.R;

/**
 * 小伙伴·对话
 * Created by Administrator on 2015/5/10.
 */
public class ConversationHeaderView extends RelativeLayout{

    private View rootView;
    private RelativeLayout[] rlGroups;

    public ConversationHeaderView(Context context) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.listview_header_conversation, this, true);
        this.initAndSetUpView();
    }

    public ConversationHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = LayoutInflater.from(context).inflate(R.layout.listview_header_conversation, this, true);
        this.initAndSetUpView();
    }

    public ConversationHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        rootView = LayoutInflater.from(context).inflate(R.layout.listview_header_conversation, this, true);
        this.initAndSetUpView();
    }

    public interface ConversationHeaderViewListerner{
        void onItemClicked(int i);
    }
    private ConversationHeaderViewListerner listener;
    public void setListener(ConversationHeaderViewListerner listener){
        this.listener = listener;
    }


    private void initAndSetUpView() {
        rlGroups = new RelativeLayout[2];
        rlGroups[0] = (RelativeLayout) rootView.findViewById(R.id.group_item_1);
        rlGroups[1] = (RelativeLayout) rootView.findViewById(R.id.group_item_2);
        rlGroups[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClicked(0);
                }
            }
        });
        rlGroups[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClicked(1);
                }
            }
        });

    }

}
