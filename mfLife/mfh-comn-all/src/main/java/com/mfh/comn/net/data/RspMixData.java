/*
 * 文件名称: ResponseMixData.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-8
 * 修改内容: 
 */
package com.mfh.comn.net.data;


/**
 * 混合数据结果
 * @author zhangyz created on 2014-3-8
 */
public class RspMixData implements IResponseData{
    private RspQueryResult<?> dataQueryResult = null;
    private RspValue<?> dataString;
    private RspMap dataKeyValue;
    private RspBean<?> dataBean;
    
    @SuppressWarnings("unchecked")
    public <T> RspQueryResult<T> getDataQueryResult() {
        return (RspQueryResult<T>) dataQueryResult;
    }
    
    public RspValue<?> getDataString() {
        return dataString;
    }
    
    public void setDataString(RspValue<?> dataString) {
        this.dataString = dataString;
    }
    
    public RspMap getDataKeyValue() {
        return dataKeyValue;
    }
    
    public void setDataKeyValue(RspMap dataKeyValue) {
        this.dataKeyValue = dataKeyValue;
    }
    
    @SuppressWarnings("unchecked")
    public <T> RspBean<T> getDataBean() {
        return (RspBean<T>) dataBean;
    }
    
    public <T> void setDataBean(RspBean<T> dataBean) {
        this.dataBean = dataBean;
    }
    
    public void setDataQueryResult(RspQueryResult<?> dataQueryResult) {
        this.dataQueryResult = dataQueryResult;
    }
}
