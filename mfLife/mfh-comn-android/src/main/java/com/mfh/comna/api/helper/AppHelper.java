package com.mfh.comna.api.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.bizz.login.MsgBridgeUtil;
import com.mfh.comna.bizz.login.entity.UserMixInfo;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.msg.logic.MsgSetUtil;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.ComnApplication;
import com.umeng.analytics.MobclickAgent;

import org.kymjs.kjframe.KJBitmap;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 应用程序帮助类
 * Created by Shicy on 14-3-18.
 */
public class AppHelper {

    /**
     * 手机有menu键actionbar就不会显示3个点的更多或者说3个点的menu按钮，调用该方法让他显示
     * @param context
     */
    public static void overflowMenu(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<KvBean> makeList(int size) {
        List<KvBean> list = new ArrayList<KvBean>();
        for (int i = 0; i < size; i++) {
            list.add(new KvBean());
        }
        return list;
    }

    public static <T>  List<KvBean<T>> makeList(List<String[]> values) {
        List<KvBean<T>> list = new ArrayList<KvBean<T>>();
        for (int i = 0; i < values.size(); i++) {
            list.add(new KvBean(new EntityWrapper(values.get(i))));
        }
        return list;
    }

    public static void toggleSoftInput(final Context context) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 400);
    }

    public static void hideSoftInput(Activity activity) {
        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView == null)
            return ;

        IBinder windowToken = currentFocusView.getWindowToken();
        if (windowToken == null)
            return ;

        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInputEver(Activity activity) {
        View currentFocusView = activity.getCurrentFocus();

        hideSoftInputEver(activity, currentFocusView);
    }

    public static void hideSoftInputEver(Activity activity, View view) {
        if (view == null)
            return ;

        IBinder windowToken = view.getWindowToken();
        if (windowToken == null)
            return ;

        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }


    public static void startSMSMessage(Activity activity, String number, String message) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra("sms_body", message);
        activity.startActivity(intent);
    }

    public static void callTel(Activity activity, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        activity.startActivity(intent);
    }

    /**
     * 保存用户登录数据
     * */
    public static void saveUserLoginInfo(String data){
        MLog.d("saveUserLoginInfo.data = " + data);
        JSONObject jsonObject = JSON.parseObject(data);
        String uid = jsonObject.getString("uid");
        String pwd = jsonObject.getString("pwd");
        JSONObject result = jsonObject.getJSONObject("result");
        if(result != null){
            //解析并保存用户登录信息
            UserMixInfo userMixInfo = JSONObject.parseObject(result.toJSONString(), UserMixInfo.class);
            MfhLoginService.get().saveUserMixInfo(uid, pwd, userMixInfo);

            MfhLoginService.get().refreshMsgBridge();
        }
    }

    private static AssetManager am;
    /**
     * 获取asset资源管理器
     * @return
     * @author zhangyz created on 2013-5-25
     */
    public static AssetManager getAm() {
        if (am == null) {
            Context context = BizApplication.getAppContext();
            if (context == null)
                throw new RuntimeException("请在AndroidManifest.xml配置文件中使用android:name=\"com.mfh.comna.bizz.ComnApplication\"");
            am = context.getApplicationContext().getAssets();
        }
        return am;
    }

    /**
     * 清除APP缓存
     * */
    public static void clearAppCache(){
        DataCleanManager.cleanDatabases(BizApplication.getAppContext());
        //清除数据缓存
        DataCleanManager.cleanInternalCache(BizApplication.getAppContext());

        //清除编辑器保存的临时内容Properties

        //清除图片缓存
        new KJBitmap().cleanCache();
    }

    public static void AppExit(){
        // 保存统计数据
        MobclickAgent.onKillProcess(ComnApplication.getAppContext());

        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
