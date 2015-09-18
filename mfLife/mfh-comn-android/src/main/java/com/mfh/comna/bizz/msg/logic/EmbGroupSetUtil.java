package com.mfh.comna.bizz.msg.logic;

import android.app.Activity;
import android.content.SharedPreferences;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.bizz.msg.MsgConstants;

/**
 * Created by Administrator on 2014/10/15.
 */
public class EmbGroupSetUtil {
    private static String SP_NAME_SUFFIX = "emb.group.session";
    private SharedPreferences spSession = null;

    /**
     * 构造函数，使用当前登录用户
     */
    public EmbGroupSetUtil(String ownerId) {
        spSession = BizApplication.getAppContext().getSharedPreferences(ownerId + "." + SP_NAME_SUFFIX, Activity.MODE_PRIVATE);
    }

    /**
     * 获取session会话的游标，用于向服务器端增量请求数据
     * @return
     */
    public Long getLastUpdate() {
        Long ret = spSession.getLong("lastUpdate", -1);
        if (ret == -1) {//兼容老的
            return MsgConstants.spSession.getLong("lastUpdate", -1);
        }
        else
            return ret;
    }

    /**
     * 清理会话游标
     */
    public void clearLastUpdate() {
        SharedPreferences.Editor editor = spSession.edit();
        editor.remove("lastUpdate");
        editor.commit();
    }

    /**
     * 保存会话游标
     * @param updateTime
     */
    public void saveLastUpdate(Long updateTime) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putLong("lastUpdate", updateTime);
        editor.commit();
    }
}
