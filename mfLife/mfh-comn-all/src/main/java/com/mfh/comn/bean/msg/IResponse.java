/*
 * 文件名称: IMsgRequest.java
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

import java.io.Serializable;

/**
 * 
 * @author jguo created on 2014-11-3
 */
public interface IResponse extends Serializable{
    /**
     * 返回码
     * @return
     * @author zhangyz created on 2015-3-20
     */
    public Integer getCode();
    
    /**
     * 返回码描述
     * @return
     * @author zhangyz created on 2015-3-20
     */
    public String getMsg();
    
    /**
     * 返回数据
     * @return
     * @author zhangyz created on 2015-3-20
     */
    public Object getData();
	
	
}
