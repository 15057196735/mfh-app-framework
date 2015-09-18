package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

@SuppressWarnings("serial")
public class VoiceParam extends BaseParam{
	private String recognition;		
	
	public VoiceParam() {
        super();
    }

    public VoiceParam(String recognition) {
		this.recognition = recognition;
	}	

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_VIDEO;
    }
	
	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}
    
    @Override
    public void attachSignName(String signname) {
        
    }

    @Override
    public boolean haveSignName() {
        return true;
    }

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
