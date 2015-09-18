package com.mfh.comna.api.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfh.comna.api.bean.SharePopupData;
import com.mfh.comna.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/20.
 */
public class SharePopupAdapter extends BaseAdapter {

    private Context context;
    List<SharePopupData> data = new ArrayList<SharePopupData>();

    static class ViewHolder {
        ImageView ivHeader;
        TextView tvTitle;
    }

    public SharePopupAdapter(Context context) {
        super();
        this.context = context;
    }

    public SharePopupAdapter(Context context, List<SharePopupData> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.listview_item_popup_share, null);
            viewHolder.ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        SharePopupData dataItem = data.get(i);
        viewHolder.ivHeader.setImageResource(dataItem.getResId());
        viewHolder.tvTitle.setText(dataItem.getName());

        return view;
    }
}
