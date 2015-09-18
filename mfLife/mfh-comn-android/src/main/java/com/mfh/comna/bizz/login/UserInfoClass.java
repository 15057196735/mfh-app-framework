package com.mfh.comna.bizz.login;

import android.app.Activity;
import android.content.SharedPreferences;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.logic.IService;
import com.mfh.comna.comn.logic.ServiceFactory;

/**
 * 用户信息保存的类
 * Created by 李潇阳 on 2015/1/22.
 */
public class UserInfoClass implements IService {

    private int messageCount;
    private String userId;
    private String headImgUrl;//头像
    private static String SP_NAME_SUFFIX = "user.info";
    private SharedPreferences spSession = null;

    public UserInfoClass() {
        spSession = BizApplication.getAppContext().getSharedPreferences(SP_NAME_SUFFIX, Activity.MODE_PRIVATE);
        init();
        ServiceFactory.putService(UserInfoClass.class.getName(), this);
    }

    public static UserInfoClass get() {

        return null;
    }
    private void init() {
        userId = spSession.getString("user.info.user.id", "");
        headImgUrl = spSession.getString("user.info.head.img.url", "");
        messageCount = spSession.getInt("user.info.count", 0);
    }


    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
        SharedPreferences.Editor editor = spSession.edit();
        editor.putInt("user.info.count", messageCount);
        editor.commit();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        SharedPreferences.Editor editor = spSession.edit();
        editor.putString("user.info.user.id", userId);
        editor.commit();
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        SharedPreferences.Editor editor = spSession.edit();
        editor.putString("user.info.head.img.url", headImgUrl);
        editor.commit();
    }

    /**
     * 当退出登录的时候清除这些信息
     */
    public void deleteWhenLoginOut() {
        SharedPreferences.Editor editor = spSession.edit();
        editor.clear().commit();
    }
}
