/*
 * 文件名称: DefaultUConfig.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-19
 * 修改内容: 
 */
package com.mfh.comna.comn.cfg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import com.mfh.comn.config.ConfigItem;
import com.mfh.comn.config.ConfigLocationItem;
import com.mfh.comn.config.IConfiguration;
import com.mfh.comn.config.SingleConfiguration;
import com.mfh.comn.config.UConfig;
import com.mfh.comn.utils.W3cDomUtils;

/**
 * 默认统一配置实现.
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-19
 * @since chch Framework 1.0
 */
public class DefaultUConfig implements UConfig {    
    
    /** 统一配置项信息 */
    private ConfigItem configItem;
    
    /** 
     * 合并规则 
     * @see {@link com.mfh.comn.config.UConfig.MergeRule }
     * @see {@link com.mfh.comn.config.UConfig.MergeRuleName }
     */
    private int mergeRule;
    
    /** 当指定合并规则为:merge时, 可以返回合并后的Document */
    private Document mergeDoc;
    
    /** 日志记录器 */
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultUConfig.class);
   
    
    /** 统一配置项 */
    private List<SingleConfiguration> compositeConfig = new ArrayList<SingleConfiguration>();
    
    private boolean isNotEmpty(List<?> collections) {
        if (collections != null && collections.size() > 0)
            return true;
        else
            return false;
    }
    
    /**
     * 从指定的配置项初始化一个1+N组合配置
     * @author LuoJingtian created on 2011-12-20 
     * @since chch Framework 1.0
     */
    @Override
    public void init(ConfigItem configItem, Map<String, Class<SingleConfiguration>> configClass) {
        this.configItem = configItem;
        // 校验统一配置项是否配置正确
        checkConfigItem();        
        try {
            switch (mergeRule) {
                case MergeRule.ORDER: {// 顺序: 按照1+N定义顺序找到第一个配置就直接返回               
                    SingleConfiguration defaultConfig = getConfiguration(configItem.getDefaultLocation(), configClass);
                    if (defaultConfig != null) {
                        compositeConfig.add(defaultConfig);
                    }
                    
                    List<SingleConfiguration> extendsConfigs = getConfigurations(configItem.getExtendsLocations(), configClass);
                    addConfigrations(extendsConfigs);
                    break;
                }
                case MergeRule.MERGE: { // 合并, 按照定义顺序添加配置, 找到第一个配置就直接返回
                    SingleConfiguration defaultConfig = getConfiguration(configItem.getDefaultLocation(), configClass);
                    if (defaultConfig != null) {
                        compositeConfig.add(defaultConfig);
                    }
                    List<SingleConfiguration> extendsConfigs = getConfigurations(configItem.getExtendsLocations(), configClass);
                    addConfigrations(extendsConfigs);

                    if (defaultConfig instanceof  XMLConfiguration) {
                        XMLConfiguration defaultXmlConfig = (XMLConfiguration)defaultConfig;
                        Document defaultDoc = defaultXmlConfig.getDocument();
                        XMLConfiguration extendsXmlConfig;
                        for (SingleConfiguration item :extendsConfigs) {
                            extendsXmlConfig = (XMLConfiguration)item;
                            W3cDomUtils.copy(extendsXmlConfig.getDocument(), defaultDoc);
                        }
                        /*XMLConfiguration mergeXmlConfig = new XMLConfiguration();
                        mergeXmlConfig.load(new InputStreamReader(new ByteArrayInputStream(W3cDomUtils.toXmlString(
                                defaultDoc).getBytes(charset)), charset));
                        mergeDoc = mergeXmlConfig.getDocument();
                        compositeConfig.add(mergeXmlConfig);*/
                        mergeDoc = defaultDoc;
                    }
                    break;
                }
                case MergeRule.REPLACE: // 替换: 以后面的N配置替换前面的N(或1)配置, 优先读取最后的配置
                default: {//默认规则与替换规则相同
                    SingleConfiguration defaultConfig = getConfiguration(configItem.getDefaultLocation(), configClass);
                    try {    
                        if (defaultConfig == null) {
                            logger.warn("配置文件" + configItem.getDefaultLocation().getLocation() + "读取失败!");
                            break;
                        }
                        List<ConfigLocationItem> configLocationItems = configItem.getExtendsLocations();                        
                        List<SingleConfiguration> extendsConfigs = new ArrayList<SingleConfiguration>();
                        if (isNotEmpty(configLocationItems)) {
                            SingleConfiguration firstOk = defaultConfig;
                            for (ConfigLocationItem configLocationItem : configLocationItems) {
                                SingleConfiguration cfg = getConfiguration(configLocationItem, configClass);
                                if (cfg != null) {
                                    extendsConfigs.add(cfg);
//                                    if (cfg.isEmpty()) {//把上一个好的的拷贝进来
//                                        InputStream defaultIn = firstOk.getReadStream();
//                                        File out = cfg.getWriteAbleFile();
//                                        if (out.exists() == false) {
//                                            if (out.getParentFile().exists() == false)
//                                                out.getParentFile().mkdirs();
//                                            out.createNewFile();
//                                        }
//                                        IOUtils.copy(defaultIn, out);
//                                        cfg.refresh();
//                                    }
//                                    else
//                                        firstOk = cfg;
                                }
                            }
                        }
                        Collections.reverse(extendsConfigs);
                        addConfigrations(extendsConfigs);
                    }
                    catch(Exception ex) {
                        logger.warn("读取扩展规则失败:" + ex.getMessage() + ";原因是:" + ex.getCause().getMessage());
                    }
                    if (defaultConfig != null) {
                        compositeConfig.add(defaultConfig);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 校验配置项
     * @author LuoJingtian created on 2011-12-21 
     * @since chch Framework 1.0
     */
    private void checkConfigItem() {
        if (configItem == null) {
            String errMsg = "读取的配置项不能为空.";
            logger.warn(errMsg);
            throw new RuntimeException(errMsg);
        }
        if (StringUtils.isBlank(configItem.getDomain())) {
            String errMsg = "读取的配置项Domain不能为空.";
            logger.warn(errMsg);
            throw new RuntimeException(errMsg);
        }
        
        checkConfigLocationItem();
    }
    
    /**
     * 校验配置位置项
     * @author LuoJingtian created on 2011-12-21 
     * @since chch Framework 1.0
     */
    private void checkConfigLocationItem() {
        this.mergeRule = handleMergeRule(configItem.getMergeRule());
        
        // 校验默认配置位置项: 必须指定默认配置位置项
        if (configItem.getDefaultLocation() == null) {
            String errMsg = "配置错误:" + configItem.getDomain() + "未指定默认配置位置.";
            logger.warn(errMsg);
            throw new RuntimeException(errMsg);
        }
        
        // 校验扩展位置配置项: 对合并规则, type必须全部为xml
        if (mergeRule == MergeRule.MERGE) { 
            List<ConfigLocationItem> extendsConfLocationItems = configItem.getExtendsLocations();
            if (isNotEmpty(extendsConfLocationItems)) {
                for (ConfigLocationItem extendsConfLocationItem : extendsConfLocationItems) {
                    if (!MergeTypeName.XML.equalsIgnoreCase(extendsConfLocationItem.getType())) {
                        String errMsg = "配置错误:" + configItem.getDomain()
                                + "指定合并规则为[merge]时, default 和 extends 配置必须同时为xml或者同时为properties.";
                        logger.warn(errMsg);
                        throw new RuntimeException(errMsg);
                    }
                }
            }
        }
    }
    
    /**
     * 对配置文件中给定的1+N配置项合并规则做映射
     * @param mergeRule 配置文件中给定的1+N配置项合并规则
     * @return 映射后的配置项合并规则
     * @author LuoJingtian created on 2011-12-21 
     * @since chch Framework 1.0
     */
    private int handleMergeRule(String mergeRule) {
        // 默认即为替换方式, 以最后一个N配置extends location为准
        if (MergeRuleName.DEFAULT.equalsIgnoreCase(mergeRule) || MergeRuleName.REPLACE.equalsIgnoreCase(mergeRule)) {
            return MergeRule.REPLACE;
        }
        // 排序方式优先使用default location的配置
        else if (MergeRuleName.ORDER.equalsIgnoreCase(mergeRule)) {
            return MergeRule.ORDER;
        }
        // 合并方式对所有配置做合并, XML(体现为Document节点合并)和properties合并后读取到的是一个列表
        else if (MergeRuleName.MERGE.equalsIgnoreCase(mergeRule)) {
            return MergeRule.MERGE;
        }
        else {
            return MergeRule.REPLACE;
        }
    }

    /**
     * 添加Configuration 到
     * @param extendsConfigs
     * @author LuoJingtian created on 2011-12-19 
     * @since chch Framework 1.0
     */
    private void addConfigrations(List<SingleConfiguration> extendsConfigs) {
        for(SingleConfiguration extendsConfiguration : extendsConfigs) {
            if (extendsConfiguration != null) {
                compositeConfig.add(extendsConfiguration);
            }
        }
    }
    
    /**
     * 从指定配置位置项列表获取所有配置接口
     * @param configLocationItems 配置位置项列表
     * @return 所有配置接口
     * @author LuoJingtian created on 2011-12-19 
     * @since chch Framework 1.0
     */
    private List<SingleConfiguration> getConfigurations(List<ConfigLocationItem> configLocationItems, 
            Map<String, Class<SingleConfiguration>> configClass)
            throws Exception {
        List<SingleConfiguration> configurationList = new ArrayList<SingleConfiguration>();
        if (isNotEmpty(configLocationItems)) {
            for (ConfigLocationItem configLocationItem : configLocationItems) {
                configurationList.add(getConfiguration(configLocationItem, configClass));
            }
        }
        return configurationList;
    }
    
    /**
     * 从指定配置位置项获取一个配置接口
     * @param configLocationItem 配置位置项
     * @return 配置接口
     * @author LuoJingtian created on 2011-12-19
     * @since chch Framework 1.0
     */
    private SingleConfiguration getConfiguration(ConfigLocationItem configLocationItem, 
            Map<String, Class<SingleConfiguration>> configClass){
        if (configLocationItem != null) {
            String type = configLocationItem.getType();
            Class<SingleConfiguration> classItem = configClass.get(type);
            if (classItem == null)
                throw new RuntimeException("指定类型" + type + "的配置文件没有定义实现类!");
            try {
                SingleConfiguration cItem = null;
                cItem = classItem.newInstance();
                String configLocation = configLocationItem.getLocation();
                // 按照Spring习惯配置的路径在非WEB环境做路径兼容处理
                configLocation = handlePath(configLocation);
                logger.info("尝试读取以下路径的资源:" + configLocation);                
                cItem.loadFromFile(configLocation);
                return cItem;
            }
            catch(Exception ex) {
                logger.error("尝试读取配置失败:" + configLocationItem.getLocation());
                throw new RuntimeException("尝试读取配置失败:" + configLocationItem.getLocation(), ex);
            }
        }        
        return null;
    }

    private static String handlePath(String configLocation) {
        return configLocation;
    }
    
    @Override
    public Document getMergeDocument() {
        if (mergeRule == MergeRule.MERGE) {
            //其实本类不支持此方法。
            return mergeDoc;
        }        
        return null;
    }
    
    // -------------------------------- 以下为Override方法 -------------------------------- //   
    /**
     * 是否替换方案
     * @return
     * @author zhangyz created on 2013-5-26
     */
    private boolean isReplace() {
        return this.mergeRule == MergeRule.REPLACE || this.mergeRule == MergeRule.DEFAULT;        
    }
    
    /**
     * 是否合并方案
     * @return
     * @author zhangyz created on 2013-5-26
     */
    private boolean isMerge() {
        return this.mergeRule == MergeRule.MERGE;
    }

    @Override
    public void clear() {
        if (isReplace())
            compositeConfig.get(0).clear();
        else if (isMerge()) {
            for (IConfiguration item : compositeConfig)
                item.clear(); 
        }
        else
            compositeConfig.get(0).clear();
    }

    @Override
    public void clearProperty(String key) {
        if (isReplace())
            compositeConfig.get(0).clearProperty(key);
        else if (isMerge()) {
            for (IConfiguration item : compositeConfig)
                item.clearProperty(key); 
        }
        else
            compositeConfig.get(0).clearProperty(key);
    }

    @Override
    public void commitWrite(){
        if (isReplace())
            compositeConfig.get(0).commitWrite();
        else {
            for (IConfiguration item : compositeConfig) 
                item.commitWrite();
        }
    }

    @Override
    public boolean isEmpty() {
        for (IConfiguration item : compositeConfig) {
            if (!item.isEmpty())
                return false;
        }
        return true;
    }
    
    @Override
    public void addProperty(String key, Object defaultValue) {
        if (compositeConfig.get(0).containsKey(key))
            compositeConfig.get(0).setProperty(key, defaultValue);
        else
            compositeConfig.get(0).addProperty(key, defaultValue);
    }

    @Override
    public void setProperty(String key, Object value) {
        if (this.isReplace() || this.isMerge()) {
            if (compositeConfig.get(0).containsKey(key))
                compositeConfig.get(0).setProperty(key, value);
            else
                compositeConfig.get(0).addProperty(key, value);                
        }
        else {
            boolean bFind = false;
            for (IConfiguration item : compositeConfig) {
                if (item.containsKey(key)) {
                    item.setProperty(key, value);
                    bFind = true;
                    break;
                }
            }
            if (bFind) {
                compositeConfig.get(0).addProperty(key, value);  
            }
        }
    }

    @Override
    public boolean containsKey(String key) {
        for (IConfiguration item : compositeConfig) {
            if (item.containsKey(key))
                return true;
        }
        return false;
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        if (key == null)
            return null;
        BigDecimal ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getBigDecimal(key);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        if (key == null)
            return defaultValue;
        BigDecimal ret = getBigDecimal(key);
        if (ret == null)
            return defaultValue;
        else
            return ret;
    }

    @Override
    public BigInteger getBigInteger(String key) {
        if (key == null)
            return null;
        BigInteger ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getBigInteger(key);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        if (key == null)
            return defaultValue;
        BigInteger ret = getBigInteger(key);
        if (ret == null)
            return defaultValue;
        else
            return ret;
    }

    @Override
    public boolean getBoolean(String key) {
        if (key == null)
            return false;
        Boolean ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getBoolean(key);
            if (ret != null)
                return ret;
        }
        return false;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (key == null)
            return defaultValue;
        Boolean ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getBoolean(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        if (key == null)
            return defaultValue;
        Boolean ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getBoolean(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public byte getByte(String key) {
        byte bb = 0;
        return getByte(key, bb);
    }

    @Override
    public byte getByte(String key, byte defaultValue) {
        if (key == null)
            return defaultValue;
        Byte ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getByte(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        if (key == null)
            return defaultValue;
        Byte ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getByte(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        if (key == null)
            return defaultValue;
        Double ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getDouble(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        if (key == null)
            return defaultValue;
        Double ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getDouble(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        if (key == null)
            return defaultValue;
        Float ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getFloat(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return compositeConfig.get(0).getFloat(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return getInt(key ,0);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        if (key == null)
            return defaultValue;
        Integer ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getInt(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return compositeConfig.get(0).getInteger(key, defaultValue);
    }

    @Override
    public Iterator<?> getKeys() {
        Iterator<?> ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getKeys();
            if (ret != null)
                return ret;
        }
        return null;
    }

    /*@Override
    public Iterator<?> getKeys(String key) {
        Iterator<?> ret;
        for (Configuration item : compositeConfig) {
            ret = item.getKeys(key);
            if (ret != null)
                return ret;
        }
        return null;
    }*/

    @Override
    public List<?> getList(String key) {
        List<?> ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getList(key);
            if (ret != null)
                return ret;
        }
        return null;
    }

    /*@SuppressWarnings("rawtypes")
    @Override
    public List<?> getList(String key, List defaultValue) {
        List<?> ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getList(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }*/

    @Override
    public long getLong(String key) {
        return getLong(key, 0);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        if (key == null)
            return defaultValue;
        Long ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getLong(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        if (key == null)
            return defaultValue;
        Long ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getLong(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Properties getProperties(String key) {
        if (key == null)
            return null;
        Properties ret;
        for (IConfiguration item : compositeConfig) {
            ret = (Properties)item.getProperty(key);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @Override
    public Object getProperty(String key) {
        if (key == null)
            return null;
        Object ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getProperty(key);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @Override
    public short getShort(String key) {
        return this.getShort(key, (short)0);
    }

    @Override
    public short getShort(String key, short defaultValue) {
        if (key == null)
            return defaultValue;
        Short ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getShort(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        if (key == null)
            return defaultValue;
        Short ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getShort(key);
            if (ret != null)
                return ret;
        }
        return defaultValue;
    }

    @Override
    public String getString(String key) {
        if (key == null)
            return null;
        String ret;
        for (IConfiguration item : compositeConfig) {
            ret = item.getString(key);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @Override
    public String getStringNotNull(String s) {
        String ret = getString(s);
        if (ret == null || ret.length() == 0)
            throw new RuntimeException(s + "配置型不能为空!");
        return ret;
    }

    @Override
    public String getString(String key, String defaultValue) {
        if (key == null)
            return defaultValue;
        String ret = getString(key);
        if (ret == null)
            return defaultValue;
        else
            return ret;
    }

    /*@Override
    public String[] getStringArray(String key) {
        String[] ret;
        for (Configuration item : compositeConfig) {
            ret = item.getStringArray(key);
            if (ret != null)
                return ret;
        }
        return null;
    }*/

    /*@Override
    public Configuration subset(String prefix) {
        throw new RuntimeException("不支持此方法");
    }*/

    @Override
    public Properties getProperties(){
        Properties props = new Properties();
        Iterator<?> iter = this.getKeys();
        while (iter.hasNext()){
            String key = (String)iter.next();
            props.put(key, this.getProperty(key));
        }
        return props;
    }
}
