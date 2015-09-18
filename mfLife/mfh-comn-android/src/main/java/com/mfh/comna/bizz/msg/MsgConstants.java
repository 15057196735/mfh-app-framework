package com.mfh.comna.bizz.msg;

import android.app.Activity;
import android.content.SharedPreferences;

import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.api.utils.StringUtils;
import com.mfh.comna.comn.database.dao.FileNetDao;
import com.mfh.comna.view.img.FineImgView;

import java.util.Calendar;

/**
 * Created by Administrator on 14-5-8.
 */
public class MsgConstants {
    public static String SP_NAME = "app.emb.session";
    public static SharedPreferences spSession = BizApplication.getAppContext().getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);

    public static final String ACTION_MSG_SERVERERROR = "action.msg.servererr";//消息服务器服务异常，但网络正常

    public static final String ACTION_DOWNLOAD_FINISH = "action.download.finish";//第一次开始下载消息会话事件
    public static final String ACTION_REFRESH_ALLUNREAD = "action.refresh.unReadCount";//发送刷新所有未读个数事件
    public static final String ACTION_REFRESH_SESSIONUNREAD = "action.refresh.session.unReadCount";//发送刷新会话未读个数事件
    public static final String ACTION_RECEIVE_MSG = "action.receive.newMsg";//接收到新消息
    public static final String ACTION_RECEIVE_MSG_BACK = "action.receiver.background";//接受到新的消息，后台发消息提示使用
    public static final String ACTION_RECEIVE_SESSION = "action.receive.newSession";//接收到新消息列表
    public static final String ACTION_BEGIN_INPUT = "action.begin.input";//开始输入
    public static final String ACTION_HIDE_MEDIAINPUT = "action.hide.media";//隐藏媒体录入
    public static final String ACTION_SEND_MSG = "action.send.msg";//隐藏媒体录入
    public static final String ACTION_SORT_SCOLL_UNREAD_MSG = "action.sort.scoll.unread.msg";//调转到未读消息
    public static final String ACTION_SAVE_FINISH = "action.save.finish";//   服务组对话保存完成
    public static final String ACTION_DIALOG_MISS = "action.dialog.miss";//消息里的消灭dialog
    public static final String GE_TUI_MSG_SHOW = "ge.tui.msg.show";//个推消息的显示
    public static final String ACTION_REFRESH_UNREAD_COUNT_MAIN = "action.refresh.unread.count.main";
    public static final String ACTION_APP_ENTER_FOREBACKGROUND = "action.app.enter.foreground";//应用程序进入前台

    //intent/bundle 参数
    public static final String EXTRA_NAME_SESSION_ID = "sessionId";//会话编号

    public static final String PARAM_unReadCount = "unReadCount";
    public static final String PARAM_tabIndex = "tabIndex";

    public static final int CODE_REQUEST_XIANGCE = 1;
    public static final int CODE_REQUEST_MATERIAL_LIB = 2;
    public static final int CODE_WORK_ORDER_ADD_XIANGCE = 3;
    public static final int CODE_REQUEST_CYY = 4;

    public static final int MSG_MODE_AREA = 1;//小区模式
    public static final int MSG_MODE_APART = 0;//楼管模式
    public static final int MSG_MODE_TAX = 2;//税务模式
    public static final int MSG_MODE_WORKER = 3;//

    public static final int MSG_NOTIFICATION = 0;//消息提示
    public static final int MSG_NOTIFICATION_SESSIOIN = 1;//会话消息提示
    public static final int NOTIFICATION_NEW_MESSAGE = 2;//会话消息提示


    public static final int CHANNEL_ID = 68;

    //类名字符串
    public static final String SERVICE_ADD_ACTIVITY_ClASS_NAME = "add_pmc_work_order";//ServiceAddActivity

    public static String MSG_IMG_DIR = "msgImgDir";

    public final static Long SystemSessionId = -1010101010101L;

    /**
     * 获取消息图像的文件访问dao对象
     * @return
     */
    public static FileNetDao getMsgImgFao() {
        return FineImgView.getFao(null, MSG_IMG_DIR);
    }


    /**
     * 通过传进来的时间返回翻译时间，如：早上 6：00
     * @param time
     * @param type 类型，昨天，前天是否带后续时间(in,代表是对话里面，带时间，out不带)
     * @return
     */
    public static String getCaptionTime (String time, String type) {
        Calendar calendar = Calendar.getInstance();
        //获取系统当前时间
        //年
        String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
        String year = time.substring(0, 4);
        //月
        String currentMon = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (currentMon.length() == 1) {
            currentMon = "0" + currentMon;
        }
        String mon = time.substring(5, 7);
        //日
        Integer currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String day = time.substring(8, 10);
        if (day.substring(0, 1).equals(0) && day.length() > 1)
            day.substring(1);

        Integer inDay = 0;
        if (day != null && StringUtils.isDigit(day))
            inDay = Integer.valueOf(day);

        //判断年月是否相等
        if (currentMon.equals(mon) && currentYear.equals(year)) {
            //判断日是否相等
            if (currentDay == inDay) {
                return getCaptionDay(time);
            }
            else if (currentDay - 1 == inDay) {
                if ("in".equals(type))//里面的时间
                    return "昨天 " + getCaptionDay(time);
                else//外面的时间
                    return "昨天";
            }
            else if (currentDay - 2 == inDay) {
                if ("in".equals(type))//里面的时间
                    return "前天 " + getCaptionDay(time);
                else//外面的时间
                    return "前天";
            }

        }
        else {
            return time.substring(5, 16);
        }

        return time.substring(5, 16);
    }

    public static String  getCaptionDay (String time) {
        String trimTime = time.trim();
        //获得时间
        String hour = trimTime.substring(11, 13);
        Integer theHour = Integer.valueOf(hour);
        if (theHour >= 0 && theHour <= 5)
            return "凌晨" + trimTime.substring(11, 16);
        else if (theHour > 5 && theHour <= 11)
            return "早上" + trimTime.substring(11, 16);
        else if (theHour > 11 && theHour <= 17)
            return "下午" + trimTime.substring(11, 16);
        else if (theHour >18 && theHour <= 23)
            return "晚上" + trimTime.substring(11, 16);
        else
            return time.substring(5, 16);
    }
}
