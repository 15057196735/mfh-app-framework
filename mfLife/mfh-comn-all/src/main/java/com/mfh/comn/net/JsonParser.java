/*
 * 文件名称: JsonParser.java
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.TypeUtils;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspCodeDomain;
import com.mfh.comn.net.data.RspListBean;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comn.net.data.RspMap;
import com.mfh.comn.net.data.RspValue;


/**
 * 负责对json格式的通讯数据包进行解析
 * @author zhangyz created on 2014-3-8
 */
public class JsonParser extends BaseParser{
    
    public final static String TAG_JSONSTR = "jsonStr";
    public static SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat normalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat defaultFormat = shortFormat;

    /**
     * 把字符串解析成简单类型
     * @param rawString
     * @param beanClass
     * @return
     * @author zhangyz created on 2014-3-11
     */
    private <T> RspValue<T> genSimpValue(String rawString, Class<T> beanClass) {
        T value = TypeUtils.castToJavaBean(rawString, beanClass);
        return new RspValue<T>(value);        
        /*if (beanClass.equals(String.class))
            return (RspValue<T>) new RspValue<String>(rawString);
        else if (beanClass.equals(Integer.class))
            return (RspValue<T>) new RspValue<Integer>(Integer.parseInt(rawString));
        else if (beanClass.equals(Long.class))
            return (RspValue<T>) new RspValue<Long>(Long.parseLong(rawString));
        else if (beanClass.equals(Date.class)) {
            SimpleDateFormat format = new java.text.SimpleDateFormat(this.dataFormat);            
            try {
                return (RspValue<T>) new RspValue<Date>(format.parse(rawString));
            }
            catch (ParseException e) {
                throw new RuntimeException("不支持的日期字串:" + rawString);
            }
        }
        else if (beanClass.equals(Short.class)) {
            return (RspValue<T>) new RspValue<Short>(Short.parseShort(rawString));
        }
        else if (beanClass.equals(Double.class)) {
            return (RspValue<T>) new RspValue<Double>(Double.parseDouble(rawString));
        }
        else if (beanClass.equals(Float.class)) {
            return (RspValue<T>) new RspValue<Float>(Float.parseFloat(rawString));
        }
        else if (beanClass.equals(Timestamp.class)) {
            return (RspValue<T>) new RspValue<Timestamp>(Timestamp.valueOf(rawString));
        }
        else
            throw new RuntimeException("不支持的简单类型:" + beanClass.getName());*/
    }
    
    @Override
    public <T> ResponseBody parser(String rawString, Class<T> beanClass, DateFormat dataFormat) {
        if (dataFormat != null)//补丁
            DefaultJSONParser.dateFormatPatternThread.set(dataFormat);
        try {
            ResponseBody ret = new ResponseBody();
            System.out.print(String.format("Mfh: parser:%s", rawString));

            JSONObject jsonObj = (JSONObject)JSON.parse(rawString);
            //jsonObj = (JSONObject)jsonObj.get(TAG_MAINRESULT);//result部分直接为一级了。        
            //String head = jsonObj.getString(TAG_MAINHEADER);
            if (jsonObj == null){
                return ret;
            }

            Integer returnCode = jsonObj.getInteger(TAG_RESULTCODE);
            if (returnCode != null)
                ret.setRetCode(returnCode);

            String returnInfo = jsonObj.getString(TAG_RESULTINFO);
            ret.setReturnInfo(returnInfo);


            //返回版本
            Integer returnVersion = jsonObj.getInteger(TAG_VERSION);
            if (returnVersion != null) {
                ret.setVersion(returnVersion);
                if (returnVersion.intValue() == 1) {
                    //补丁：更改响应中的日期格式
                    defaultFormat = normalFormat;
                }
            }

            Object obj = jsonObj.get(TAG_MAINDATA);
            if (obj != null) {
                if (obj instanceof JSONObject){
                    System.out.print(String.format("Mfh: parser.data(JSONObject):%s", JSONObject.toJSONString(obj)));
                    JSONObject jsonData = (JSONObject)obj;//(JSONObject)jsonObj.getJSONObject(TAG_MAINDATA);
                    ret.setData(parseData(jsonData, beanClass));
                }
                else if (obj instanceof JSONArray) {
                    System.out.print(String.format("Mfh: parser.data(JSONArray):%s", JSONObject.toJSONString(obj)));
                    JSONArray array = (JSONArray)obj;
                    ret.setData(parseData(array, beanClass));
                }
                else {
                    System.out.print(String.format("Mfh: parser.data(JSONObject):%s", JSONObject.toJSONString(obj)));
                    ret.setData(genSimpValue(obj.toString(), beanClass));
                }
            }
            return ret;
        }
        catch(Throwable ex) {
            throw new RuntimeException("服务器返回数据格式出现错误:" + ex.getMessage(), ex);
        }
    }

    @Override
    public <T> ResponseBody parser(String rawString, Class<T> beanClass, String strDataFormat) {
        return parser(rawString, beanClass, new SimpleDateFormat(strDataFormat));
    }
    
