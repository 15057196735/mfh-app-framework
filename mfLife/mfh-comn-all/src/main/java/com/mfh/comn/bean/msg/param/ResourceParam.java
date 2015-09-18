/*
 * 文件名称: ResourceParam.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-11-25
 * 修改内容: 
 */
package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

/**
 * 
 * @author zhangyz created on 2014-11-25
 */
@SuppressWarnings("serial")
public class ResourceParam extends BaseParam {
    
    private Integer resourceId;
    
    public ResourceParam() {
        super();
    }

    public ResourceParam(Integer resourceId){
        super();
        this.resourceId = resourceId;
    }

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_RESOURCE;
    }

    @Override
    public void attachSignName(String name) {
        return;
    }

    @Override
    public boolean haveSignName() {
        return true;
    }
    
    public Integer getResourceId() {
        return resourceId;
    }
    
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
}
