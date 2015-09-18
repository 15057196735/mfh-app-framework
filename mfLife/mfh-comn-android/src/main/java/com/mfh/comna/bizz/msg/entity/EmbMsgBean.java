/*
 * 文件名称: EmbMsgBean.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-11-10
 * 修改内容: 
 */
package com.mfh.comna.bizz.msg.entity;

import com.mfh.comna.MfhEntity;

/**
 * 包含了会话创建者的消息bean
 * @author zhangyz created on 2014-11-10
 */
@SuppressWarnings("serial")
public class EmbMsgBean extends MfhEntity<String> {
    private String param; //消息内容
    private Long fromguid; //发送者
    private String techType; //消息技术类型
    private Long sessionid; //会话编号，关联会话表
    private Integer isdel = 0; //删除标志
    //private Integer unread = 0; //是否未读，0-未读  1-已读
    private Integer fromchannelid; // 发送渠道
    private String channelpointid;//发送端点号    
    private String tagOne;//消息标签1，备用
    private String tagTwo; //消息标签2，备用
    private String tagThree; //消息标签3，备用
       
    private Long extparam;//额外业务关联信息
    private Integer bind ;//0:未绑定 1:绑定
    private Long guid;//创建人guid
    private String localheadimageurl;//头像
    private String pointName; //端点名 可空 对应point_type=1有用
        
    public String getParam() {
        return param;
    }

    
    public void setParam(String param) {
        this.param = param;
    }

    
    public Long getFromguid() {
        return fromguid;
    }

    
    public void setFromguid(Long fromguid) {
        this.fromguid = fromguid;
    }

    
    public String getTechType() {
        return techType;
    }

    
    public void setTechType(String techType) {
        this.techType = techType;
    }

    
    public Long getSessionid() {
        return sessionid;
    }

    
    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }

    
    public Integer getIsdel() {
        return isdel;
    }

    
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    
    public Integer getFromchannelid() {
        return fromchannelid;
    }

    
    public void setFromchannelid(Integer fromchannelid) {
        this.fromchannelid = fromchannelid;
    }

    
    public String getChannelpointid() {
        return channelpointid;
    }

    
    public void setChannelpointid(String channelpointid) {
        this.channelpointid = channelpointid;
    }

    
    public String getTagOne() {
        return tagOne;
    }

    
    public void setTagOne(String tagOne) {
        this.tagOne = tagOne;
    }

    
    public String getTagTwo() {
        return tagTwo;
    }

    
    public void setTagTwo(String tagTwo) {
        this.tagTwo = tagTwo;
    }

    
    public String getTagThree() {
        return tagThree;
    }

    
    public void setTagThree(String tagThree) {
        this.tagThree = tagThree;
    }

    /** 保存格式化后的时间显示 */
    private String formatCreateTime;
    
    public Long getExtparam() {
        return extparam;
    }
    
    public void setExtparam(Long extparam) {
        this.extparam = extparam;
    }
    
    public Integer getBind() {
        return bind;
    }
    
    public void setBind(Integer bind) {
        this.bind = bind;
    }
    
    public Long getGuid() {
        return guid;
    }
    
    public void setGuid(Long guid) {
        this.guid = guid;
    }
    
    public String getLocalheadimageurl() {
        return localheadimageurl;
    }
    
    public void setLocalheadimageurl(String localheadimageurl) {
        this.localheadimageurl = localheadimageurl;
    }
    
    public String getPointName() {
        return pointName;
    }
    
    public void setPointName(String pointName) {
        this.pointName = pointName;
    }
    
    public String getFormatCreateTime() {
        return formatCreateTime;
    }

    public void setFormatCreateTime(String formatCreateTime) {
        this.formatCreateTime = formatCreateTime;
    }    
    
}