    @Override
    public <T> ResponseBody parser(String rawString, Class<T> beanClass) {
        DateFormat format = null;
        return parser(rawString, beanClass, format);
    }

    @Override
    public ResponseBody parser(String rawString) {
        DateFormat format = null;
        return parser(rawString, null, format);
    }

    private <T> IResponseData parseData(JSONArray array, Class<T> beanClass) {
        RspListBean<T> beanList = new RspListBean<T>();
        for (int ii = 0; ii < array.size(); ii++) {
            JSONObject jsonData = array.getJSONObject(ii);
            T bean = JSONObject.toJavaObject(jsonData, beanClass);
            beanList.addBean(bean);
        }
        return beanList;
    }

    /**
     * 解析具体的数据部分,data标签里面的：
     * 第一种:{total:12,rows:[{....}]},其中total和rows都是关键字，查询结果集
     * 第二种:{props:{key1:value1,key2:value2....}，其中props是关键字,map类型
     * 第三种:{prop1:value1,prop2:value2...}，单值，纯bean
     * 第四种:{val:.......},纯字符
     * 第五种:{bean:{...},caption:{...}}
     * 若混合存在还是比较难以分辨的
     * @param jsonData
     * @return 返回的结果数据
     * @author zhangyz created on 2014-3-8
     */
    private <T> IResponseData parseData(JSONObject jsonData, Class<T> beanClass) {
        IResponseData responseData = null;
        System.out.print(String.format("Mfh: parseData", JSONObject.toJSONString(jsonData)));
        if (jsonData.containsKey(TAG_ROWS) && jsonData.containsKey(TAG_TOTAL)){//结果集
            responseData = parseQueryResult(jsonData, beanClass);
        }
        else if (jsonData.containsKey(TAG_DATAPROPS)) {//hashMap
            JSONObject jsonProps = (JSONObject)jsonData.get(TAG_DATAPROPS);
            responseData = parsePorps(jsonProps);
        }
        else if (jsonData.containsKey(TAG_VAL)) {//单独值            
            responseData = genSimpValue(jsonData.getString(TAG_VAL), beanClass);
        }
        else if (jsonData.containsKey(TAG_BEAN) && jsonData.containsKey(TAG_CAPTION)) {//单独bean，但是属于EntityWrapper
            //JSONObject wrapperJson = jsonData.getJSONObject(TAG_BEAN);
            EntityWrapper<T> wrapper = parserEntityWrapper(jsonData, beanClass);
            responseData = wrapper;
        }
        /*else if (jsonData.containsKey(TAG_DEEP_TYPE) && jsonData.containsKey(TAG_LEVEL_NUM)) {//编码
            responseData = parentCodeDomain(jsonData, beanClass);
        }*/
        else if (beanClass == null){
            //按map解析
            responseData = parsePorps(jsonData);
        }
        else {
            RspBean<T> bean = new RspBean<T>(JSONObject.toJavaObject(jsonData, beanClass));
            System.out.print(String.format("Mfh: parseData(RspBean):%s", JSONObject.toJSONString(bean)));
            responseData = bean;            
        }
        return responseData;

        /*RspMixData mixData = null;        
        int partSize = jsonData.size();
        if (partSize > 1)
            mixData = new RspMixData();
        Iterator<String> iter = jsonData.keySet().iterator();
        String key;
        while (iter.hasNext()) {
            key = iter.next();
            if (key.equals(TAG_QUERYRESULT)) {
                JSONObject jsonResult = (JSONObject)jsonData.get(key);
                QueryResult<?> result = parseQueryResult(jsonResult, beanClass);
                if (mixData != null)
                    mixData.setDataQueryResult(result);
                responseData = result;
            }
            else if (key.equals(TAG_DATAPROPS)) {
                JSONObject jsonProps = (JSONObject)jsonData.get(key);
                RspMap rspMap = parsePorps(jsonProps);
                if (mixData != null)
                    mixData.setDataKeyValue(rspMap);
                responseData = rspMap;
            }
        }
        if (mixData != null)
            return mixData;
        else
            return responseData;*/
    }
    
    /**
     * 解析Data里的属性参数
     * @param jsonProps
     * @param jsonProps
     * @author zhangyz created on 2013-5-14
     */
    private RspMap parsePorps(JSONObject jsonProps) {
        RspMap ret = new RspMap();
        Iterator<String> iter = jsonProps.keySet().iterator();
        String key,value;
        while (iter.hasNext()) {
            key = iter.next();
            value = jsonProps.getString(key);
            ret.addDataParam(key, value);
        }
        return ret;
    }
    
