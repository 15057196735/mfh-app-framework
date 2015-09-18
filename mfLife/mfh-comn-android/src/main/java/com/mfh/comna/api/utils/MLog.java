package com.mfh.comna.api.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/7/15.
 */
public class MLog {
    public static final String TAG = "Mfh--" + getClassName();

    public static boolean DEBUG = true;
    private static boolean bShowLink = false;//是否显示链接

    //LEVEL
    private static final int LEVEL_VERBOSE = 0;
    private static final int LEVEL_DEBUG = 1;
    private static final int LEVEL_INFO = 2;
    private static final int LEVEL_WARN = 3;
    private static final int LEVEL_ERROR = 4;
    private static final int LEVEL_ASSET = 5;

    private int logLevel = LEVEL_VERBOSE;

    /**
     * verbose
     * */
    public static final void v(String log) {
        if (DEBUG)
            Log.v(TAG, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * verbose
     * */
    public static final void v(String tag, String log) {
        if (DEBUG)
            Log.v(tag, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * debug
     * */
    public static final void d(String log) {
        if (DEBUG)
            Log.d(TAG, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * debug
     * */
    public static final void d(String tag, String log) {
        if (DEBUG)
            Log.d(tag, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * info
     * */
    public static final void i(String log) {
        if (DEBUG)
            Log.i(TAG, log);
    }

    /**
     * Warn
     * */
    public static final void w(String tag, String log) {
        if (DEBUG)
            Log.w(tag, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * Warn
     * */
    public static final void w(String log) {
        if (DEBUG)
            Log.w(TAG, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * info
     * */
    public static final void i(String tag, String log) {
        if (DEBUG)
            Log.i(tag, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * error
     * */
    public static final void e(String log) {
//        if (DEBUG)
            Log.e(TAG, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * error
     * */
    public static final void e(String tag, String log) {
        if (DEBUG)
            Log.e(tag, String.format("%s %s", log, callMethodAndLine()));
    }

    /**
     * @return 当前的类名(simpleName)
     */
    private static String getClassName() {
        String result;
        StackTraceElement traceElement = (new Exception()).getStackTrace()[2];
        result = traceElement.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result;
    }

    /**
     * 显示超链
     */
    private static String callMethodAndLine() {
        if (!bShowLink){
            return "";
        }
        String result = "at ";
        StackTraceElement traceElement = (new Exception()).getStackTrace()[1];
        result += traceElement.getClassName()+ ".";
        result += traceElement.getMethodName();
        result += "(" + traceElement.getFileName();
        result += ":" + traceElement.getLineNumber() + ")  ";
        return result;
    }

}
