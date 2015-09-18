package com.mfh.comna.bizz.member.logic;

import android.content.SharedPreferences;

import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.bizz.member.entity.Human;
import com.mfh.comna.comn.database.dao.CursorUtil;

/**
 * 通讯录相关设置
 * Created by Administrator on 14-5-29.
 */
public class MemberSetUtil extends CursorUtil<String> {

    private static String SP_NAME_SUFFIX = "member.cfg";
    private SharedPreferences memSession = null;
    /**
     * 构造函数，使用当前登录用户
     */
    public MemberSetUtil(String ownerId) {
        super(ownerId, Human.class.getName(), String.class);
        memSession = SharedPreferencesHelper.getPreferences(ownerId + "." + SP_NAME_SUFFIX);
    }

    /**
     * 获取最大下载的通讯录数
     * @return
     */
    public int getMaxMemberNum() {
        return memSession.getInt("maxMemberNum", 1000);
    }

    /**
     * 设置最大通讯录数
     * @param sessionNum
     */
    public void setMaxMemberNum(int sessionNum) {
        SharedPreferences.Editor editor = memSession.edit();
        editor.putInt("maxMemberNum", sessionNum);
        editor.commit();
    }
}
