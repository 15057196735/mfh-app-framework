/*
 * 文件名称: UConfigHelper.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-21
 * 修改内容: 
 */
package com.mfh.comna.comn.cfg;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.mfh.comn.config.ConfigClass;
import com.mfh.comn.config.ConfigItem;
import com.mfh.comn.config.ConfigLocationItem;
import com.mfh.comn.config.ConfigsParseHelper;
import com.mfh.comn.config.UConfig;

/**
 * 统一配置助手类, 非缓存的方式返回统一配置接口. <BR>
 * 使用示例:
 * 
 * <pre>
 * UConfig uc = UConfigHelper.getUConfig(&quot;SHK.DB.UPGRADE&quot;);
 * System.out.println(uc.getString(&quot;upgrade-config.domain&quot;));
 * System.out.println(uc.getString(&quot;upgrade-config(0).domain&quot;));
 * System.out.println(uc.getString(&quot;upgrade-config(1).description&quot;));
 * System.out.println(uc.getString(&quot;upgrade-config(2).versin&quot;));
 * 
 * System.out.println(uc.getList(&quot;upgrade-config.domain&quot;));
 * 
 * Document doc = uc.getMergeDocument();
 * NodeList nodeList = doc.getDocumentElement().getElementsByTagName(&quot;upgrade-config&quot;);
 * for (int i = 0; i &lt; nodeList.getLength(); i++) {
 *     System.out.println(W3cDomUtils.getStringValueByXPath(nodeList.item(i), &quot;domain&quot;));
 *     System.out.println(W3cDomUtils.getStringValueByXPath(nodeList.item(i), &quot;versin&quot;));
 *     System.out.println(W3cDomUtils.getStringValueByXPath(nodeList.item(i), &quot;datasource-id&quot;));
 * }
 * </pre>
 * 
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-21
 * @since chch Framework 1.0
 */
public final class UConfigHelper extends UConfigWrapper{
    private static UConfigHelper configCache = null;
    
    public UConfigHelper() {
        super();
    }
    
    public UConfigHelper(String configAlias) {
        super(configAlias);
    }

    /**
     * 直接获取app配置域
     * @return
     */
    public static UConfig getAppConfig() {
        return getConfig().getDomain(UConfig.CONFIG_APP);
    }
    
    public static UConfigHelper getConfig(){
        if (configCache == null) {
            synchronized (UConfigCache.class) {
                if (configCache == null)
                    configCache = new UConfigHelper();
            }
        }
        return configCache;
    }
    
    public static UConfigHelper getConfig(String configAlias){
        if (configCache == null) {
            synchronized (UConfigCache.class) {
                if (configCache == null) {
                    configCache = new UConfigHelper();
                    configCache.configAlias = configAlias;
                }
            }
        }
        return configCache;
    }
    
    /**
     * UConfig 助手类, 获取指定domian的UConfig配置;
     * 此接口无缓存；若需要缓存，调用UConfigCache类
     * @param domain 配置所属域
     * @return UConfig配置接口
     * @author LuoJingtian created on 2011-12-21
     * @since chch Framework 1.0
     */
    public UConfig getDomain(String domain) {
        ConfigItem configItem = ConfigsParseHelper.getConfig(domain, configAlias);
        ConfigClass configClass = ConfigsParseHelper.getConfigClass(configAlias);
        UConfig uConfigImpl;
        try {
            uConfigImpl = configClass.getUconfigClass().newInstance();
            uConfigImpl.init(configItem, configClass.getConfigDomainClass());
        }
        catch (Exception e) {
            throw new RuntimeException("构造统一配置对象失败:" + e.getMessage(), e);
        }
        return uConfigImpl;
    }
    
    /**
     * 获取配置项的配置路径，若存在多个，取最后一个
     * @param domain
     * @return
     * @author zhangyz created on 2012-3-27
     */
    public URL getUConfigFilePath(String domain){
        ConfigItem configItem = ConfigsParseHelper.getConfig(domain, configAlias);
        ConfigLocationItem item = configItem.getLastConfigLocationItem();
        if (item == null)
            return null;
        return getConfigUrl(item.getLocation());
    }
    
    public File getUConfigFile(String domain) {
        ConfigItem configItem = ConfigsParseHelper.getConfig(domain, configAlias);
        ConfigLocationItem item = configItem.getLastConfigLocationItem();
        if (item == null)
            return null;
        return getConfigFile(item.getLocation());
    }
    
    /**
     * 获取所有配置路径
     * @param domain
     * @return
     * @author zhangyz created on 2012-3-22
     */
    public URL[] getConfigLocations(String domain){
        ConfigItem configItem = ConfigsParseHelper.getConfig(domain, configAlias);
        ConfigLocationItem dc = configItem.getDefaultLocation();
        if (dc == null)
            return new URL[0];
        List<ConfigLocationItem> configs = configItem.getExtendsLocations();
        URL[] ret = new URL[1 + configs.size()];
        URL url = getConfigUrl(configItem.getDefaultLocation().getLocation());
        ret[0] = url;
        int index = 1;
        for (ConfigLocationItem item:configs){
            url = getConfigUrl(item.getLocation());
            ret[index ++] = url;
        }
        return ret;
    }
}
