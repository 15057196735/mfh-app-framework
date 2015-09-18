/*
 * 文件名称: RegisteParam.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: jguo
 * 修改日期: 2014-10-25
 * 修改内容: 
 */
package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

/**
 * 各个渠道注册时，携带的注册信息参数类
 * @author jguo created on 2014-10-25
 */
@SuppressWarnings("serial")
public class RegisterParam implements EmbBody {
	
	String param;
	
	Integer bind = 0;
	
	public Integer getBind() {
		return bind;
	}

	public void setBind(Integer bind) {
		this.bind = bind;
	}

	public RegisterParam() {
		super();
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public void attachSignName(String name) {
		
	}

    @Override
    public boolean haveSignName() {
        return true;
    }

    @JSONField(serialize=false)
    @Override
    public String getType() {
        return MsgConstant.MSG_TECHTYPE_JSON;
    }
}
