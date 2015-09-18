package com.mfh.comna.view;

import com.mfh.comn.bean.IObject;
import com.mfh.comna.comn.logic.MyPageListCheckAdapter;
import android.view.View;
import android.widget.AbsListView;

/**
 * 支持包含checkbox的listView基类。子类实现的数据适配器必须从MyPageListAdapter类继承。
 * @param <T>
 * @author zhangyz created on 2013-4-14
 * @since Framework 1.0
 */
public abstract class BaseListCheckActivity<T extends IObject> extends BaseListActivity<T> {
    protected abstract int getItemCheckResId ();

    @Override
    protected void createAdapter() {        
        mAdapter = new MyPageListCheckAdapter<T>(getItemCheckResId(), this);
    }
    
    @Override
    protected void onListItemClick(AbsListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        MyPageListCheckAdapter<T> adapter = (MyPageListCheckAdapter<T>)mAdapter; 
        adapter.doItemClick(l, v, position, id);
    }
}
