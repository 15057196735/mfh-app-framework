package com.mfh.comn.bean.msg.param;

@SuppressWarnings("serial")
public class DataItem implements java.io.Serializable {
	private String title,description,picurl,url;
	
	/**
	 * json反序列化时需要	
	 */
	public DataItem() {
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
