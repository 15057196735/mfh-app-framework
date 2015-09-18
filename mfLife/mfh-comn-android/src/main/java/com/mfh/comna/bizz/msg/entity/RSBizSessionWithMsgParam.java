/*
 * 文件名称: BizSessionWithMsg.java
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
import com.mfh.comn.bean.msg.BizSessionBean;
import com.mfh.comn.bean.msg.MsgParameterBean;

/**
 * 用于业务场景的消息会话信息（含最后一条消息信息）
 * @author zhangyz created on 2015-3-20
 */@SuppressWarnings("serial")
   public class RSBizSessionWithMsgParam extends BizSessionBean implements java.io.Serializable{

    private BizSessionBean session; //会话本身信息
    private MsgParameterBean lastMsg; //最后一条消息本身信息


    public BizSessionBean getSession() {
        return session;
    }
    
    public void setSession(BizSessionBean session) {
        this.session = session;
    }
    
    public MsgParameterBean getLastMsg() {
        return lastMsg;
    }
    
    public void setLastMsg(MsgParameterBean lastMsg) {
        this.lastMsg = lastMsg;
    }
    
    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }    
}
