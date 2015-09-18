package com.mfh.comn.code;

/**
 * 普通的一个编码项
 * T: 编码值类型
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public interface ICodeItem<T> {
    public final static String DOMAIN_NULL = "\\null";//租户范围内所有    
    public final static String DOMAIN_NULL_CAPTION = "所有";
    
    /**
     * 获取编码，（复杂的编码可能是：层次分割,如：13_DOWN_0777)
     * @return
     * @author zhangyz created on 2012-5-29
     */
    public T getCode();
    
    /**
     * 获取编码描述
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public String getValue();
    
    /**
     * 是否空编码值
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public boolean isNullId();
    
    /**
     * 是否允许有子节点
     * @return
     * @author zhangyz created on 2014-6-21
     */
    public boolean hasChildAbility();
}
