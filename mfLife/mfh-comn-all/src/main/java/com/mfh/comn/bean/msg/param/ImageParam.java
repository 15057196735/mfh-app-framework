package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

@SuppressWarnings("serial")
public class ImageParam extends BaseParam{
	
	private String description,picurl,title;
	
	public ImageParam() {
        super();
    }

    public ImageParam(String title, String description, String picurl) {
		super();
		this.description = description;
		this.picurl = picurl;
		this.title = title;
	}

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_IMAGE;
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
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
    
    @Override
    public void attachSignName(String signname) {
        //使用SIGN_LINE
    	this.title = signname;
    }

    @Override
    public boolean haveSignName() {
        return title != null && (title.lastIndexOf(SIGN_LINE) > 0);
    }
}
