/*
 * 文件名称: ITenantId.java
 * 版权信息: Copyright 2001-2013 hangzhou chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2013-8-31
 * 修改内容: 
 */
package com.mfh.comn.bean;


/**
 * sass平台的租户信息
 * @author zhangyz created on 2013-8-31
 * @since Framework 1.0
 */
public interface ITenantId<T> {
    
    /**
     * 获取租户信息
     * @return
     * @author zhangyz created on 2013-8-31
     */
    public T getTenantId();

}
