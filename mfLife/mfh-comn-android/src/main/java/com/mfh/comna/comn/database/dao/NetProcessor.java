/*
 * 文件名称: NetProcessor.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-11
 * 修改内容: 
 */
package com.mfh.comna.comn.database.dao;

import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspCodeDomain;
import com.mfh.comn.net.data.RspListBean;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comn.net.data.RspValue;

import java.util.List;

/**
 * 为Service层请求提供的简化的回调处理函数，比NetCallBack中对应回调函数更易用。
 * @author zhangyz created on 2014-3-11
 */
public class NetProcessor<T> {
    /**
     * 网络返回通用回调处理器
     * @param <T> 返回值或列表中item的bean类
     */
    public static abstract class Processor<T> {
        /**
         * 错误处理,默认不需要
         * @param t
         * @param errMsg
         */
        protected void processFailure(Throwable t, String errMsg) {
            
        }

        /**
         * 执行成功回调
         * @param rspData
         */
        public abstract void processResult(IResponseData rspData);
    }
    
    /**
     * 分页列表查询结果集处理器
     * @param <T>
     * @author zhangyz created on 2014-3-11
     */
    public static abstract class QueryRsProcessor<T> extends Processor<T>{
        protected PageInfo pageInfo;
        
        public PageInfo getPageInfo() {
            return pageInfo;
        }
        
        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        /**
         * 补充设置总数，如果存在分页信息的话
         * @param totalNum
         */
        public void setTotalNum(long totalNum) {
            PageInfo pageInfo = getPageInfo();
            if (pageInfo != null) {
                //重新设置分页信息
                pageInfo.setTotalCount((int)totalNum);
            }
        }

        /**
         * @param pageInfo 分页信息，需要指定pageNo和pageSize。可以为空，代表无须分页。
         * @param pageInfo
         */
        public QueryRsProcessor(PageInfo pageInfo) {
            super();
            this.pageInfo = pageInfo;
        }

        @Override
        public void processResult(IResponseData result) {
            RspQueryResult<T> rs = (RspQueryResult<T>)result;
            this.setTotalNum(rs.getTotalNum());
            this.processQueryResult(rs);
        }

        /**
         * 处理查询结果集，子类必须继承
         * @param rs 查询结果集
         * @author zhangyz created on 2014-3-10
         */
        public abstract void processQueryResult(RspQueryResult<T> rs);
    }


    /**
     * //编码集查询,还没有实现
     * @param <T>
     */
    public static abstract class RspCodeDomainProcessor<T> extends Processor<T>{
        protected PageInfo pageInfo;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }
        public void setTotalNum(long totalNum) {
            PageInfo pageInfo = getPageInfo();
            if (pageInfo != null) {
                //重新设置分页信息
                pageInfo.setTotalCount((int)totalNum);
            }
        }
        /**
         * @param pageInfo 分页信息，需要指定pageNo和pageSize。可以为空，代表无须分页。
         * @param pageInfo
         */
        public RspCodeDomainProcessor(PageInfo pageInfo) {
            super();
            this.pageInfo = pageInfo;
        }
        @Override
        public void processResult(IResponseData result) {
            RspCodeDomain<T> rs = (RspCodeDomain<T>)result;
            //this.setTotalNum(rs.getTotalNum());
            this.processQueryResult(rs);
        }
        public abstract void processQueryResult(RspCodeDomain<T> rs);
    }

    /**
     * 普通列表查询处理器，无分页，一次请求全部返回
     * @param <T>
     */
    public static abstract class QueryListProcessor<T> extends Processor<T> {
        @Override
        public void processResult(IResponseData result) {
            RspListBean<T> rs = (RspListBean<T>)result;
            processQueryResult(rs.getValue());
        }
        /**
         * 处理查询到的bean对象，子类必须继承
         * @param rs
         * @author zhangyz created on 2014-3-11
         */
        protected abstract void processQueryResult(List<T> rs);
    }
    
    /**
     * bean查询结果处理器
     * @param <T> bean类型
     * @author zhangyz created on 2014-3-11
     */
    public static abstract class BeanProcessor<T> extends Processor<T> {
        @Override
        public void processResult(IResponseData result) {
            EntityWrapper<T> rs = (EntityWrapper<T>)result;
            processBean(rs);
        }
        /**
         * 处理查询到的bean对象，子类必须继承
         * @param rs
         * @author zhangyz created on 2014-3-11
         */
        protected abstract void processBean(EntityWrapper<T> rs);
    }
    
    /**
     * 执行保存后或一般性操作后的回调处理器，返回数据为普通类型
     * @param <PK> 返回的参数类型
     * @author zhangyz created on 2014-3-11
     */
    public static abstract class ComnProcessor<PK> extends Processor<PK> {
        @Override
        public void processResult(IResponseData rspData){
            RspValue<PK> retData = (RspValue<PK>)rspData;
            PK resultKey = retData.getValue();
            processOperResult(resultKey);
        }

        /**
         * 处理保存结果，子类必须继承
         * @param resultKey 新增时返回的主键标识,为空代表没有可能是修改。
         * @author zhangyz created on 2014-3-10
         */
        protected abstract void processOperResult(PK resultKey);
    }

    /**
     * 删除后回调处理器
     * @author zhangyz created on 2014-3-11
     */
    public static abstract class DeleteProcessor<T> extends Processor<T> {
        @Override
        public void processResult(IResponseData rspData){
            RspValue<Integer> retData = (RspValue<Integer>)rspData;
            Integer delCount = retData.getValue();
            processDeleteResult(delCount);
        }

        /**
         * 处理删除结果，子类必须继承
         * @param delCount 删除个数，-1代表无意义
         * @author zhangyz created on 2014-3-10
         */
        protected abstract void processDeleteResult(Integer delCount);
    }
}
