/*
 * 文件名称: NetCallBack.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-10
 * 修改内容: 
 */
package com.mfh.comna.comn.database.dao;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.net.JsonParser;
import com.mfh.comn.net.ResponseBody;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comn.net.data.RspListBean;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comn.net.data.RspValue;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.comn.bean.BusinessException;
import com.mfh.comna.comn.logic.AsyncTaskCallBack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 为网络dao层请求提供的回调接口
 * @author zhangyz created on 2014-3-10
 */
public class NetCallBack {
    /**
     * 标准网络调用后回调，子类需要继承processResult方法，对成功结果继续处理。
     * 若需要对错误结果进行处理，亦可手工继承processFailure方法
     * @param <T> 返回的数据对象，若是列表则是item对象
     */
    public static abstract class NormalNetTask<T> extends NetTaskCallBack<T, NetProcessor.Processor<T>> {

        public NormalNetTask(Class<T> pojoClass) {
            super(null, pojoClass);
        }

        public NormalNetTask(Class<T> pojoClass, Context context) {
            super(null, pojoClass, context);
        }

        @Override
        protected void doFailure(Throwable t, String errMsg) {
            super.doFailure(t, errMsg);
            processFailure(t, errMsg);
        }

        @Override
        protected void doSuccessInner(IResponseData rspData){
            processResult(rspData);
        }

        /**
         * 错误处理,默认不需要
         * @param t
         * @param errMsg
         */
        protected void processFailure(Throwable t, String errMsg) { }

        /**
         * 执行成功回调
         * @param rspData
         */
        public abstract void processResult(IResponseData rspData);
    }

    /**
     * 与JAVA后台对接的通用网络回调接口。
     * @param <T> 返回的数据类型,随接口业务含义不同而不同。
     * @author zhangyz created on 2014-3-10
     */
    public static class NetTaskCallBack<T, P extends NetProcessor.Processor<T>> extends AsyncTaskCallBack<String> {
        protected Class<T> pojoClass = null; 
        protected P processor = null;
        protected DateFormat dataFormat = null;

        /**
         * 构造函数
         * @param pojoClass 返回结构中的数据部分的类型定义,参见ResponseBody
         */
        public NetTaskCallBack(P processor, Class<T> pojoClass) {
            super();
            this.processor = processor;
            this.pojoClass = pojoClass;
            this.dataFormat = JsonParser.defaultFormat;
        }
        
        public NetTaskCallBack(P processor, Class<T> pojoClass, Context context) {
            super(context);
            this.processor = processor;
            this.pojoClass = pojoClass;
            this.dataFormat = JsonParser.defaultFormat;
        }

        public NetTaskCallBack(NetProcessor.RspCodeDomainProcessor<T> processor, Class<T> pojoClass, Context context) {
            super(context);
            this.processor = (P) processor;
            this.pojoClass = pojoClass;
            this.dataFormat = JsonParser.defaultFormat;
        }

        public NetTaskCallBack<T,P> setDataFormat(DateFormat dataFormat) {
            if (dataFormat != null)
                this.dataFormat = dataFormat;
            return this;
        }

        public NetTaskCallBack<T,P> setDataFormat(String dataFormat) {
            this.dataFormat = new SimpleDateFormat(dataFormat);
            return this;
        }

        /**
         * 解析服务器返回的结果集。子类有机会再继承覆盖一下。
         * @param rawValue
         * @return
         */
        protected ResponseBody parserResponse(String rawValue) {
            JsonParser parser = new JsonParser();
            ResponseBody resp = parser.parser(rawValue, pojoClass, dataFormat);
            return resp;
        }

        @Override
        protected void doSuccess(String rawValue) {
            ResponseBody resp = parserResponse(rawValue);
            if (!resp.isSuccess()) {
                String errMsg = resp.getRetCode() + ":" + resp.getReturnInfo();
                this.doFailure(new BusinessException(errMsg), errMsg);
            }
            else {                
                IResponseData rspData = resp.getData();
                doSuccessInner(rspData);
            }
        }
        
        @Override
        protected void doFailure(Throwable t, String errMsg) {
            super.doFailure(t, errMsg);
            if (processor != null)
                processor.processFailure(t, errMsg);
        }
        
        /**
         * 网络调用成功后的内部回调接口
         * @param rspData 具体数据部分
         * @author zhangyz created on 2014-3-10
         */
        protected void doSuccessInner(IResponseData rspData){
            if (processor != null)
                processor.processResult(rspData);
        }
    }

    /**
     * 针对查询结果集的异步处理基类,中间通过json转换，给子类提供机会自行转换成实际的Bean
     *
     * @author zhangyz created on 2014-3-10
     */
    public static abstract class QueryRsCallBackOfJson<T> extends NetTaskCallBack<T, NetProcessor.QueryRsProcessor<T>> {
        public QueryRsCallBackOfJson(NetProcessor.QueryRsProcessor processor, Class pojoClass) {
            super(processor, pojoClass);
        }

