/*
 * 文件名称: NetDao.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-9
 * 修改内容: 
 */
package com.mfh.comna.comn.database.dao;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.IObject;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.JsonParser;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.comn.ComnApplication;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过网络（向后台服务器）获取数据的dao基类
 * @author zhangyz created on 2014-3-9
 */
public abstract class BaseNetDao<T extends IObject, PK> extends ComnDao<T, PK> implements IAsyncDao<T, PK> {
    private static String serverUrl = null;
    protected DaoUrl daoUrl = new DaoUrl();//请求的相对url，相当于表名
    private transient Map<String, String> urlCache = new HashMap<>();//url临时缓存
    private DateFormat dateFormat = null;//默认是，包含时分秒，JSON.DEFFAULT_DATE_FORMAT
    private String strDataFormat = null;
    
    /**
     * 带有android上下文的构造函数
     * @param context
     */
    /*public BaseNetDao(Context context) {
        super();
        this.setContext(context);
        init();
    }*/

    /**
     * 无参构造函数
     */
    public BaseNetDao() {
        super();
        init();
    }
    
    /**
     * 初始化.子类可以继承，如设置dataFormat
     * 
     * @author zhangyz created on 2014-3-10
     */
    protected void init() {
        if (serverUrl == null) {
            serverUrl = NetFactory.getServerUrl();
        }
        initUrlInfo(daoUrl);
    }
    
    public DateFormat getDataFormat() {
        return dateFormat;
    }
    
    /**
     * 设置日期解析格式，在bean中可能存在日期类型的属性，反序列化时需要知道
     * @param strDataFormat
     * @author zhangyz created on 2014-3-13
     */
    @SuppressLint("SimpleDateFormat")
    public void setStrDataFormat(String strDataFormat) {
        this.strDataFormat = strDataFormat;

        this.dateFormat = new SimpleDateFormat(strDataFormat);
    }

    /**
     * 构建实际 的访问url
     * @param accessType
     * @param factUrl
     * @return
     * @author zhangyz created on 2014-3-10
     */
    protected String getFullUrl(DaoUrl.DaoType accessType, String... factUrl) {
        if (factUrl != null && factUrl.length > 0)
            return serverUrl + factUrl[0];//+ this.daoUrl.getTableName()
        String key = accessType.toString();
        String retUrl = urlCache.get(key);
        if (retUrl == null) {
            synchronized(urlCache) {
                retUrl = urlCache.get(key);
                if (retUrl == null) {
                    if (accessType.equals(DaoUrl.DaoType.list))
                        retUrl = daoUrl.getListUrl();
                    else if (accessType.equals(DaoUrl.DaoType.getById))
                        retUrl = daoUrl.getGetUrl();
                    else if (accessType.equals(DaoUrl.DaoType.create))
                        retUrl = daoUrl.getCreateUrl();
                    else if (accessType.equals(DaoUrl.DaoType.update))
                        retUrl = daoUrl.getUpdateUrl();
                    else if (accessType.equals(DaoUrl.DaoType.multiDelete))
                        retUrl = daoUrl.getDeleteUrl();
                    retUrl = serverUrl + retUrl;
                    urlCache.put(key, retUrl);
                }
            }
        }
        return retUrl;        
    }
    
    /**
     * 子类提供表名
     * @param daoUrl 地址信息
     * @author zhangyz created on 2013-6-8
     */
    protected abstract void initUrlInfo(DaoUrl daoUrl);
    
    /*protected <R> void doNetRequest(AjaxParams params, AjaxCallBack<R> callBack) {       
        NetFactory.getHttp().get(fullUrl, params, callBack);
    }*/

    /**
     * 获取分页查询回调函数，子类可以覆盖
     * @return
     */
    protected NetCallBack.NetTaskCallBack<T, NetProcessor.QueryRsProcessor<T>> genQueryCallBack(NetProcessor.QueryRsProcessor<T> callBack) {
        return new NetCallBack.QueryRsCallBack<T>(callBack, this.getPojoClass(), this.getContext());
    }

    /**
     * 获取分页查询回调函数，子类可以覆盖
     * @return
     */
    protected NetCallBack.QueryRsCallBack<T> genQueryCallBack(NetProcessor.RspCodeDomainProcessor<T> callBack) {
        return new NetCallBack.QueryRsCallBack<T>(callBack, this.getPojoClass(), this.getContext());
    }

    /**
     * 获取简单列表查询回调函数，子类可以覆盖
     * @return
     */
    protected NetCallBack.NetTaskCallBack<T, NetProcessor.QueryListProcessor<T>> genQueryCallBack(NetProcessor.QueryListProcessor<T> callback) {
        return new NetCallBack.QueryListRsCallBack<T>(callback, this.getPojoClass(), this.getContext());
    }

