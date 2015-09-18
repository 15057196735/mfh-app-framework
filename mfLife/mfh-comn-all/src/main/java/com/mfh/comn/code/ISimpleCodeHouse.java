package com.mfh.comn.code;

import java.util.List;

/**
 * 简单编码库提供者
 * 
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public interface ISimpleCodeHouse<T> extends ICodeDomainHouse<T>{

    /**
     * 获取指定父编码下的所有编码列表
     * @param parentCode 可以为空，若指定则查询该编码下的所有列表。
     * @return
     * @author zhangyz created on 2014-3-17
     */
    public List<ICodeItem<T>> getOptions(T parentCode);
    
    /**
     * 获取顶层编码列表
     * @return
     * @author zhangyz created on 2014-3-17
     */
    public List<ICodeItem<T>> getOptions();
}
