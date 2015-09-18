package com.mfh.comna.api.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NAT.ZZN on 2015/5/26.
 */
public class SharedPreferencesUtil {

    public static void set(Context context, String prefName, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences(context,prefName).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void set(Context context, String prefName, String key, String value) {
        SharedPreferences.Editor editor = getPreferences(context,prefName).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void set(Context context, String prefName, String key, int value) {
        SharedPreferences.Editor editor = getPreferences(context,prefName).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void set(Context context, String prefName, String key, long value) {
        SharedPreferences.Editor editor = getPreferences(context,prefName).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static boolean get(Context context, String prefName, String key, boolean defValue) {
        return getPreferences(context,prefName).getBoolean(key, defValue);
    }

    public static String get(Context context, String prefName, String key, String defValue) {
        return getPreferences(context,prefName).getString(key, defValue);
    }

    public static Long getLong(Context context, String prefName, String key, Long defValue) {
        return getPreferences(context,prefName).getLong(key, defValue);
    }

    public static int getInt(Context context, String prefName, String key, int defValue) {
        return getPreferences(context,prefName).getInt(key, defValue);
    }

    public static void clear(Context context, String prefName){
        SharedPreferences.Editor editor = getPreferences(context,prefName).edit();
        editor.clear();
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context context, String prefName) {
        SharedPreferences pre = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);//MODE_MULTI_PROCESS
        return pre;
    }
}