    /**
     * 解析查询输出部分
     * @param jsonResult
     * @author zhangyz created on 2013-5-14
     */
    private <T> RspQueryResult<T> parseQueryResult(JSONObject jsonResult, Class<T> beanClass) {
        RspQueryResult<T> result = new RspQueryResult<T>();
        Long total = jsonResult.getLong(TAG_TOTAL);
        if (total != null)
            result.setTotalNum(total);
        JSONArray dataArray = jsonResult.getJSONArray(TAG_ROWS);
        
        JSONArray fieldArray =  jsonResult.getJSONArray("head");
        
        if (fieldArray != null) {//字段名和类型单独拿出来定义,如果存在的话
            List<QfiledBase> fields = parseResultField(fieldArray);
            result.setRowFields(fields);
        }  
        result.setRowDatas(parseResultRows(dataArray, beanClass));
        
        return result;
    }

    /**
     *
     * @param jsonRow
     * @param beanClass
     * @param <T>
     * @return
     */
    private <T> RspCodeDomain<T> parentCodeDomain(JSONObject jsonRow, Class<T> beanClass) {
        RspCodeDomain<T> result = new RspCodeDomain<T>();
        //Long total = jsonRow.getLong(TAG_JSONSTR)
        JSONArray fieldArray = jsonRow.getJSONArray("head");
        JSONArray dataArray = jsonRow.getJSONArray("options");
        if (fieldArray != null) {
            List<QfiledBase> fields = parseResultField(dataArray);
            result.setRowFields(fields);
        }
        result.setRowDatas(parentChildItems(dataArray, beanClass));
        return result;
    }

    /**
     * 解析字段列表
     * @param fieldArray
     * @return
     * @author zhangyz created on 2013-5-14
     */
    private List<QfiledBase> parseResultField(JSONArray fieldArray) {
        List<QfiledBase> fields = new ArrayList<QfiledBase> ();
        JSONObject jsonObj;
        QfiledBase field;
        for (int ii = 0; ii < fieldArray.size(); ii++ ){
            field = new QfiledBase();
            
            jsonObj = fieldArray.getJSONObject(ii);
            jsonObj = jsonObj.getJSONObject("field");
            field.setName(jsonObj.getString("name"));
            field.setCaption(jsonObj.getString("caption"));
            
            fields.add(field);
        }
        return fields;
    }


    
    /**
     * 解析出一个wrapperObject对象
     * @param jsonRow
     * @param beanClass
     * @return
     * @author zhangyz created on 2014-3-10
     */
    @SuppressWarnings("unchecked")
    private <T> EntityWrapper<T> parserEntityWrapper(JSONObject jsonRow, Class<T> beanClass) {
        JSONObject jsonBean, jsonCaption;
        T entity = null;
        Map<String, String> map = null;
        jsonBean = jsonRow.getJSONObject(TAG_BEAN);
        jsonCaption = jsonRow.getJSONObject(TAG_CAPTION);
        if (jsonBean == null) {
            if (beanClass.equals(JSONObject.class))
                entity = (T)jsonRow;//直接返回json对象
            else
                entity = JSONObject.toJavaObject(jsonRow, beanClass);//认为直接就是bean了,兼容老格式。
        }
        else {
            if (beanClass.equals(JSONObject.class))
                entity = (T)jsonRow;//直接返回json对象
            else {
                entity = JSONObject.toJavaObject(jsonBean, beanClass);
                map = JSONObject.toJavaObject(jsonCaption, Map.class);
            }
        }
        EntityWrapper<T> wrapper = new EntityWrapper<T>(entity, map);        
        return wrapper;
    }

    private <T> ParentChildItem<T> parserChildItem(JSONObject jsonRow, Class<T> beanClass) {
        T entity = null;
        entity = JSONObject.toJavaObject(jsonRow, beanClass);
        ParentChildItem<T> childItem = new ParentChildItem<T>(entity);
        return childItem;
    }
    
    /**
     * 解析查询数据集--新版
     * @param dataArray
     * @param beanClass
     * @return
     * @author zhangyz created on 2014-3-8
     */
    private <T> List<EntityWrapper<T>> parseResultRows(JSONArray dataArray, Class<T> beanClass){
        List<EntityWrapper<T>> rows = new ArrayList<EntityWrapper<T>>();
        try {         
            JSONObject jsonRow;
            for (int ii = 0; ii < dataArray.size(); ii++) {
                jsonRow = dataArray.getJSONObject(ii);
                
                EntityWrapper<T> wrapper = parserEntityWrapper(jsonRow, beanClass);                
                rows.add(wrapper);
                
                /*//第二种
                entity = JSONObject.toJavaObject(jsonRow, beanClass);
                rows.add(entity);*/
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("解析数据失败:" + ex.getMessage(), ex);
        }
        return rows;        
    }

    private <T> List<ParentChildItem<T>> parentChildItems(JSONArray dataArray, Class<T> beanClass) {
        List<ParentChildItem<T>> rows = new ArrayList<ParentChildItem<T>>();
        JSONObject jsonRow;
        for (int ii = 0;ii < dataArray.size(); ii++) {
            jsonRow = dataArray.getJSONObject(ii);
            ParentChildItem<T> childItem = parserChildItem(jsonRow, beanClass);
            rows.add(childItem);
        }
        return rows;
    }
}
