/*
 * 文件名称: EntityWrapper.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-5
 * 修改内容: 
 */
package com.mfh.comn.bean;

import java.util.HashMap;
import java.util.Map;

import com.mfh.comn.net.data.IResponseData;

/**
 * 针对原始bean的包装，增加某些属性的caption内容，以便于客户端展现
 * @author zhangyz created on 2014-3-5
 */
@SuppressWarnings("serial")
public class EntityWrapper <T> implements java.io.Serializable, IResponseData{
    private T bean;
    private Map<String, String> caption;
    
    public EntityWrapper(T bean) {
        super();
        this.bean = bean;        
    }
    
    public EntityWrapper(T bean, Map<String, String> caption) {
        super();
        this.bean = bean;
        this.caption = caption;
    }

    /**
     * 增加一个字段值的描述值
     * @param fieldName 字段名
     * @param valCaption 字段值的描述值
     */
    public void addCaption(String fieldName, String valCaption) {
        if (caption == null) {
            caption = new HashMap<>();
        }
        caption.put(fieldName, valCaption);
    }    
    
    /**
     * 获取属性的描述值（部分属性有）
     * @return key：属性名；value:属性值字符串
     * @author zhangyz created on 2014-3-12
     */
    public Map<String, String> getCaption() {
        return caption;
    }

    /**
     * 获取某个属性值对应的描述值,若不存在返回空
     * @param propName
     * @return
     */
    public String getPropCaption(String propName) {
        if (caption == null)
            return propName;
        else
            return caption.get(propName);
    }

    /**
     * 设置属性的描述值（部分属性有）
     * @param map<String,String>  key：属性名；value:属性值字符串
     * @author zhangyz created on 2014-3-12
     */
    public void setCaption(Map<String, String> caption) {
        this.caption = caption;
    }
    
    /**
     * 获取实际的bean值
     * @return
     * @author zhangyz created on 2014-3-12
     */
    public T getBean() {
        return bean;
    }
}
