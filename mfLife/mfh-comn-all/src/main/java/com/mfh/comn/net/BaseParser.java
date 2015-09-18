/*
 * 文件名称: IParser.java
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

/**
 * 响应串解析实现抽象接口
 * @author zhangyz created on 2014-3-8
 */
public abstract class BaseParser {
    //基础类的
    public final String TAG_RESULTCODE = "code";
    public final String TAG_RESULTINFO = "msg";
    public final String TAG_VERSION = "version";
    public final String TAG_MAINDATA = "data";
    public final String TAG_MAINHEADER = "head";
    
    //查询类的
    public final String TAG_QUERYRESULT = "result";
    public final String TAG_TEMP_cacheDomain = "cacheDomain";

    public final static String TAG_ROWS = "rows";
    public final static String TAG_TOTAL = "total";
    public final static String TAG_ROW = "row";
    public final static String TAG_CODE = "code";
    public final static String TAG_CAPTION = "caption";

    public final static String TAG_BEAN = "bean";    
    
    public final String TAG_DATAPROPS = "props";//针对json需要,xml自己本身有属性    
    public final String TAG_VAL = "val";
    public final String TAG_ACCESS = "access";
    public final String TAG_DEEP_TYPE ="deepType";
    public final String TAG_LEVEL_NUM = "levelNum";
    //参考com.alibaba.fastjson.util.TypeUtil
    protected String dataFormat;//日期格式化格式

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public abstract <T> ResponseBody parser(String rawString, Class<T> beanClass, String strDataFormat);

    /**
     * 从原始串解析出简单的Response对象，至少包含查询结果集
     * @param rawString 原始字串
     * @param beanClass 返回值的类型，对于返回map的无意义。
     * @param dataFormat 其中的日期属性格式，若同时有多个不同格式的日期属性则不支持。
     * @return
     * @author zhangyz created on 2014-3-8
     */
    public abstract <T> ResponseBody parser(String rawString, Class<T> beanClass, DateFormat dataFormat);

    /**
     * 从原始串解析出简单的Response对象，至少包含查询结果集
     * @param rawString 原始字串
     * @param beanClass 返回值的类型，对于返回map的无意义。
     * @return
     * @author zhangyz created on 2014-3-8
     */
    public abstract <T> ResponseBody parser(String rawString, Class<T> beanClass);

    /**
     * 从原始串解析出简单的Response对象
     * @param rawString
     * @return
     * @author zhangyz created on 2014-3-8
     */
    public abstract ResponseBody parser(String rawString);
}
