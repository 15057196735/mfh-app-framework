/*
 * 文件名称: ConfigsParseHelper.java
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

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 配置项入口UConfig.xml解析类, 包级私有, 不允许包外访问. 统一配置入口固定为classpath路径的uconfig.xml. 配置样例:<BR>
 * <pre>
 * &lt;configs&gt;
 *    &lt;config domain=&quot;COM.SHK.COMMON&quot; description=&quot;DESC&quot; merge-rule=&quot;default&quot;&gt;
 *      &lt;default type=&quot;properties&quot; location=&quot;jdbc.properties&quot;/&gt;
 *      &lt;extends type=&quot;properties&quot; location=&quot;/WEB-INF/classes/jdbc2.properties&quot;/&gt;
 *      &lt;extends type=&quot;properties&quot; location=&quot;/WEB-INF/classes/jdbc3.properties&quot;/&gt;
 *    &lt;/config&gt;
 * &lt;/configs&gt;
 * </pre>
 * 
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-19
 * @since chch Framework 1.0
 */
public class ConfigsParseHelper {
    /** 日志记录器 */
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ConfigsParseHelper.class);
    public final static ThreadLocal<String> configLocal = new ThreadLocal<>();//使用哪个配置文件

    private static Map<String, Document> configDomFull = new HashMap<>();//uConfig.xml总配置文件的缓存。
    public static final String configAlias = "uconfig.xml";//统一配置文件
    
    /**
     * 初始化
     * @param configAlias 统一配置文件区别标识，整个操作系统可以支持多个
     * @param stream 文件流
     * @author zhangyz created on 2013-5-25
     */
    public static void init(String configAlias, InputStream stream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            String key = configAlias;
            if (configLocal.get() != null)
                key = configLocal.get();

            Document doc = configDomFull.get(key);
            if (doc == null){
                doc = docBuilder.parse(stream);
                configDomFull.put(key, doc);
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("解析统一配置文件入口失败!");
        }
        finally {
            if (stream != null)
                try {
                    stream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    
    /**
     * 获取所有统一配置项信息
     * 
     * @return 所有统一配置项信息 Key=Domain
     * @author LuoJingtian created on 2011-12-20
     * @since chch Framework 1.0
     */
    public static Map<String, ConfigItem> getConfigs(String configAlias) {
        Map<String, ConfigItem> configs = new HashMap<String, ConfigItem>();
        try {
            Document doc = getUConfigXmlDocument(configAlias);
            NodeList configNodes = doc.getDocumentElement().getElementsByTagName("config");
            Element configElem;
            for (int i = 0; i < configNodes.getLength(); i++) {
                configElem = (Element) configNodes.item(i);
                ConfigItem configItem = parseConfigItem(configElem);
                if (configItem != null)
                    configs.put(configItem.getDomain(), configItem);
            }
        }
        catch (ParserConfigurationException e) {
            logger.error("解析统一配置入口文件失败, 不能创建解析器.", e);
            throw new RuntimeException("解析统一配置入口文件失败, 不能创建解析器.", e);
        }
        catch (SAXException e) {
            logger.error("解析统一配置入口文件失败, 文件格式不正确.", e);
            throw new RuntimeException("解析统一配置入口文件失败, 文件格式不正确.", e);
        }
        catch (IOException e) {
            logger.error("解析统一配置入口文件失败.", e);
            throw new RuntimeException("解析统一配置入口文件失败.", e);
        }
        catch (Exception e) {
            logger.error("解析统一配置入口文件失败.", e);
            throw new RuntimeException("解析统一配置入口文件失败.", e);
        }
        return configs;
    }
    
    /**
     * 获取统一配置项的实现类定义
     * @param configAlias
     * @return
     * @author zhangyz created on 2013-5-25
     */
    @SuppressWarnings("unchecked")
    public static ConfigClass getConfigClass(String configAlias) {
        ConfigClass configClass = new ConfigClass();
        Map<String, Class<SingleConfiguration>> propClassMap = new HashMap<String, Class<SingleConfiguration>> ();
        try {
            Document doc = getUConfigXmlDocument(configAlias);            
            Element classNode;
            NodeList classNodes = doc.getDocumentElement().getElementsByTagName("class");
            if (classNodes.getLength() > 0) {
                Element classEle = (Element)classNodes.item(0);
                String uclassName = classEle.getAttribute("uconfig");
                configClass.setUconfigClass((Class<UConfig>)Class.forName(uclassName));
                classNodes = classEle.getElementsByTagName("item");
                for (int ii = 0; ii < classNodes.getLength(); ii++) {
                    classNode = (Element) classNodes.item(ii);
                    String fileType = classNode.getAttribute("type");
                    String domainClass = classNode.getTextContent();
                    Class<SingleConfiguration> classItem = (Class<SingleConfiguration>)Class.forName(domainClass);
                    propClassMap.put(fileType, classItem);
                }
            }
            configClass.setConfigDomainClass(propClassMap);
            return configClass;
        }
        catch (Exception e) {
            logger.error("解析统一配置入口文件失败.", e);
            throw new RuntimeException("解析统一配置入口文件失败.", e);
        }
    }
    
    /**
     * 获取所有统一配置项信息
     * 
     * @return 所有统一配置项信息 Key=Domain
     * @author LuoJingtian created on 2011-12-20
     * @since chch Framework 1.0
     */
    public static ConfigItem getConfig(String domain, String configAlias) {
        ConfigItem configItem = null;
        try {
            Document doc = getUConfigXmlDocument(configAlias);
            NodeList configNodes = doc.getDocumentElement().getElementsByTagName("config");
            Element configElem;
            for (int i = 0; i < configNodes.getLength(); i++) {
                configElem = (Element) configNodes.item(i);
                if (configElem.getAttribute("domain").equalsIgnoreCase(domain)) {
                    configItem = parseConfigItem(configElem);
                    break;
                }
            }
        }
        catch (ParserConfigurationException e) {
            logger.error("解析统一配置入口文件失败, 不能创建解析器.", e);
            throw new RuntimeException("解析统一配置入口文件失败, 不能创建解析器.", e);
        }
        catch (SAXException e) {
            logger.error("解析统一配置入口文件失败, 文件格式不正确.", e);
            throw new RuntimeException("解析统一配置入口文件失败, 文件格式不正确.", e);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("解析统一配置入口文件失败.", e);
            throw new RuntimeException("解析统一配置入口文件失败.", e);
        }
        catch (Exception e) {
            logger.error("解析统一配置入口文件失败.", e);
            throw new RuntimeException("解析统一配置入口文件失败.", e);
        }
        
        return configItem;
    }
    
    /**
     * 获取UConfig XML文档对象, uconfig.xml必须在classpath路径下
     * @return UConfig XML文档对象
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @author LuoJingtian created on 2011-12-22 
     * @since chch Framework 1.0
     */
    private static Document getUConfigXmlDocument(String configAlias) 
                throws ParserConfigurationException, SAXException, IOException {
        if (configLocal.get() != null)
            return configDomFull.get(configLocal.get());
        else
            return configDomFull.get(configAlias);
    }

    /**
     * 解析单个配置节点
     * @param configElem 单个配置节点, /configs/config
     * @return
     * @author LuoJingtian created on 2011-12-21 
     * @since chch Framework 1.0
     */
    private static ConfigItem parseConfigItem(Element configElem) {
        ConfigItem configItem = new ConfigItem();
        configItem.setDomain(configElem.getAttribute("domain"));
        configItem.setDescription(configElem.getAttribute("description"));
        configItem.setMergeRule(configElem.getAttribute("merge-rule"));

        NodeList defaults = configElem.getElementsByTagName("default");
        if (defaults != null && defaults.getLength() > 0){
            Element defalutElem = (Element) defaults.item(0);
            configItem.setDefaultLocation(parseConfigLocationItem(defalutElem));
    
            configItem.setExtendsLocations(parseExtendsLocation(configElem));
            return configItem;
        }
        else
            return null;
    }

    /**
     * 解析扩展配置节点元素
     * 
     * @param configElem 配置节点 /configs/config/extends
     * @return 扩展配置信息
     * @author LuoJingtian created on 2011-12-19
     * @since chch Framework 1.0
     */
    private static List<ConfigLocationItem> parseExtendsLocation(Element configElem) {
        NodeList extedsNodes = configElem.getElementsByTagName("extends");
        int extendsCount = extedsNodes.getLength();
        List<ConfigLocationItem> extendsLocations = new ArrayList<ConfigLocationItem>(extendsCount);

        Element extendsElem;
        for (int i = 0; i < extendsCount; i++) {
            extendsElem = (Element) extedsNodes.item(i);
            extendsLocations.add(parseConfigLocationItem(extendsElem));
        }

        return extendsLocations == null ? new ArrayList<ConfigLocationItem>(0) : extendsLocations;
    }

    /**
     * 解析单个配置项
     * 
     * @param configLocationElem 配置位置节点
     * @return 配置位置项信息
     * @author LuoJingtian created on 2011-12-20
     * @since chch Framework 1.0
     */
    private static ConfigLocationItem parseConfigLocationItem(Element configLocationElem) {
        ConfigLocationItem configLocationItem = new ConfigLocationItem();
        configLocationItem.setType(configLocationElem.getAttribute("type"));
        configLocationItem.setLocation(configLocationElem.getAttribute("location"));
        configLocationItem.setParser(configLocationElem.getAttribute("parser"));//针对xml类型的
        return configLocationItem;
    }
}
