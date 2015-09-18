/*
 * 文件名称: MsgParamWithSession.java
 * 版权信息: Copyright 2013-2015 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2015-3-20
 * 修改内容: 
 */
package com.mfh.comna.bizz.msg.entity;

import com.alibaba.fastjson.JSON;
import com.mfh.comna.MfhEntity;
import com.mfh.comn.bean.IObject;
import com.mfh.comn.bean.msg.MsgParameterBean;

import java.io.Serializable;

/**
 * 传递给客户端的最新消息结构，包含了会话的基本信息,用于辅助客户端寻找该消息显示在界面中的哪个会话里。
 * 如果未找到，则要求客户端自己请求该会话信息!!!!!!!!!
 * 
 * 判断该消息属于哪个业务分组,总共六个分组
 * sessionType=1 or (sessionType=101 and sessionTagOne is null)  -- 陌生粉丝组
 * sessionType=101 and sessionTagOne is not null -- 客户组(或称会员组、业主组)
 * sessionType=0 and sessionBizType=0            -- 个人组,然后根据fromguid或sessionid判断具体位置
 * sessionType=0 and sessionBizType=1            -- 同事组 ,然后根据fromguid或sessionid判断具体位置
 * sessionType=2 and sessionBizType=0            -- 普通群组会话,然后根据sessionid判断具体位置
 * sessionType=2 and sessionBizType=1            -- 服务群组会话(如针对一个订单),然后根据sessionid判断具体位置
 * 
 * @author zhangyz created on 2015-3-20
 */
@SuppressWarnings("serial")
public final class RSBizMsgParamWithSession extends MfhEntity<String> implements Serializable, IObject {
    private MsgParameterBean msg; //最后一条消息本身信息

    private Integer sessionType;//会话技术类型，在getById()调用时会填充，其他情况下为提升性能没有填充。
    private Integer sessionBizType;//会话的业务类型
    private Integer sessionTagOne;//会话的业务属性,为空代表还是陌生用户，不是自己的会员客户

    public RSBizMsgParamWithSession() {
        super();
    }

    public RSBizMsgParamWithSession(MsgParameterBean msg) {
        super();
        this.msg = msg;
    }

    public MsgParameterBean getMsg() {
        return msg;
    }
    
    public void setMsg(MsgParameterBean msg) {
        this.msg = msg;
    }

    
    public Integer getSessionType() {
        return sessionType;
    }

    
    public void setSessionType(Integer sessionType) {
        this.sessionType = sessionType;
    }

    
    public Integer getSessionBizType() {
        return sessionBizType;
    }

    
    public void setSessionBizType(Integer sessionBizType) {
        this.sessionBizType = sessionBizType;
    }

    
    public Integer getSessionTagOne() {
        return sessionTagOne;
    }

    
    public void setSessionTagOne(Integer sessionTagOne) {
        this.sessionTagOne = sessionTagOne;
    }
    
    /**
     * 填充相关信息
     * @author zhangyz created on 2015-3-23
     */
    /*public void fillSessionInfo(MsgCaptionBean msgCaption) {
        msg.setFormatCreateTime(DateFormatUtils.setSpecialFormat(msgData.getTime()));      
        msg.setHeadimageurl(msgCaption.getLocalheadimageurl());
        msg.setSpokesman(msgCaption.getPointName());
        
        setSessionType(msgCaption.getSessionType());
        setSessionBizType(msgCaption.getSessionBizType());
        setSessionTagOne(msgCaption.getSessionTagOne());
    }*/
    
    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
    
}
