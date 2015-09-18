package com.mfh.comna.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mfh.comn.config.UConfig;
import com.mfh.comna.comn.cfg.UConfigHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Caij on 2014/8/29.
 * 微信分享的工具类，实现微信分享，如果想实现接收微信的请求及返回值需要自己在写一个activity，详情见微信开发文档
 * 工具类中有涉及到网络的验证，加载网络上面的图片， 需要在子线程中执行
 */
public class WeiXinUtil {

    public static IWXAPI api;

    public static String APP_ID;

    static {
        APP_ID = UConfigHelper.getConfig().getDomain(UConfig.CONFIG_COMMON)
                .getString(UConfig.CONFIG_WX_ID);
    }

    private static void init(Context context, String appID) {
        if(api == null) {
            api = WXAPIFactory.createWXAPI(context, appID, true);
            api.registerApp(appID);
        }
    }

    /**
     * @param  context 上下文环境
     * @param appID app在微信官网注册的id
     * @param  pageUrl 分享网页的url
     * @param imageUri 对应网页的网上图片uri
     * @param title 分享内容的标题
     * @param description 分享内容的描述
     * */
    public static void shareToWxOfUri(Context context, String appID, String pageUrl,String imageUri, String title, String description) {
        WXMediaMessage msg = initMMsg(context,appID,pageUrl,title,description);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(imageUri);
            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == 200) {
                Bitmap bmp = BitmapFactory.decodeStream(response.getEntity().getContent());
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                bmp.recycle();
                msg.setThumbImage(thumbBmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendMsg(msg);
    }

    /**
     * @param  context 上下文环境
     * @param appID app在微信官网注册的id
     * @param  pageUrl 分享网页的url
     * @param imageRId 分享网页对应的本地图片id
     * @param title 分享内容的标题
     * @param description 分享内容的描述
     * */
    public static  void shareToWxOfReId(Context context, String appID, String pageUrl,int imageRId,String title, String description) {
        WXMediaMessage msg = initMMsg(context,appID,pageUrl,title,description);
        try {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),imageRId);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendMsg(msg);
    }

    /**
     * @param  context 上下文环境
     * @param appID app在微信官网注册的id
     * @param  pageUrl 分享网页的url
     * @param imageSdPath 网页对应的本地sd卡图片
     * @param title 分享内容的标题
     * @param description 分享内容的描述
     * */
    public static  void shareToWxOfSd(Context context, String appID, String pageUrl,String imageSdPath,String title, String description) {
       WXMediaMessage msg = initMMsg(context,appID,pageUrl,title,description);
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imageSdPath);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendMsg(msg);
    }

    /*
    * 需要在配置文件中配置app.id
    * */
    public static void shareToWxOfUri(Context context, String pageUrl,String imageUri, String title, String description) {
        shareToWxOfUri(context,APP_ID,pageUrl,imageUri,title,description);
    }
   /*
    * 需要在配置文件中配置app.id
    * */
    public static void shareToWxOfReId(Context context, String pageUrl,int imageRId,String title, String description) {
        shareToWxOfReId(context,APP_ID,pageUrl,imageRId,title,description);
    }

    /*
   * 需要在配置文件中配置app.id
   * */
    public  static void shareToWxOfSd(Context context, String pageUrl,String imageSdPath,String title, String description) {
        shareToWxOfSd(context,APP_ID,pageUrl,imageSdPath,title,description);
    }

    private static WXMediaMessage initMMsg(Context context,String appID,String pageUrl,String title,String description) {
        init(context,appID);
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = pageUrl;
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = description;
        return  msg;
    }

    private static void sendMsg(WXMediaMessage msg) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }
}
