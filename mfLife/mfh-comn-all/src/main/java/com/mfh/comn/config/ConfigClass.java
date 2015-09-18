package com.mfh.comn.config;

import java.util.Map;

/**
 * 关于统一配置项的相关实现类
 * 
 * @author zhangyz created on 2013-5-26
 * @since Framework 1.0
 */
public class ConfigClass {    
    private Map<String, Class<SingleConfiguration>> configDomainClass;//统一配置内每种单配置实现类
    private Class<UConfig> uconfigClass;//统一配置实现类
    
    public Map<String, Class<SingleConfiguration>> getConfigDomainClass() {
        return configDomainClass;
    }
    
    public void setConfigDomainClass(Map<String, Class<SingleConfiguration>> configDomainClass) {
        this.configDomainClass = configDomainClass;
    }
    
    public Class<UConfig> getUconfigClass() {
        return uconfigClass;
    }
    
    public void setUconfigClass(Class<UConfig> uconfigClass) {
        this.uconfigClass = uconfigClass;
    }
}
