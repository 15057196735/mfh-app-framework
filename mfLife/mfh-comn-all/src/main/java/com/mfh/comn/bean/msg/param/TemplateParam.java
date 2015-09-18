package com.mfh.comn.bean.msg.param;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;
import com.mfh.comn.bean.msg.TemplateIdConst;
/**
 * 消息桥模板消息定义
 * 
 * @author zhangyz created on 2014-11-13
 */
@SuppressWarnings("serial")
public class TemplateParam implements EmbBody {
	
	static final String defaultTopColor = "";
	
	static final String defaultColor = "";
	
	private Long templateid;//模板素材编号,统一在我们的素材库中编号
	
	private String url,topcolor;
	
	private Map<String, TemplateValueItem> data = new HashMap<String, TemplateValueItem>();
	
	/**
	 * 把模板消息转成普通消息
	 * @return
	 * @author zhangyz created on 2014-11-11
	 */
	public BaseParam toBaseParam() {
		
		String temString = TemplateIdConst.getTemplateStr(templateid);
		
		for(String key : data.keySet()){
			temString = temString.replace("{{" + key + ".DATA}}", data.get(key).getValue());
		}
		
		ImageTextParam imageTextParam = new ImageTextParam();
		DataItem dataItem = new DataItem(MsgConstant.MsgTitle.SYS, url, temString);
		imageTextParam.AddDateItem(dataItem);

	    return imageTextParam;
	}
	
	public TemplateParam(){
		super();
	}

	/**
	 * @param templateid
	 * @param url
	 * @param topcolor
	 */
	public TemplateParam(Long templateid, String url, String topcolor) {
		super();
		this.templateid = templateid;
		this.url = url;
		this.topcolor = topcolor;
	}
	
	public TemplateParam(Long tempalteid,Map<String, Object> keyValue) {
		this(tempalteid, "", keyValue);
	}
	
	public TemplateParam(Long tempalteid,String url,Map<String, Object> keyValue) {
		this(tempalteid,url,defaultTopColor);
		for(String key : keyValue.keySet()){
			this.addData(key, keyValue.get(key).toString());
		}
	}

	public Map<String, TemplateValueItem> getData() {
		return data;
	}

	public TemplateParam setData(Map<String, TemplateValueItem> data) {
		this.data = data;
		return this;
	}
	
	public TemplateParam addData(TemplateDataItem data) {
		if(!this.data.containsKey(data.getKey())){
			this.data.put(data.getKey(), data.getValue());
		}
		return this;
	}
	
	public TemplateParam addData(String key,String value,String color) {
		if(!this.data.containsKey(key)){
			this.data.put(key, new TemplateValueItem(value, color));
		}
		
		return this;
	}

	public TemplateParam addData(String key,String value) {
		if(!this.data.containsKey(key)){
			this.data.put(key, new TemplateValueItem(value, defaultColor));
		}
		return this;
	}

	public Long getTemplateid() {
		return templateid;
	}

	public TemplateParam setTemplateid(Long templateid) {
		this.templateid = templateid;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public TemplateParam setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public TemplateParam setTopcolor(String topcolor) {
		this.topcolor = topcolor;
		return this;
	} 
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	public void attachSignName(String name) {
		
	}

    @Override
    public boolean haveSignName() {
        return true;
    }

	@Override
	@JSONField(serialize=false)
	public String getType() {
		return MsgConstant.MSG_TECHTYPE_TEMP;
	}
}
