package com.mfh.comn.code;

import java.util.List;

/**
 * 树编码提供者
 * @deprecated
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public interface ITreeCodeHouse<T> extends ICodeHouse<T> {
    
    /**
     * 获取所有编码列表
     * @param parentCode 可以为空，若指定则查询该编码下的所有列表。
     * @return
     */
    public List<ITreeCodeItem<T>> getOptions(@SuppressWarnings("unchecked") T... parentCode);
    
}
