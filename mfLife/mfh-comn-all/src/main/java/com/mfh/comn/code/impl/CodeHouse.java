/*
 * 文件名称: DomainRef.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-19
 * 修改内容: 
 */
package com.mfh.comn.code.impl;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.mfh.comn.code.ICodeDomainHouse;
import com.mfh.comn.code.ICodeItem;
import com.mfh.comn.code.ITreeCodeItem;
import com.mfh.comn.code.bean.ParentChildItem;

/**
 * 对编码提供者值域的包装,可能包括直接的数据或引用编码集、表内隐含编码集,此时以url形式提供出来
 * 
 * @author zhangyz created on 2012-4-1
 * @since Framework 1.0
 */
public abstract class CodeHouse<T> implements ICodeDomainHouse<T> {

    protected String parent = null;
    
    /**
     * 是否支持多层深度
     * @return
     * @author zhangyz created on 2012-4-29
     */
    public abstract boolean isTreeAble();
    
    public String getParent() {
        return parent;
    }
    
    public void setParent(String parent) {
        this.parent = parent;
    }
    
    /**
     * 清理编码
     * 
     * @author zhangyz created on 2013-6-7
     */
    protected abstract void clearCodes();
    
    /**
     * 
     * 返回json格式
     * @return
     * @author zhangyz created on 2012-4-1
     */
    public abstract JSONObject getJsonObject();
    
    
    /**
     * 获取内存对象数据
     * @return
     * @author zhangyz created on 2012-4-11
     */
    public abstract List<ICodeItem<T>> getOptions();
    
    /**
     * 获取子节点个数
     * @return
     * @author zhangyz created on 2014-7-7
     */
    public int getOptionSize() {
        List<ICodeItem<T>> ops = getOptions();
        if (ops == null)
            return 0;
        return ops.size();
    }
    
    /**
     * 克隆节点列表，并设置每个节点还具有子节点标志
     * @return
     * @author zhangyz created on 2012-4-12
     */
    public List<ParentChildItem<T>> cloneOptionsAndHintChild(){
        return cloneOptionsAndHintChild(this.getOptions());
    }
    
    /**
     * 克隆节点列表，并设置每个节点还具有子节点标志
     * @return
     * @author zhangyz created on 2012-4-12
     */
    public static <T>  List<ParentChildItem<T>> cloneOptionsAndHintChild(List<ICodeItem<T>> curOptions){
        if (curOptions == null)
            return null;
        
        List<ParentChildItem<T> > options = new ArrayList<ParentChildItem<T> >();
        ParentChildItem<T>  option;
        for (int ii = 0; ii < curOptions.size(); ii++){
            option = ((ParentChildItem<T>)curOptions.get(ii)).clone();
            option.notifyHaveChild();
            options.add(option);
        }
        return options;
    }
    
    /**
     * 设置每个节点还具有子节点标志,并返回自己
     * @return
     * @author zhangyz created on 2012-4-12
     */
    public static <T> List<ICodeItem<T>> hintChild(List<ICodeItem<T>> curOptions){
        if (curOptions == null)
            return null;
        ITreeCodeItem<T> option;
        for (int ii = 0; ii < curOptions.size(); ii++){
            option = (ITreeCodeItem<T>)curOptions.get(ii);
            option.notifyHaveChild();
        }
        return curOptions;
    }
}
