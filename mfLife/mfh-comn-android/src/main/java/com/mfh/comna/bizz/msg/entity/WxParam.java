/*
 * 文件名称: WxParam.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-4-4
 * 修改内容: 
 */
package com.mfh.comna.bizz.msg.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息体基类
 * @author zhangyz created on 2014-4-4
 */
public abstract class WxParam {

	protected String type;//参见WxMsgTypeConstant定义

    /**
     * 格式化摘要消息
     * @param longMsg
     * @return
     */
    protected String genShortMsg(String longMsg) {
        if (longMsg == null || longMsg.length() == 0)
            return "空消息";
        else if (longMsg.length() > 15) {
            return longMsg.substring(0, 15) + "...";
        }
        else
            return longMsg;
    }

    /**
     * 获取可显示的文本摘要信息
     * @return
     */
    public abstract String getSummary();

    /**
     * 获取实际内容
     * @return
     */
    public abstract String getContent();

	public WxParam(String type) {
		super();
		this.type = type;
	}

    /**
     * 从json串中构造
     * @param rawString
     * @return
     */
    public static WxParam fromJson(String rawString) {
//        Log.d("Nat: WxParam.rawString",rawString);
        JSONObject json = JSONObject.parseObject(rawString);

        if(json == null){
            return new TextParam("不支持的消息媒体格式:");
        }

        String type = json.getString("type");
        JSONObject bodyObject = json.getJSONObject("body");
        if (WxMsgTypeConstant.TEXT.equals(type))
            return JSONObject.toJavaObject(bodyObject, TextParam.class);
        else if (WxMsgTypeConstant.IMG.equals(type))
            return JSONObject.toJavaObject(bodyObject, ImageParam.class);
        else if (WxMsgTypeConstant.TUWEN.equals(type))
            return JSONObject.toJavaObject(bodyObject, ImageTextParam.class);
        else if (WxMsgTypeConstant.VOICE.equals(type))
            return JSONObject.toJavaObject(bodyObject, VoiceParam.class);
        else
            return new TextParam("不支持的消息媒体格式:" + type);
    }

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
