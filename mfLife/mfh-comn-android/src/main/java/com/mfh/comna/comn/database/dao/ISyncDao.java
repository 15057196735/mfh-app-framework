/*
 * 文件名称: IDao.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-9
 * 修改内容: 
 */
package com.mfh.comna.comn.database.dao;


/**
 * 数据同步调用访问接口，数据访问可能从db、从后台服务端等访问。
 * @author zhangyz created on 2014-3-9
 */
public interface ISyncDao<T, PK> extends IDao<T, PK>{
    
    T getEntityById(PK pkId);
    
    PK save(T bean);
    
    void update(T bean);

    void saveOrUpdate(T bean);

    void deleteAll();
    
    void deleteById(PK pkId);
}