        public QueryRsCallBackOfJson(NetProcessor.QueryRsProcessor processor, Class pojoClass, Context context) {
            super(processor, pojoClass, context);
        }

        @Override
        protected ResponseBody parserResponse(String rawValue) {
            JsonParser parser = new JsonParser();
            //固定使用Json接收返回对象
            ResponseBody resp = parser.parser(rawValue, JSONObject.class, dataFormat);
            return resp;
        }

        /**
         * 将原始的json串转换成实际的Bean对象
         * @param json
         * @return T对象
         */
        protected abstract T changeJsonToBean(JSONObject json);

        @Override
        protected void doSuccessInner(IResponseData rspData) {
            if (rspData != null && rspData instanceof RspQueryResult) {
                @SuppressWarnings("unchecked")
                RspQueryResult<JSONObject> rsRaw = (RspQueryResult<JSONObject>)rspData;
                RspQueryResult<T> rs = new RspQueryResult<T>();

                //执行拷贝和转换
                rs.setRowFields(rsRaw.getRowFields());
                rs.setTotalNum(rsRaw.getTotalNum());
                List<EntityWrapper<T>> newRs = new ArrayList<EntityWrapper<T>>();
                rs.setRowDatas(newRs);
                List<EntityWrapper<JSONObject>> rss = rsRaw.getRowDatas();
                for (EntityWrapper<JSONObject> item : rss) {
                    //bean转换
                    EntityWrapper<T> newItem = new EntityWrapper<T>(changeJsonToBean(item.getBean()));
                    newRs.add(newItem);
                }

                processor.setTotalNum(rs.getTotalNum());
                processor.processQueryResult(rs);
            }
            else {
                throw new RuntimeException("返回的格式不是查询结果集格式!");
            }
        }
    }

    /**
     * 针对分页查询结果集的异步处理基类
     * 
     * @author zhangyz created on 2014-3-10
     */
    public static class QueryRsCallBack<T> extends NetTaskCallBack<T, NetProcessor.QueryRsProcessor<T>> {
        /**
         * 构造参数（无须分页）
         * @param pojoClass 查询结果集中的bean类
         */
        public QueryRsCallBack(NetProcessor.QueryRsProcessor<T> processor, Class<T> pojoClass) {
            super(processor, pojoClass);
        } 
        
        /**
         * 构造函数（包含所有需要参数）
         * @param pojoClass 查询结果集中的bean类
         * @param context android上下文参数
         */
        public QueryRsCallBack(NetProcessor.QueryRsProcessor<T> processor, Class<T> pojoClass,Context context) {
            super(processor, pojoClass, context);
        }

        public QueryRsCallBack(NetProcessor.RspCodeDomainProcessor<T> processor, Class<T> pojoClass,Context context) {
            super(processor, pojoClass, context);
            new NetTaskCallBack(processor, pojoClass, context);
        }

        @Override
        protected void doSuccessInner(IResponseData rspData) {      
            if (rspData != null && rspData instanceof RspQueryResult) {
                @SuppressWarnings("unchecked")
                RspQueryResult<T> rs = (RspQueryResult<T>)rspData;
                processor.processResult(rs);
            }
//            else if (rspData != null && rspData instanceof RspCodeDomain) {
//
//            }
            else {
                throw new RuntimeException("返回的格式不是分页查询结果集格式!");
            }
        }
    }

    /**
     * 针对无分页查询结果集的异步处理基类
     * @author zhangyz created on 2014-3-10
     */
    public static class QueryListRsCallBack<T> extends NetTaskCallBack<T, NetProcessor.QueryListProcessor<T>> {
        /**
         * 构造参数
         * @param pojoClass 查询结果集中的bean类
         */
        public QueryListRsCallBack(NetProcessor.QueryListProcessor<T> processor, Class<T> pojoClass) {
            super(processor, pojoClass);
        }

        public QueryListRsCallBack(NetProcessor.QueryListProcessor<T> processor, Class<T> pojoClass,Context context) {
            super(processor, pojoClass, context);
        }

        @Override
        protected void doSuccessInner(IResponseData rspData) {
            if (rspData != null && rspData instanceof RspListBean) {
                processor.processResult(rspData);
            }
            else {
                throw new RuntimeException("返回的格式不是简单列表查询结果集格式!");
            }
        }
    }
    
    /**
     * 针对单bean查询结果集的异步处理基类
     * 
     * @author zhangyz created on 2014-3-10
     */
    public static class GetBeanCallBack<T> extends NetTaskCallBack<T, NetProcessor.BeanProcessor<T>> {
        public GetBeanCallBack(NetProcessor.BeanProcessor<T> processor, Class<T> pojoClass) {
            super(processor, pojoClass);
        } 
        
