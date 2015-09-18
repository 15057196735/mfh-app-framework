package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

@SuppressWarnings("serial")
public class TextParam extends BaseParam {

	private String content;

	public TextParam() {
        super();
    }

    public TextParam(String content){
		super();
		this.content = content;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content,String signname) {
		this.content = content + SIGN_LINE + signname;
	}
	
	@Override
	public void attachSignName(String signname) {
	    this.content = content + SIGN_LINE + signname;
	}

    @Override
    public boolean haveSignName() {
        return content != null && (content.lastIndexOf(SIGN_LINE) > 0);
    }

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_TEXT;
    }
}
