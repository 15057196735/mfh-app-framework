package com.mfh.comna.bizz.login.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comn.net.JsonParser;
import com.mfh.comn.net.ResponseBody;
import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.web.WebViewUtils;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.bizz.config.URLConf;
import com.mfh.comna.bizz.login.LoginCallback;
import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comna.bizz.login.MsgBridgeUtil;
import com.mfh.comna.bizz.login.entity.Office;
import com.mfh.comna.bizz.login.entity.SubdisList;
import com.mfh.comna.bizz.login.entity.UserAttribute;
import com.mfh.comna.bizz.login.entity.UserComInfo;
import com.mfh.comna.bizz.login.entity.UserMixInfo;
import com.mfh.comna.bizz.msg.logic.MsgSetUtil;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 登录服务
 * Created by Administrator on 14-5-5.
 */
public class MfhLoginService implements LoginService {
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
    public static final String PREF_KEY_LOGIN_SESSION_DATE= "app.session.date";

    public static final String PREF_KEY_LOGIN_USER_CPID = "app.user.cpid";//channel point id = cpid
    public static final String PREF_KEY_LOGIN_USER_HUMANNAME = "app.user.humanName";
    public static final String PREF_KEY_LOGIN_USER_HEADIMAGE = "app.headimage";
    public static final String PREF_KEY_LOGIN_USER_SEX = "app.user.sex";


    public static final String PREF_KEY_LOGIN_USER_OFFICE_IDS = "app.user.office.ids";
    public static final String PREF_KEY_LOGIN_USER_CURRENT_STOCKID = "app.user.current.stock.id";

    private SharedPreferences spLogin = null;
    private String loginName;//登录账号
    private String password;//登录密码
    private String sessionId = null;
    private String guid = null;//humanId
    private String cpid = null;
    private Long userId = null;
    private String mySubdisIds = null;//关联的小区
    private Long curSubdis = null;
//    private String officeIds = null;//
    private String curStockId = null;
    private String moduleNames = null;//当前用户支持的功能列表
    private String subdisNames ="";
    private String telephone = "";
    private String humanName = "";//用户名
    private Long spid = 0L;//公司Id
    private Long sessionDate = 0L;
    private String cookie = "";
    private String headimage = "";
    private String sex;

    private static MfhLoginService instance = null;

    /**
     * 获取实例
     * @return
     */
    public static MfhLoginService get() {
        String lsName = MfhLoginService.class.getName();
        if (ServiceFactory.checkService(lsName))
            instance = ServiceFactory.getService(lsName);
        else {
            instance = new MfhLoginService();//初始化登录服务
        }
        return instance;
    }

    /**
     * 构造函数
     */
    public MfhLoginService() {
        spLogin = SharedPreferencesHelper.getPreferences(PREF_NAME_LOGIN);
        restore();
        ServiceFactory.putService(MfhLoginService.class.getName(), this);
    }

    /**
     * 重新从缓存中读取
     */
    public void  restore() {
        loginName =  spLogin.getString(PREF_KEY_LOGIN_USERNAME, "");// UConfigHelper.getAppConfig().getString("app.login.name");
        password = spLogin.getString(PREF_KEY_LOGIN_PASSWORD, "");// UConfigHelper.getAppConfig().getString("app.login.password");
        sessionId = spLogin.getString(PREF_KEY_LOGIN_LAST_SESSION_ID, null);
        guid = spLogin.getString(PREF_KEY_LOGIN_USER_GUID, null);
        mySubdisIds = spLogin.getString(PREF_KEY_LOGIN_USER_SUBDIS_ID, null);
//        officeIds = spLogin.getString(PREF_KEY_LOGIN_USER_OFFICE_IDS, null);
        curStockId = spLogin.getString(PREF_KEY_LOGIN_USER_CURRENT_STOCKID, null);
        userId = spLogin.getLong(PREF_KEY_LOGIN_USER_ID, -1L);
        cpid = spLogin.getString(PREF_KEY_LOGIN_USER_CPID,guid);
        moduleNames = spLogin.getString(PREF_KEY_LOGIN_USER_MODULES, null);
        subdisNames = spLogin.getString(PREF_KEY_LOGIN_USER_SUBDIS_NAME, null);
        telephone = spLogin.getString(PREF_KEY_LOGIN_USER_TELEPHONE, "");
        humanName = spLogin.getString(PREF_KEY_LOGIN_USER_HUMANNAME, "");
        spid = spLogin.getLong(PREF_KEY_LOGIN_APP_SPID, 0L);
        sessionDate = spLogin.getLong(PREF_KEY_LOGIN_SESSION_DATE, 0L);
        cookie = spLogin.getString(PREF_KEY_LOGIN_HTTP_COOKIE, "");
        headimage = spLogin.getString(PREF_KEY_LOGIN_USER_HEADIMAGE, "");
        sex = spLogin.getString(PREF_KEY_LOGIN_USER_SEX, "");
    }

