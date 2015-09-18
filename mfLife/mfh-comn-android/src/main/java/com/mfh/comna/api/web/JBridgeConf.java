package com.mfh.comna.api.web;

/**
 * Created by Administrator on 2015/7/7.
 */
public class JBridgeConf {
    //选择图片：相册/拍照
    public static final String HANDLE_NAME_SELECT_PICTURE = "selectPicture";
    //打开浏览器
    public static final String HANDLE_NAME_OPEN_BROWSER = "openBrowser";
    //拨打电话
    public static final String HANDLE_NAME_CALL_PHONE= "callPhone";
    //保存用户登录信息
    public static final String HANDLE_NAME_SAVE_USER_LOGIN_INFO = "saveUserLoginInfo";

    //支付：支付宝(0)/微信(1)
    public static final String HANDLE_NAME_NATIVE_PAY = "nativePay";
    //订单支付：支付宝(0)/微信(1)
    public static final String HANDLE_NAME_NATIVE_PAY_ORDER = "nativePayOrder";
    //分享：微信/朋友圈
    public static final String HANDLE_NAME_NATIVE_SHARE = "nativeShare";

    //提交图片
    public static final String HANDLE_NAME_SUBMIT_IMAGE = "imageChooseHandler";
    //支付结果
    public static final String HANDLE_NAME_PAYRESULT = "payResult";

}
