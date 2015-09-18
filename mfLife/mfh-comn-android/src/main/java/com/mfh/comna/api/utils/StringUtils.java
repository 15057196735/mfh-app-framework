package com.mfh.comna.api.utils;

import java.util.Random;
import java.util.regex.Pattern;

public class StringUtils {

    private final static Pattern URL = Pattern.compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

    public static String toStr(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static String toStr(Object obj, String defautl) {
        if (isEmpty(obj)) {
            return defautl;
        }
        return obj.toString();
    }
    
    public static String toStrE(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static boolean isEmpty(Object obj) {
        boolean ret = false;
        if (obj == null || "".equals(obj.toString())) {
            ret = true;
        }
        return ret;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String removeDiskStr(String path) {
        String ret = toStrE(path);
        int start = ret.indexOf(":");
        if (start != -1) {
            ret = ret.substring(start + 1);
        }
        return ret;
    }

    public static boolean toBoolean(String arg0) {
        boolean ret = true;
        if ("0".equals(arg0)) {
            ret = false;
        }

        return ret;
    }

    public static boolean isLetter(char[] chars) {
        for (char c : chars) {
            if (c != 0) {
                if (!('a' <= c && c <= 'z') && !('A' <= c && c <= 'Z')) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isDigit(String val) {
        return isDigit(val.toCharArray());
    }

    public static boolean isDigit(char[] chars) {
        for (char c : chars) {
            if (c != 0) {
                if (!('0' <= c && c <= '9')) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean startsWithNum(char[] chars) {
        char c = chars[0];
        if ('0' <= c && c <= '9') {
            return true;
        }
        return false;
    }

    /**
     * 转义XML数据字符
     * 
     * @param  source 待转义的字符
     * @return 转义后的字符
     */
    public static String convertXmlString(String source) {
        String ret = toStrE(source);
        ret = ret.replace("&", "&amp;");
        ret = ret.replace("'", "&apos;");
        ret = ret.replace("\"", "&quot;");
        ret = ret.replace(">", "&gt;");
        ret = ret.replace("<", "&lt;");
        
        return ret;
    }
    
    /**
     * 比较两个是否相等
     * @param the
     * @param other
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public static boolean equals(String the, String other) {
        if (the == null && other == null)
            return true;
        else if (the == null || other == null)
            return false;
        return the.equals(other);
    }
    
    public static int ComareStr(String the, String other) {
        if (the == null && other == null)
            return 0;
        else if (the == null)
            return -1;
        else if (other == null)
            return 1;
        char[] thisChars = the.toCharArray();
        char[] oChars = other.toCharArray();
        int thisCount = thisChars.length;
        for(int i = 0; i < thisCount; i++) {
            if (thisChars[i] > oChars[i]) {
                return 1;
            } else if (thisChars[i] < oChars[i]) {
                return -1;
            }
        }
        return 0;
    }

    public static String padRight(String val, char padChar, int length) {
        String ret = (val == null ? "" : val);
        if (val != null && val.length() < length) {
            for (int i = 0; i < length - val.length(); i++) {
                ret += padChar;
            }
        }
        return ret;
    }

    //比较两个字符串，如果有包含的，就返回那个比较长的字符串
    public static String ReturnLongerWhereContain(String str1, String str2) {
        String longer = "";
        String shorter = "";
        if (str1.length() > str2.length()) {
            longer = str1;
            shorter = str2;
        }
        else {
            shorter = str1;
            longer = str2;
        }
        if (longer.contains(shorter))
            return longer;
        else
            return null;
    }

    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return URL.matcher(str).matches();
    }

    /**
     * 获取随机字符串
     * @param length 字符串长度
     * */
    public static String genNonceStr(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