    /**
     * 保存用户相关信息
     */
    public void save() {
        SharedPreferences.Editor editor = spLogin.edit();
        editor.putString(PREF_KEY_LOGIN_USERNAME, loginName);
        editor.putString(PREF_KEY_LOGIN_PASSWORD, password);
        editor.putString(PREF_KEY_LOGIN_LAST_SESSION_ID, sessionId);
        editor.putString(PREF_KEY_LOGIN_USER_GUID, guid);
        editor.putString(PREF_KEY_LOGIN_USER_TELEPHONE, telephone);
        editor.putString(PREF_KEY_LOGIN_HTTP_COOKIE, cookie);
        editor.putLong(PREF_KEY_LOGIN_USER_ID, userId);
        editor.putString(PREF_KEY_LOGIN_USER_HUMANNAME, humanName);
        editor.putString(PREF_KEY_LOGIN_USER_HEADIMAGE, headimage);
        editor.putString(PREF_KEY_LOGIN_USER_SEX, sex);
        editor.putLong(PREF_KEY_LOGIN_APP_SPID, spid);
        editor.putString(PREF_KEY_LOGIN_USER_SUBDIS_ID, mySubdisIds);
//        editor.putString(PREF_KEY_LOGIN_USER_OFFICE_IDS, officeIds);
        editor.putString(PREF_KEY_LOGIN_USER_CURRENT_STOCKID, curStockId);
        editor.putString(PREF_KEY_LOGIN_USER_SUBDIS_NAME, subdisNames);
        editor.putLong(PREF_KEY_LOGIN_SESSION_DATE, sessionDate);
        editor.putString(PREF_KEY_LOGIN_USER_CPID, cpid);
        editor.putString(PREF_KEY_LOGIN_USER_MODULES, moduleNames);
        editor.commit();
    }



    /**
     * 清除当前登录用户的缓存信息
     */
    public void clear() {
        SharedPreferences.Editor editor = spLogin.edit();
        editor.clear();
        editor.commit();

//        SharedPreferencesHelper.saveLastSessionId("");
        restore();
    }



    /**
     * 获取当前用户所关联的小区列表
     * @return
     */
    public String getMySubdisIds() {
        return mySubdisIds;
    }

    public void setMySubdisIds(String mySubdisIds) {
        this.mySubdisIds = mySubdisIds;
    }


//    public String getOfficeIds() {
//        return officeIds;
//    }
//
//    public void setOfficeIds(String officeIds) {
//        this.officeIds = officeIds;
//    }

    public String getModuleNames() {
        return moduleNames;
    }


    public void setModuleNames(String moduleNames) {
        this.moduleNames = moduleNames;
    }

    /**
     * 获取模块名数组
     * @return
     */
    public String[] getModuleNameArray() {
        if (StringUtils.isBlank(moduleNames))
            return null;
        return StringUtils.splitByWholeSeparator(moduleNames, ",");
    }

    /**
     * 获取当前登录后选择的小区
     * @return
     */
    public Long getCurSubdis() {
        if (curSubdis == null && StringUtils.isNotBlank(mySubdisIds)) {
            if (mySubdisIds == null || mySubdisIds.length() == 0)
                return null;
            String[] ids = StringUtils.splitByWholeSeparator(mySubdisIds, ",");
            return Long.parseLong(ids[0]);
        }
        else
            return curSubdis;
    }

    public String getCurStockId() {
        return curStockId;
    }

    public void setCurStockId(String curStockId) {
        this.curStockId = curStockId;
    }


    //    public Long getCurrentStockId(){
//        if (curStockId == null){
//            if (StringUtils.isEmpty(officeIds)){
//                return  null;
//            }else{
//               String[] ids = StringUtils.splitByWholeSeparator(officeIds, ",");
//                //TODO
//            }
//
//        }else{
//            return curStockId;
//        }
//    }

    @Override
    public String getCpId() {
        return cpid;
    }

