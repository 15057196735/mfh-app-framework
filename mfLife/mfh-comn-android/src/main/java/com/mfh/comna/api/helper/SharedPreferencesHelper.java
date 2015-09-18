package com.mfh.comna.api.helper;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comna.api.Constants;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.SharedPreferencesUtil;
import com.mfh.comna.bizz.login.entity.SubdisList;
import com.mfh.comna.bizz.login.entity.UserAttribute;
import com.mfh.comna.bizz.login.entity.UserComInfo;
import com.mfh.comna.bizz.login.entity.UserMixInfo;
import com.mfh.comna.comn.ComnApplication;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/6/17.
 */
public class SharedPreferencesHelper {
    //APP
    public static final String PREF_NAME_APP = "APP";

    //软件更新
    public static final String PREF_NAME_APP_UPDATE = "app_update";
    public static final String PREF_KEY_APP_UPDATE_NAME = "app_name";
    public static final String PREF_KEY_APP_UPDATE_VERSIONNAME = "app_versionName";

    //个推
    public static final String PREF_NAME_PUSH = "push.clientid.share";
    public static final String PREF_KEY_PUSH_CLIENT = "push.clientid.value";

    public static SharedPreferences getPreferences(String prefName) {
        return SharedPreferencesUtil.getPreferences(ComnApplication.getAppContext(),
                prefName);
    }
//
//
//
//    public static void setLoginUsername(String clientId){
//        SharedPreferencesUtil.set(ComnApplication.getAppContext(),
//                PREF_NAME_LOGIN, PREF_KEY_LOGIN_USERNAME, clientId);
//    }
//
//    public static String getLoginUsername(){
//        return SharedPreferencesUtil.get(ComnApplication.getAppContext(),
//                PREF_NAME_LOGIN, PREF_KEY_LOGIN_USERNAME, "");
//    }
//
//    public static void saveLastSessionId(String sessionId){
//        SharedPreferencesUtil.set(ComnApplication.getAppContext(),
//                PREF_NAME_LOGIN, PREF_KEY_LOGIN_LAST_SESSION_ID, sessionId);
//    }
//
//    public static String getLastSessionId(){
//        return SharedPreferencesUtil.get(ComnApplication.getAppContext(),
//                PREF_NAME_LOGIN, PREF_KEY_LOGIN_LAST_SESSION_ID, null);
//    }
//
//    public static String getUserSubdisId(){
//        return SharedPreferencesUtil.get(ComnApplication.getAppContext(), PREF_NAME_LOGIN, PREF_KEY_LOGIN_USER_SUBDIS_ID, null);
//    }
//
//    public static String getUserGuid(){
//        return SharedPreferencesUtil.get(ComnApplication.getAppContext(), PREF_NAME_LOGIN, PREF_KEY_LOGIN_USER_GUID, null);
//    }
//
//    public static Long getUserId(){
//        return SharedPreferencesUtil.getLong(ComnApplication.getAppContext(),
//                PREF_NAME_LOGIN, PREF_KEY_LOGIN_USER_ID, 0L);
//    }
//
//    public static String getCookies(){
//        return SharedPreferencesUtil.get(ComnApplication.getAppContext(),
//                PREF_NAME_LOGIN, PREF_KEY_LOGIN_HTTP_COOKIE, "");
//    }

    public static void setLocationAcceptEnabled(boolean enabled){
        SharedPreferencesUtil.set(ComnApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_LOCATION_ACCEPT, enabled);
    }

    /**
     * 是否开启位置服务：默认true
     * */
    public static boolean getLocationAcceptEnabled(){
        return SharedPreferencesUtil.get(ComnApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_LOCATION_ACCEPT, true);
    }

    public static void setNotificationAcceptEnabled(boolean enabled){
        SharedPreferencesUtil.set(ComnApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_NOTIFICATION_ACCEPT, enabled);
    }

    /**
     * 是否接收通知：默认true
     * */
    public static boolean getNotificationAcceptEnabled(){
        return SharedPreferencesUtil.get(ComnApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_NOTIFICATION_ACCEPT, true);
    }

    public static String getPushClientId(){
        return SharedPreferencesUtil.get(ComnApplication.getAppContext(),
                PREF_NAME_PUSH, PREF_KEY_PUSH_CLIENT, null);
    }

    public static void savePushClientId(String clientId){
        SharedPreferencesUtil.set(ComnApplication.getAppContext(),
                PREF_NAME_PUSH, PREF_KEY_PUSH_CLIENT, clientId);
    }







}
