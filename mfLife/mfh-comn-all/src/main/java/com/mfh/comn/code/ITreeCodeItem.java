package com.mfh.comn.code;

import java.util.List;

/**
 * 支持树形能力的编码
 * T: 编码值类型
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public interface ITreeCodeItem<T> extends ICodeValueItem<T> {    
    
    
    public T getPid();
    
    public void setPid(T pid);
    
    /**
     * 是否还有子编码项
     * @return
     * @author zhangyz created on 2014-6-20
     */
    public boolean hasChildFact();
    
    /**
     * 设置具有子编码
     * 
     * @author zhangyz created on 2014-6-21
     */
    public void notifyHaveChild();
    
    /**
     * 设置没有子编码
     * 
     * @author zhangyz created on 2014-6-21
     */
    public void notifyNoChild();
    
    /**
     * 获取子节点
     * @return
     * @author zhangyz created on 2014-6-21
     */
    public List<ICodeItem<T>> getChildItems();
}
