package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.JSON;

public class TemplateValueItem {
	private String color,value;
	
	/**
	 * 默认构造函数，json需要	
	 */
	public TemplateValueItem() {
        super();
    }

    public TemplateValueItem(String value,String color) {
		this.color = color;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
