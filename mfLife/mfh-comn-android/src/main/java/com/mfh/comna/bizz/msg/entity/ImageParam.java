package com.mfh.comna.bizz.msg.entity;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;

/**
 * 图片消息
 */
public class ImageParam extends WxParam{
	private String title, description, picurl,url;
	
	public ImageParam(String title, String description, String picurl, String url) {
        super(WxMsgTypeConstant.IMG);
		this.title = title;
		this.description = description;
		this.picurl = picurl;
		this.url = url;
	}

    public ImageParam() {
        super(WxMsgTypeConstant.IMG);
    }

	public ImageParam(String title, String description,String url) {
		this(title, description, "", url);
	}

    @Override
    public String getSummary() {
        if (StringUtils.isNotBlank(title))
            return genShortMsg(title);
        else if (StringUtils.isNotBlank(description))
            return genShortMsg(title);
        else
            return "发了一张图片";
    }

    @Override
    public String getContent() {
        String ret = "";
        if (description != null && description.length() > 0)
            ret = title + "\r\n" + description;
        else
            ret = title;
        if (picurl != null)
            ret += "\r\n图片地址:" + picurl.toString();
        if (url != null)
            ret += "\r\n外部链接:" + url.toString();
        return ret;
    }

    /**
     * 从json串中构造
     * @param rawString
     * @return
     */
    public static ImageParam fromJson(String rawString) {
        JSONObject json = JSONObject.parseObject(rawString);
        ImageParam ret = JSONObject.toJavaObject(json, ImageParam.class);
        return ret;
    }

	public String getTitle() {
		return title;
	}
	
	public void setSignname(String signname){
		setTitle(signname+getTitle());
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString(){
        return JSONObject.toJSONString(this);
	}
}
