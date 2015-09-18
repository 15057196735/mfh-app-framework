/*
 * 文件名称: DaoUrl.java
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

import org.apache.commons.lang3.StringUtils;

/**
 * 后台dao Url参数定义。
 * 若各url为null，则按默认规则自动构建url，但此时tableName不能为空。
 * 
     默认规则：
    daoUrl.setQueryUrl("/list");
    daoUrl.setGetUrl("/getById");
    daoUrl.setSaveUrl("/create");
    daoUrl.setUpdateUrl("/update");
    daoUrl.setDeleteUrl("/multiDelete");
 * @author zhangyz created on 2014-3-10
 */
public class DaoUrl {
    public enum DaoType{
        list,
        getById,
        create,
        update,
        multiDelete
    }
    
    private String listUrl;//查询url
    private String getUrl;//获取单bean url
    private String createUrl;//保存url
    private String updateUrl;//修改url
    private String deleteUrl;//删除url
    
    private String tableName = "";//实体名,url的前缀,如：/TableName/queryUrl....
    private String tableCaption;//实体描述 
    
    public DaoUrl(String tableName) {
        super();
        this.tableName = tableName;
    }

    public DaoUrl(String tableName, String tableCaption) {
        super();
        if (tableName != null)
            this.tableName = StringUtils.capitalize(tableName);
        this.tableCaption = tableCaption;
    }
    
    public DaoUrl() {
        super();
    }
        
    public DaoUrl(String queryUrl, String getUrl, String saveUrl, 
            String updateUrl, String deleteUrl) {
        setBatch(queryUrl, getUrl, saveUrl, updateUrl, deleteUrl);
    }
    
    /**
     * 批量设置各个url
     * @param listUrl
     * @param getUrl
     * @param createUrl
     * @param updateUrl
     * @param deleteUrl
     * @author zhangyz created on 2014-3-10
     */
    public void setBatch(String listUrl, String getUrl, String createUrl, 
            String updateUrl, String deleteUrl) {
        this.listUrl = listUrl;
        this.getUrl = getUrl;
        this.createUrl = createUrl;
        this.updateUrl = updateUrl;
        this.deleteUrl = deleteUrl;
    }
    
    private String genDefaultUrl(String pre) {
        if (StringUtils.isBlank(tableName))
            throw new RuntimeException(tableName);
        if (tableName.startsWith("/"))
            return tableName + "/" + pre;
        else
            return "/" + tableName + "/" + pre;
    }

    public String getListUrl() {
        if (listUrl == null || listUrl.length() == 0)
            return this.genDefaultUrl(DaoType.list.toString());
        return listUrl;
    }
    
    public void setListUrl(String queryUrl) {
        this.listUrl = queryUrl;
    }
    
    public String getGetUrl() {
        if (getUrl == null || getUrl.length() == 0)
            return this.genDefaultUrl(DaoType.getById.toString());
        return getUrl;
    }
    
    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }
    
    public String getCreateUrl() {
        if (createUrl == null || createUrl.length() == 0)
            return this.genDefaultUrl(DaoType.create.toString());
        return createUrl;
    }
    
    public void setCreateUrl(String saveUrl) {
        this.createUrl = saveUrl;
    }
    
    public String getUpdateUrl() {
        if (updateUrl == null || updateUrl.length() == 0)
            return this.genDefaultUrl(DaoType.update.toString());
        return updateUrl;
    }
    
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
    
    public String getDeleteUrl() {
        if (deleteUrl == null || deleteUrl.length() == 0)
            return this.genDefaultUrl(DaoType.multiDelete.toString());
        return deleteUrl;
    }
    
    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getTableCaption() {
        return tableCaption;
    }
    
    public void setTableCaption(String tableCaption) {
        this.tableCaption = tableCaption;
    }

}
