package com.mfh.comn.net.data;

/**
 * 存放单值对象
 * @param <T>
 * @author zhangyz created on 2014-3-10
 */
public class RspValue<T> implements IResponseData{
    private T value;
    
    public RspValue(T value) {
        super();
        this.value = value;
    }

    public T getValue() {
        if (value == null)
            return null;
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }    
}
