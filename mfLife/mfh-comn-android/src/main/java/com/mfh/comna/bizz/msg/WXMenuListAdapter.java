package com.mfh.comna.bizz.msg;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mfh.comna.R;
import com.mfh.comna.bizz.msg.wx.WXMenuData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/20.
 */
public class WXMenuListAdapter extends BaseAdapter {

    private Context context;
    List<WXMenuData> data = new ArrayList<WXMenuData>();

    private int curSelectedId = 0;

    static class ViewHolder {
        TextView tvTitle;
    }

    public WXMenuListAdapter(Context context) {
        super();
        this.context = context;
        this.curSelectedId = 0;
    }

    public WXMenuListAdapter(Context context, List<WXMenuData> data) {
        super();
        this.context = context;
        this.data = data;
        this.curSelectedId = 0;
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
            view = View.inflate(context, R.layout.listview_item_popup_wx, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        WXMenuData menuData = data.get(i);
        viewHolder.tvTitle.setText(menuData.getName());

//        if (curSelectedId == i){
//            viewHolder.markerLeft.setVisibility(View.VISIBLE);
//            viewHolder.markerRight.setVisibility(View.INVISIBLE);
//            view.setBackgroundColor(context.getResources().getColor(R.color.listitem_white));
////            viewHolder.tvTitle.setTextColor(Color.parseColor("#ffff0000"));
//            viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.category_selected));
//        }else{
//            viewHolder.markerLeft.setVisibility(View.INVISIBLE);
//            viewHolder.markerRight.setVisibility(View.VISIBLE);
//            view.setBackgroundColor(context.getResources().getColor(R.color.listitem_gray));
////            viewHolder.tvTitle.setTextColor(Color.parseColor("#ffffffff"));
//            viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.category_normal));
//        }

        return view;
    }

    public  void setSelectId(int selectId) {
        this.curSelectedId = selectId;
    }

}
