package com.mfh.comna.comn.logic;

import java.util.List;
import java.util.Map;
import com.mfh.comna.comn.bean.ILetterIndexAble;
import com.mfh.comna.comn.bean.KvBean;

/**
 * 支持按字母索引的适配器
 * @param <T>
 * @author zhangyz created on 2013-5-13
 * @since Framework 1.0
 */
public class LetterPageAdapter<T extends ILetterIndexAble> extends MyPageListAdapter<T> implements IAdapterLetterIndexAble{
    private LetterPageHelper<T> lpHelper;    
    public LetterPageAdapter(IFillItemView<T> itemViewAdapter) {
        super(itemViewAdapter);
        lpHelper = new LetterPageHelper<T>(this, null, null);
    }
    
    public LetterPageAdapter(IFillItemView<T> itemViewAdapter, String[] paramLetters) {
        super(itemViewAdapter);
        lpHelper = new LetterPageHelper<T>(this, null, paramLetters);
    }    
    
    public LetterPageAdapter(List<KvBean<T>> params, IFillItemView<T> itemViewAdapter) {
        super(params, itemViewAdapter);            
        lpHelper = new LetterPageHelper<T>(this, params, null);
    }
    
    public LetterPageAdapter(List<KvBean<T>> params, IFillItemView<T> itemViewAdapter, String[] paramLetters) {
        super(params, itemViewAdapter);            
        lpHelper = new LetterPageHelper<T>(this, params, paramLetters);
    }

    @Override
    public Map<String, Integer> getSelector() {
        return lpHelper.getSelector();
    }
    
    @Override
    public synchronized boolean addDataItems(List<KvBean<T>> params) {
        boolean ret = super.addDataItems(params);
        lpHelper.addDataItems(params);
        return ret;
    }
    
    @Override
    public synchronized void setDataItems(List<KvBean<T>> params) {
        super.setDataItems(params);
        lpHelper.addDataItems(params);
    }

    @Override
    public void addDataItem(KvBean<T> param) {
        super.addDataItem(param);
        lpHelper.addDataItem(param);
    }

    @Override
    public void clearData() {
        super.clearData();
        lpHelper.clearData();
    }

    @Override
    public synchronized void addDataItem(KvBean<T>[] params) {
        super.addDataItem(params);
        lpHelper.addDataItems(params);
    }
    
    @Override
    protected boolean needCacheViewItem() {
        return false;
    }
}
