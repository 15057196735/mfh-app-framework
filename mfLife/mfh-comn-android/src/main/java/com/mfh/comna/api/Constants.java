package com.mfh.comna.api;

/**
 * Created by Administrator on 2015/4/30.
 */
public class Constants {
    public static final String EXTRA_HANDLING_NOTIFICATION = "Notification.EXTRA_HANDLING_NOTIFICATION";

    //应用配置
    public static final String PREF_NAME_CONFIG = "app_config";
    public static final String PREF_KEY_APP_FIRST_START= "PREF_KEY_APP_FIRST_START";//首次启动
    public static final String PREF_KEY_CONFIG_NOTIFICATION_ACCEPT  = "PREF_KEY_CONFIG_NOTIFICATION_ACCEPT";//开启通知
    public static final String PREF_KEY_CONFIG_LOCATION_ACCEPT      = "PREF_KEY_CONFIG_LOCATION_ACCEPT";//开启位置服务

    //网络请求参数关键字
    public static final String PARAM_KEY_SOURCE_ID = "sourceid";
    public static final String PARAM_KEY_SESSION_ID = "sessionid";
    public static final String PARAM_KEY_CHANNEL_POINT_ID = "channelpointid";
    public static final String PARAM_KEY_GUID = "guid";
    public static final String PARAM_KEY_JSON_STR = "jsonStr";
    public static final String PARAM_KEY_SUBDIS_ID = "subdisid";
    public static final String PARAM_KEY_BUREAD_UID = "bureaduid";
    public static final String PARAM_KEY_LASTUPDATE = "lastupdate";
    public static final String PARAM_KEY_CREATE_GUID = "createguid";
    public static final String PARAM_KEY_TYPE = "type";
    public static final String PARAM_KEY_BIND = "bind";
    public static final String PARAM_KEY_PIC_URL = "picUrl";


    //Broadcast
    public static final String ACTION_BEACONS_UPDATE = "ACTION_BEACONS_UPDATE";
    public static final String KEY_BEACONS_EXIST = "KEY_BEACONS_EXIST";
    public static final String ACTION_REDIRECT_TO_LOGIN_H5 = "ACTION_REDIRECT_TO_LOGIN_H5";//跳转到登录页面

    public static final String ACTION_WEIXIN_GET_PREPARE_ID_SUCCESS = "ACTION_WEIXIN_GET_PREPARE_ID_SUCCESS";


    //UI Bundle
    public static final String BUNDLE_EXTRA_KEY_HUMAN_ID = "humanId";
    public static final String BUNDLE_EXTRA_KEY_LATITUDE = "latitude";
    public static final String BUNDLE_EXTRA_KEY_LONGITUDE = "longitude";
    public static final String BUNDLE_EXTRA_KEY_MARKER_TITLE = "marker_title";
    public static final String BUNDLE_EXTRA_KEY_MARKER_SNIPPET = "marker_snippet";
    public static final String BUNDLE_EXTRA_KEY_TOPBAR_TITLE = "topbarTitle";


}
