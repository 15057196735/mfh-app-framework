/*
 * 文件名称: ConfigItem.java
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
import java.util.List;

/**
 * 一个统一配置项Item
 * 
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-19
 * @since chch Framework 1.0
 */
public class ConfigItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8222807050492304821L;

    /** 配置项所属域 */
    private String domain = null;

    /** 配置项描述 */
    private String description = null;

    /** 配置项合并规则 */
    private String mergeRule = null;

    /** 默认配置路径 */
    private ConfigLocationItem defaultLocation = null;

    /** 所有扩展配置路径, 合并的顺序依赖于扩展配置路径定义的顺序以及合并规则. 
     * @see DefaultUConfig */
    private List<ConfigLocationItem> extendsLocations = null;

    /** 默认构造函数 */
    public ConfigItem() {
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ConfigItem{")
            .append("domain=").append(domain)
            .append(", description=").append(description)
            .append(", mergeRule=").append(mergeRule)
            .append(", defaultLocation=").append(defaultLocation)
            .append(", extendsLocation=").append(extendsLocations)
            .append('}');
        return sb.toString();
    }

    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMergeRule() {
        return mergeRule;
    }

    public void setMergeRule(String mergeRule) {
        this.mergeRule = mergeRule;
    }

    public ConfigLocationItem getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(ConfigLocationItem defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public List<ConfigLocationItem> getExtendsLocations() {
        return extendsLocations;
    }

    public void setExtendsLocations(List<ConfigLocationItem> extendsLocations) {
        this.extendsLocations = extendsLocations;
    }
    
    /**
     * 获取最后一个配置项
     * @return
     * @author zhangyz created on 2012-3-27
     */
    public ConfigLocationItem getLastConfigLocationItem(){
        if (extendsLocations != null && extendsLocations.size() > 0)
            return extendsLocations.get(extendsLocations.size() - 1);//原来是0
        else
            return defaultLocation;
    }
}
