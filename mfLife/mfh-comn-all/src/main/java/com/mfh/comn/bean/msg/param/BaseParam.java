/*
 * 文件名称: BaseParam.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: jguo
 * 修改日期: 2014-10-23
 * 修改内容: 
 */
package com.mfh.comn.bean.msg.param;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 媒体类消息基类
 * @author jguo created on 2014-10-23
 */
@SuppressWarnings("serial")
public abstract class BaseParam implements Serializable, EmbBody{

	/**
	 * @return the type
	 */
    @JSONField(serialize=false)
	public abstract String getType();
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
