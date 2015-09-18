package com.mfh.comna.network;

import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.bizz.AppConfig;
import com.mfh.comn.config.UConfig;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.comn.cfg.UConfigHelper;

import net.tsz.afinal.FinalHttp;

import org.apache.http.cookie.Cookie;

/**
 * 服务器网络连接工厂
 * 
 * @author zhangyz created on 2013-5-15
 * @since Framework 1.0
 */
public class NetFactory {
    private static FinalHttp fh = null;//fh可以被多线程同时使用
    public static final String CLIENTSESSION = "JSESSIONID";//传给服务器的会话Id
    public static Cookie cookie;
    
    /**
     * 获取http连接
     * @return
     * @author zhangyz created on 2013-5-15
     */
    public static FinalHttp getHttp() {
        if (fh == null) {
            synchronized (FinalHttp.class) {
                if (fh == null) {
                    fh = new FinalHttp();
                    //必要的配置工作,以后要从配置文件中读取
                    fh.configCharset("utf-8");
                    fh.configTimeout(30000);
                    fh.configRequestExecutionRetryCount(0);//0代表无需重试。
                }
            }
        }

        String sessionId = MfhLoginService.get().getCurrentSessionId();
        if (sessionId != null){
            fh.addHeader("Set-Cookie", String.format("JSESSIONID=%s", sessionId));
            fh.addHeader("Cookie", String.format("JSESSIONID=%s", sessionId));
            fh.addHeader("cookie", String.format("JSESSIONID=%s", sessionId));
        }else{
            fh.removeHeader("Set-Cookie");
            fh.removeHeader("Cookie");
            fh.removeHeader("cookie");
        }

        return fh;
    }

    public static FinalHttp getHttp(boolean bAttachCookie) {
        if (fh == null) {
            synchronized (FinalHttp.class) {
                if (fh == null) {
                    fh = new FinalHttp();
                    //必要的配置工作,以后要从配置文件中读取
                    fh.configCharset("utf-8");
                    fh.configTimeout(30000);
                    fh.configRequestExecutionRetryCount(0);//0代表无需重试。
                }
            }
        }

        String sessionId = MfhLoginService.get().getCurrentSessionId();
        if (bAttachCookie && !StringUtils.isEmpty(sessionId)){
            fh.addHeader("Set-Cookie", String.format("JSESSIONID=%s", sessionId));
            fh.addHeader("Cookie", String.format("JSESSIONID=%s", sessionId));
            fh.addHeader("cookie", String.format("JSESSIONID=%s", sessionId));
        }else{
            fh.removeHeader("Set-Cookie");
            fh.removeHeader("Cookie");
            fh.removeHeader("cookie");
        }

        return fh;
    }

    /**
     * 获取服务器，不以/结尾
     * 带参数，上面那个方法不带参，属于默认的只有一个配置文件的方法，
     * 本方法适用于一个以上的配置文件
     * @paam common
     * @param url
     * @return
     */
    public static String getServerUrl(String common, String url){
        String serverUrl = UConfigHelper.getConfig().getDomain(common)
                .getString(url);
        if (serverUrl != null && serverUrl.endsWith("/")){
            serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
        }

        MLog.d("getServerUrl:" + serverUrl);
        return serverUrl;
    }

    /**
     * 获取服务器url地址，不以/结尾。
     * @return
     */
    public static String getServerUrl() {
        if(AppConfig.RELEASE){
            return getServerUrl(UConfig.CONFIG_COMMON, UConfig.CONFIG_PARAM_SERVERURL);
        }else{
            return getServerUrl(UConfig.CONFIG_COMMON, "dev." + UConfig.CONFIG_PARAM_SERVERURL);
        }
    }

    /**
     * 获取升级地址，不以/结尾
     * @return
     */
    public static String getUpdateServerUrl() {
        if(AppConfig.RELEASE){
            return getServerUrl(UConfig.CONFIG_COMMON, UConfig.CONFIG_PARAM_UPDATEURL);
        }else{
            return getServerUrl(UConfig.CONFIG_COMMON, "dev." + UConfig.CONFIG_PARAM_UPDATEURL);
        }
    }

    /**
     * 获取上传图片的URL
     * @return
     */
    public static String getImageUploadUrl() {
        return getServerUrl(UConfig.CONFIG_COMMON, UConfig.CONFIG_PARAM_IMAGE_UPLOAD);
    }

    public static String getChannelId(){
        String channelId = UConfigHelper.getConfig().getDomain(UConfig.CONFIG_COMMON)
                .getString("channel.id");
        return channelId;
    }


}
