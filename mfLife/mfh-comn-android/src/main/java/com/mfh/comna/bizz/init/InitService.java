package com.mfh.comna.bizz.init;

import android.content.Context;
import com.mfh.comn.config.UConfig;
import com.mfh.comn.upgrade.UpgradeConfigParseHelper;
import com.mfh.comna.comn.database.dao.BaseDbDao;
import com.mfh.comna.comn.logic.AsyncTaskCallBack;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.network.NetFactory;
import com.mfh.comna.network.NetStateService;
import com.mfh.comna.comn.database.upgrade.SqlliteUpgradeSupport;

import net.tsz.afinal.http.AjaxParams;

/**
 * 系统初始化服务，所有启动时需要的检测和初始化工作放在此处
 *
 * @author zhangyz created on 2013-5-9
 * @since Framework 1.0
 */
public class InitService extends BaseService {

    public static InitService getService(Context context) {
        return ServiceFactory.getService(InitService.class, context);
    }

    @Override
    protected Class getDaoClass() {
        return null;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }


    /**
     * 系统初始化过程
     * @param context
     * @author zhangyz created on 2013-5-9
     */
    public void init (Context context) {
        ///data/data/<package name>/shares_prefs
        /*SharedPreferences sp = context.getSharedPreferences("itm.cfg.xml", Context.MODE_APPEND);
        System.out.println(sp.getString("itm.ip", "192.168.0.1"));
        Editor editor = sp.edit();
        editor.putString("itm.ip", "localhost");
        editor.commit();*/
        NetStateService.registerReceiver();

        checkDb(context);

        initOther();
    }

    /**
     * 检测和初始化数据库
     *
     * @author zhangyz created on 2013-5-7
     */
    protected void checkDb(Context context) {
        //com.dinsc.comn.utils.SyncUtil.copyDatabase(context, com.dins.itm.comn.Constants.DBNAME);
        String dbName = uconfig.getDomain(UConfig.CONFIG_COMMON).getString(UConfig.CONFIG_PARAM_DB_NAME, "mfh.db");
        String dbPath = uconfig.getDomain(UConfig.CONFIG_COMMON).getString(UConfig.CONFIG_PARAM_DB_PATH);//"/storage/sdcard0/dinsItm"

        UpgradeConfigParseHelper helper = new UpgradeConfigParseHelper();
        UConfig uc = uconfig.getDomain(UConfig.CONFIG_DBUPGRADE);
        SqlliteUpgradeSupport support = new SqlliteUpgradeSupport();

        boolean bCreate = BaseDbDao.initDao(context, dbName, dbPath);
        if (bCreate) {
            helper.doDbUpdate(uc, support, bCreate);
        }
        else {
            helper.doDbUpdate(uc, support);
        }
    }

    /**
     * getSync可以在后台线程中调用；
     * 但get只需在UI主线程中调用，因为内部已经开启了其他子线程并采用了异步。
     *
     * @author zhangyz created on 2013-5-15
     */
    protected void checkNetInBack() {
        AjaxParams params = new AjaxParams();
        params.put("loginName", "sys");
        params.put("pwd", "123456");
        params.put("needXml", "true");
        String ret = (String)NetFactory.getHttp().getSync("http://192.168.1.200:8080/glp/priv/queryUser.action",params);
        System.out.println(ret);
    }

    /**
     * 检查网络
     * @param context
     */
    public void checkNet(Context context) {
        AjaxParams params = new AjaxParams();
        params.put("loginName", "sys");
        params.put("pwd", "123456");
        params.put("needXml", "true");
        NetFactory.getHttp().get("http://192.168.1.200:8080/glp/priv/queryUser.action", params, new AsyncTaskCallBack<String>(context) {
            @Override
            protected void doSuccess(String rawValue) {
                System.out.println(rawValue);
            }
        });
    }

    /**
     * 其他必要的初始化工作
     *
     * @author zhangyz created on 2013-5-9
     */
    protected void initOther() {

    }
}
