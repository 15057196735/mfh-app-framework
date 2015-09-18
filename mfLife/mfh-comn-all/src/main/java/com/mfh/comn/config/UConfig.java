/*
 * 文件名称: UConfig.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-20
 * 修改内容: 
 */
package com.mfh.comn.config;

import org.w3c.dom.Document;

import java.util.Map;

/**
 * 统一配置接口定义. 额外提供对xml的merge合并方式返回Document的接口.
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-20
 * @since chch Framework 1.0
 */
public interface UConfig extends IConfiguration{
    String CONFIG_COMMON = "COMMON";//基础类
    String CONFIG_APP = "APP";//应用类
    String CONFIG_PRIV = "BMP.PRIV";//权限类
    String CONFIG_FUNC = "BMP.FUNCTION";//功能模块类
    String CONFIG_DBUPGRADE = "DB.UPGRADE";//数据库升级类

    String CONFIG_PARAM_BIND_URL = "app.bind.url";
    String CONFIG_PARAM_BIZ_URL = "app.biz.url";
    String CONFIG_PARAM_SERVERURL = "app.server.url";
    String CONFIG_PARAM_UPDATEURL = "app.update.url";//升级地址
    String CONFIG_PARAM_IMAGE_UPLOAD = "app.image.update.url";
    
    String CONFIG_PARAM_MAXCODENUM = "app.comn.maxCodeNum";
    String CONFIG_PARAM_ISLOCALMAP = "app.comn.localMap";//是否本地map

    //数据库
    String CONFIG_PARAM_DB_NAME = "app.db.name";
    String CONFIG_PARAM_DB_PATH = "app.db.path";

    

    String CONFIG_VM_WEBDIR = "vm.web.ootpath";
    String CONFIG_VM_MOBLIEDIR = "vm.mobile.rootpath";

    String CONFIG_WX_ID = "app.id"; //微信分享的配置项
    String CONFIG_NETPHONE_IP = "netphone.ip"; //  网络电话REST服务器地址
        
    /**
     * 获取合并的统一配置文档对象. 当指定的合并规则为merge时, 可以返回合并后的document文档对象. 其他合并规则返回null;
     * @return 获取合并的统一配置文档对象
     * @author LuoJingtian created on 2011-12-22 
     * @since chch Framework 1.0
     */
    Document getMergeDocument();
    
    /**
     * 初始化
     * @param configItem
     * @param configClass
     * @author zhangyz created on 2013-5-26
     */
    void init(ConfigItem configItem, Map<String, Class<SingleConfiguration>> configClass);
    
    
    /**
     * 合并规则常量定义
     * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-21
     * @since chch Framework 1.0
     */
    interface MergeRule {
        /** 顺序查找, 从1到N1,N2...找到第一个就立即返回 */
        int ORDER = 1;
        
        /** 参数替换, 从1到N1,N2...以找到的最后一个为准 */
        int REPLACE = 2;
        
        /** 配置合并, 对XML为合并Document, 对Properties读取为列表 */
        int MERGE = 3;
        
        /** 默认为用最后一个N配置进行替换 */
        int DEFAULT = REPLACE;
    }
    /**
     * 合并规则常量定义
     * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-21
     * @since chch Framework 1.0
     */
    interface MergeRuleName {
        /** 顺序查找, 从1到N1,N2...找到第一个就立即返回 */
        String ORDER = "order";
        
        /** 参数替换, 从1到N1,N2...以找到的最后一个为准 */
        String REPLACE = "replace";
        
        /** 配置合并, 对XML为合并Document, 对Properties读取为列表 */
        String MERGE = "merge";
        
        /** 默认为用最后一个N配置进行替换, is REPLACE*/
        String DEFAULT = "default";
    }
    
    /**
     * 合并规则常量定义
     * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-21
     * @since chch Framework 1.0
     */
    interface MergeTypeName {
        /** 顺序查找, 从1到N1,N2...找到第一个就立即返回 */
        String PROPERTIES = "properties";
        
        /** 参数替换, 从1到N1,N2...以找到的最后一个为准 */
        String XML = "xml";
    }
}
