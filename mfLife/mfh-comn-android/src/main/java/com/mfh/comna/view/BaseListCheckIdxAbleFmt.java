package com.mfh.comna.view;

import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import com.mfh.comna.comn.bean.ILetterIndexAble;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.logic.LetterPageCheckAdapter;
import com.mfh.comna.comn.logic.MyPageListAdapter;
import com.mfh.comna.comn.logic.MyPageListCheckAdapter;
/**
 * 支持通过字母索引的列表Fragment，并且每个条目可以带有check。
 * @param <T>
 * @author zhangyz created on 2013-5-13
 * @since Framework 1.0
 */
public abstract class BaseListCheckIdxAbleFmt<T extends ILetterIndexAble>  extends BaseListIdxAbleFmt <T>{
    
    /**
     * 获取checkbox的资源id
     * @return
     * @author zhangyz created on 2013-4-16
     */
    protected abstract int getItemCheckResId ();    
    
    @Override
    protected MyPageListAdapter<T> createAdapter() {
        return new LetterPageCheckAdapter<T>(getItemCheckResId(), this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        MyPageListCheckAdapter<T> adapter = (MyPageListCheckAdapter<T>)mAdapter; 
        adapter.doItemClick(l, v, position, id);
    }
    
    /**
     * 获取数据适配器
     */
    public LetterPageCheckAdapter<T> getCheckAdapter() {
        return (LetterPageCheckAdapter<T>) (mAdapter);
    }
    
    /**
     * 获取选中的对象列表
     * @return
     * @author zhangyz created on 2013-5-20
     */
    public List<KvBean<T>> getSelectObjectsList() {
        return this.getCheckAdapter().getSelectObjectsList();
    }
    
    /**
     * 获取选中的对象Id列表
     * @return
     * @author zhangyz created on 2013-5-20
     */
    public List<Integer> getSelectItemsList() {
        return this.getCheckAdapter().getSelectItemsList();
    }
}
