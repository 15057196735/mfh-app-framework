/*
 * 文件名称: PathUtils.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-20
 * 修改内容: 
 */
package com.mfh.comn.utils;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;


/**
 * 路径工具类. 该类是假设工具类所在的cds-framework.jar一定位于${WebRoot}/WEB-INF/classes目录下.
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-20
 * @since SHK Framework 1.0
 */
public class PathUtils {
    
    private static Set<Character> invalidChar = new HashSet<Character>();

    static {
        //invalidChar.add('\\');
        //invalidChar.add('/');
        //invalidChar.add(':');
        invalidChar.add('*');
        invalidChar.add('"');
        invalidChar.add('<');
        invalidChar.add('>');
        invalidChar.add('|');
        invalidChar.add(' ');
        invalidChar.add('\t');
    }
    
    /**
     * 判断两个目录参数是否互指向同一个路径下(一个路径包含另一个路径也认为指向同一个路径).
     * 
     * @param path1 目录1
     * @param path2 目录2
     * 
     * @return
     * @author LuoJingtian created on 2012-2-22
     * @since DE 6.0
     */
    public static boolean isSamePath(String path1, String path2) {
        // 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 统一转为小写字母
        path1 = trimEndFileSeparator(path1).toLowerCase();
        path2 = trimEndFileSeparator(path2).toLowerCase();

        // 如果两个目录的父目录相同: 两个路径不同, 不是同一个路径; 否则是同一个路径
        String path1Parent = new File(path1).getParent();
        String path2Parent = new File(path2).getParent();
        if (path1Parent != null && path1Parent.equals(path2Parent)) {
            return path1.equals(path2);
        }

        // 如果两个目录互不包含, 不是同一个路径
        if (!path1.contains(path2) && !path2.contains(path1)) {
            return false;
        }

        // 如果一个路径包含另一个路径, 指向同一个路径
        if (path1.replace(path2, path1).equals(path1) || path2.replace(path1, path2).equals(path2)) {
            return true;
        }

        return false;
    }
    
    public static boolean isEqualsPath(String path1, String path2) {
        path1 = trimEndFileSeparator(path1).toLowerCase();
        path2 = trimEndFileSeparator(path2).toLowerCase();
        return path1.equals(path2);
    }
    
    public static boolean isNotEqualsPath(String path1, String path2) {
        return !isEqualsPath(path1, path2);
    }

    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 如果路径不以"/"开头, 则添加"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     * @author LuoJingtian created on 2012-2-22
     * @since DE 6.0
     */
    public static String appendBeginFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        if (!afterPath.startsWith("/")) {
            afterPath = "/" + afterPath;
        }
        return afterPath;
    }
    
    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 截去开头的"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     * @author LuoJingtian created on 2012-2-22
     * @since DE 6.0
     */
    public static String trimBeginFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        while (afterPath.startsWith("/")) {
            afterPath = afterPath.substring(1);
        }
        return afterPath;
    }
    
    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 如果路径不以"/"结尾, 则追加"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     * @author LuoJingtian created on 2012-2-22
     * @since DE 6.0
     */
    public static String appendEndFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        if (!afterPath.endsWith("/")) {
            afterPath = afterPath + "/";
        }
        return afterPath;
    }
    
    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 截去末尾的"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     * @author LuoJingtian created on 2012-2-22
     * @since DE 6.0
     */
    public static String trimEndFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        while (afterPath.endsWith("/")) {
            afterPath = afterPath.substring(0, afterPath.length() - 1);
        }
        return afterPath;
    }
    
    public static String replacePathSeparator(String path) {
        return path.trim().replaceAll("\\\\", "/");
    }
    
    /**
     * 判断指定路径是否包含非法字符
     * @param path 路径
     * @return
     * @author LuoJingtian created on 2012-2-22 
     * @since DE 6.0
     */
    public static boolean containsInvalidCharacter(String path) {
        char[] charArray = path.trim().toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (invalidChar.contains(charArray[i])) {
                return true;
            }
        }
        return false;
    }
    

    /**
     * 获取ClassPath路径
     * @return ClassPath路径
     * @author LuoJingtian created on 2011-12-20 
     * @since SHK Framework 1.0
     */
    public static String getClassPath() {
        URL url = PathUtils.class.getResource("/");
        if (url == null)
            return null;
        String path = url.getPath();
        return path;
    }
    
    
    
    /**
     * 获取WebInf路径
     * @return WebInf路径
     * @author LuoJingtian created on 2011-12-20 
     * @since SHK Framework 1.0
     */
    public static String getWebInfPath() {
        String clsPath = getClassPath();
        if (clsPath == null)
            return null;
        return StringUtils.substringBeforeLast(StringUtils.substringBeforeLast(clsPath, "/"), "/");
    }
    
    /**
     * 获取WebInf路径
     * @return WebInf路径
     * @author LuoJingtian created on 2011-12-20 
     * @since SHK Framework 1.0
     */
    public static String getWebRootPath() {
        String webPath = getWebInfPath();
        if (webPath == null)
            return null;
        return StringUtils.substringBeforeLast(webPath, "/");
    }    

}
