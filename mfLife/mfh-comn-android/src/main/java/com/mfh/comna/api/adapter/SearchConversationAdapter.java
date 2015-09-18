package com.mfh.comna.api.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfh.comna.bizz.msg.entity.EmbSession;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.view.img.FineImgView;


/**
 * 会话
 * Created by Administrator on 2015/4/20.
 */
public class SearchConversationAdapter extends ListBaseAdapter<EmbSession> {

    static class ViewHolder {
        FineImgView ivHeader;
        TextView tvName;
        TextView tvMessage;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.chat_list_item, null);
            viewHolder.ivHeader = (FineImgView) convertView.findViewById(R.id.ms_headImg);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.ms_humanName);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.ms_lastMsgContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        KvBean<EmbSession> entity = dataItems.get(position);

        //显示头像
        viewHolder.ivHeader.setSrc(entity.getBean().getHeadimageurl());
        viewHolder.tvName.setText(entity.getBean().getHumanname());
        viewHolder.tvMessage.setText(entity.getBean().getHumanname());
        return convertView;
    }

}
