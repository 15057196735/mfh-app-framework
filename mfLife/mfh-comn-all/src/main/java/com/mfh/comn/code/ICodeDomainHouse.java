/*
 * 文件名称: ICodeDomainHouse.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-8-12
 * 修改内容: 
 */
package com.mfh.comn.code;

import com.alibaba.fastjson.JSONObject;


/**
 * 支持编码域和编码转换的接口
 * @author zhangyz created on 2014-8-12
 */
public interface ICodeDomainHouse<T> extends ICodeHouse<T>{
    
    /**
     * 将指定的列表输出成json格式
     * @param theOptions
     * @return
     * @author zhangyz created on 2014-6-18
     */
    public JSONObject getJsonObject();
    
    /**
     * 是树形还是简单型
     * @return
     * @author zhangyz created on 2014-8-12
     */
    public boolean isTreeAble();
}
