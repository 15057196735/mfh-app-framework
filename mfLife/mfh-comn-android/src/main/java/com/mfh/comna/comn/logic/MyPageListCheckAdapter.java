package com.mfh.comna.comn.logic;

import java.util.ArrayList;
import java.util.List;
import com.mfh.comn.bean.IObject;
import com.mfh.comna.comn.bean.KvBean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.RadioButton;

/**
 * 支持选择框如多选框、单选框的数据适配器。
 * @param <T>
 * @author zhangyz created on 2013-4-14
 * @since Framework 1.0
 */
public class MyPageListCheckAdapter<T extends IObject> extends MyPageListAdapter<T> { 
    
    private int itemCheckResId;
    
    protected List<Integer> selectItemsList = new ArrayList<Integer>();//选中条目的ID（对有的类型可能就是位置）
    private Boolean isSingle = null;//是否单选
    private int oldSelectItemPos = -1;//单选模式下，需要记住上一选择
    
    public MyPageListCheckAdapter (List<KvBean<T>> dataItems, int itemCheckResId, IFillItemView<T> itemViewAdapter) {
        super(dataItems, itemViewAdapter);
        this.itemCheckResId = itemCheckResId;
    }
        
    public MyPageListCheckAdapter(int itemCheckResId, IFillItemView<T> itemViewAdapter) {
        super(itemViewAdapter);
        this.itemCheckResId = itemCheckResId;
    }

    /**
     * 获取选中的对象Id列表
     * @return
     * @author zhangyz created on 2013-5-20
     */
    public List<Integer> getSelectItemsList() {
        return selectItemsList;
    }    
    
    /**
     * 获取选中的对象列表
     * @return
     * @author zhangyz created on 2013-5-20
     */
    public List<KvBean<T>> getSelectObjectsList() {
        List<Integer> selIds = getSelectItemsList();
        List<KvBean<T>> ret = new ArrayList<KvBean<T>>();
        if (posIsId()) {
            for (int ii = 0; ii < selIds.size(); ii++) {
                ret.add(dataItems.get(selIds.get(ii)));
            }
        }
        else {
            int selId;
            for (int ii = 0; ii < selIds.size(); ii++) {
                selId = selIds.get(ii);
                for (int jj = 0; jj < dataItems.size(); jj++) {
                    if (dataItems.get(jj).getBean().getId().equals(selId)) {
                        ret.add(dataItems.get(jj));
                        break;
                    }
                }
            }
        }
        return ret;
    }
    
    /**
     * 预先增加一个条目编号
     * @param itemId
     * @author zhangyz created on 2013-4-14
     */
    public void addSelectItemId(int itemId) {
        selectItemsList.add(itemId);
    }
    
    public int getItemCheckResId() {
        return itemCheckResId;
    }
    
    @Override
    public void clearData() {
        super.clearData();
        selectItemsList.clear();
        oldSelectItemPos = -1;
    }
    
    /**
     * 是否为单选框
     * @return
     * @author zhangyz created on 2013-4-14
     */
    public boolean isSingle() {
        if (isSingle == null)
            return false;
        else
            return isSingle;
    }
    
    private int getOldSelectItemPos() {
        return oldSelectItemPos;
    }
    
    private void setOldSelectItemPos(int oldSelectItemPos) {
        this.oldSelectItemPos = oldSelectItemPos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        convertView = super.getView(position, convertView, parent);
        Checkable checkbox = (Checkable) convertView.findViewById(itemCheckResId);
        if (checkbox != null) {
            if (isSingle == null) {
                if (checkbox instanceof RadioButton) {
                    isSingle = Boolean.valueOf(true);//是单选
                }
                else
                    isSingle = Boolean.valueOf(false); //是多选
            }

            //checkbox.setTag(Integer.valueOf(position));

            if (selectItemsList != null && oldSelectItemPos == -1) {
                Integer itemId = Integer.valueOf((int)getItemId(position));
                boolean bSel = selectItemsList.contains(itemId);
                checkbox.setChecked(bSel);
                if (bSel && isSingle)
                    this.oldSelectItemPos = position;
            }
            else
                checkbox.setChecked(false);
        }
        
        return convertView;
    }
    
    /**
     * 响应list表的item点击事件
     * @param l 列表view
     * @param v 当前被点击的item
     * @param position 当前被点击item的位置。
     * @param id 被点击条目的id
     * @return  点击后是否处于被选中状态， true:是，false，否。
     */
    public boolean doItemClick(AdapterView<?> l, View v, int position, long id) {
        Checkable checkbox = (Checkable) v.findViewById(getItemCheckResId());      
        //Integer.valueOf((int)adapter.getItemId(position)); // Integer.valueOf(position);  
        Integer itemId = (int)id; 
        List<Integer> list = getSelectItemsList();
        
        if (list.contains(itemId)) {
            if (isSingle()) {
                return true; //重复选择
            }
            else {
                if (checkbox != null)
                    checkbox.setChecked(false);
                list.remove(itemId);
                return false;
            }
        }
        else {
            if (isSingle()) {
                if (list.size() > 0) {
                    int prePos = getOldSelectItemPos();
                    View itemView = l.getChildAt(prePos);
                    if (itemView != null) {
                        Checkable oldItem = (Checkable)(itemView.findViewById(getItemCheckResId()));
                        oldItem.setChecked(false);
                    }
                    else {
                        System.out.println("不存在的位置" + prePos);
                    }
                    list.clear();
                }
                if (checkbox != null)
                    checkbox.setChecked(true);
                setOldSelectItemPos(position);
            }
            else if (checkbox != null)
                checkbox.setChecked(true);
            list.add(itemId);
            return true;
        }
    }
}
