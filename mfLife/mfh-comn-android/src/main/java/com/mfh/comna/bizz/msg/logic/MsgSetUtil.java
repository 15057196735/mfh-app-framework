package com.mfh.comna.bizz.msg.logic;

import android.content.SharedPreferences;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.api.utils.MLog;
import com.mfh.comna.api.utils.SharedPreferencesUtil;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.comn.logic.ServiceFactory;

/**
 * Created by Administrator on 14-5-8.
 */
public class MsgSetUtil {
    private static String SP_NAME_SUFFIX = "emb.session";

    private static final String PREF_KEY_LAST_UPDATE = "lastUpdate";//最后一次更新时间
    private static final String PREF_KEY_MAX_UPDATE = "maxUpdate";//登录时间

    private SharedPreferences spSession = null;


    /**
     * 构造函数，使用当前登录用户
     */
    public MsgSetUtil(String ownerId) {
        if (ownerId == null)
            return;
        spSession = SharedPreferencesUtil.getPreferences(BizApplication.getAppContext(), ownerId + "." + SP_NAME_SUFFIX);
    }

    /**
     * 获取最大下载的会话数
     * @return
     */
    public int getMaxSessionNum() {
        return spSession.getInt("maxSessionNum", 200);
    }

    /**
     * 设置最大会话数
     * @param sessionNum
     */
    public void setMaxSessionNum(int sessionNum) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putInt("maxSessionNum", sessionNum);
        editor.commit();
    }

    /**
     * 获取最大下载的消息数/单会话
     * @return
     */
    public int getMaxMsgNumOneSession() {
        return spSession.getInt("maxMsgNumOneSession", 1000);
    }

    /**
     * 设置最大下载的消息数/单会话
     * @param sessionNum
     */
    public void setMaxMsgNumOneSession(int sessionNum) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putInt("maxMsgNumOneSession", sessionNum);
        editor.commit();
    }

    /**
     * 获取session会话的游标，用于向服务器端增量请求数据
     * @return
     */
    public Long getLastUpdate() {
        Long ret = spSession.getLong(PREF_KEY_LAST_UPDATE, -1);
        if (ret == -1) {//兼容老的
            return MsgConstants.spSession.getLong(PREF_KEY_LAST_UPDATE, -1);
        }
        else
            return ret;
    }

    /**
     * 清理会话游标
     */
    public void clearLastUpdate() {
        SharedPreferences.Editor editor = spSession.edit();
        editor.remove(PREF_KEY_LAST_UPDATE);
        editor.commit();
    }

    /**
     * 保存会话游标
     * @param updateTime
     */
    public void saveLastUpdate(Long updateTime) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putLong(PREF_KEY_LAST_UPDATE, updateTime);
        editor.commit();
    }

    /**
     * 获取session会话的游标，用于向服务器端增量请求数据
     * @return
     */
    public Long getMaxId(Long sessionId) {
        return spSession.getLong("maxMsgId_" + sessionId.toString(), -1);
    }

    /**
     * 保存会话游标
     * @param maxMsgId
     */
    public void saveMaxId(Long sessionId, Long maxMsgId) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putLong("maxMsgId_" + sessionId.toString(), maxMsgId);
        editor.commit();
    }

    /**
     * 获得会话游标
     * @param sessionId
     * @return
     */
    public String getMaxCreateTime(Long sessionId) {
        String maxCreateTime = spSession.getString("maxCreateTime_" + sessionId, "0000-00-00 00:00:00");
        if (StringUtils.isEmpty(maxCreateTime))
            return "0000-00-00 00:00:00";
        else
            return maxCreateTime;
    }

    /**
     * 保存消息游标
     * @param sessionId
     * @param createTiem
     */
    public void saveMaxCreateTime(Long sessionId, String createTiem) {
        MLog.d(String.format("saveMaxCreateTime, sessionId=%s, createTime=%s", String.valueOf(sessionId), createTiem));
        SharedPreferences.Editor editor = spSession.edit();
        editor.putString("maxCreateTime_" + sessionId, createTiem);
        editor.commit();
    }

    /**
     * 保存session的Msg最大游标
     * @param sessionId
     * @param lastUpdate
     */
    public void saveMaxMsgLastUpdateDate(Long sessionId, String lastUpdate) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putString("maxMsgLastUpdateDate_" + sessionId, lastUpdate);
        editor.commit();
    }

    /**
     * 获取session的Msg最大游标
     * @param sessionId
     * @return
     */
    public String getMaxMsgLastUpdateDate(Long sessionId) {
        return spSession.getString("maxMsgLastUpdateDate_" + sessionId, "");
    }

    /**
     * 清理所有配置
     */
    public void clearConfig() {
        SharedPreferences.Editor editor = MsgConstants.spSession.edit();
        if (editor != null) {
            editor.clear();
            editor.commit();
        }

        editor = spSession.edit();
        editor.clear();
        editor.commit();
    }

    public void setMaxMsgUpdateDate(String updateDate) {
        SharedPreferences.Editor editor = spSession.edit();
        editor.putString(PREF_KEY_MAX_UPDATE, updateDate);
        editor.commit();
    }

    public String getMaxMsgUpdateDate() {
        return spSession.getString(PREF_KEY_MAX_UPDATE, "");
    }

    /**
     * 第一次登陆，把对话等都油标都设置为最大
     */
    public void setLastUpdateDate() {
        EmbSessionService sessionService = ServiceFactory.getService(EmbSessionService.class);
        sessionService.queryFromNetToSaveMaxUpDateDate();
    }
}
