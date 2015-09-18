package com.mfh.comna.comn.cfg;

import android.content.Context;

import com.mfh.comn.net.ResponseBody;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comna.comn.ComnApplication;
import com.mfh.comna.comn.database.dao.BaseDbDao;
import com.mfh.comna.comn.logic.JsonParser;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * Created by Administrator on 2015/1/28.
 */
public class ServerConfig {

    private Config config = new Config();
    private static ServerConfig instance;
    private Context context;
    FinalDb db;

    private ServerConfig(Context context) {
        db = BaseDbDao.getDb(); // = FinalDb.create(context);
        db.checkTableExist(Config.class);
        List<Config> configs = db.findAll(Config.class);
        for (Config config : configs) {
            if (config.getId().equals(ComnApplication.getVersionCode())) {
                this.config = config;
            }
        }
        this.context = context;
    }

    public static ServerConfig getServerConfig(Context context) {
        if (instance == null) {
            synchronized (ServerConfig.class) {
                if (instance == null) {
                    instance = new ServerConfig(context);
                }
            }
        }
        return instance;
    }

    public void init() {
        try {
            AjaxParams params = new AjaxParams();
            params.put("buildVersion", String.valueOf(ComnApplication.getVersionCode()));
            params.put("platform", "android");
            String result = NetFactory.getHttp().getSync(NetFactory.getServerUrl() + "/sysClientConfig/list", params).toString();
            ResponseBody body = JsonParser.parserResponse(result, Config.class);
            if (body.isSuccess()) {
                //TODO
                RspBean<Config> bean = (RspBean<Config>) body.getData();
                config = bean.getValue();
                int count = db.findCountById(Config.class, config.getId());
                if (count == 0)
                    db.save(config);
                else
                    db.update(config);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//          new AjaxCallBack<Object>() {
//            @Override
//            public void onSuccess(Object o) {
//                super.onSuccess(o);
//                ResponseBody body = JsonParser.parserResponse(o.toString(), Config.class);
//                if (body.isSuccess()) {
//                    RspBean<Config> bean = (RspBean<Config>) body.getData();
//                    config = bean.getValue();
//                   int count =  db.findCountById(Config.class, config.getId());
//                    if (count == 0)
//                        db.save(config);
//                    else
//                        db.update(config);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t, String strMsg) {
//                super.onFailure(t, strMsg);
//            }
//        });
    }


    public String getDomain() {
        return config.getDomain();
    }

    public void setDomain(String domain) {
        config.setDomain(domain);
    }

    public String getConfigItem() {
        return config.getConfigItem();
    }

    public void setConfigItem(String configItem) {
        config.setConfigItem(configItem);
    }

    public String getConfigValue() {
        return config.getConfigValue();
    }

    public void setConfigValue(String configValue) {
        config.setConfigValue(configValue);
    }

    public String getConfigCaption() {
        return config.getConfigCaption();
    }

    public void setConfigCaption(String configCaption) {
        config.setConfigCaption(configCaption);
    }

    public String getPlatform() {
        return config.getPlatform();
    }

    public void setPlatform(String platform) {
        config.setPlatform(platform);
    }

    public String getBuildVersion() {
        return config.getBuildVersion();
    }

    public void setBuildVersion(String buildVersion) {
        config.setBuildVersion(buildVersion);
    }

    public void commitWrite() {
        db.update(config);
    }

}
