/*
 * 文件名称: IAsyncDao.java
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


import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 异步dao调用接口，特别适用于网络调用情况
 * @author zhangyz created on 2014-3-10
 */
public interface IAsyncDao<T, PK> extends IDao<T, PK> {
    /**
     * 执行结果集查询
     * @param params 查询参数
     * @param callBack 查询结果处理函数
     * @param factUrl 查询接口url，可以为空，代表使用默认
     * @author zhangyz created on 2014-3-10
     */
    void query(AjaxParams params, NetProcessor.QueryRsProcessor<T> callBack, String... factUrl);

    /**
     * 执行查询所有列表，无须分页
     * @param params
     * @param callBack
     * @param factUrl
     */
    void queryAll(AjaxParams params, NetProcessor.QueryRsProcessor<T> callBack, String... factUrl);
    
    /**
     * 获取一个实体
     * @param pkId 主键标识
     * @param callBack 查询到后的回调函数
     * @param factUrl 同上
     * @author zhangyz created on 2014-3-10
     */
    void getEntityById(PK pkId, NetProcessor.BeanProcessor<T> callBack, String... factUrl);
    
    void save(T bean, NetProcessor.ComnProcessor<PK> callBack, String... factUrl);
    
    void update(T bean, NetProcessor.ComnProcessor<PK> callBack, String... factUrl);
    
    void deleteAll(List<PK> pkIds, NetProcessor.DeleteProcessor<T> callBack, String... factUrl);
    
    void deleteById(PK pkId, NetProcessor.DeleteProcessor<T> callBack, String... factUrl);
    
}
