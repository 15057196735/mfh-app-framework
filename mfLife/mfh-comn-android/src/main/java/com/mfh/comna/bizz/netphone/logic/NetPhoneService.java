package com.mfh.comna.bizz.netphone.logic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hisun.phone.core.voice.CCPCall;
import com.hisun.phone.core.voice.Device;
import com.hisun.phone.core.voice.DeviceListener;
import com.hisun.phone.core.voice.model.setup.UserAgentConfig;
import com.mfh.comn.config.UConfig;
import com.mfh.comna.api.Constants;
import com.mfh.comna.comn.cfg.UConfigHelper;
import com.mfh.comna.bizz.config.URLConf;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.bizz.netphone.dao.NetPhoneDbDao;
import com.mfh.comna.bizz.netphone.entity.NetPhoneInfo;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.network.NetFactory;
import com.mfh.comna.bizz.netphone.view.CallInActivity;


import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caij on 2014/9/22.
 * 网络电话的服务逻辑类
 */
public class NetPhoneService extends BaseService<NetPhoneInfo, Long, NetPhoneDbDao>{

    private Device device;

    @Override
    protected Class<NetPhoneDbDao> getDaoClass() {
        return NetPhoneDbDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }

    /**
     * 获取当前账号的网络电话账号信息
     * */
    public void queryFromNet(String guid) {
        AjaxParams params = new AjaxParams();
        params.put(Constants.PARAM_KEY_GUID, guid);
        NetFactory.getHttp().post(URLConf.URL_NET_PHONE, params, new NetCallBack.NormalNetTask<NetPhoneInfo>(NetPhoneInfo.class) {
            @Override
            public void processResult(IResponseData rspData) {
                if (rspData instanceof RspBean) {
                    RspBean<NetPhoneInfo> retData = (RspBean<NetPhoneInfo>) rspData;
                    if(retData != null){
                        NetPhoneInfo info = retData.getValue();
                        CCPCall.init(getContext(), new MInitListener(info));
                    }
                }
            }
        });

    }

    /**
     * 初始化监听
     * */
    private class  MInitListener implements CCPCall.InitListener {
        private NetPhoneInfo info;

        public MInitListener(NetPhoneInfo info) {
            this.info = info;
        }

        @Override
        public void onInitialized() {
            createDevice(info);
        }

        @Override
        public void onError(Exception e) {
            if ("CCPCall.init() already called.".equalsIgnoreCase(e.getMessage().trim())) {
                createDevice(info);      //当已经初始化的时候，就直接创建device
            }
        }
    }

    /**
     *创建device设备
     * */
    private void createDevice(NetPhoneInfo info) {
        // CCP SDK 初始化成功回调此方法，开发者可在该处向云通讯平台注册你的帐号信息
        // 封装参数
        Map<String, String> params = new HashMap<String, String>();
        // * REST服务器地址
        String keyIp = UConfigHelper.getConfig().getDomain(UConfig.CONFIG_COMMON).getString(UConfig.CONFIG_NETPHONE_IP);
        params.put(UserAgentConfig.KEY_IP, keyIp);  //上线后地址需要改变
        // * REST服务器端口
        params.put(UserAgentConfig.KEY_PORT, "8883");
        // * VOIP账号 , 可以填入CCP网站Demo管理中的测试VOIP账号信息
        params.put(UserAgentConfig.KEY_SID, info.getVoipAccount());
        // * VOIP账号密码, 可以填入CCP网站Demo管理中的测试VOIP账号密码
        params.put(UserAgentConfig.KEY_PWD, info.getVoipPwd());
        // * 子账号,可以填入CCP网站Demo管理中的测试子账号信息
        params.put(UserAgentConfig.KEY_SUBID,info.getSubAccountSid());
        // * 子账号密码,可以填入CCP网站Demo管理中的测试子账号密码
        params.put(UserAgentConfig.KEY_SUBPWD, info.getSubtoken());
        // User-Agent
        params.put(UserAgentConfig.KEY_UA, getUser_Agent());

        device = CCPCall.createDevice(new DeviceListener() {

            @Override
            public void onConnected() {
                Intent intentCall = new Intent(getContext(), CallInActivity.class);
                PendingIntent pendingIntent =PendingIntent.getActivity(getContext(), 0, intentCall, PendingIntent.FLAG_UPDATE_CURRENT);
                getDevice().setIncomingIntent(pendingIntent);
                Log.e("------------", "连接成功...............");
            }

            @Override
            public void onReceiveEvents(CCPEvents ccpEvents) { }

            @Override
            public void onDisconnect(Reason reason) { }

            @Override
            public void onFirewallPolicyEnabled() { }

        }, params);
    }

    public Device getDevice() {
        return device;
    }

    /*-- 以下是网络电话参数的拼装方法 --*/
    private String getMacAddress() {
        // start get mac address
        WifiManager wifiMan = (WifiManager) getContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiMan != null) {
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                // 34:7C:6D:E4:D7
                return wifiInf.getMacAddress();
            }
        }
        return null;
    }

    private String getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getDeviceId();
        }
        return null;
    }

    private String getDevicNO() {
        if (!TextUtils.isEmpty(getDeviceId())) {
            return getDeviceId();
        }

        if (!TextUtils.isEmpty(getMacAddress())) {
            return getMacAddress();
        }
        return " ";
    }

    private String getUser_Agent() {
        String ua = "Android;" + Build.VERSION.RELEASE + ";"
                + com.hisun.phone.core.voice.Build.SDK_VERSION + ";"
                + com.hisun.phone.core.voice.Build.LIBVERSION.FULL_VERSION
                + ";" + Build.BRAND + "-" + Build.MODEL + ";";
        ua = ua + getDevicNO() + ";" + System.currentTimeMillis() + ";";

        return ua;
    }

}
