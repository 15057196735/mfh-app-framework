package com.mfh.comna.bizz.msg.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 声音格式，但已经识别的
 * Created by Administrator on 14-5-13.
 */
public class VoiceParam extends WxParam{
    private String recognition;

    public VoiceParam() {
        super(WxMsgTypeConstant.VOICE);
    }

    public VoiceParam(String content){
        super(WxMsgTypeConstant.VOICE);
        this.recognition = content;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    @Override
    public String getSummary() {
        return genShortMsg(recognition);
    }

    @Override
    public String getContent() {
        return recognition;
    }

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
