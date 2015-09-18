package com.mfh.comna.api.web;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.api.helper.HtmlManager;
import com.mfh.comna.api.helper.URLHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.bizz.AppConfig;
import com.mfh.comna.comn.ComnApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/7.
 */
public class WebViewUtils {
    private static final String TAG = WebViewUtils.class.getSimpleName();

    //"/data/data/package_name/app_cache"
    public static final String APP_CACHE_DIR = BizApplication.getAppContext()
            .getDir("cache", Context.MODE_PRIVATE).getPath();
    //"/data/data/package_name/cache"
    public static final String APP_CACHE_DIR2 = BizApplication.getAppContext().getCacheDir().getAbsolutePath();
    ///"/data/data/com.mfh.owner/files"
    public static final String APP_FILES_DIR = BizApplication.getAppContext().getFilesDir().getAbsolutePath();
    public static final String DB_PATH = BizApplication.getAppContext()
            .getDir("database", Context.MODE_PRIVATE).getPath();


    public static void syncCookie(Context context, String domainNameUrl, String... strings) {
        CookieSyncManager.createInstance(context);
        //CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        for (String s : strings) {
            cookieManager.setCookie(domainNameUrl, s);

        }
        MLog.d("syncCookie: " +  cookieManager.getCookie(domainNameUrl));
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 同步Cookie
     * 将Cookie和url关联，保持登录状态,在调用loadUrl方法之前设置
     * Cookie 需要在Setting设置后设置，否则无效。
     * Sets a cookie for the given URL. Any existing cookie with the same host, path and name will be replaced with the new cookie.
     * The cookie being set must not have expired and must not be a session cookie, otherwise it will be ignored.
     *
     * Set-Cookie: =[; =][; expires=][; domain=][; path=][; secure][; httponly]
     * expires=: 设置cookie的有效期，如果cookie超过date所表示的日期时，cookie将失效。如果没有设置这个选项，那么cookie将在浏览器关闭时失效。
     */
    public static void syncCookies(Context context, String url, String cookies){
        try{
            MLog.d(String.format("synCookies:[url=%s][cookie=%s]", url, cookies));
            CookieSyncManager.createInstance(context);
//            cookieSyncManager.sync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();// 移除
//        cookieManager.removeAllCookie();

            cookieManager.setCookie(url, cookies);

            CookieSyncManager.getInstance().sync();
        }
        catch(Exception e){
            Log.e(TAG, e.toString());
        }
    }

    /**
     * set cookie
     * */
    public static void setCookie(String url, String cookies){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookies);
    }

    public static void showCookie(Context context, String url) {
        // TODO Auto-generated method stub
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        cookieSyncManager.sync();
        CookieManager cookieManager = CookieManager.getInstance();
        String cookieStr = cookieManager.getCookie(url);
        if (cookieStr != null) {
            MLog.d("Cookie: " + cookieStr);
        }
    }

    /**
     * Get a historical list of webview
     * @param webView
     * @return
     */
    public static WebBackForwardList getHistoricalList(WebView webView) {
        return webView.copyBackForwardList();
    }

    /**
     * Get the latest url of webview
     * @param webView
     * @return
     */
    public static String getHistoricalUrl(WebView webView) {
        WebBackForwardList webBackForwardList = getHistoricalList(webView);
        return webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1).getUrl();
    }

    private void getHost(String url){
//        int index = url.lastIndexOf("/");
    }

    private Map<String,String> generateAdditionalHttpHeaders(){
        HashMap<String, String> map = new HashMap<String,String>();
//            //手动设置Referer来判断页面的来源
//            map.put("Referer", view.getOriginalUrl());
        map.put("Accept-Encoding", "gzip");
        map.put("Host", "devmobile.manfenjiayuan.com");
        map.put("User-Agent", ComnApplication.getUserAgent());
        map.put("Connection", "Keep-Alive");
        return map;
    }

    public static String urlToFilePath(String url, String prefix){
//        String suffix = ".html";
//        return url.substring(url.indexOf(prefix) + prefix.length(),
//                url.lastIndexOf(".html"));

//        MLog.d(String.format("raw: %s, new: %s", url, url.substring(url.indexOf(".com") + prefix.length(),
//                url.lastIndexOf(suffix) + suffix.length())));
//        return url.substring(url.indexOf(".com") + prefix.length(),
//                url.lastIndexOf(suffix) + suffix.length());
        //避免URL中带有参数
        return url.substring(url.indexOf(".com") + prefix.length(),url.length());
    }

    /**
     * 加载网页
     * @return true 加载本地资源; false 加载网络资源
     * */
    public static boolean loadUrl(WebView webView, String url){
        //TODO
        //测试服务器才加载本地文件，因为html中有刷新按钮，后面会做优化
        if(AppConfig.DEBUG){
        //判断是否需要加载本地文件
//        if(loadLocalFile(webView, urlToFilePath(url, "http://")) || loadLocalFile(webView, urlToFilePath(url, "https://"))){
            if(loadLocalFile(webView, urlToFilePath(url, ".com"))){
                return true;
            }
        }

        if(!url.contains("&t=")){
            url = URLHelper.appendTime(url);
        }

        MLog.d("loadUrl: " + url);
        webView.loadUrl(url);
        return false;
    }

    public static boolean loadLocalFile(WebView webView, String path){
        if(!TextUtils.isEmpty(path)){
            String localHtmlData = HtmlManager.getInstance(webView.getContext()).getLocalData(path);
            if(localHtmlData != null){
                MLog.d("loadDataWithBaseURL:" + path);
                webView.loadDataWithBaseURL(HtmlManager.BASE_URL_HOME, localHtmlData,
                        HtmlManager.MIME_TYPE, HtmlManager.ENCODING_UTF8, null);
                return true;
            }
        }

        return false;
    }

    /**
     * 加载网页
     * @param webView webview控件
     * @param url 地址
     * @param localFileName 本地文件名
     * */
    public static void loadUrl(WebView webView, String url, String localFileName){
        String localHtmlData = HtmlManager.getInstance(webView.getContext()).getLocalData(localFileName);
        if(localHtmlData != null){
            MLog.d("loadDataWithBaseURL" + localFileName);
            webView.loadDataWithBaseURL(HtmlManager.BASE_URL_HOME, localHtmlData,
                    HtmlManager.MIME_TYPE, HtmlManager.ENCODING_UTF8, null);
        }
        else{
            MLog.d("loadUrl" + url);
            webView.loadUrl(url);
        }

        // 无参数调用
//        webView.loadUrl("javascript:javacalljs()");
//        java代码调用js并传递参数
//        webView.loadUrl("javascript:test('" + mCurrentUrl+ "')"); //aa是js的函数test()的参数
    }

    public static void loadUrl2(WebView webView, String url, String localFileName){
        String localFilePath = HtmlManager.getInstance(webView.getContext()).getLocalFilePath(localFileName);
        if(localFilePath != null){
//            String newUrl = "content://com.android.htmlfileprovider/" + localFilePath;
            String newUrl = localFilePath;
            MLog.d("load local htm, " + newUrl);
            //打开本地sd卡内的html文件
            webView.loadUrl(newUrl);
        }
        else{
            MLog.d("load web html, " + url);
            webView.loadUrl(url);
        }

        // 无参数调用
//        webView.loadUrl("javascript:javacalljs()");
//        java代码调用js并传递参数
//        webView.loadUrl("javascript:test('" + mCurrentUrl+ "')"); //aa是js的函数test()的参数
    }
}
