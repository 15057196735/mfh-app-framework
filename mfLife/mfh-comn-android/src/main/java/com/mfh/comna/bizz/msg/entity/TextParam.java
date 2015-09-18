package com.mfh.comna.bizz.msg.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 文本消息
 */
public class TextParam extends WxParam {
	String content;

    @Override
    public String getSummary() {
        return genShortMsg(content);
    }

    public TextParam() {
        super(WxMsgTypeConstant.TEXT);
    }

    public TextParam(String content){
		super(WxMsgTypeConstant.TEXT);
		this.content = content;
	}

    /**
     * 从json串中构造
     * @param rawString
     * @return
     */
    public static TextParam fromJson(String rawString) {
        JSONObject json = JSONObject.parseObject(rawString);
        TextParam ret = JSONObject.toJavaObject(json, TextParam.class);
        return ret;
    }

	@Override
	public String toString(){
        return JSONObject.toJSONString(this);
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
		this.content = content + "\n-------------------------\n" + signname;
	}

	public String getRealSignname(String signname) {
		return "\n-------------------------\n" + signname;
	}
}
