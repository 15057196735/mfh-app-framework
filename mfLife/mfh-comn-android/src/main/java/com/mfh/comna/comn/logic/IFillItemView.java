package com.mfh.comna.comn.logic;

import com.mfh.comna.comn.bean.KvBean;
import android.view.View;
import android.view.ViewGroup;

/**
 * listView的行条目view填充接口
 * 
 * @author zhangyz created on 2013-4-14
 * @since Framework 1.0
 */
public interface IFillItemView<T> {    
    /**
     * 获取指定条目位置使用的layout资源Id（一般情况下所有条目使用的layout都一样，但允许不一样）
     * @param position 条目位置（据此位置可以从adapter中获取对应的bean，再作进一步判断采用何种layout）
     * @return layout资源Id
     * @author zhangyz created on 2014-3-11
     */
    public int getItemResLayoutId(int position);
    
    /**
     * 填充listView的一行
     * @param kvBean 待显示的bean
     * @param listItemView 该行view的root,其tag已经设置了bean
     * @param position 是第几行条目，备用
     * @param parent listView
     * @author zhangyz created on 2013-4-14
     */
    public void fillListItemView(KvBean<T> kvBean, View listItemView, int position, ViewGroup parent);

    /**
     * 确定某一项是否有效
     * @param position
     * @return
     * @author shicy on 2014-3-24
     */
    public boolean isItemEnabled(int position);

}
