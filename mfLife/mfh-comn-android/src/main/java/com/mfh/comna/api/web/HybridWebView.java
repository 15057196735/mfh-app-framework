package com.mfh.comna.api.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;

import com.mfh.comna.api.observablescrollview.ObservableScrollViewCallbacks;
import com.mfh.comna.api.observablescrollview.ObservableWebView;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.NetWorkUtil;
import com.mfh.comna.comn.ComnApplication;

/**
 * TO CONFIRM web-app & native-app
 * Created by NAT.ZZN on 2015/5/13.
 */
public class HybridWebView extends ObservableWebView {
    private static final String TAG = HybridWebView.class.getSimpleName();

    protected Context context;

    public HybridWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HybridWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public HybridWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void destroy() {
        removeAllViews();
        try{
            super.destroy();
        }
        catch (Exception localException)
        {}
    }

    /**
     * 初始化
     * */
    private void init(){
        setVerticalScrollBarEnabled(false);
//        requestFocus();
        CookieManager.getInstance().setAcceptCookie(true);

        initWebSettings();

        //W/AwContents﹕ nativeOnDraw failed; clearing to background color.
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * 设置WebView属性
     * */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSettings(){
        WebSettings localWebSettings = getSettings();
        //        localWebSettings.setSupportZoom(true);
//        localWebSettings.setBuiltInZoomControls(true);//支持缩放
//        localWebSettings.setDefaultFontSize(12);
//        localWebSettings.setLoadWithOverviewMode(true);

        //设置可以访问文件（读取文件缓存,manifest生效）
        localWebSettings.setAllowFileAccess(true);
        //enable JavaScript execution.
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setUserAgentString(ComnApplication.getUserAgent());
        /**
         * 设置缓存模式
         * LOAD_DEFAULT:  根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，优先使用缓存
         * 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
         * */
        if(NetWorkUtil.isConnect(getContext())){
            localWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }else{
            localWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        //AppCache使我们可以有选择的缓冲web浏览器中所有的东西，从页面、图片到脚本、CSS等。Android上需要
        //手动开启setAppCacheEnabled，并设置路径setAppCachePath和容量setAppCacheMaxSize。
        //Android中webkit使用一个db文件来保存AppCache数据(my_path/ApplicationCache.db)
        localWebSettings.setAppCacheEnabled(true);//enable Application Caches
//        Log.d("Nat: ", "APP_CACHE_DIR=" + APP_CACHE_DIR);
//        Log.d("Nat: ", "APP_CACHE_DIR2=" + APP_CACHE_DIR2);
//        Log.d("Nat: ", "APP_CACHE_DIR3=" + APP_CACHE_DIR3);
        localWebSettings.setAppCachePath(WebViewUtils.APP_CACHE_DIR2);//set Application Caches 缓存目录
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            //This method was deprecated in API level 18. In future quota will be managed automatically.
            localWebSettings.setAppCacheMaxSize(1024*1024*8);//8M
        }

        //DOM Storage存储一些简单的key/value数据。Session Storage(会话级别的存储，页面关闭即消失)
        //Local Storage(本地存储，除非主动删除，否则数据永远不会过期)
        //Android中webkit为DOM Storage产生两个文件(my_path/localstorage/***.localstorage和my_path/localstorage/Database.db)
        //Android中清除缓存时，如果需要清除LocalStorage的话，仅仅删除LocalStorage的本地存储文件是不够
        // 的，内存里面有缓存数据。如果再次进入页面，LocalStorage中的缓存数据同样存在。需要杀死程序
        // 运行的当前进程再重新启动才可以。
        localWebSettings.setDomStorageEnabled(true);//Set whether the DOM Storage APIs are enabled.
        localWebSettings.setDatabaseEnabled(true);//enable database storage
        // This method was deprecated in API level 19. Database paths are managed by the
        // implementation and calling this method will have no effect.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            MLog.d("DB_PATH=" + WebViewUtils.DB_PATH);
            localWebSettings.setDatabasePath(WebViewUtils.DB_PATH);//设置数据库缓存路径
        }

//        localWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级(Deprecated)
//        localWebSettings.setBlockNetworkImage(true);//把图片加载放在最后来加载渲染
    }

    public void setUserAgent(String text){
        WebSettings localWebSettings = getSettings();
        localWebSettings.setUserAgentString(text);
    }

    @Override
    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
        super.setScrollViewCallbacks(listener);
    }

    public boolean canScrollHor(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }
}
