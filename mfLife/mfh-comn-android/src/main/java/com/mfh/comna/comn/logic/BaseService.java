package com.mfh.comna.comn.logic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mfh.comn.code.ICodeService;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.androidService.ComnaAndroidService;
import com.mfh.comna.bizz.priv.PrivSession;
import com.mfh.comna.comn.cfg.UConfigCache;
import com.mfh.comna.comn.database.dao.ComnDao;
import com.mfh.comna.comn.database.dao.IDao;

/**
 * 公司service的基类
 * @author zhangyz created on 2013-5-25
 * @since Framework 1.0
 */
public abstract class BaseService<T, PK, D extends IDao<T, PK>> extends ComnService{
    //统一配置项，uconfig.getDomain(domainName).getString(..);如果需要修改配置则setProperty(...).最后需要调用commitWrite();
    protected UConfigCache uconfig = BizApplication.getUconfig();
    protected D dao = null;
    protected IAndroidService mService;
    protected DataSyncStrategy dataSyncStrategy;
    //提供编码服务的类(备用)
    protected ICodeService codeService = null;

    @Override
    public Context getContext() {
        if (dao == null)
            return null;
        Context ret = ((ComnDao)dao).getContext();
        if (ret == null)
            return BizApplication.getAppContext();
        else
            return ret;
    }

    @Override
    public void setContext(Context context) {
        if (dao == null)
            return;
        ((ComnDao)dao).setContext(context);
    }
    
    /**
     * 获取dao对象
     * @return
     * @author zhangyz created on 2014-3-11
     */
    public D getDao() {
        return dao;
    }

    /**
     * 获取dao的类名
     * @return
     * @author zhangyz created on 2014-3-11
     */
    protected abstract Class<D> getDaoClass();

    
    public BaseService() {
        super();
        Class<D> daoClass = getDaoClass();
        if (daoClass != null) {
            try {
                dao = daoClass.newInstance();
            }
            catch (Exception e) {
                throw new RuntimeException(daoClass.getName() + "dao对象创建失败:" + e.getMessage(), e);
            }
        }
        dataSyncStrategy = getDataSyncStrategy();
    }

    /**
     * 获取编码服务
     * @return
     * @author zhangyz created on 2013-6-10
     */
    public ICodeService getCodeService() {
        if (codeService == null)
            codeService = ServiceFactory.getCodeService();
        return codeService;
    }
    
    /**
     * 获取当前登录用户的权限会话信息
     * @return
     * @author zhangyz created on 2013-6-13
     */
    protected PrivSession getPrivSession() {
        return PrivSession.getPrivSession();
       /* PrivSession pv = PrivSession.initPrivSession();
        if (pv.getCurrentUser() == null)
            pv = PrivSession.getDefaultSession();
        return pv;*/
    }

    /*---------------------------------------------------以下为缓存策略的实现-----------------------------------------------------*/
    /**
     * 单一定时数据同步
     */
    public void initAllSyncDataLogic() {
        if (dataSyncStrategy != null) {
            Intent intent = new Intent(getContext(), ComnaAndroidService.class);
            getContext().bindService(intent, new MyConnection(), Context.BIND_AUTO_CREATE);
        }
    }


    /**
     * 从当前层逐步向上层同步数据
     * */
    public void syncDataFromFrontToEnd(int fromLayerIndex, final SyncDataCallBack callBack) {
        if (dataSyncStrategy != null) {
            new MyAsyncTask<Integer, PK>(false) {

                @Override
                protected void onPreExecute() {
                    if (callBack != null) callBack.start();
                }

                @Override
                protected PK doInBackgroundInner(Integer... params) {
                    dataSyncStrategy.syncDataFromFrontToEnd(params[0]);
                    return null;
                }

                @Override
                protected void onPostExecuteInner(PK result, Integer... params) {
                    if (callBack != null) callBack.success();
                }

                @Override
                protected void doInBackgroundException(Throwable ex, Integer... params) {
                    super.doInBackgroundException(ex, params);
                    if (callBack != null) callBack.fail();
                }
            }.execute(fromLayerIndex);
        }
    }

    /**
     * 获取当前Service的缓存策略
     * @return
     */
    public abstract DataSyncStrategy getDataSyncStrategy();

    private class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = (IAndroidService) service;
            mService.setService(BaseService.this, dataSyncStrategy.getIntervaldTime());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public static abstract class SyncDataCallBack {
        public abstract void success();
        public abstract void fail();
        public void start() {};
    }
}
