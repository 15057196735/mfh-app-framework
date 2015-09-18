package com.mfh.comna.comn.database.dao;

import android.app.Activity;
import android.content.SharedPreferences;

import com.mfh.comna.api.helper.SharedPreferencesHelper;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.ComnApplication;

import java.text.SimpleDateFormat;

/**
 * 本地游标工具,用于记录某表在本地同步后当前的最新游标值
 * @param <T> 游标值的类型
 * Created by Administrator on 14-5-8.
 */
public class CursorUtil<T> {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制才可以保证游标正确
    private static final String SP_NAME_SUFFIX = "cursor.value";
    private SharedPreferences sp = null;
    private String cfgItemName;
    private Class<T> cusorClass;//游标值类型

    /**
     * 构造函数，
     * @param loginName 使用当前登录用户
     * @param tableName 表名
     * @param cursorClass 游标值类型
     */
    public CursorUtil(String loginName, String tableName, Class<T> cursorClass) {
        sp = SharedPreferencesHelper.getPreferences(loginName + "." + SP_NAME_SUFFIX);
        cfgItemName = tableName + "_" + "lastUpdate";
        this.cusorClass = cursorClass;
    }

    /**
     * 获取session会话的游标，用于向服务器端增量请求数据
     * @return
     */
    public T getLastUpdate() {
        if (cusorClass.equals(Long.class)) {
            Long value = sp.getLong(cfgItemName, -1);
            return (T)value;
        }
        else if (cusorClass.equals(String.class)) {
            String value = sp.getString(cfgItemName, null);
            return (T)value;
        }
        else if (cusorClass.equals(Integer.class)) {
            Integer value = sp.getInt(cfgItemName, -1);
            return (T)value;
        }
        else {
            throw new RuntimeException("不支持的游标类型:" + cusorClass.getName());
        }
    }

    /**
     * 清理会话游标
     */
    public void clearLastUpdate() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(cfgItemName);
        editor.commit();
    }

    /**
     * 保存会话游标
     * @param updateTime
     */
    public void saveLastUpdate(T updateTime) {
        SharedPreferences.Editor editor = sp.edit();
        if (cusorClass.equals(Long.class)) {
            editor.putLong(cfgItemName, (Long)updateTime);
        }
        else if (cusorClass.equals(String.class)) {
            editor.putString(cfgItemName, (String) updateTime);
        }
        else if (cusorClass.equals(Integer.class)) {
            editor.putInt(cfgItemName, (Integer) updateTime);
        }
        else {
            throw new RuntimeException("不支持的游标类型:" + cusorClass.getName());
        }
        editor.commit();
    }


    private transient T maxDate = null;//用于临时记录当前获得的游标，取最大值，最后保存

    /**
     * 开始记录游标
     */
    public void beginRecordCursor() {
        maxDate = null;
    }

    /**
     * 记录下一个
     * @param value
     */
    public void nextRecordCurosr(T value) {//获取最大游标
        if (maxDate == null){
            maxDate = value;
        }
        else if (value != null){
            int compareValue;
            if (cusorClass.equals(Long.class)) {
                compareValue = ((Long)maxDate).compareTo((Long)value);
            }
            else if (cusorClass.equals(String.class)) {
                compareValue = ((String)maxDate).compareTo((String)value);
            }
            else if (cusorClass.equals(Integer.class)) {
                compareValue = ((Integer)maxDate).compareTo((Integer)value);
            }
            else {
                throw new RuntimeException("不支持的游标类型:" + cusorClass.getName());
            }

            if (compareValue < 0){
                maxDate = value;
            }
        }
    }

    /**
     * 提交保存最后获取的最大游标值
     */
    public void commitCursor() {
        if (maxDate == null)
            return;
        saveLastUpdate(maxDate);
    }
}
