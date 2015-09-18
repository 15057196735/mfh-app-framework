package com.mfh.comn.bean;

import java.io.Serializable;

/**
 * 对象接口
 * 要求所有对象都有getId和setId接口，其中参数是一个对象，一般为字符串，但当复合主键时也是一个对象。
 * @author zhangyz created on 2012-7-1
 * @since Framework 1.0
 */
public interface IObject<T> extends Serializable{    

    public T getId();
    
    //public void setId(T id);
}
