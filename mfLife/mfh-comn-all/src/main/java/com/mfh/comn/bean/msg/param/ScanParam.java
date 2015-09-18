/*
 * 文件名称: ScanParam.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: jguo
 * 修改日期: 2014-10-29
 * 修改内容: 
 */
package com.mfh.comn.bean.msg.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.mfh.comn.bean.msg.MsgConstant;

/**
 * 
 * @author jguo created on 2014-10-29
 */
@SuppressWarnings("serial")
public class ScanParam implements EmbBody{
    
	public ScanParam() {
        super();
    }

    String scancode;
	
	public String getScancode() {
		return scancode;
	}

	public void setScancode(String scancode) {
		this.scancode = scancode;
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
