package com.mfh.comna.api.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.R;
import com.mfh.comna.bizz.config.URLConf;
import com.mfh.comna.api.ui.dialog.CommonDialog;
import com.mfh.comna.api.ui.dialog.DialogHelper;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.web.NativeWebViewActivity;
import com.mfh.comna.api.web.WebViewUtils;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.netphone.logic.NetPhoneService;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.utils.CameraSessionUtil;
import com.mfh.comna.utils.DialogUtil;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * Created by Administrator on 2015/6/17.
 */
public class UIHelper {
    //Intent Requtst Code
    public static final int REQUEST_CODE_CAMERA     = 0X00;//拍照
    public static final int REQUEST_CODE_XIANGCE    = 0X01;//浏览相册
    public static final int REQUEST_CODE_CROP       = 0X02;//裁剪图片

    /**
     * 同步Cookie
     * */
    public static void syncCookies(Context context, String url){
        String sessionId = MfhLoginService.get().getCurrentSessionId();
        if(sessionId != null){
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", sessionId));
            sbCookie.append(String.format(";domain=%s", URLConf.DOMAIN));
            sbCookie.append(String.format(";path=%s", "/"));
            String cookieValue = sbCookie.toString();

            WebViewUtils.syncCookies(context, url, cookieValue);
        }
    }

    /**
     * 设置Cookie
     * */
    public static void setCookies(String url){
        String sessionId = MfhLoginService.get().getCurrentSessionId();

        if(sessionId != null){
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", sessionId));
            sbCookie.append(String.format(";domain=%s", URLConf.DOMAIN));
            sbCookie.append(String.format(";path=%s", "/"));
            String cookieValue = sbCookie.toString();

            WebViewUtils.setCookie(url, cookieValue);
        }
    }

    /**
     * 网络电话注册
     * */
    public static void registNetPhone(Context context) {
        NetPhoneService service = ServiceFactory.getService(NetPhoneService.class, context);
        service.queryFromNet(MfhLoginService.get().getCurrentGuId());
    }

    /**
     * 跳转页面
     * */
    public static void redirectToActivity(Context context, java.lang.Class<?> cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void redirectToActivity(Context context, java.lang.Class<?> cls, Bundle extras){
        Intent intent = new Intent(context, cls);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public static void callPhone(Context context, String phoneNumber){
        try{
            //用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//        intent.setAction(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
        catch (Exception e){
            MLog.e("callPhone failed:" + e.toString());
        }
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//            W/System.err﹕ at java.lang.reflect.Method.invokeNative(Native Method)
//            W/System.err﹕ at dalvik.system.NativeStart.main(Native Method)
            context.startActivity(it);
        } catch (Exception e) {
            //android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.VIEW dat=bonjour ami朋厨烘培 }
            MLog.e("openBrowser failed:" + e.toString());
//            e.printStackTrace();
//            ToastMessage(context, "无法浏览此网页", 500);
//            DialogUtil.showHint("无法打开链接");
        }
    }

    /**
     * 显示可复制的文本*/
    public static void showCopyTextOption(final Context context, final String text) {
        CommonDialog dialog = new CommonDialog(context);
        dialog.setMessage(text);
        dialog.setPositiveButton(R.string.dialog_button_copy, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cbm.setText(text);
                DialogUtil.showHint(context.getString(R.string.toast_copy_success));
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 网络链接选项
     * */
    public static void showUrlOption(final Activity context, final String url) {
        CommonDialog dialog = new CommonDialog(context);
        dialog.setMessage(context.getString(R.string.dialog_message_link_option, url));
        dialog.setPositiveButton(R.string.dialog_button_open, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //满分家园的链接在当前应用的webview打开，其他的链接启动浏览器打开
                if (url.contains(URLConf.DOMAIN)) {
                    NativeWebViewActivity.actionStart(context, url);
                } else {
                    openBrowser(context, url);
                }
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 选择头像
     * */
    public static void showSelectPictureDialog(final Activity context) {
        final CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(context);

        View.OnClickListener click = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = v.getId();
                dialog.dismiss();
                if(id == R.id.tv_option_1){
                    context.startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            UIHelper.REQUEST_CODE_XIANGCE);
                }else if(id == R.id.tv_option_2){
                    CameraSessionUtil cameraUtil = ServiceFactory.getService(CameraSessionUtil.class.getName());
                    cameraUtil.makeCameraRequest(context);
                }
            }
        };

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_select_picture, null);
        view.findViewById(R.id.tv_option_1).setOnClickListener(click);
        view.findViewById(R.id.tv_option_2).setOnClickListener(click);

        dialog.setContent(view);
        dialog.show();
    }

    /**
     * 显示清除缓存Dialog
     * */
    public static void showCleanCacheDialog(Context context) {
        CommonDialog dialog = new CommonDialog(context);
        dialog.setMessage(R.string.dialog_message_clean_cache);
        dialog.setPositiveButton(R.string.dialog_button_clean, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppHelper.clearAppCache();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 发送广播
     * */
    public static void sendBroadcast(String action){
        Intent intent = new Intent(action);
        BizApplication.getAppContext().sendBroadcast(intent);
    }
    public static void sendBroadcast(String action, Bundle extras){
        Intent intent = new Intent(action);
        intent.putExtras(extras);
        BizApplication.getAppContext().sendBroadcast(intent);
    }


}
