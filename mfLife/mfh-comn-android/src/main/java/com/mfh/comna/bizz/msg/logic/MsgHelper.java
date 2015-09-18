package com.mfh.comna.bizz.msg.logic;

import android.content.Context;
import android.content.Intent;

import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.bizz.msg.entity.EmbMsg;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comna.comn.logic.ServiceFactory;

import java.text.SimpleDateFormat;

/**
 * 消息帮助类
 * Created by Administrator on 2015/7/8.
 */
public class MsgHelper {

    public static final SimpleDateFormat SDF_INNER_DATAFORMAT = new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT);
//
//    private static String SP_NAME_SUFFIX = "emb.session";
//    private static final String PREF_KEY_LAST_UPDATE = "lastUpdate";//最后一次更新时间
//    private static final String PREF_KEY_MAX_UPDATE = "maxUpdate";//登录时间
//

//    private static SharedPreferences spSession = null;
//    public static void restore(){
//        spSession = SharedPreferencesUtil.getPreferences(ComnApplication.getAppContext(),
//                SharedPreferencesHelper.getLoginUsername() + "." + SP_NAME_SUFFIX);
//    }
//    public static void clear(){
//        spSession = null;
//    }

    /**
     * 改变一个会话的未读消息数
     * @param sessionId
     * @param totalCount 若是正数则增加，负数则减少。
     */
    public static void changeSessionUnReadCount(Context context, Long sessionId, int totalCount) {
        EmbSessionService ss = ServiceFactory.getService(EmbSessionService.class);
        if(ss == null){
            return;
        }

        //update
        ss.getDao().addUnReadCount(sessionId, totalCount);

        //刷新当前会话的未读消息个数
        Intent intentSession = new Intent(MsgConstants.ACTION_REFRESH_SESSIONUNREAD);
        intentSession.putExtra(MsgConstants.EXTRA_NAME_SESSION_ID, sessionId);
        context.sendBroadcast(intentSession);

        //刷新当前用户所有消息的未读个数
        Integer unReadCount = ss.getDao().getTotalUnReadCount(MfhLoginService.get().getLoginName());
        if (unReadCount != null && unReadCount != -1) {
            MsgHelper.sendBroadcastForUpdateUnread(context, unReadCount);
        }
    }

    /**
     * 广播：刷新未读消息数目
     * */
    public static void sendBroadcastForUpdateUnread(Context context, int unreadCount){
        Intent intent = new Intent(MsgConstants.ACTION_REFRESH_ALLUNREAD);
        intent.putExtra(MsgConstants.PARAM_unReadCount, unreadCount);
        intent.putExtra(MsgConstants.PARAM_tabIndex, 0);//第一个tab
        context.sendBroadcast(intent);
    }

    /**
     * 判断是否自己的消息
     * @param msg
     * @return
     */
    public static boolean isMySelf(EmbMsg msg) {
        String guid = String.valueOf(msg.getFromguid());
        String myId = MfhLoginService.get().getCurrentGuId();
        if(guid == null || myId == null){
            return false;
        }

        //直接比较两个Long值是不相等的，应该是long
        return guid.equals(myId);
    }

}
