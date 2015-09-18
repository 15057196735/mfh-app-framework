/*
 * 文件名称: UpgradeConfigInfo.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-22
 * 修改内容: 
 */
package com.mfh.comn.upgrade;

import java.io.Serializable;

/**
 * 升级配置信息
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-22
 * @since SHK BMP 1.0
 */
public class UpgradeConfigInfo implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 4105326189854599216L;

    /** 升级的域, 如: 公共平台SHK.COMMON.DB.UPGRADE */
    private String domain;
    
    /** 升级描述信息 */
    private String description;
    
    /** 升级版本 */
    private int versin;
    
    /** 升级脚本文件路径 */
    private String scriptFilePath;
    
    /** 升级脚本文件名前缀 */
    private String scriptFilePrefix;
    
    /** 升级目标数据源标识: 对应Spring配置applicationContext.xml数据源Bean Id */
    private String dataSourceId;
    
    private String className;//升级实现类，可空，则使用默认实现类
    
    /** 默认构造函数 */
    public UpgradeConfigInfo() {
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("UpgradeConfigInfo{")
            .append("domain=").append(domain)
            .append(", description=").append(description)
            .append(", versin=").append(versin)
            .append(", scriptFilePath=").append(scriptFilePath)
            .append(", scriptFilePrefix=").append(scriptFilePrefix)
            .append(", dataSourceId=").append(dataSourceId)
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

    public int getVersin() {
        return versin;
    }

    public void setVersin(int versin) {
        this.versin = versin;
    }

    public String getScriptFilePath() {
        return scriptFilePath;
    }

    public void setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }

    public String getScriptFilePrefix() {
        return scriptFilePrefix;
    }

    public void setScriptFilePrefix(String scriptFilePrefix) {
        this.scriptFilePrefix = scriptFilePrefix;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    
    public String getClassName() {
        return className;
    }

    
    public void setClassName(String className) {
        this.className = className;
    }
}
