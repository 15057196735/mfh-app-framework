package com.mfh.comn.code.impl;

import com.mfh.comn.code.ITreeCodeItem;


@SuppressWarnings("serial")
public class DomanOkCache<ParentItem> extends java.util.HashSet<ParentItem>{
    /**
     * 通知本对象没有数据域验证的情况
     * 
     * @author zhangyz created on 2012-7-7
     */
    public void notifyNoDomainValidate(){
        this.add(null);
    }
    
    /**
     * 通知本对象有数据域验证的情况
     * 
     * @author zhangyz created on 2012-7-7
     */
    public void notifyHaveDomainValidate(){
        this.remove(null);
    }
    
    /**
     * 判断本对象是否没有数据域验证的情况
     * @return
     * @author zhangyz created on 2012-7-7
     */
    public boolean isNoDomainValidate(){
        return this.contains(null);
    }
    
    /**
     * 获取某个编码的可访问性
     * @param e
     * @return
     * @author zhangyz created on 2012-7-8
     */
    public int  getAccessFlag(ParentItem e){
        if (isNoDomainValidate())
            return ITreeCodeItem.NODE_VIEW_OK;
        if (this.contains(e))
            return ITreeCodeItem.NODE_VIEW_OK;
        else
            return ITreeCodeItem.NODE_VIEW_CONTINUE;
    }
    
    @Override
    public boolean add(ParentItem e){
        if (e != null){
            notifyHaveDomainValidate();
        }
        return super.add(e);
    }
    
}
