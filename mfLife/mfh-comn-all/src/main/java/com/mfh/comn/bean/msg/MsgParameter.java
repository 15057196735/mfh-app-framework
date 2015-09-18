/*
 * 文件名称: MsgParameter.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-10-13
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息内容,包括一系列消息头属性和一个消息体属性。
 * <T> 其中泛型是指消息体的类型
 * @author zhangyz created on 2014-10-13
 */
@SuppressWarnings("serial")
public class MsgParameter implements Serializable {    
    private final static String TAG_ORIGION_ID = "msg_origionId:";
    private FromInfo from;//发送方信息
    private DestInfo to;//接收方信息    
    private MsgData msgBean;//消息内容

    //private String tagOne;//消息标签1，备用
    //private String tagTwo; //消息标签2，备用
    //private String tagThree; //消息标签3，备用
    private Map<String, String> meta = new HashMap<String, String>();
    
    /**
     * 无参构造函数，反序列化时需要
     */
    public MsgParameter() {
        super();
    }

    public MsgParameter(MsgData msgBean) {
        super();
        this.msgBean = msgBean;
    }
    
    /**
     * 检查并返回真正的消息发送方Id
     * @return
     * @author zhangyz created on 2014-11-20
     */
    public Long checkOrigionId() {
        String tagOne = this.getTagOne();
        if (tagOne == null)
            return null;
        else if (tagOne.startsWith(TAG_ORIGION_ID)) {
            Long oId = Long.parseLong(tagOne.substring(TAG_ORIGION_ID.length()));
            this.setTagOne(null);
            return oId;
        }
        else
            return null;
    }
    
    /**
     * 附加上真正的消息发送方Id
     * @param origionId
     * @author zhangyz created on 2014-11-20
     */
    public void attachOrigionId(Long origionId) {
        this.setTagOne(TAG_ORIGION_ID + origionId);
    }
        
    @Override
    public String toString(){
    	return JSON.toJSONString(this);
    }
    
    public MsgData getMsgBean() {
		return msgBean;
	}

	public void setMsgBean(MsgData msgBean) {
		this.msgBean = msgBean;
	}

    @JSONField(serialize=false)
	public String getTagOne() {
        return meta.get("tagOne");
    }

    @JSONField(serialize=false)
    public void setTagOne(String tagOne) {
        meta.put("tagOne", tagOne);
    }

    @JSONField(serialize=false)
    public String getTagTwo() {
        return meta.get("tagTwo");
    }

    @JSONField(serialize=false)
    public void setTagTwo(String tagTwo) {
        meta.put("tagTwo", tagTwo);
    }

    @JSONField(serialize=false)
    public String getTagThree() {
        return meta.get("tagThree");
    }

    @JSONField(serialize=false)
    public void setTagThree(String tagThree) {
        meta.put("tagThree", tagThree);
    }

    @JSONField(serialize=false)
    public Long getSid() {
        return getTo().getSid();
    }

    @JSONField(serialize=false)
    public void setSessionId(Long sid) {
        this.getTo().setSid(sid);
    }

    @JSONField(serialize=false)
    public PhysicalPoint getFromPhysicalPoint() {
        return this.getFrom().getPp();
    }

    @JSONField(serialize=false)
    public Long getFromGuid() {
        return this.getFrom().getGuid();  
    }
    
    /**
     * 填充tag信息
     * @param tagInfo
     * @author zhangyz created on 2014-10-23
     */
    public void fillTagInfo(String... tagInfo) {
        if (tagInfo != null) {
            if (tagInfo.length > 0)
                setTagOne(tagInfo[0]);
            if (tagInfo.length > 1)
                setTagTwo(tagInfo[1]);
            if (tagInfo.length > 2)
                setTagThree(tagInfo[2]);
        }
    }
    
    /**
     * 是否来源者信息都具备，物理和逻辑端点都有.
     * 其实业务层借此想判断是否已经绑定过
     * @return
     * @author zhangyz created on 2014-10-29
     */
    public boolean haveAllFrom() {
        if (from != null)
            return from.haveAllFrom();
        else
            return false;
    }
    
    public FromInfo getFrom() {
        if (from == null)
            from = new FromInfo();
        return from;
    }
    
    public void setFrom(FromInfo from) {
        this.from = from;
    }
    
    public DestInfo getTo() {
        if (to == null)
            to = new DestInfo();
        return to;
    }    
    
    public Map<String, String> getMeta() {
        return meta;
    }
    
    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    /**
     * 添加一个元数据
     * @param metaName
     * @param metaValue
     * @author zhangyz created on 2015-3-20
     */
    public void addMeta(String metaName, String metaValue) {
        meta.put(metaName, metaValue);
    }
    
    /**
     * 设置目标信息
     * @param to
     * @author zhangyz created on 2015-3-20
     */
    public void setTo(DestInfo to) {
        this.to = to;
    }

    /**
     * 返回是否是模板消息
     * @return
     * @author jguo created on 2014-11-19
     */
    @JSONField(serialize=false)
    public boolean isTemplateMsg(){
    	return msgBean != null && MsgConstant.MSG_TECHTYPE_TEMP.equals(msgBean.getType());
    }

    @JSONField(serialize=false)
    public Integer getBind() {
        if(getFromGuid() == null || getFromGuid() < 0){
            return 0;
        }
        else
            return 1;
    }
}
