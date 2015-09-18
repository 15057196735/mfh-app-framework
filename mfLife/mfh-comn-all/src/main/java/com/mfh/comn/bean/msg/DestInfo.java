/*
 * 文件名称: DestInfo.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-10-23
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

/**
 * 发送到目标信息。
 * @author zhangyz created on 2014-10-23
 */
@SuppressWarnings("serial")
public class DestInfo implements java.io.Serializable{
    /**
     * 下面三个不能同时为空
     */
    private Long sid;//会话Id，空代表无须会话，-1，代表需要新建会话，其他>0代表实际的会话ID
    private Long guid = null;//逻辑端点信息
    private PhysicalPoint pp = null;//目标物理端点信息    
    
    /**
	 * fastjson 反序列化是需要默认无参构造函数
	 */
	public DestInfo() {
		super();
	}

	/**
     * 构造函数
     * @param sessionId 会话编号
     */
    public DestInfo(Long sessionId) {
        super();
        this.sid = sessionId;
    }

    /**
     * 构造函数
     * @param toGuid 目标端编号
     * @param sessionId 空
     */
    public DestInfo(Long toGuid, Long sessionId) {
        super();
        this.guid = toGuid;
    }

    /**
     * 构造函数
     * @param toPhysicalPoint 物理端点信息
     */
    public DestInfo(PhysicalPoint toPhysicalPoint) {
        super();
        this.pp = toPhysicalPoint;
    }
    
    /**
     * 构造函数
     * @param channelId 物理渠道编号
     * @param pointId 物理端点号
     */
    public DestInfo(Integer channelId, String pointId, Integer channelType) {
        super();
        this.pp = new PhysicalPoint(channelId, channelType, pointId);
    }
        
    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getSid() {
        return sid;
    }
    
    public Long getGuid() {
        return guid;
    }
    
    public PhysicalPoint getPp() {
        return pp;
    }
    
    public void setPp(PhysicalPoint pp) {
        this.pp = pp;
    }
    
    public void setGuid(Long guid) {
        this.guid = guid;
    }
    
}
