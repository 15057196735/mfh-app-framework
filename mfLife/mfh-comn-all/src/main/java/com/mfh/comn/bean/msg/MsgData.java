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
 * 消息本身信息
 * @author zhangyz created on 2014-10-18
 */
@SuppressWarnings("serial")
public class MsgData implements Serializable {
    private String id;//消息编号    
    private Integer bizType = MsgConstant.MSG_BIZTYPE_CHAT;//业务类型,不能为空
    private String clsName = null;
    private String type = MsgConstant.MSG_TECHTYPE_JSON;//技术类型,不能为空    
    private Integer rli;//消息可靠性0~3, 0代表普通，3最高
    private Integer pri;//消息优先级,0~n， 0代表普通， 数字越大优先级越高
    private Integer zip;//压缩类型，备用
    private String sn;//消息签名

    private Object body; //具体消息内容,不能为空
    
    @JSONField (format="yyyy-MM-dd HH:mm:ss")  
    private Date time;//消息创建时间
    private Integer ver = 0;//版本号
        
    /**
     * 无参构造函数，反序列化时需要
     */
    public MsgData() {
        super();
    }

    public MsgData(String msg) {
        super();
        this.time = new Date();
        this.bizType = MsgConstant.MSG_BIZTYPE_CHAT;
        this.type = MsgConstant.MSG_TECHTYPE_TEXT;
        this.setBody(new TextParam(msg));
    }

    public MsgData(String msg, Integer bizType) {
        super();
        this.time = new Date();
        this.bizType = bizType;
        this.setBody(new TextParam(msg));
    }
    
    /**
     * 构造函数，构造一个聊天业务类型的消息
     * @param msgBody
     */
    public MsgData(EmbBody msgBody) {
        super();
        this.time = new Date();
        this.setBody(msgBody);
    } 
    
    public MsgData(EmbBody msgBody, Integer bizType) {
        super();
        this.body = msgBody;
        this.time = new Date();
        this.bizType = bizType;
        this.setBody(msgBody);
    }

    /**
     * 构造函数，构造一个聊天业务类型的消息,同时指定消息Id
     * @param msgBody
     */
    public MsgData(EmbBody msgBody, String msgId) {
        super();
        this.id = msgId;
        this.time = new Date();
        this.bizType = MsgConstant.MSG_BIZTYPE_CHAT;
        this.setBody(msgBody);
    }
    
    public String getId() {
        return id;
    }
    
    public String getClsName() {
        if (StringUtils.isBlank(clsName)) {
        	if(body != null){
                if (body instanceof EmbBody) {
                    this.setClsName(body.getClass().getName());
                }
        	}
        }
        return clsName;
    }
    
    public void setClsName(String bodyClassName) {
        this.clsName = bodyClassName;
    }

    public void setId(String msgId) {
        this.id = msgId;
    }

    public Integer getBizType() {
        return bizType;
    }
    
    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String techType) {
        this.type = techType;
    }
    
    public Integer getRli() {
        return rli;
    }
    
    public void setRli(Integer reliability) {
        this.rli = reliability;
    }
    
    public Integer getPri() {
        return pri;
    }
    
    public void setPri(Integer priority) {
        this.pri = priority;
    }
    
    public Integer getZip() {
        return zip;
    }
    
    public void setZip(Integer zipType) {
        this.zip = zipType;
    }    
    
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    public Date getTime() {
		return time;
	}

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
	public void setTime(Date createTime) {
		this.time = createTime;
	}

	public Object getBody() {
        return body;
    }
    
	/**
	 * 设置消息体，自动判断消息技术类型
	 * @param msgBody
	 * @author zhangyz created on 2014-10-30
	 */
    public void setBody(Object msgBody) {
        this.body = msgBody;
//        if (msgBody != null) {
//            if (msgBody instanceof EmbBody) {
//                this.type = ((EmbBody)msgBody).getType();
//            }
//        }
    }
    
    /**
     * 本身自解析body部分
     * @throws Exception
     * @author zhangyz created on 2014-10-29
     */
    public void parseBodySelf() throws Exception {
        if (this.body == null)
            return;
        String jsonString = this.body.toString();
        Object factBody = null;
        if (MsgConstant.MSG_TECHTYPE_JSON.equals(this.type) && this.clsName != null)
            factBody = JSON.parseObject(jsonString, Class.forName(this.clsName));
        else if (MsgConstant.MSG_TECHTYPE_TEXT.equals(this.type) || MsgConstant.MSG_TECHTYPE_EMOTION.equals(this.type))
            factBody = JSON.parseObject(jsonString, TextParam.class); 
        else if (MsgConstant.MSG_TECHTYPE_RAW.equals(this.type))
            return; 
        else if (MsgConstant.MSG_TECHTYPE_IMAGE.equals(this.type))
            factBody = JSON.parseObject(jsonString, ImageParam.class);  
        else if (MsgConstant.MSG_TECHTYPE_VOICE.equals(this.type))
            factBody = JSON.parseObject(jsonString, VoiceParam.class);            
        else if (MsgConstant.MSG_TECHTYPE_TUWEN.equals(this.type))
            factBody = JSON.parseObject(jsonString, ImageTextParam.class);                       
        else if (MsgConstant.MSG_TECHTYPE_TEMP.equals(this.type))
        	factBody = JSON.parseObject(jsonString, TemplateParam.class);                    
        else if (MsgConstant.MSG_TECHTYPE_RESOURCE.equals(this.type))
            factBody = JSON.parseObject(jsonString, ResourceParam.class);
        else
            return;
        setBody(factBody);
    }
    
    @Override
    public String toString(){
    	return JSON.toJSONString(this);
    }
    
    public String getSn() {
        return sn;
    }
    
    public void setSn(String signName) {
        this.sn = signName;
    }
    
    public Integer getVer() {
        return ver;
    }
    
    public void setVer(Integer ver) {
        this.ver = ver;
    }
}
