package com.mfh.comna.api.utils;

/**
 * 工具类 · 时间
 * Created by Administrator on 2015/7/9.
 */
public class TimeUtil {
    /**
     生成时间戳(10位)
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

}
