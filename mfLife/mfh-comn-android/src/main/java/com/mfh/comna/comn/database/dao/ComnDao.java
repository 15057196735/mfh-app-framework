/*
 * 文件名称: ComnDao.java
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
import com.mfh.comn.bean.IObject;
import com.mfh.comna.bizz.BizApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 所有dao的基类
 * @author zhangyz created on 2014-3-10
 */
public abstract class ComnDao <T extends IObject, PK> {    
    protected Class<T> pojoClass;
    protected Class<PK> pkClass;
    
    private Context context;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public Context getContext() {
        if (context != null)
            return context;
        else
            return BizApplication.getAppContext();
    }

    public void setContext(Context context) {

        this.context = context;
    }
    
    /**
     * 子类提供pojo类名
     * @return
     * @author zhangyz created on 2013-6-8
     */
    protected abstract Class<T> initPojoClass();

    /**
     * 子类提供pk的类名
     * @return
     */
    protected abstract Class<PK> initPkClass();
    
    /**
     * 构造函数
     */
    public ComnDao() {
        super();
        this.pojoClass = initPojoClass();
        this.pkClass = initPkClass();
    }
    
    public Class<T> getPojoClass() {
        return pojoClass;
    }

    public Class<PK> getPkClass() {return pkClass;}
}
