/*
 * 文件名称: XmlParser.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-8
 * 修改内容: 
 */
package com.mfh.comn.net;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 负责对xml格式的通讯数据包进行解析
 * @author zhangyz created on 2014-3-8
 */
public class XmlParser extends BaseParser{
    public final String TAG_MAINRESULT = "Result";

    @Override
    public ResponseBody parser(String rawString) {
        DateFormat format = null;
        return parser(rawString, null, format);
    }

    @Override
    public <T> ResponseBody parser(String rawString, Class<T> beanClass) {
        DateFormat format = null;
        return parser(rawString, null, format);
    }

    @Override
    public <T> ResponseBody parser(String rawString, Class<T> beanClass, DateFormat dataFormat) {
        return null;
    }
    
    @Override
    public <T> ResponseBody parser(String rawString, Class<T> beanClass, String strDataFormat) {
        return parser(rawString, null, new SimpleDateFormat(strDataFormat));
    }
}
