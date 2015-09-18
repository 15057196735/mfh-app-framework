/*
 * 文件名称: DbUpgrade.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-24
 * 修改内容: 
 */
package com.mfh.comn.upgrade;

import java.io.BufferedReader;

/**
 * 数据库升级接口定义
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-24
 * @since SHK BMP 1.0
 */
public interface DbUpgrade extends Upgrade {
    /**
     * 初始化
     * @param uci
     * @param support 具体数据域升级实现类
     * @author zhangyz created on 2012-11-3
     */
    void init(UpgradeConfigInfo uci, UpgradeSupport support);
        
    /**
     * 获取升级的SQL脚本的输入字符流
     * @param version 版本号
     * @return
     * @author LuoJingtian created on 2011-12-24 
     * @since SHK BMP 1.0
     */
    BufferedReader getUpgradeSqlScriptsReader(int version);
}