        public GetBeanCallBack(NetProcessor.BeanProcessor<T> processor,
                Class<T> pojoClass,Context context) {
            super(processor, pojoClass, context);
        }
              
        @SuppressWarnings("unchecked")
        @Override
        protected void doSuccessInner(IResponseData rspData) {  
            if (rspData != null && rspData instanceof EntityWrapper) {
                EntityWrapper<T> bean = (EntityWrapper<T>)rspData;
                processor.processBean(bean);
            }
            /*else if (rspData instanceof RspBean) {//废弃，该分支不需要
                @SuppressWarnings("rawtypes")
                RspBean rs = (RspBean)rspData;
                processBean((EntityWrapper<T>)rs.getValue());                    
            }*/
            else {
                throw new RuntimeException("返回的格式不是EntityWrapper格式!");
            }
        }        
    }
    
    /**
     * 针对新增/修改/其他操作的处理的回调
     * 泛型参数T:默认是返回的主键值
     * @author zhangyz created on 2014-3-10
     */
    public static class SaveCallBack<T> extends NetTaskCallBack<T, NetProcessor.ComnProcessor<T>> {
        public SaveCallBack(NetProcessor.ComnProcessor<T> processor, Class<T> pkClass) {
            super(processor, pkClass);
        } 
        
        public SaveCallBack(NetProcessor.ComnProcessor<T> processor, Class<T> pkClass,Context context) {
            super(processor, pkClass, context);
        }

        @Override
        protected ResponseBody parserResponse(String rawValue) {
            JsonParser parser = new JsonParser();
            ResponseBody resp = parser.parser(rawValue, pojoClass, dataFormat);
            return resp;
        }
        
        @Override
        protected void doSuccessInner(IResponseData rspData) {  
            if (rspData == null) {
                processor.processOperResult(null);
            }
            else if (rspData instanceof RspValue) {
                @SuppressWarnings("unchecked")
                RspValue<T> retData = (RspValue<T>)rspData;
                processor.processOperResult(retData.getValue());
            }
            else {
                throw new RuntimeException("返回的格式不是字符串格式!");
            }
        }        
    }

    /**
     * 针对新增/修改/其他操作的处理的回调.但网络返回的中间值是json，需要子类自行转换为需要的T类型对象。
     * 泛型参数T:默认是返回的主键值
     * @author zhangyz created on 2014-3-10
     */
    public static abstract class SaveCallBackJson<T> extends NetTaskCallBack<T, NetProcessor.ComnProcessor<T>> {
        public SaveCallBackJson(NetProcessor.ComnProcessor<T> processor, Class<T> pkClass) {
            super(processor, pkClass);
        }

        public SaveCallBackJson(NetProcessor.ComnProcessor<T> processor, Class<T> pkClass,Context context) {
            super(processor, pkClass, context);
        }

        @Override
        protected ResponseBody parserResponse(String rawValue) {
            MLog.d(String.format("rawValue: %s", rawValue));
            JsonParser parser = new JsonParser();
            ResponseBody resp = parser.parser(rawValue, JSONObject.class, dataFormat);
            return resp;
        }

        /**
         * 将原始的json串转换成实际的Bean对象
         * @param json
         * @return T对象
         */
        protected abstract T changeJsonToBean(JSONObject json);

        @Override
        protected void doSuccessInner(IResponseData rspData) {
            if (rspData == null) {
                processor.processOperResult(null);
            }
            else if (rspData instanceof RspBean) {
                @SuppressWarnings("unchecked")
                RspBean<JSONObject> midRet = (RspBean<JSONObject>)rspData;
                T finalResult = changeJsonToBean(midRet.getValue());//转换
                RspValue<T> retData = new RspValue<T>(finalResult);
                processor.processOperResult(retData.getValue());
            }
            else {
                throw new RuntimeException("返回的格式不是字符串格式!");
            }
        }
    }

    /**
     * 针对删除处理的回调
     * 
     * @author zhangyz created on 2014-3-10
     */
    public static class DeleteCallBack<T> extends NetTaskCallBack<T, NetProcessor.DeleteProcessor<T>> {
        public DeleteCallBack(NetProcessor.DeleteProcessor<T> processor, Class<T> pojoClass) {
            super(processor, pojoClass);
        }
        
        public DeleteCallBack(NetProcessor.DeleteProcessor<T> processor,
                Class<T> pojoClass, Context context) {
            super(processor, pojoClass, context);
        }
        
        @Override
        protected void doSuccessInner(IResponseData rspData) {
            if (rspData == null) {
                if (processor != null)
                    processor.processDeleteResult(-1);
            }
            else if (rspData instanceof RspValue) {
                @SuppressWarnings("unchecked")
                RspValue<Integer> retData = (RspValue<Integer>)rspData;
                if (processor != null)
                    processor.processDeleteResult(retData.getValue());
            }
            else {
                throw new RuntimeException("返回的格式不是整形格式!");
            }
        }        
    }
}
