package com.mfh.comna.comn.logic;

import java.util.List;
import java.util.Map;
import com.mfh.comna.comn.bean.ILetterIndexAble;
import com.mfh.comna.comn.bean.KvBean;

/**
 * 支持按字母索引的且支持条目能够被单选或多选的适配器
 * @param <T>
 * @author zhangyz created on 2013-5-13
 * @since Framework 1.0
 */
public class LetterPageCheckAdapter<T extends ILetterIndexAble> 
        extends MyPageListCheckAdapter<T> implements IAdapterLetterIndexAble{

    private LetterPageHelper<T> lpHelper;    
    
    public LetterPageCheckAdapter(List<KvBean<T>> dataItems, int itemCheckResId, 
            IFillItemView<T> itemViewAdapter) {
        super(dataItems, itemCheckResId, itemViewAdapter);
        lpHelper = new LetterPageHelper<T>(this, dataItems, null);
    }
    
    public LetterPageCheckAdapter(List<KvBean<T>> dataItems, int itemCheckResId, 
            IFillItemView<T> itemViewAdapter, String[] paramLetters) {
        super(dataItems, itemCheckResId, itemViewAdapter);
        lpHelper = new LetterPageHelper<T>(this, dataItems, paramLetters);
    }

    public LetterPageCheckAdapter(int itemCheckResId, IFillItemView<T> itemViewAdapter) {
        super(itemCheckResId, itemViewAdapter);
        lpHelper = new LetterPageHelper<T>(this, null, null);
    }

    public LetterPageCheckAdapter(int itemCheckResId, IFillItemView<T> itemViewAdapter, String[] paramLetters) {
        super(itemCheckResId, itemViewAdapter);
        lpHelper = new LetterPageHelper<T>(this, null, paramLetters);
    }

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
