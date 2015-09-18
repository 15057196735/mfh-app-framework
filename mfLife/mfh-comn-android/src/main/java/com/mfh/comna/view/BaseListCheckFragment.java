package com.mfh.comna.view;

import android.view.View;
import android.widget.AdapterView;

import com.mfh.comn.bean.IObject;
import com.mfh.comna.comn.logic.MyPageListCheckAdapter;

/**
 * 支持包含checkbox的listView基类。子类实现的数据适配器必须从MyPageListAdapter类继承。
 * @param <T>
 * @author zhangyz created on 2013-4-16
 * @since Framework 1.0
 */
public abstract class BaseListCheckFragment <T extends IObject> extends BaseListFragment<T> {    
    public BaseListCheckFragment() {
        super();
    }

    /**
     * 获取checkbox的资源id
     * @return
     * @author zhangyz created on 2013-4-16
     */
    protected abstract int getItemCheckResId ();
    
    @Override
    protected MyPageListCheckAdapter<T> createAdapter() {
        return new MyPageListCheckAdapter<T>(getItemCheckResId(), this);
    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        MyPageListCheckAdapter<T> adapter = (MyPageListCheckAdapter<T>)mAdapter; 
        adapter.doItemClick(l, v, position, id);
    }
    
    /**
     * 获取数据适配器
     */
    public MyPageListCheckAdapter<T> getCheckAdapter() {
        return (MyPageListCheckAdapter<T>) (mAdapter);
    }
}
