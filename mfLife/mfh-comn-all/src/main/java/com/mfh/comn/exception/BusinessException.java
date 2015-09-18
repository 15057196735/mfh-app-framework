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
package com.mfh.comn.exception;

/**
 * 业务异常逻辑
 * @author zhangyz created on 2014-3-10
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException{

    public BusinessException(String message)
    {
        super(message);
    }        

    public BusinessException(Exception e) {
        super(e);
    }

    public BusinessException(String message, Exception e)
    {
        super(message, e);
    }

    public String getSource()
    {
        return this.getMessage();
    }
    
    public static String getExDetailInfo(Exception ex){
        java.io.StringWriter out = new java.io.StringWriter();
        ex.printStackTrace(new java.io.PrintWriter(out));
        String ret = out.toString();
        return ret;
    }
    
    public String getDetailInfo(){
        String ret = getExDetailInfo(this);
        ret = ret.replaceAll("\r\n", "<br>");
        return ret;
    }
}
