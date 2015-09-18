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

/**
 * 升级接口定义
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-24
 * @since SHK BMP 1.0
 */
public interface Upgrade {
    
    /**
     * 升级接口
     * @author LuoJingtian created on 2011-12-24 
     * @since SHK BMP 1.0
     */
    void upgrade();

    /**
     * 直接更新到最新版本，中间不升级
     */
    void directToNewVersion();
}
