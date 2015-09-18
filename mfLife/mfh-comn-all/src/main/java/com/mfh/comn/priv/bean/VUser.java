package com.mfh.comn.priv.bean;

import com.mfh.comn.annotations.NoTable;

/**
 * Created on 2008-6-2
 * 本类功能:结合了单位和用户的父子视图
 * 
 * @author zhangyz
 * @version 1.0
 * @see 
 */
@NoTable
public class VUser {
	private String id;
	private String name;
	private String pid;
	public VUser() {//有地方需要无参构造
        super();
    }
	
    public VUser(String id, String name, String pid) {
		super();
		this.id = id;
		this.name = name;
		this.pid = pid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
}
