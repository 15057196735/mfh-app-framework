package com.mfh.comna.comn;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mfh.comn.config.ConfigsParseHelper;
import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.cfg.UConfigCache;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通用应用类。我们所有的应用都要从该类继承!
 * 
 * @author zhangyz created on 2013-5-25
 * @since Framework 1.0
 */
public class ComnApplication extends Application{
    private static final String TAG = ComnApplication.class.getSimpleName();

    private static Context context;

    //配置对象
    private static UConfigCache uconfig;
    private static String appUserAgent;

    //启动机制
    public enum LaunchMechanism {
        DIRECT,
        NOTIFICATION,
        URL
    }
    private LaunchMechanism launchMechanism = LaunchMechanism.DIRECT;

    /**
     * 获取统一配置文件别名，以示区分
     * @return
     * @author zhangyz created on 2013-5-25
     */
    protected String getConfigAlias() {
        return ConfigsParseHelper.configAlias;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //初始化配置对象
        try {
            InputStream in = AppHelper.getAm().open(getConfigAlias());
            //            String[] files = AppHelper.getAm().list("/");
//            for (int ii = 0; ii < files.length; ii++) {
//                System.out.println(files[ii]);
//            }

            uconfig = UConfigCache.getConfig(getConfigAlias());
            uconfig.init(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 获取应用上下文
     * @return
     * @author zhangyz created on 2014-3-8
     */
    public static Context getAppContext() {
        return context;
    }

    /**==
     * 获取配置读取接口
     * @return
     * @author zhangyz created on 2014-3-8
     */
    public static UConfigCache getUconfig() {
        return uconfig;
    }

    /**
     * 获取App安装包信息
     * @return
     */
    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            PackageManager pm = ComnApplication.getAppContext().getPackageManager();
            String packageName = ComnApplication.getAppContext().getPackageName();
            info = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if(info == null) {
            info = new PackageInfo();
        }
        return info;
    }

    /**
     * 获取当前程序内部版本号.若没有或失败返回-1.
     * @param context 上下文
     */
    public static int getVersionCode() {
        try {
            PackageManager pm = ComnApplication.getAppContext().getPackageManager();
            String packageName = ComnApplication.getAppContext().getPackageName();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi.versionCode;
        }
        catch (Exception e) {
            return -1;
        }
    }
    /**
     * 获取当前程序内部版本号.若没有或失败返回-1.
     * @param context 上下文
     */
    public static String getVersionName() {
        PackageManager pm = ComnApplication.getAppContext().getPackageManager();//context为当前Activity上下文
        String packageName = ComnApplication.getAppContext().getPackageName();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取UserAgent,登录时填入Header
     * */
    public static String getUserAgent() {
        if(StringUtils.isEmpty(appUserAgent)) {
            StringBuilder ua = new StringBuilder("MFH");
            ua.append('/'+ getVersionName() + '_' + getVersionCode());//App版本
            ua.append("/Android");//手机系统平台
            ua.append("/"+ android.os.Build.VERSION.RELEASE);//手机系统版本
            ua.append("/"+ android.os.Build.MODEL); //手机型号
            ua.append("/"+ ComnAppHelper.getAppId());//客户端唯一标识
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

    public void setLaunchMechanism(LaunchMechanism launchMechanism) {
        this.launchMechanism = launchMechanism;
    }

}
