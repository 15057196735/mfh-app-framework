package com.mfh.comna.api.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.mfh.comna.bizz.BizApplication;

import java.io.File;

/**
 * Created by Administrator on 2015/5/15.
 */
public class DeviceUtils {
//    public static boolean isTablet() {
//        if (_isTablet == null) {
//            boolean flag;
//            if ((0xf & BaseApplication.context().getResources()
//                    .getConfiguration().screenLayout) >= 3)
//                flag = true;
//            else
//                flag = false;
//            _isTablet = Boolean.valueOf(flag);
//        }
//        return _isTablet.booleanValue();
//    }

    /**
     * 隐藏软键盘
     * @param context
     *          context
     * @param view
     *          the currently focused view
     * */
    public static void hideSoftInput(Context context, View view) {
        if (context == null || view == null){
            return ;
        }

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftInput(Activity context) {
        if (context == null){
            return ;
        }

        if(context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
            if (context.getCurrentFocus() != null){
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }


    }


    /**
     * 显示软键盘
     * @param context
     *          context
     * @param view
     *          the currently focused view, which would like to receive soft keyboard input.
     * */
    public static void showSoftInput(Context context, View view) {
        if (context == null || view == null){
            return ;
        }

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 判断是否存在指定包名的应用
     * */
    public static boolean isPackageExist(String pckName) {
        try {
            PackageInfo pckInfo = BizApplication.getAppContext().getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            MLog.e(e.getMessage());
        }
        return false;
    }

    /**
     * 安装APP
     * */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists()){
            MLog.e("安装包不存在");
            return;
        }

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载App
     * */
    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }
}
