package com.mfh.comn.code.impl;

import com.mfh.comn.code.ICodeHouse;
/**
 * 直接树编码管理器 
 * @deprecated
 * @author zhangyz created on 2012-4-6
 * @since Framework 1.0
 */
public class DirectTreeHouse<T> extends DirectCodeHouse<T> implements ICodeHouse<T> {
    
    public static <T> DirectTreeHouse<T> getRef(){
        return new DirectTreeHouse<T>();
    }
    
}
