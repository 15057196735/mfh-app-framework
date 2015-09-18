package com.mfh.comna.api.web;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mfh.comna.bizz.config.URLConf;
import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comna.api.ui.view.EmptyLayout;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.view.BaseFragmentActive;

import com.mfh.comna.R;
import java.util.HashMap;
import java.util.Map;


/**
 * 网页
 * */
public class NativeWebViewActivity extends BaseFragmentActive {
    public static final String EXTRA_KEY_USE_LOCALE_RESOURCE = "EXTRA_KEY_USE_LOCALE_RESOURCE";
    public static final String EXTRA_KEY_LOCALE_URL = "EXTRA_KEY_LOCALE_URL";
    public static final String EXTRA_KEY_REDIRECT_URL = "redirectUrl";
    public static final String EXTRA_KEY_SYNC_COOKIE = "syncCookie";
    public static final String EXTRA_KEY_BACKASHOMEUP = "backAsHomeUp";

    private TextView tvTopBarTitle;
    private ImageButton ibBack;

    private Context context;
    private HybridWebView myWebView;
    private String mCurrentUrl = "";
    private Map<String, String> titleMap = new HashMap<String, String>();

    private boolean bNeedSyncCookie;//是否需要同步cookie
    private boolean backAsHomeUp = true;//true,关闭网页(默认);false,返回上一页

    private EmptyLayout emptyView;

    private boolean useLocaleResource;//是否使用本地资源
    private String mLocalUrl;


