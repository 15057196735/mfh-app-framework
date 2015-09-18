package com.mfh.comn.priv.bean;

/**
 * Created on 2010-12-2
 * 本类功能:结合了单位和子单位视图
 * 
 * @author jinj
 * @version 1.0
 * @see 
 */
public class VOffice {
	private String id;
	private String name;
	private String pid;
	
	public VOffice(String id, String name, String pid) {
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
