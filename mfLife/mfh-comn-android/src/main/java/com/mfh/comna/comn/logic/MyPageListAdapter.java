package com.mfh.comna.comn.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mfh.comn.bean.IIntId;
import com.mfh.comn.bean.ILongId;
import com.mfh.comn.bean.IObject;
import com.mfh.comna.comn.bean.KvBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于支持listView等的数据适配器，支持翻页的
 * 所有同步操作交由外面进行。
 * @author zhangyz created on 2013-4-14
 * @since Framework 1.0
 */
public class MyPageListAdapter<T extends IObject> extends BaseAdapter{
    protected List<KvBean<T>> dataItems = null;
    private IFillItemView<T> itemViewAdapter;
    
    public MyPageListAdapter(List<KvBean<T>> params, IFillItemView<T> itemViewAdapter) {
        super();
        this.dataItems = new ArrayList<KvBean<T>>();
        this.dataItems.addAll(params);
        this.itemViewAdapter = itemViewAdapter;
    }
    
    public MyPageListAdapter(IFillItemView<T> itemViewAdapter) {
        super();
        this.itemViewAdapter = itemViewAdapter;
    }

    /*@Override
    public int getCount2() {   
        return pageInfo.getTotalCount();   
    }        
    //第三种方法
    public int getCount3() {
    int totalCount = pageInfo.getTotalCount();
        int pageIndex = pageInfo.getPageIndex();            
        if (totalCount < pageSize) {// 如果总行数小于一页显示的行数，返回总行数
            return totalCount;
        }            
        else if (totalCount < pageIndex * pageSize) {// 即最后一页不足页面行数
            return (totalCount - (pageIndex - 1) * pageSize);
        }
        else{//其他情况返回页面尺寸            
            return pageSize;
        }
    }
    
    //根据总页数定位
    private IndiItemDefine getItemInfo2(int position) {
        int pageIndex = position / pageSize;
        if (pageIndex > pageInfo.getPageIndex()) {//再读取
            pageInfo.moveToNext();
            indiItems = indiService.queryIndiItems(pageInfo);
        }
        int factPosition = position % pageSize;
        IndiItemDefine define = indiItems.get(factPosition);
        return define;
    }*/

    /**
     * 在前面插入数据
     * @return 是否第一次加入
     * @param params
     */
    public boolean insertDataItems(List<KvBean<T>> params) {
        if (params == null)
            return false;
        if (dataItems == null) {
            dataItems = new ArrayList<KvBean<T>>();
            dataItems.addAll(params);
            return true;
        }
        else {
            dataItems.addAll(0, params);
            return false;
        }
    }

    /**
     * 在后面追加数据
     * @param params
     * @return 是否第一次加入
     * @author zhangyz created on 2013-5-3
     */
    public boolean addDataItems(List<KvBean<T>> params) {
        if (params == null)
            return false;
        if (dataItems == null) {
            dataItems = new ArrayList<KvBean<T>>();
            dataItems.addAll(params);
            return true;
        }
        else {
            dataItems.addAll(params);
            return false;
        }

    }
    
    /**
     * 覆盖已有数据
     * @param params
     * @author zhangyz created on 2013-5-3
     */
    public void setDataItems(List<KvBean<T>> params) {
        if (params == null)
            return;
        if (dataItems == null)
            dataItems = new ArrayList<KvBean<T>>();
        else {
            dataItems.clear();
        }
        dataItems.addAll(params);
    }

    /**
     * 清除数据
     * 
     * @author zhangyz created on 2013-5-3
     */
    public void clearData() {
        if (dataItems == null)
            return;
        dataItems.clear();
        dataItems = null;
    }
    
    public void addDataItem(KvBean<T> param) {
        if (param == null)
            return;
        if (dataItems == null)
            dataItems = new ArrayList<KvBean<T>>();
        dataItems.add(param);
    }
    
    public synchronized void addDataItem(KvBean<T>[] params) {
        if (params == null)
            return;
        if (dataItems == null)
            dataItems = new ArrayList<KvBean<T>>();
        for(KvBean<T> item : params){
            dataItems.add(item);
        }
    }
    
    public List<KvBean<T>> getDataItems() {
        return dataItems;
    }
    
    /**
     * 对其中的两个数据项交换位置
     * @param srcIndex 源位置
     * @param endIndex 目标位置
     * @author zhangyz created on 2013-5-3
     */
    public void exchangePos(int srcIndex, int endIndex) {
        KvBean<T> t1 = dataItems.get(srcIndex);
        KvBean<T> t2 = dataItems.get(endIndex);
        dataItems.set(srcIndex, t2);
        dataItems.set(endIndex, t1);
    }
    
    /**
     * 是否已经初始化过数据
     * @return
     * @author zhangyz created on 2013-4-14
     */
    public final boolean haveNoData() {
        if (dataItems == null){
            return true;
        }

        return false;
    }

    @Override
    public int getCount() {
        if (dataItems == null)
            return 0;
        return dataItems.size();
    }

    @Override
    public KvBean<T> getItem(int position) {
        if (dataItems == null || position >= dataItems.size())
            return null;
        return dataItems.get(position);
    }   
    
    /**
     * 判断列表的位置编号是否就作为对象的主键
     * @return
     * @author zhangyz created on 2013-5-20
     */
    public boolean posIsId() {
        Object obj = this.getItem(0);
        if (obj == null)
            return true;
        if (obj instanceof IIntId) {
            return false;
        }
        else if (obj instanceof ILongId) {
            return false;
        }
        else
            return true;
    }

    @Override
    public long getItemId(int position) {
        try {
            KvBean<T> obj = getItem(position);
            T bean = obj.getBean();
            if (obj == null || bean == null)
                return position;
            if (bean instanceof IIntId) {
                return ((IIntId)bean).getId();
            }
            else if (bean instanceof ILongId) {
                return ((ILongId)bean).getId();
            }
            else
                return position;
        }
        catch (Throwable ex) {
            return position;
        }
    }

    /**
     * 是否需要缓存list viewItem。可以加快显示速度。但如果不同的list item显示不一样，则不行。
     * @return
     * @author zhangyz created on 2013-5-12
     */
    protected boolean needCacheViewItem() {
        return true;
    }
    
    /**
     * 实现android原生适配器，绘制列表的每一行
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //框架一次性会连续调用显示一屏的行，剩下的在scroll进来的时候在调用。
        if (null == convertView || (needCacheViewItem() == false)) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    itemViewAdapter.getItemResLayoutId(position), parent, false);
        }
        
        KvBean<T> bean = this.getItem(position);
        convertView.setTag(bean);
        itemViewAdapter.fillListItemView(bean, convertView, position, parent);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return itemViewAdapter.isItemEnabled(position);
    }
}
