/*
 * 文件名称: IMsgHttp.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: jguo
 * 修改日期: 2014-11-3
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

import com.mfh.comn.bean.msg.IResponse;
import com.mfh.comn.bean.msg.IMsgRequest;

/**
 * 上行消息处理接口
 * @author jguo created on 2014-11-3
 */
public interface IPointMsgListener {
	 /**
     * 适配器接收到本渠道内客户端上行消息的内部处理
     * @param IMsgRequest
     * @throws Exception
     * @author jguo created on 2014-11-03
     */
    public abstract IResponse onUpMessage(IMsgRequest request) throws Exception;
}
