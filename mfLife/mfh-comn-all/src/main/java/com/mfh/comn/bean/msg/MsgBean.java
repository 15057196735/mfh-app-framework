/*
 * 文件名称: MsgBean.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-10-18
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.param.EmbBody;
import com.mfh.comn.bean.msg.param.ImageParam;
import com.mfh.comn.bean.msg.param.ImageTextParam;
import com.mfh.comn.bean.msg.param.ResourceParam;
import com.mfh.comn.bean.msg.param.TemplateParam;
import com.mfh.comn.bean.msg.param.TextParam;
import com.mfh.comn.bean.msg.param.VoiceParam;

/**
 * 老版本消息本身结构。为了兼容老版本而存在。
 * @author zhangyz created on 2014-10-18
 * @deprecated
 */
@SuppressWarnings("serial")
public class MsgBean implements Serializable {
    private String msgId;//消息编号    
    private Integer bizType = MsgConstant.MSG_BIZTYPE_CHAT;//业务类型,不能为空
    private String bodyClassName = null;
    private String techType = MsgConstant.MSG_TECHTYPE_JSON;//技术类型,不能为空    
    private Integer reliability;//消息可靠性0~3, 0代表普通，3最高
    private Integer priority;//消息优先级,0~n， 0代表普通， 数字越大优先级越高
    private Integer zipType;//压缩类型，备用
    private String signName;//消息签名

    private Object msgBody; //具体消息内容,不能为空
    
    @JSONField (format="yyyy-MM-dd HH:mm:ss")  
    private Date createTime;//消息创建时间
        
    /**
     * 无参构造函数，反序列化时需要
     */
    public MsgBean() {
        super();
    }

    public MsgBean(String msg) {
        super();
        this.createTime = new Date();
        this.bizType = MsgConstant.MSG_BIZTYPE_CHAT;
        this.techType = MsgConstant.MSG_TECHTYPE_TEXT;
        this.setMsgBody(new TextParam(msg));
    }

    public MsgBean(String msg, Integer bizType) {
        super();
        this.createTime = new Date();
        this.bizType = bizType;
        this.setMsgBody(new TextParam(msg));
    }
    
    /**
     * 构造函数，构造一个聊天业务类型的消息
     * @param msgBody
     */
    public MsgBean(EmbBody msgBody) {
        super();
        this.createTime = new Date();
        this.setMsgBody(msgBody);
    } 
    
    public MsgBean(EmbBody msgBody, Integer bizType) {
        super();
        this.msgBody = msgBody;
        this.createTime = new Date();
        this.bizType = bizType;
        this.setMsgBody(msgBody);
    }

    /**
     * 构造函数，构造一个聊天业务类型的消息,同时指定消息Id
     * @param msgBody
     */
    public MsgBean(EmbBody msgBody, String msgId) {
        super();
        this.msgId = msgId;
        this.createTime = new Date();
        this.bizType = MsgConstant.MSG_BIZTYPE_CHAT;
        this.setMsgBody(msgBody);
    }
    
    public String getMsgId() {
        return msgId;
    }
    
    public String getBodyClassName() {
        if (StringUtils.isBlank(bodyClassName)) {
        	if(msgBody != null){
                if (msgBody instanceof EmbBody) {
                    this.setBodyClassName(msgBody.getClass().getName());
                }
        	}
        }
        return bodyClassName;
    }
    
    public void setBodyClassName(String bodyClassName) {
        this.bodyClassName = bodyClassName;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getBizType() {
        return bizType;
    }
    
    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }
    
    public String getTechType() {
        return techType;
    }
    
    public void setTechType(String techType) {
        this.techType = techType;
    }
    
    public Integer getReliability() {
        return reliability;
    }
    
    public void setReliability(Integer reliability) {
        this.reliability = reliability;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public Integer getZipType() {
        return zipType;
    }
    
    public void setZipType(Integer zipType) {
        this.zipType = zipType;
    }    
    
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
		return createTime;
	}

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Object getMsgBody() {
        return msgBody;
    }
    
	/**
	 * 设置消息体，自动判断消息技术类型
	 * @param msgBody
	 * @author zhangyz created on 2014-10-30
	 */
    public void setMsgBody(Object msgBody) {
        this.msgBody = msgBody;
        if (msgBody != null) {
            if (msgBody instanceof EmbBody) {
                this.techType = ((EmbBody)msgBody).getType();   
            }
        }
    }
    
    /**
     * 本身自解析body部分
     * @throws Exception
     * @author zhangyz created on 2014-10-29
     */
    public void parseBodySelf() throws Exception {
        if (this.msgBody == null)
            return;
        String jsonString = this.msgBody.toString();
        Object factBody = null;
        if (MsgConstant.MSG_TECHTYPE_JSON.equals(this.techType) && this.bodyClassName != null)
            factBody = JSON.parseObject(jsonString, Class.forName(this.bodyClassName));
        else if (MsgConstant.MSG_TECHTYPE_TEXT.equals(this.techType) || MsgConstant.MSG_TECHTYPE_EMOTION.equals(this.techType))
            factBody = JSON.parseObject(jsonString, TextParam.class); 
        else if (MsgConstant.MSG_TECHTYPE_RAW.equals(this.techType))
            return; 
        else if (MsgConstant.MSG_TECHTYPE_IMAGE.equals(this.techType))
            factBody = JSON.parseObject(jsonString, ImageParam.class);  
        else if (MsgConstant.MSG_TECHTYPE_VOICE.equals(this.techType))
            factBody = JSON.parseObject(jsonString, VoiceParam.class);            
        else if (MsgConstant.MSG_TECHTYPE_TUWEN.equals(this.techType))
            factBody = JSON.parseObject(jsonString, ImageTextParam.class);                       
        else if (MsgConstant.MSG_TECHTYPE_TEMP.equals(this.techType))
        	factBody = JSON.parseObject(jsonString, TemplateParam.class);                    
        else if (MsgConstant.MSG_TECHTYPE_RESOURCE.equals(this.techType))
            factBody = JSON.parseObject(jsonString, ResourceParam.class);
        else
            return;
        setMsgBody(factBody);
    }
    
    @Override
    public String toString(){
    	return JSON.toJSONString(this);
    }
    
    public String getSignName() {
        return signName;
    }
    
    public void setSignName(String signName) {
        this.signName = signName;
    }
}
