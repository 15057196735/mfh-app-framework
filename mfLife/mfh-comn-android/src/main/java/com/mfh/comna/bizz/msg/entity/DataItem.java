package com.mfh.comna.bizz.msg.entity;


import com.mfh.comna.bizz.material.entity.MsgAttr;

public class DataItem {
	private String title,description,picurl,url;

    public DataItem() {//缺省构造函数，序列化需要。
        super();
    }

    public DataItem(String title, String description, String picurl, String url) {
		this.title = title;
		this.description = description;
		this.picurl = picurl;
		this.url = url;
	}
	
	public DataItem(String title, String description,String url) {
		this(title, description, "", url);
	}

    /**
     * 转换
     * @return
     */
    public MsgAttr toMsgAttr() {
        MsgAttr ret = new MsgAttr();
        ret.setContent(getDescription());
        ret.setTitle(getTitle());
        ret.setPicurl(getPicurl());
        ret.setUrl(getUrl());
        return ret;
    }

	public String getTitle() {
		return title;
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
	
}
