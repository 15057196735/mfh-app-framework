package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.JSON;

public class TemplateDataItem {
	private String key;
	
	TemplateValueItem value;	
	
	public TemplateDataItem() {
        super();
    }

    public TemplateDataItem(String key, TemplateValueItem templateValueItem) {
		this.key = key;
		this.value = templateValueItem;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public TemplateValueItem getValue() {
		return value;
	}

	public void setValue(TemplateValueItem value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
