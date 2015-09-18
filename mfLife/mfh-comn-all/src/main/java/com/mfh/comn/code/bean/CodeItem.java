package com.mfh.comn.code.bean;

import com.mfh.comn.code.ICodeItem;

/**
 * 一个普通编码项
 * 
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public class CodeItem<T> implements ICodeItem<T> {
    protected T code;
    protected String value;

    /**
     * 是否全数据域
     * @param id
     * @return
     * @author zhangyz created on 2015-2-26
     */
    public static boolean isNullId(Object id){
        return (id == null) || id.equals(ICodeItem.DOMAIN_NULL);// || id.equals(ICodeItem.DOMAIN_NULL_ALL)
    }
    
    public CodeItem() {
        super();
    }

    /**
     * 构造函数
     * @param id 编码值
     * @param name 编码描述
     */
    public CodeItem(T code, String value) {
        super();
        this.code = code;
        this.value = value;
    }

    /**
     * @return Returns the id.
     */
    public T getCode() {
        return code;
    }

    /**
     * 
     * @param id The id to set.
     */
    public void setId(T code) {
        this.code = code;
    }

    /**
     * @return Returns the name.
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String value) {
        this.value = value;
    }

    @Override
    public boolean isNullId() {
        return CodeItem.isNullId(code);
    }
    
    @Override
    public boolean hasChildAbility() {
        return false;
    }
}
