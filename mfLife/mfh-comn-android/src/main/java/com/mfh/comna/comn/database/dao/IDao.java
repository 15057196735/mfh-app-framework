/*
 * 文件名称: IDao.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-10
 * 修改内容: 
 */
package com.mfh.comna.comn.database.dao;

/**
 * dao通用接口，象征意义
 * T:bean的类型，主键的类型
 * @author zhangyz created on 2014-3-10
 */
public interface IDao<T, PK> {

    
    public static boolean isAyncDao = true;//底层框架是否采用异步dao,以后可以统一配置

}
