package com.mfh.comna.bizz.config;

import com.mfh.comna.bizz.AppConfig;
import com.mfh.comn.config.UConfig;
import com.mfh.comna.network.NetFactory;

/**
 * 后台接口相关配置·URL
 * Created by Administrator on 2015/6/11.
 */
public class URLConf {
    public final static String URL_BASE_SERVER = NetFactory.getServerUrl();
    //网络电话
    public final static String URL_NET_PHONE = URL_BASE_SERVER.replace(":8080/pmc", "") + "/msgcore/embYtx/getYuninfoByGuid";
    //登录
    public final static String URL_LOGIN = URL_BASE_SERVER + "/login";
    //退出
    public final static String URL_LOGOUT = URL_BASE_SERVER + "/exit";
    //会话
    public final static String URL_GET_SESSION_BY_ID = URL_BASE_SERVER + "/biz/msg/getSessionById";

    //域名
    public static String DOMAIN = "devmobile.manfenjiayuan.com";
    public static String URL_DEFAULT = "http://devmobile.manfenjiayuan.com/";

    //注册消息桥参数
    public final static String PARAM_KEY_CHANNEL_ID = "channelid";//渠道编号
    public final static String PARAM_KEY_QUEUE_NAME = "queuename";
    public final static String PARAM_KEY_JSONSTR = "jsonStr";
    public static int PARAM_VALUE_CHANNEL_ID_DEF = 62;//渠道编号
    public static String PARAM_VALUE_QUEUE_NAME_DEF = "pmc-app-queue";


    static{
        if(AppConfig.RELEASE){
            DOMAIN = "mobile.manfenjiayuan.com";
            URL_DEFAULT = "http://mobile.manfenjiayuan.com/";
            PARAM_VALUE_QUEUE_NAME_DEF = "pmc-app-queue";
        }else{
            DOMAIN = "devmobile.manfenjiayuan.com";
            URL_DEFAULT = "http://devmobile.manfenjiayuan.com/";
            PARAM_VALUE_QUEUE_NAME_DEF = "pmc-app-queue-test";
        }
    }

    //URL
    public final static String URL_TOKEN_REGISTER_MESSAGE = "app.message.url";
    public final static String URL_TOKEN_REGISTER_MESSAGE_DEV = "dev.app.message.url";


    //登录参数
    public final static String PARAM_KEY_USERNAME = "username";
    public final static String PARAM_KEY_PASSWORD = "password";
    public final static String PARAM_KEY_LOGIN_TYPE = "loginType";
    public final static String PARAM_KEY_LOGIN_KIND = "loginKind";
    public final static String PARAM_VALUE_LOGIN_TYPE_DEF = "PMC";

    public final static String URL_SESSION_LIST = "/biz/msg/getSessionList";

    public static String getUrlForMessage(){
        if(AppConfig.RELEASE){
            return NetFactory.getServerUrl(UConfig.CONFIG_COMMON, URL_TOKEN_REGISTER_MESSAGE);
        }else{
            return NetFactory.getServerUrl(UConfig.CONFIG_COMMON, URL_TOKEN_REGISTER_MESSAGE_DEV);
        }
    }



}
