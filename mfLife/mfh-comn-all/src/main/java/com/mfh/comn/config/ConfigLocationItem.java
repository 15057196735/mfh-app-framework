/*
 * 文件名称: ConfigLocationItem.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-19
 * 修改内容: 
 */
package com.mfh.comn.config;

import java.io.Serializable;

/**
 * 配置项位置Item
 * 
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-19
 * @since chch Framework 1.0
 */
public class ConfigLocationItem implements Serializable {
    public static String DOM4J = "dom4j";
    public static String W3C = "w3c";
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1257175606491933231L;

    private String type;
    
    private String parser;

    private String location;

    /** 默认构造函数 */
    public ConfigLocationItem() {
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ConfigLocationItem{")
            .append("type=").append(type)
            .append(", location=").append(location)
            .append('}');
        return sb.toString();
    }

    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //
    
    /**
     * 单配置文件类型：properties/xml等
     * @return
     * @author zhangyz created on 2013-6-15
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }
    
    /**
     * 是否采用dom4j解析器
     * @return
     * @author zhangyz created on 2012-3-21
     */
    public boolean isDom4j(){
        if (parser == null || parser.length() == 0)
            return false;
        else if (parser.equals(DOM4J))
            return true;
        else
            return false;                   
    }
}
