/*
 * 文件名称: BindParamter.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-10-30
 * 修改内容: 
 */
package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;


/**
 * 身份绑定时由业务系统发给适配器的消息内容
 * 
 * @author zhangyz created on 2014-10-30
 */
@SuppressWarnings("serial")
public class BindParam implements EmbBody{

    private Integer channelId;//待绑定的渠道编号
    
    private String pointId; //待绑定的渠道端点号
    
    private Long guid; //被关联的逻辑端号
    
    private Long bizRelationId;//业务关联信息

    /**
     * 构造函数
     * @param channelId 待绑定的渠道编号
     * @param pointId 待绑定的渠道端点号
     * @param guid 被关联的逻辑端号
     * @param bizRelationId 业务管理信息
     */
    public BindParam(Integer channelId, String pointId, Long guid, Long bizRelationId) {
        super();
        this.channelId = channelId;
        this.pointId = pointId;
        this.guid = guid;
        this.bizRelationId = bizRelationId;
    }
    
    public Integer getChannelId() {
        return channelId;
    }
    
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
    
    public String getPointId() {
        return pointId;
    }
    
    public void setPointId(String pointId) {
        this.pointId = pointId;
    }
   
    public Long getGuid() {
        return guid;
    }
   
    public void setGuid(Long guid) {
        this.guid = guid;
    }
    
    public Long getBizRelationId() {
        return bizRelationId;
    }
    
    public void setBizRelationId(Long bizRelationId) {
        this.bizRelationId = bizRelationId;
    }

    @Override
    public void attachSignName(String name) {
        
    }

    @Override
    public boolean haveSignName() {
        return true;
    }

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_JSON;
    }
}
