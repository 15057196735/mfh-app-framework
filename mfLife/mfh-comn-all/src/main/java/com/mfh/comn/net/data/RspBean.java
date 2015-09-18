/*
 * 文件名称: RspBean.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-8
 * 修改内容: 
 */
package com.mfh.comn.net.data;

/**
 * 单独的bean对象
 * @author zhangyz created on 2014-3-8
 */
public class RspBean <T> implements IResponseData{
    private T value;

    public RspBean(T value) {
        super();
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
}
