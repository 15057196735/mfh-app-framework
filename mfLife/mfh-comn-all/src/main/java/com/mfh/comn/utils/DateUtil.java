package com.mfh.comn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * 
 * @author zhangyz created on 2013-4-12
 * @since Framework 1.0
 */
public class DateUtil {

    public static final String INNER_DATAFORMAT = "yyyy-MM-dd HH:mm:ss";// 与calendar.jsp结合。

    public static final String INNER_DATAFORMAT_MM = "yyyy-MM-dd HH:mm";

    public static final String JSON_DATAFORMAT = INNER_DATAFORMAT;// 数据接口中的日期格式json。原来是"yyyy-MM-dd HH:mm"

    public static final String JSON_SHORTDATE_RECEIVE = INNER_DATAFORMAT;// 数据接口中的日期格式json。

    public static final String JSON_DATE_RECEIVE = INNER_DATAFORMAT;// 数据接口中的日期格式json。

    public static final String INNER_FOR_MESSAGE_SHOW_DATAFORMAT = "yyyy-MM-dd HH:mm";

    public static final String INNER_DATAFORMATSHORT = "yyyy-MM-dd";// 与calendar.jsp结合。

    public static final String INNER_TIMEFORMAT = "HH:mm:ss";// 与calendar.jsp结合。

    public static final String INNER_DATAFORMATSHORTMONTH = "yyyy-MM";// 
    
    public static final SimpleDateFormat TimeFormat = new SimpleDateFormat(INNER_TIMEFORMAT, Locale.CHINA);// 内部使用的日期格式。

    public static final SimpleDateFormat InnerFormat = new SimpleDateFormat(INNER_DATAFORMAT, Locale.CHINA);// 内部使用的日期格式。

    public static final SimpleDateFormat InnerFormatShort = new SimpleDateFormat(INNER_DATAFORMATSHORT, Locale.CHINA);// 内部使用的日期格式。

    public static final SimpleDateFormat timeFormat() {
        return new SimpleDateFormat(INNER_TIMEFORMAT, Locale.CHINA);// 内部使用的日期格式。
    }

    public static final SimpleDateFormat innerFormat() {
        return new SimpleDateFormat(INNER_DATAFORMAT, Locale.CHINA);// 内部使用的日期格式。
    }

    public static final SimpleDateFormat innerFormatShort() {
        return new SimpleDateFormat(INNER_DATAFORMATSHORT, Locale.CHINA);// 内部使用的日期格式。
    }
    
    public static final SimpleDateFormat innerFormatShortMONTH() {
        return new SimpleDateFormat(INNER_DATAFORMATSHORTMONTH, Locale.CHINA);// 内部使用的日期格式。
    }

    /**
     * 日期转字符
     * 
     * @param source
     * @return 格式: yyyy-MM-dd HH:mm:ss
     * @author zhangyz created on 2014-9-29
     */
    public static String toStringInnerFormat(Date source) {
        if (source == null)
            return null;
        return innerFormat().format(source);
    }

    /**
     * 日期转字符
     * 
     * @param source
     * @return 格式: yyyy-MM-dd
     * @Author:zhanggd created on 2014-10-30
     */
    public static String toStringInnerFormatShort(Date source) {
        if (source == null)
            return null;
        return innerFormatShort().format(source);
    }

    /**
     * 日期转字符
     * 
     * @param source
     * @return 格式: yyyy-MM
     * @Author:zhanggd created on 2014-12-18
     */
    public static String toStringInnerFormatShortMonth(Date source) {
        if (source == null)
            return null;
        return innerFormatShortMONTH().format(source);
    }
    
    /**
     * 字符转日期
     * 
     * @param source
     * @return
     * @author zhangyz created on 2014-9-29
     */
    public static Date toDateInnerFormat(String source) {
        try {
            if (source == null)
                return null;
            return innerFormat().parse(source);
        }
        catch (ParseException e) {
            throw new RuntimeException("错误的日期格式:" + source + ",应该是:" + INNER_DATAFORMAT);
        }
    }

    /**
     * @Description: 将字符串转化为指定格式的日期
     * @Author: 张国栋
     * @Since: 2013-3-9下午02:39:24
     * @param date
     * @return
     */
    public static Date stringToDate(String date, String formatStr) {
        SimpleDateFormat formatDate = new SimpleDateFormat(formatStr);
        Date time = null;
        try {
            time = formatDate.parse(date);
        }
        catch (Exception e) {
            throw new RuntimeException(formatStr + "日期转换出错：" + e.getMessage());
        }
        return time;
    }
    
    /**
     * 日期比较
     * @param oldDate
     * @param newDate
     * @return
     * @Author:zhanggd created on 2014-12-13
     */
    public static boolean dateAfter(Date newDate, Date oldDate){
        return newDate.after(oldDate);
    }

    public static int compareDatesByCompareTo(Date newDate, Date oldDate){
        return oldDate.compareTo (newDate);
    }
    
    /**
     * 格林尼治时间转换为本地时间
     * 
     * @param date
     * @return
     * @Author:zhanggd created on 2014-9-22
     */
    public static Date gmtToLocalDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date time = null;
        try {
            time = sdf.parse(date);
        }
        catch (Exception e) {
            throw new RuntimeException(date + "日期转换出错：" + e.getMessage());
        }
        return time;
    }

    public static void tdate3() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date ftime = null;
        try {
            ftime = sdf.parse("Thu, 14 Jun 2012 07:17:21 GMT");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // sdf2.setTimeZone(TimeZone.getDefault());
        System.out.println(sdf.format(ftime));
        System.out.println(sdf2.format(ftime));
    }

    public static void main(String[] args) {

        Date ftime = gmtToLocalDate("Sat, 19 Jul 2014 03:09:25 GMT");
        System.out.println(ftime);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf2.format(ftime));
        tdate3();

        System.out.println(toStringInnerFormatShort(new Date()));
        
        System.out.println(compareDatesByCompareTo(new Date(), toDateInnerFormat("2014-12-01 00:00:00")));
       System.out.println(dateAfter(new Date(), toDateInnerFormat("2014-12-01 00:00:00")));
       System.out.println(dateAfter(toDateInnerFormat("2014-12-01 00:00:00"), new Date()));
    }
}