    public void setCpid(String cpid){
        this.cpid = cpid;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getCurrentSessionId() {
        return sessionId;
    }

    public void setLastSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    @Override
    public String getLoginName() {
        return loginName;//"rchywy";
    }

    @Override
    public String getCurrentGuId() {
        return this.guid;//1038L
    }

    public void setCurrentGuid(String guid){
        this.guid = guid;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 修改密码
     * */
    public void changePassword(String newPwd){
        this.password = newPwd;

        SharedPreferences.Editor editor = spLogin.edit();
        editor.putString(PREF_KEY_LOGIN_PASSWORD, newPwd);
        editor.commit();
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getSpid() {
        return spid;
    }

    /**
     * 修改密码
     * */
    public void updateSex(String sex){
        this.sex = sex;

        SharedPreferences.Editor editor = spLogin.edit();
        editor.putString(PREF_KEY_LOGIN_USER_SEX, sex);
        editor.commit();
    }

    private UserMixInfo parseResponse( ResponseBody resp) {
        if (!resp.isSuccess()) {
            //401/unau###
            String errMsg = resp.getRetCode() + ":" + resp.getReturnInfo();
//            throw new RuntimeException(errMsg);
            MLog.e("loginFailed: " +  errMsg);
            return null;
        }
        else {
            IResponseData rspData = resp.getData();
            RspBean<UserMixInfo> retValue = (RspBean<UserMixInfo>)rspData;
            UserMixInfo um = retValue.getValue();
            return um;
        }
    }

    /**
     * 是否已经成功登录过
     * @return
     */
    public boolean haveLogined() {
        return sessionId != null;
    }

    /**
     * 执行登录(同步调用)
     * @param name
     * @param pwd
     * @return
     */
    public UserMixInfo doLogin(String name, String pwd) {
        try{
            Object ret = NetFactory.getHttp(false).postSync(URLConf.URL_LOGIN,
                    new AjaxParams(URLConf.PARAM_KEY_USERNAME,name,
                            URLConf.PARAM_KEY_PASSWORD, pwd,
                            URLConf.PARAM_KEY_LOGIN_TYPE, URLConf.PARAM_VALUE_LOGIN_TYPE_DEF));

            //解析
            JsonParser parser = new JsonParser();
            ResponseBody resp = parser.parser(ret.toString(), UserMixInfo.class, JsonParser.defaultFormat);
            return parseResponse(resp);
        }
        catch(Exception e){
            MLog.e("doLogin failed, " + e.toString());
            return null;
        }
    }

    /**
     * 执行异步登录
     * @param name
     * @param pwd
     * @param loginCallback
     * @param loginKind
     * @return
     */
    public void doLoginAsync(final String name, final String pwd, final LoginCallback loginCallback,
                              String url, String loginType, String loginKind) {
        //回调
        NetCallBack.NetTaskCallBack callback = new NetCallBack.NetTaskCallBack<UserMixInfo,
                NetProcessor.Processor<UserMixInfo>>(new NetProcessor.Processor<UserMixInfo>() {
            @Override
            public void processResult(IResponseData rspData) {
                RspBean<UserMixInfo> retValue = (RspBean<UserMixInfo>)rspData;
//                Log.d("Nat: loginResponse", String.format("retValue= %s", retValue.toString()));
                UserMixInfo um = retValue.getValue();

                saveUserMixInfo(name, pwd, um);
                refreshMsgBridge();

                if (loginCallback != null){
                    loginCallback.loginSuccess(um);
                }
            }

            @Override
            protected void processFailure(Throwable t, String errMsg) {
                super.processFailure(t, errMsg);
                if (loginCallback != null){
                    loginCallback.loginFailed(errMsg);
                }
            }
        }, UserMixInfo.class, BizApplication.getAppContext()) {
        };

        AjaxParams params = new AjaxParams();
        params.put(URLConf.PARAM_KEY_USERNAME, name);
        params.put(URLConf.PARAM_KEY_PASSWORD, pwd);
        if (!TextUtils.isEmpty(loginKind)) {
            params.put(URLConf.PARAM_KEY_LOGIN_KIND, loginKind);
        }
        if (!TextUtils.isEmpty(loginType)) {
            params.put(URLConf.PARAM_KEY_LOGIN_TYPE, loginType);
        }

        FinalHttp finalHttp =  NetFactory.getHttp(false);
        //TODO
//        finalHttp.configUserAgent(ComnApplication.getUserAgent());
        finalHttp.post(url, params, callback);
    }
    public void doLoginAsync(String name, String pwd, LoginCallback loginCallback) {
//        doLoginAsync(name, pwd, loginCallback, URLConf.URL_LOGIN,
//                URLConf.PARAM_VALUE_LOGIN_TYPE_DEF, URLConf.PARAM_KEY_LOGIN_KIND);
        doLoginAsync(name, pwd, loginCallback, URLConf.URL_LOGIN, null, null);
    }


    @Override
    public String doLogin() {
        UserMixInfo um = doLogin(this.loginName, this.password);
        if (um != null){
            saveUserMixInfo(this.loginName, this.password, um);
            refreshMsgBridge();
            return um.getSessionId();
        }
        else{
            return null;
        }
    }

    /**
     * 执行注销
     */
    public void logout(NetCallBack.NormalNetTask callback) {
        if (sessionId == null){
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put(NetFactory.CLIENTSESSION, sessionId);
        NetFactory.getHttp().post(URLConf.URL_LOGOUT, params, callback);
        //注销消息桥，发广播。。。
    }

    /**
     * 使会话失效
     * */
    public void expireSession(NetCallBack.NormalNetTask callback){
        if (sessionId == null)
            return;
        String loginUrl = NetFactory.getServerUrl() + "/exit";
        AjaxParams params = new AjaxParams();
        params.put("sid", sessionId);
        params.put("lgdrt", "2");
        NetFactory.getHttp().post(loginUrl,params, callback);
    }

    @Override
    public String getSubdisNames() {
        return subdisNames;
    }

    public void setSubdisNames(String subdisNames) {
        this.subdisNames = subdisNames;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

    public String getHumanName() {
//        return humanName;
        return spLogin.getString(PREF_KEY_LOGIN_USER_HUMANNAME, "");
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public void changeHumanName(String humanName){
        this.humanName = humanName;

        SharedPreferences.Editor editor = spLogin.edit();
        editor.putString(PREF_KEY_LOGIN_USER_HUMANNAME, humanName);
        editor.commit();
    }

    public String getHeadimage() {
//        return headimage;
        return spLogin.getString(PREF_KEY_LOGIN_USER_HEADIMAGE, "");
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public void updateHeadimage(String headimage){
        SharedPreferences.Editor editor = spLogin.edit();
        editor.putString(PREF_KEY_LOGIN_USER_HEADIMAGE, headimage);
        editor.commit();

        this.headimage = headimage;
    }

    public Long getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Long sessionDate) {
        this.sessionDate = sessionDate;
    }

    //这个方法获取的值不正确，可能在别的地方创建了MfhLoginService的实例
    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        SharedPreferences.Editor editor = spLogin.edit();
        editor.putString(PREF_KEY_LOGIN_HTTP_COOKIE, cookie);
        editor.commit();
    }

    /**
     * 同步Cookie
     * */
    public void synCookies(Context context, String url) {
        //获取cookie信息
        WebViewUtils.syncCookies(context, url, cookie);
    }

    /**
     * 保存用户登录相关信息
     * @param username 登录用户名
     * @param password 登录密码
     * @param um 用户详细信息
     * */
    public void saveUserMixInfo(String username, String password, UserMixInfo um){
        if(um == null){
            return;
        }

        final MfhLoginService loginService = this;
        loginService.loginName = username;
        loginService.password = password;

        loginService.guid = String.valueOf(um.getHumanId());
        loginService.sessionId = um.getSessionId();
        loginService.telephone = um.getPhonenumber();

        if (um.getCookiees() != null){
            loginService.cookie = um.getCookiees().get(0);
        }
        Object objId = um.getId();
        if (objId instanceof String)
            loginService.userId = Long.parseLong(objId.toString());
        else
            loginService.userId = um.getId();

        UserAttribute userAttribute = um.getUserAttribute();
        if(userAttribute != null){
//            loginService.cpid = userAttribute.getCpid();
            loginService.humanName = userAttribute.getHumanName();
            loginService.headimage = userAttribute.getHeadimage();
//            loginService.moduleNames = userAttribute.getModuleNames();
//            loginService.spid = userAttribute.getSpid();
            loginService.sex = userAttribute.getSex();
        }

        List<UserComInfo> userComInfos = um.getComInfos();
        StringBuilder sbSubdisNames = new StringBuilder();
        if(userComInfos != null && userComInfos.size() > 0){
            UserComInfo userComInfo = userComInfos.get(0);
            loginService.spid = userComInfo.getSpid();

            loginService.mySubdisIds = userComInfo.getSubdisIds();

            loginService.curStockId = null;
            Long curOffice = userComInfo.getCurOffice();
            List<Office> offices = userComInfo.getOffices();
            if (offices != null && offices.size() > 0){
                for (Office office : offices){
                    if (office.getCode().compareTo(curOffice) == 0){
                        loginService.curStockId = office.getStockId();
                        break;
                    }
                }
            }

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
        }
        loginService.subdisNames = sbSubdisNames.toString();

        loginService.sessionDate = new Date().getTime() + 1000 * 60 * 60 * 3;//三个小时内不去请求

        loginService.save();
        restore();
    }

    public void refreshMsgBridge(){
        //EmbSessionService sessionService = ServiceFactory.getService(EmbSessionService.class);
        //sessionService.queryFromNetToSaveMaxUpDateDate();//保存第一次登陆的时间
        //保存当前的时间，即第一次登陆的时间
        MsgSetUtil msgSet = new MsgSetUtil(loginName);
        msgSet.setMaxMsgUpdateDate(new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT).format(new Date()));//保存登陆时间

        //注册到消息桥
        MsgBridgeUtil.registerMsg();
    }

}

