/*
 * 文件名称: BusinessException.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-10
 * 修改内容: 
 */
package com.mfh.comna.comn.bean;

/**
 * 业务异常逻辑
 * @author zhangyz created on 2014-3-10
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException{
    
    public BusinessException(String detailMessage) {
        super(detailMessage);
    }
}
