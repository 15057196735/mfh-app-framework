/*
 * 文件名称: MfhEntity.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-12
 * 修改内容: 
 */
package com.mfh.comna;

import java.util.Date;

/**
 * 统一定义Mfh的entity基类.
 * 与java服务器端的MfhEntity作用相同，且包名相同。但因为不会同时部署，故没关系。
 * 引入给类的意图是让android端和java端可以共用bean部分的代码。
 */
@SuppressWarnings("serial")
public abstract class MfhEntity<T> implements java.io.Serializable{
    protected T id; //主键

    protected Date createdDate = null;//创建日期

    protected Date updatedDate; //修改日期

    public T getId() {
        return id;
    }

    protected String createdBy = null;
    protected String updatedBy = null;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public void setId(T id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}