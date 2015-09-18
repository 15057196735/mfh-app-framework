/*
 * 文件名称: ResponseData.java
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

import java.util.HashMap;
import java.util.Map;

/**
 * 响应的数据部分, T可能是QueryResult、String、Map<String, String>三类。
 * @author zhangyz created on 2014-3-8
 */
public class RspMap implements IResponseData{
    private Map<String, String> value;
    
    public Map<String, String> getValue() {
        return value;
    }
    
    public void setValue(Map<String, String> value) {
        this.value = value;
    }
    
    /**
     * 增加一个参数
     * @param paramName
     * @param paramValue
     * @author zhangyz created on 2013-5-14
     */
    public void addDataParam(String paramName, String paramValue) {
        if (value == null)
            value = new HashMap<String, String>();
        value.put(paramName, paramValue);
    }
}
