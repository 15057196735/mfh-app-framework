/*
 * 文件名称: UpgradeSupport.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-6-6
 * 修改内容: 
 */
package com.mfh.comn.upgrade;

import java.io.InputStream;
import java.util.List;


/**
 * 
 * @author zhangyz created on 2014-6-6
 */
public interface UpgradeSupport {
    String tableVersionName = "T_COM_VERSION";

    /**
     * 获取当前升降库的数据库类型
     * @return
     * @author zhangyz created on 2014-6-6
     */
    String getDsType();

    /**
     * 初始化,升级工作支持提交和回退。故此次应初始化为手工提交。后面执行commit方法时进行。
     * @param dsId 数据源标识
     * @throws Exception
     */
    void init(String dsId) throws Exception;
    
    /**
     * 获取指定相对路径的升级脚本文件的文件流
     * @param scriptRelativePath
     * @return
     * @author zhangyz created on 2014-6-7
     */
    InputStream getUpgradeSqlScriptsStream(String scriptRelativePath);
    
    /**
     * 判断版本表T_COM_VERSION是否存在
     * @return
     */
    boolean existTable();
    
    /**
     * 创建版本表
     * @throws Exception
     * @author zhangyz created on 2014-6-6
     */
    void createVersionTable() throws Exception;
    
    /**
     * 执行具体的数据库升级工作
     * @param sqls sql语句列表，内部应批量执行
     * @author zhangyz created on 2014-6-6
     */
    void upgradeVersion(List<String> sqls) throws Exception;
    
    /**
     * 提交升级工作
     * 
     * @author zhangyz created on 2014-6-6
     */
    void commit();
    
    /**
     * 回退升级工作
     * 
     * @author zhangyz created on 2014-6-6
     */
    void rollback();
    
    /**
     * 升级后的清理工作
     */
    void close();
    
    /**
     * 查询指定表的当前版本信息
     * @param tableDomain 表名
     * @return
     */
    VersionInfo queryVersion(String tableDomain);
    
    /**
     * 修改表的版本信息
     * @param versionInfo
     */
    void updateVersion(VersionInfo versionInfo);
    
    /**
     * 新增一个表的版本信息
     * @param versionInfo
     * @author zhangyz created on 2014-6-6
     */
    void saveVersion(VersionInfo versionInfo);

}
