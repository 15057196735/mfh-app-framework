package com.mfh.comna.comn;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comna.api.Constants;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.SharedPreferencesUtil;
import com.mfh.comna.bizz.login.entity.SubdisList;
import com.mfh.comna.bizz.login.entity.UserAttribute;
import com.mfh.comna.bizz.login.entity.UserComInfo;
import com.mfh.comna.bizz.login.entity.UserMixInfo;
import com.mfh.comna.bizz.BizApplication;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 应用程序帮助类
 * @author Nat.ZZN(https://github.com/bingshanguxue)
 * Created on 2015/6/17.
 */
public class ComnAppHelper {
    //COMN_APP
    public static final String PREF_NAME_COMN_APP = "PREF_NAME_COMN_APP";
    public static final String PREF_KEY_COMN_APP_UNIQUEID = "COMN_APP_UNIQUEID";//App唯一标识

    //登录相关
    public static final String PREF_NAME_LOGIN = "login";
    public static final String PREF_KEY_LOGIN_USERNAME = "app.login.name";
    public static final String PREF_KEY_LOGIN_PASSWORD = "app.login.password";
    public static final String PREF_KEY_LOGIN_LAST_SESSION_ID = "app.user.lastSessionId";
    public static final String PREF_KEY_LOGIN_USER_GUID = "app.user.guid";
    public static final String PREF_KEY_LOGIN_USER_TELEPHONE = "app.telephone";
    public static final String PREF_KEY_LOGIN_HTTP_COOKIE = "app.http.cookie";
    public static final String PREF_KEY_LOGIN_USER_SUBDIS_ID = "app.user.subdisid";
    public static final String PREF_KEY_LOGIN_USER_ID = "app.user.id";
    public static final String PREF_KEY_LOGIN_USER_MODULES = "app.user.modules";
    public static final String PREF_KEY_LOGIN_USER_SUBDIS_NAME = "app.subdisName";
    public static final String PREF_KEY_LOGIN_APP_SPID = "app.spid";
    public static final String PREF_KEY_LOGIN_SESSION_DATE= "app.session.data";

    public static final String PREF_KEY_LOGIN_USER_CPID = "app.user.cpid";//channel point id = cpid
    public static final String PREF_KEY_LOGIN_USER_HUMANNAME = "app.user.humanName";
    public static final String PREF_KEY_LOGIN_USER_HEADIMAGE = "app.headimage";
    public static final String PREF_KEY_LOGIN_USER_SEX = "app.user.sex";

    //软件更新
    public static final String PREF_NAME_APP_UPDATE = "app_update";
    public static final String PREF_KEY_APP_UPDATE_NAME = "app_name";
    public static final String PREF_KEY_APP_UPDATE_VERSIONNAME = "app_versionName";

    //个推
    public static final String PREF_NAME_PUSH = "push.clientid.share";
    public static final String PREF_KEY_PUSH_CLIENT = "push.clientid.value";

    public static SharedPreferences getPreferences(String prefName) {
        return SharedPreferencesUtil.getPreferences(BizApplication.getAppContext(),
                prefName);
    }

    /**
     * 获取App唯一标识,若没有则自动生成一个
     * @return
     */
    public static String getAppId(){
        String uniqueID = SharedPreferencesUtil.get(ComnApplication.getAppContext(),
                PREF_NAME_COMN_APP, PREF_KEY_COMN_APP_UNIQUEID, null);

        if(StringUtils.isEmpty(uniqueID)){
            uniqueID = UUID.randomUUID().toString();

            SharedPreferencesUtil.set(ComnApplication.getAppContext(),
                    PREF_NAME_COMN_APP, PREF_KEY_COMN_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public static void setAppId(String appId){
        SharedPreferencesUtil.set(BizApplication.getAppContext(),
                PREF_NAME_COMN_APP, PREF_KEY_COMN_APP_UNIQUEID, appId);
    }

    /**
     * 保存用户登录相关信息
     * @param username 登录用户名
     * @param password 登录密码
     * @param userMixInfo 用户详细信息
     * */
    public static void saveUserLoginInfo(String username, String password, UserMixInfo userMixInfo){
        if (userMixInfo == null) {
            return;
        }
        MLog.d("saveUserMixInfo" + JSONObject.toJSONString(userMixInfo));

        SharedPreferences.Editor editor = getPreferences(PREF_NAME_LOGIN).edit();

        //登录用户名和密码
        editor.putString(PREF_KEY_LOGIN_USERNAME, username);
        editor.putString(PREF_KEY_LOGIN_PASSWORD, password);

        //登录
        editor.putString(PREF_KEY_LOGIN_LAST_SESSION_ID, userMixInfo.getSessionId());
        editor.putString(PREF_KEY_LOGIN_USER_GUID, String.valueOf(userMixInfo.getHumanId()));
        editor.putString(PREF_KEY_LOGIN_USER_TELEPHONE, userMixInfo.getPhonenumber());
        //"cookiees":["JSESSIONID=7089e003-db7e-4dbb-9709-05c595e5771e; Path=/pmc"]
        List<String> cookies = userMixInfo.getCookiees();
        if (cookies != null && cookies.size() > 0){
            editor.putString(PREF_KEY_LOGIN_HTTP_COOKIE, cookies.get(0));
        }
        Object objId = userMixInfo.getId();
        if (objId instanceof String){
            editor.putLong(PREF_KEY_LOGIN_USER_ID, Long.parseLong(objId.toString()));
        }
        else{
            editor.putLong(PREF_KEY_LOGIN_USER_ID, userMixInfo.getId());
        }

        UserAttribute userAttribute = userMixInfo.getUserAttribute();
        if(userAttribute != null){
//            editor.putString(PREF_KEY_LOGIN_USER_CPID, userAttribute.getCpid());
            editor.putString(PREF_KEY_LOGIN_USER_HUMANNAME, userAttribute.getHumanName());
            editor.putString(PREF_KEY_LOGIN_USER_HEADIMAGE, userAttribute.getHeadimage());
//            editor.putString(PREF_KEY_LOGIN_USER_MODULES, userAttribute.getModuleNames());
            editor.putString(PREF_KEY_LOGIN_USER_SEX, userAttribute.getSex());
        }

        List<UserComInfo> userComInfos = userMixInfo.getComInfos();
        if(userComInfos != null && userComInfos.size() > 0){
            UserComInfo userComInfo = userComInfos.get(0);

            editor.putLong(PREF_KEY_LOGIN_APP_SPID, userComInfo.getSpid());
            editor.putString(PREF_KEY_LOGIN_USER_SUBDIS_ID, userComInfo.getSubdisIds());

            StringBuilder sbSubdisNames = new StringBuilder();
            List<SubdisList> subdises = userComInfo.getSubdisList();
            if (null != subdises) {
                for (int i = 0; i < subdises.size(); i++) {
                    //loginService.subdisNames.put(subdises.get(i).getId(), subdises.get(i).getSubdisName());
                    if (i > 0){
                        sbSubdisNames.append(",");
                    }
                    sbSubdisNames.append(subdises.get(i).getSubdisName());
                }
            }
            editor.putString(PREF_KEY_LOGIN_USER_SUBDIS_NAME, sbSubdisNames.toString());
        }


        editor.putLong(PREF_KEY_LOGIN_SESSION_DATE, new Date().getTime() + 1000 * 60 * 60 * 3);//三个小时内不去请求

        editor.commit();
    }

    public static void setLoginUsername(String clientId){
        SharedPreferencesUtil.set(BizApplication.getAppContext(),
                PREF_NAME_LOGIN, PREF_KEY_LOGIN_USERNAME, clientId);
    }

    public static String getLoginUsername(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(),
                PREF_NAME_LOGIN, PREF_KEY_LOGIN_USERNAME, "");
    }

    public static void saveLastSessionId(String sessionId){
        SharedPreferencesUtil.set(BizApplication.getAppContext(),
                PREF_NAME_LOGIN, PREF_KEY_LOGIN_LAST_SESSION_ID, sessionId);
    }

    public static String getLastSessionId(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(),
                PREF_NAME_LOGIN, PREF_KEY_LOGIN_LAST_SESSION_ID, null);
    }

    public static String getUserSubdisId(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(), PREF_NAME_LOGIN, PREF_KEY_LOGIN_USER_SUBDIS_ID, null);
    }

    public static String getUserGuid(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(), PREF_NAME_LOGIN, PREF_KEY_LOGIN_USER_GUID, null);
    }

    public static Long getUserId(){
        return SharedPreferencesUtil.getLong(BizApplication.getAppContext(),
                PREF_NAME_LOGIN, PREF_KEY_LOGIN_USER_ID, 0L);
    }

    public static String getCookies(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(),
                PREF_NAME_LOGIN, PREF_KEY_LOGIN_HTTP_COOKIE, "");
    }

    public static void setLocationAcceptEnabled(boolean enabled){
        SharedPreferencesUtil.set(BizApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_LOCATION_ACCEPT, enabled);
    }

    /**
     * 是否开启位置服务：默认true
     * */
    public static boolean getLocationAcceptEnabled(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_LOCATION_ACCEPT, true);
    }

    public static void setNotificationAcceptEnabled(boolean enabled){
        SharedPreferencesUtil.set(BizApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_NOTIFICATION_ACCEPT, enabled);
    }

    /**
     * 是否接收通知：默认true
     * */
    public static boolean getNotificationAcceptEnabled(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(), Constants.PREF_NAME_CONFIG,
                Constants.PREF_KEY_CONFIG_NOTIFICATION_ACCEPT, true);
    }

    public static String getPushClientId(){
        return SharedPreferencesUtil.get(BizApplication.getAppContext(),
                PREF_NAME_PUSH, PREF_KEY_PUSH_CLIENT, null);
    }

    public static void savePushClientId(String clientId){
        SharedPreferencesUtil.set(BizApplication.getAppContext(),
                PREF_NAME_PUSH, PREF_KEY_PUSH_CLIENT, clientId);
    }







}
