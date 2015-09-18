package com.mfh.comna.bizz.update;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mfh.comn.net.data.IResponseData;
import com.mfh.comn.net.data.RspBean;
import com.mfh.comna.R;
import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.api.ui.dialog.DialogHelper;
import com.mfh.comna.api.utils.DeviceUtils;
import com.mfh.comna.api.utils.FileUtil;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.SharedPreferencesUtil;
import com.mfh.comna.comn.ComnApplication;
import com.mfh.comna.comn.database.dao.FileNetDao;
import com.mfh.comna.comn.database.dao.NetCallBack;
import com.mfh.comna.network.NetFactory;

import net.tsz.afinal.http.AjaxParams;

import java.io.File;

/**
 * 应用升级工具类
 * TODO ,异步通知
 * Created by Administrator on 14-6-4.
 */
public class AppUpdateManager {
    private Context appContext;
    private FileNetDao fileNetDao;

    public static String APK_NAME_FOR_UPDATE = "appName";//应用程序名，用于检查版本更新
    public static String APK_DOWNLOAD_DIR_NAME = "download";//apk下载存放目录

    public static final String ACTION_APPUPDATE_CHECK_FINISH = "app.update.check.finish";
    public static final String APP_DOWNLOAD_NOTIFY = "app.download.notify";
    public static final String APP_UPDATE_NAME_BY_INIT = "init.activity.app.update.name";

    private static final String URL_APP_UPDATE_VERSIOIN = NetFactory.getUpdateServerUrl() + "/app/update/version";
    private static final String URL_APP_UPDATE_DOWNLOAD = NetFactory.getUpdateServerUrl() + "/app/update/download";

    /**
     * 构造函数
     * @param context
     */
    public AppUpdateManager(Context context) {
        this.appContext = context;

        //使用SD卡根路径存储下载的临时文件，若没有sd卡，则使用程序私有目录，比较昂贵
        fileNetDao = new FileNetDao(APK_DOWNLOAD_DIR_NAME, URL_APP_UPDATE_DOWNLOAD, FileUtil.getSDRootPath());//"/storage/sdcard0"
        fileNetDao.setUseLocalFirst(false);//每次都重新下载
    }

    /**
     * 检查服务器端版本号，若有新版本则启动下载并安装
     * @return
     */
    public void checkServVersionCode(AjaxParams param) {
        NetFactory.getHttp().get(URL_APP_UPDATE_VERSIOIN + "?apk=" + APK_NAME_FOR_UPDATE, param,
                new NetCallBack.NormalNetTask<AppInfo>(AppInfo.class) {
                    @Override
                    public void processResult(IResponseData rspData) {
                        RspBean<AppInfo> result = (RspBean<AppInfo>) rspData;
                        final AppInfo appInfo = result.getValue();
                        if (appInfo == null) {
                            return;
                        }

                        int curVersion = ComnApplication.getVersionCode();
                        int newVersionCode = appInfo.getVersionCode();
                        MLog.d(String.format("checkServVersionCode.processResult, serverVC=%d, localVC=%d",
                                newVersionCode, curVersion));

                        if (appInfo.getVersionCode() > curVersion) {
                            String appName = appInfo.getApkName();
                            if (!TextUtils.isEmpty(appName)) {
                                SharedPreferencesUtil.set(appContext, SharedPreferencesHelper.PREF_NAME_APP_UPDATE,
                                        SharedPreferencesHelper.PREF_KEY_APP_UPDATE_NAME, appInfo.getApkName());
                                SharedPreferencesUtil.set(appContext, SharedPreferencesHelper.PREF_NAME_APP_UPDATE,
                                        SharedPreferencesHelper.PREF_KEY_APP_UPDATE_VERSIONNAME, appInfo.getVersionName());

                                Message msg = new Message();
                                msg.what = MSG_UPDATE_NEWVERSION;
//                                msg.obj = appInfo.getApkName();
                                uiHandler.sendMessage(msg);
                            }
                        } else {
                            SharedPreferencesUtil.clear(appContext, SharedPreferencesHelper.PREF_NAME_APP_UPDATE);
                            notifyFinish();
                        }
                    }

                    @Override
                    protected void doFailure(Throwable t, String errMsg) {
                        MLog.d("doFailure " + errMsg);
                        super.doFailure(t, errMsg);
                        notifyFinish();
                    }
                });
    }

    /**
     * 升级检查结束
     */
    protected void notifyFinish() {
//        Intent intent = new Intent(ACTION_APPUPDATE_CHECK_FINISH);
//        appContext.sendBroadcast(intent);
    }

    /**
     * 杀死当前进程
     */
    private void killProcess() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
        /*ActivityManager activityMan = (ActivityManager)appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> process = activityMan.getRunningAppProcesses();
        int len = process.size();
        for(int i = 0;i<len;i++) {
            if (process.get(i).processName.equals(appContext.getPackageName())) {
                android.os.Process.killProcess(process.get(i).pid);
                break;
            }
        }*/
    }

    private static final int MSG_UPDATE_NEWVERSION = 0;
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_NEWVERSION:{
                    YesOrNoToNewVersion();
                }
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 提示是否安装更新
     * */
    private void YesOrNoToNewVersion() {
        final String appName = SharedPreferencesUtil.get(appContext, SharedPreferencesHelper.PREF_NAME_APP_UPDATE,
                SharedPreferencesHelper.PREF_KEY_APP_UPDATE_NAME, "");
        final String versionName = SharedPreferencesUtil.get(appContext, SharedPreferencesHelper.PREF_NAME_APP_UPDATE,
                SharedPreferencesHelper.PREF_KEY_APP_UPDATE_VERSIONNAME, "");
        SharedPreferencesUtil.clear(appContext, SharedPreferencesHelper.PREF_NAME_APP_UPDATE);

        AlertDialog.Builder dialog = DialogHelper.getConfirmDialog(appContext,
                appContext.getString(R.string.dialog_message_app_update),
                appContext.getString(R.string.dialog_button_appupdate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doDownLoad(appName);
            }
        });
//        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setTitle(appContext.getString(R.string.dialog_title_app_update, versionName));
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 执行下载
     */
    public void doDownLoad(String apkName) {
        final Dialog dialog = DialogHelper.genProgressDialog(appContext,false,"正在下载更新程序...");
        //执行下载
        fileNetDao.processFile(apkName, new FileNetDao.CallBack() {
            @Override
            public void processFile(File file) {
                try {
                    //下载完成，自动执行安装
                    doSetUp(file);
                }
                catch (Throwable e) {
                    if (dialog != null)
                        dialog.dismiss();
                    notifyFinish();
                }
            }

            @Override
            public void onFailure(String fileName, Throwable e) {
                if (dialog != null)
                    dialog.dismiss();
                notifyFinish();
            }
        });
    }

    /**
     * 执行安装
     */
    private void doSetUp(File file) {
        if (!file.exists())
            throw new RuntimeException("安装包不存在!");

        DeviceUtils.installAPK(appContext, file);

        killProcess();
    }
}