    public static void actionStart(Activity context, String redirectUrl){
        actionStart(context, redirectUrl, true, false, false);
    }
    public static void actionStart(Activity context, String redirectUrl, boolean useLocaleResource, String localUrl){
        actionStart(context, redirectUrl, useLocaleResource, localUrl, true, false, false);
    }
    public static void actionStart(Activity context, String redirectUrl,
                                   boolean useLocaleResource, String localUrl,
                                   boolean bSyncCookie, boolean backAsHomeUp, boolean bAnimation){
        Intent intent = new Intent(context, NativeWebViewActivity.class);
        intent.putExtra(EXTRA_KEY_REDIRECT_URL, redirectUrl);
        intent.putExtra(EXTRA_KEY_USE_LOCALE_RESOURCE, useLocaleResource);
        intent.putExtra(EXTRA_KEY_LOCALE_URL, localUrl);
        intent.putExtra(EXTRA_KEY_SYNC_COOKIE, bSyncCookie);
        intent.putExtra(EXTRA_KEY_BACKASHOMEUP, backAsHomeUp);
        context.startActivity(intent);

        if(bAnimation){
            //Activity切换动画,缩放+透明
            context.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        }
    }
    public static void actionStart(Activity context, String redirectUrl, boolean bSyncCookie,
                                   boolean backAsHomeUp, boolean bAnimation){
        Intent intent = new Intent(context, NativeWebViewActivity.class);
        intent.putExtra(EXTRA_KEY_REDIRECT_URL, redirectUrl);
        intent.putExtra(EXTRA_KEY_SYNC_COOKIE, bSyncCookie);
        intent.putExtra(EXTRA_KEY_BACKASHOMEUP, backAsHomeUp);
        context.startActivity(intent);

        if(bAnimation){
            //Activity切换动画,缩放+透明
            context.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Intent intent = this.getIntent();
        if(intent != null){
            mCurrentUrl = intent.getStringExtra(EXTRA_KEY_REDIRECT_URL);//intent.getExtra().getString("redirectUrl");
            bNeedSyncCookie = intent.getBooleanExtra(EXTRA_KEY_SYNC_COOKIE, false);
            backAsHomeUp = intent.getBooleanExtra(EXTRA_KEY_BACKASHOMEUP, true);
            useLocaleResource = intent.getBooleanExtra(EXTRA_KEY_USE_LOCALE_RESOURCE, false);
            mLocalUrl = intent.getStringExtra(EXTRA_KEY_LOCALE_URL);
//            myWebView.loadUrl("file:///android_asset/XX.html");//加载asset文件夹网页文件
//            myWebView.setEnabled(true);
//            myWebView.setActivated(true);
        }

        initTopBar();
        initWebView();

        emptyView = (EmptyLayout) findViewById(R.id.error_view);
        emptyView.setLoadingTheme(1);
        emptyView.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        titleMap.clear();

        emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (useLocaleResource){
            WebViewUtils.loadUrl(myWebView, mCurrentUrl, mLocalUrl);
//            WebViewUtils.loadUrl2(myWebView, mCurrentUrl, mLocalUrl);
        }else{
            MLog.d("load web html, mCurrentUrl = " + mCurrentUrl);
            myWebView.loadUrl(mCurrentUrl);
        }
    }

    public void onResume(){
        super.onResume();
//        /* Resume cookie sync
//         */
//        CookieSyncManager.getInstance().startSync();
//
//        //清空WebView缓存
////        myWebView.clearCache(false);
        myWebView.onResume();
    }
//    public void onStart(){
//        super.onStart();
//    	/* Create singleton object to get the CookieSyncManager
//         */
//        CookieSyncManager.createInstance(this);
//    }
    public void onPause(){
        super.onPause();
//    	/* Stop the session and store information for forum
//         * session.
//         */
//        CookieSyncManager.getInstance().stopSync();
        myWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myWebView.destroy();
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {  //表示按返回键 时的操作
//            myWebView.goBack();   //后退
//            return true;    //已处理
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 初始化导航栏视图
     * */
    private void initTopBar(){
        tvTopBarTitle = (TextView) findViewById(R.id.topbar_title);
        ibBack = (ImageButton) findViewById(R.id.ib_back);

        tvTopBarTitle.setText("");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!backAsHomeUp && myWebView != null && myWebView.canGoBack()) {
                    myWebView.goBack();
                    updateWebTitle();
                }
                else{
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    /**
     * 初始化WebView
     * */
    private void initWebView(){
            try{
//            实例化WebView组件
            myWebView = (HybridWebView) findViewById(R.id.webview_custom);

            myWebView.setWebViewClient(new MyWebViewClient());
            myWebView.setWebChromeClient(new MyWebChromeClient());
            //佚名内部类处理,点击系统“Back”键，浏览的网页回退而不是退出浏览器。
            myWebView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Check if the key event was the Back button and if there's history
                    if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
                        myWebView.goBack();
                        updateWebTitle();
                        return true;    //已处理
                    }
//                }
                    return false;
                }
            });

            if(bNeedSyncCookie){
                UIHelper.syncCookies(this, URLConf.URL_DEFAULT);
            }
        }
        catch(Exception e){
            Log.e("Nat: initWebView failed", e.getMessage());
        }
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MLog.d(String.format("shouldOverrideUrlLoading %s", url));
            onUrlLoading(view, url);
            boolean flag =  super.shouldOverrideUrlLoading(view, url);
            mCurrentUrl = url;
            return flag;

//            url = Uri.decode(url);
//            view.loadUrl(url);
//            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onUrlFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onWebTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            onWebIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
//            if (newProgress > 90) {
////                loadingImageView.toggle(false);
//            }
        }
    }

    /**
     * 载入链接之前会被调用
     *
     * @param view
     *            WebView
     * @param url
     *            链接地址
     */
    protected void onUrlLoading(WebView view, String url) {
        emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);

        UIHelper.syncCookies(this, url);
    }

    /**
     * 链接载入成功后会被调用
     *
     * @param view
     *            WebView
     * @param url
     *            链接地址
     */
    protected void onUrlFinished(WebView view, String url) {
        mCurrentUrl = url;
        if(emptyView.getErrorState() == EmptyLayout.NETWORK_LOADING){
            emptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
//            Log.d("Nat: webView.onPageFinished.Url", view.getUrl());
//            Log.d("Nat: webView.onPageFinished.OriginalUrl", view.getOriginalUrl());
    }

    /**
     * 当前WebView显示页面的标题
     *
     * @param view
     *            WebView
     * @param title
     *            web页面标题
     */
    protected void onWebTitle(WebView view, String title) {
        try{
            // 必须做判断，由于webview加载属于耗时操作，可能会本Activity已经关闭了才被调用
            if (context != null && myWebView != null) {
//                Log.d("Nat: webView.onReceivedTitle.Url", view.getUrl());
//                Log.d("Nat: webView.onReceivedTitle.OriginalUrl", view.getOriginalUrl());
                tvTopBarTitle.setText(myWebView.getTitle());

                //ISSUES: 保存标题，解决当goback后标题不改变问题。
                titleMap.put(myWebView.getUrl(), myWebView.getTitle());
            }
        }catch(Exception e){
            Log.e("Nat: onWebTitle", e.toString());
        }

    }

    /**
     * 当前WebView显示页面的图标
     *
     * @param view
     *            WebView
     * @param icon
     *            web页面图标
     */
    protected void onWebIcon(WebView view, Bitmap icon) {
    }

    /**
     * 刷新网页标题
     * */
    private void updateWebTitle(){
        try{
            if(myWebView != null){
//            Log.d("Nat: webView.updateWebTitle.Url", myWebView.getUrl());
//            Log.d("Nat: webView.updateWebTitle.OriginalUrl", myWebView.getOriginalUrl());
                String url = myWebView.getOriginalUrl();
                if(url != null && titleMap != null && titleMap.containsKey(url)){
                    tvTopBarTitle.setText(titleMap.get(url));
                }else{
                    tvTopBarTitle.setText(myWebView.getTitle());
                }

//            Log.d("Nat: webView.Title", tvTopBarTitle.getText().toString());
            }
        }
        catch(Exception e){
            Log.e("Nat: updateWebTitle", e.toString());
        }
    }

    private void reload(){
        emptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        if(myWebView != null){
            String url = myWebView.getUrl();
            if(TextUtils.isEmpty(url)){
                //TODO
//                url = MobileURLConf.URL_AUTH_GUIDE;
                return;
            }
            emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
            myWebView.clearHistory();
            myWebView.loadUrl(url);
        }
    }

}