    @Override
    public void query(AjaxParams params, NetProcessor.QueryRsProcessor<T> callBack, String... factUrl) {
        PageInfo pageInfo = callBack.getPageInfo();
        if (pageInfo != null) {
            params.put("page", Integer.toString(pageInfo.getPageNo()));
            params.put("rows", Integer.toString(pageInfo.getPageSize()));
        }
        attachSessionId(params);
//        NetFactory.getHttp().post(getFullUrl(DaoType.list, factUrl), params,
//                genQueryCallBack(callBack).setDataFormat(dateFormat));
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.list, factUrl), params,
                genQueryCallBack(callBack).setDataFormat(dateFormat));
    }

    public void getCodes(AjaxParams params, NetProcessor.RspCodeDomainProcessor<T> codeDomain, String... factUrl) {
        PageInfo pageInfo = codeDomain.getPageInfo();
        if (pageInfo != null) {
            params.put("page", Integer.toString(pageInfo.getPageNo()));
            params.put("rows", Integer.toString(pageInfo.getPageSize()));
        }
        attachSessionId(params);
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.list, factUrl), params,
                genQueryCallBack(codeDomain));
    }

    @Override
    public void queryAll(AjaxParams params, NetProcessor.QueryRsProcessor<T> callBack, String... factUrl) {
        final NetProcessor.QueryRsProcessor<T> origion = callBack;
        NetProcessor.QueryListProcessor<T> callBackNew = new NetProcessor.QueryListProcessor<T>() {
            @Override
            protected void processQueryResult(List<T> rs) {
                origion.setTotalNum(rs.size());
                RspQueryResult<T> qrs = new RspQueryResult<T>();
                qrs.setTotalNum(rs.size());
                for (T bean : rs) {
                    qrs.addRowItem(new EntityWrapper<T>(bean));
                }
                origion.processQueryResult(qrs);
            }
        };
        attachSessionId(params);
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.list, factUrl), params,
                genQueryCallBack(callBackNew).setDataFormat(dateFormat));
    }

    @Override
    public void getEntityById(PK pkId, NetProcessor.BeanProcessor<T> callBack, String... factUrl) {
        AjaxParams params = new AjaxParams();
        params.put(JsonParser.TAG_JSONSTR, "{id:" + pkId.toString() + "}");
        params.put("wrapper", "true");
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.getById, factUrl), params,
                new NetCallBack.GetBeanCallBack<T>(callBack, this.getPojoClass(), this.getContext()).setDataFormat(dateFormat));
    }


    public void getEntityParams(AjaxParams params, NetProcessor.QueryRsProcessor<T> callBack, String... factUrl) {

        //params.put("wrapper", "true");
        attachSessionId(params);
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.list, factUrl), params,
                genQueryCallBack(callBack).setDataFormat(dateFormat));
    }

    @Override
    public void save(T bean, NetProcessor.ComnProcessor<PK> callBack, String... factUrl) {
        AjaxParams params = new AjaxParams();
        attachSessionId(params);
        String jsonStr = JSON.toJSONStringWithDateFormat(bean, strDataFormat);
        params.put(JsonParser.TAG_JSONSTR, jsonStr);
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.create, factUrl), params,
                new NetCallBack.SaveCallBack<PK>(callBack, this.getPkClass(), this.getContext()).setDataFormat(dateFormat));
    }

    @Override
    public void update(T bean, NetProcessor.ComnProcessor<PK> callBack, String... factUrl) {
        AjaxParams params = new AjaxParams();
        attachSessionId(params);
        String jsonStr = JSON.toJSONStringWithDateFormat(bean, strDataFormat);
        params.put(JsonParser.TAG_JSONSTR, jsonStr);
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.update, factUrl), params,
                new NetCallBack.SaveCallBack<PK>(callBack, this.getPkClass(), this.getContext()).setDataFormat(dateFormat));
    }

    @Override
    public void deleteAll(List<PK> pkIds, NetProcessor.DeleteProcessor<T> callBack, String... factUrl) {
        AjaxParams params = new AjaxParams();
        StringBuilder ids = new StringBuilder();
        if (pkIds != null && pkIds.size() > 0) {
            for (PK pkId : pkIds) {
                if (ids.length() > 0)
                    ids.append(",");
                ids.append(pkId.toString());
            }
            params.put(JsonParser.TAG_JSONSTR, "{ids:" + ids.toString() + "}");
        }
        attachSessionId(params);
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.multiDelete, factUrl), params,
                new NetCallBack.DeleteCallBack<T>(callBack, this.getPojoClass(), this.getContext()).setDataFormat(dateFormat));
    }
    
    @Override
    public void deleteById(PK pkId, NetProcessor.DeleteProcessor<T> callBack, String... factUrl) {
        AjaxParams params = new AjaxParams();
        params.put(JsonParser.TAG_JSONSTR, "{id:" + pkId.toString() + "}");
        attachSessionId(params);
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.multiDelete, factUrl), params,
                new NetCallBack.DeleteCallBack<T>(callBack, this.getPojoClass(), this.getContext()).setDataFormat(dateFormat));
    }


    public void operate(AjaxParams params, NetProcessor.ComnProcessor<PK> callBack, String... factUrl) {
        attachSessionId(params);
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.update, factUrl), params,
                new NetCallBack.SaveCallBack<PK>(callBack, this.getPkClass(), this.getContext()).setDataFormat(dateFormat));
    }

    public void operateForGet(AjaxParams params, NetProcessor.ComnProcessor<PK> callBack, String... factUrl) {
        attachSessionId(params);
        NetFactory.getHttp().get(getFullUrl(DaoUrl.DaoType.update, factUrl), params,
                new NetCallBack.SaveCallBack<PK>(callBack, this.getPkClass(), this.getContext()).setDataFormat(dateFormat));
    }

    /**
     * 附加上会话Id，服务器据此知道是谁
     * @param params
     */
    protected void attachSessionId(AjaxParams params) {
        String sessionId = MfhLoginService.get().getCurrentSessionId();
        Long userId = MfhLoginService.get().getUserId();

        if (sessionId != null){
            params.put(LoginService.CLIENTSESSION, sessionId);
        }
        if (userId != 0L && !userId.equals(0L)) {
            params.put(LoginService.OPERATORID, String.valueOf(userId));
        }
    }
}
