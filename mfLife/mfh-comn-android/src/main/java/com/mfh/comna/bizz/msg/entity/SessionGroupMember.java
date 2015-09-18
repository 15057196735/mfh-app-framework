package com.mfh.comna.bizz.msg.entity;

import com.mfh.comna.bizz.member.entity.Ihuman;

/**
 * 参与会话的人关联会话的信息
 */
public class SessionGroupMember implements Ihuman {

	private Long id;
	private String name = "未知";
	private String guid;
	private Long sessionid;
	private Integer status ;//1 会话中 0 离开
	private String headimageurl = "";//头像
	private int isdefault;//0=默认成员，不可移除

    @Override
	public Long getId() {
		if (id == null)
            return (long)guid.hashCode();
        else
            return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    @Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    @Override
	public String getGuid() {
		return guid;
	}

    @Override
    public String getLetterIndex() {
        return null;
    }

    public void setGuid(String guid) {
		this.guid = guid;
	}
	public Long getSessionid() {
		return sessionid;
	}
	public void setSessionid(Long sessionid) {
		this.sessionid = sessionid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getHeadimageurl() {
		return headimageurl;
	}

    public String getHeadimage() {return headimageurl;}

	public void setHeadimageurl(String headimageurl) {
		this.headimageurl = headimageurl;
	}
	public int getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}
	
}
