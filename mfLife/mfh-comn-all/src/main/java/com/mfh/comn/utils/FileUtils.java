/*
 * 文件名称: FileUtil.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2012-2-21
 * 修改内容: 
 */
package com.mfh.comn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

/**
 * 文件工具类
 * @since DE 6.0
 重大修改说明：
 * 合并了niezg的FileUtil类，其注释如下。2009-6-30 by cheny
 * * Created on 2009-3-17
 * * @author niezg
 * * 提供一个文件工具类
 * * 实现上是考虑到了其他操作系统，但未在除windows操作系统以外的测试过，
 * * 如要在windows平台外使用需要测试
 */
public class FileUtils {

    /**
     * 使用指定的编码，把文件读取到字符串中。注意：适用于小文件。
     * 
     * @param fileName
     *            文件名
     * @param encode
     *            编码
     * @return
     * @throws Exception
     */
    public static String readFileToString(String fileName, String encode) throws Exception{
        return readFileToString(new FileInputStream(fileName), encode);
    }
    
    /**
     * 使用指定的编码，把输入流读取到字符串中，注意：适用于小输入流。
     * @param in 输入流
     * @param encode 编码
     * @return
     * @author zhangyz created on 2012-5-7
     */
    public static String readFileToString(InputStream in, String encode){
        InputStreamReader reader = null;
        try{
            reader = new InputStreamReader(in, encode);
            StringBuffer strBuffer = new StringBuffer("");
            int length = 500;
            char b[] = new char[length];
            int i;
            do{
                i = reader.read(b, 0, length);
                if (i < 0)
                    break;
                strBuffer.append(new String(b, 0, i));
            }
            while (true);
            if (in != null)
                in.close();
            return strBuffer.toString();
        }
        catch(Exception ex){
            throw new RuntimeException("读取文件流失败:" + ex.getMessage(), ex);
        }
        finally{
            if (reader != null)
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 根据传入的文件全路径，返回文件所在路径
     * 
     * @param fullPath
     *            文件全路径
     * @return 文件所在路径
     * @roseuid 3FBE26DE029A
     */
    public static String getDir(String fullPath){
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(0, iPos1 + 1);
    }

    /**
     * 把字符串内容存储到指定的目录里。
     * 
     * @param fullPath
     *            目标文件名。
     * @param content
     *            内容
     * @param replace
     *            是否替换
     * @return
     * @throws Exception
     */
    public static boolean saveStringToFile(String fullPath, String content, boolean replace, String encode) throws Exception{
        File f = new File(fullPath);
        if (f.exists()){
            if (replace == false){
                return true;
            }
            if (f.isFile() && replace == true){
                if(f.delete() == false)
                    return false;
            }
        }
        else{
            String dir = getDir(fullPath);
            f = new File(dir);
            if (!f.exists()){
                if(f.mkdirs() == false)
                    return false;
            }
        }
        
        java.io.FileOutputStream fw = null;
        try{
            fw = new java.io.FileOutputStream(fullPath);
            fw.write(content.getBytes(encode));
            /*FileWriter fw = new FileWriter(fullPath);
            fw.write(content);*/
            return true;
        }
        catch(IOException e){
            throw new Exception("保存内容至文件" + fullPath + "失败:" + e.getMessage(), e);
        }
        finally{
            if (fw != null)
                fw.close();
        }
    }
    
    /**
     * 判定在指定目录下有没有和指定的文件名同名的，如果有生成一个新文件名。新文件名是采用后面递增加1的办法。
     * @param path 文件路径
     * @param fileName 文件名
     * @return 没有重名的新文件对象，但没有实际创建它。
     */
    public static File getFileNameNoSame(String path, String fileName){     
        File file = null;
        file = new File(path, fileName);
        if (file.exists() == false)//不存在
            return file;
        
        String[] segs = divideFileName(fileName);       
        int index = 1;
        do{
            fileName = segs[0] + index + segs[1];//文件名加1
            file = new File(path, fileName);
            if (file.exists() == false)
                break;
            index ++;
        }
        while(true);
        return file;
    }
    
    /**
     * 判定在指定目录下有没有和指定的文件名同名的，如果有生成一个新文件名。新文件名是采用文件名加当前时间的办法。
     * @param path 文件路径
     * @param fileName 文件名
     * @return 没有重名的新文件对象，但没有实际创建它。
     * @author jh  2011-08-10
     */
    public static File getFileNameNoSameAddTime(String path, String fileName){      
        File file = null;
        file = new File(path, fileName);
        if (file.exists() == false)//不存在
            return file;
        
        String[] segs = divideFileName(fileName);
        //分隔符
        String separator = "_";
        //初始化时间模版
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        //当前时间
        String dateTime = null;

        do {
            //获取当前时间
            dateTime = tempDate.format(new java.util.Date());
            //拼接新文件名
            fileName = segs[0] + separator + dateTime + segs[1];
            file = new File(path, fileName);
            if (file.exists() == false)
                break;
        }
        while (true);
        return file;
    }
    
    /**
     * 把一个文件名分隔成文件名本身和扩展名两部分。
     * @param name
     * @return String[];0:为文件名本身加.号;1:为扩展名,含点号
     */
    public static String[] divideFileName(String name){
        String preName;// 文件名
        String suffix;// 扩展名
        int index = name.lastIndexOf(".");
        // 下面文件名拆分一下。
        if (index > 0) {
            preName = name.substring(0, index);
            suffix = name.substring(index);//含.号
        }
        else {
            preName = name;
            suffix = "";
        }
        return new String[]{preName, suffix};
    }
    
    /**
     * 获取文件的扩展名
     * @param name
     * @return
     * @author zhangyz created on 2012-4-27
     */
    public static String getSuffixName(String name){
        if (name == null)
            return "";
        String suffix;// 扩展名
        int index = name.lastIndexOf(".");
        // 下面文件名拆分一下。
        if (index > 0 && index < name.length() - 1) {
            suffix = name.substring(index + 1);
        }
        else {
            suffix = "";
        }
        return suffix;
    }
    
    /**
     * 获取文件名的名子部分，不包含扩展名
     * @param fullName
     * @return
     * @author zhangyz created on 2012-5-22
     */
    public static String getOnlyFileName(String fullName){
        String fn;// 扩展名
        int index = fullName.lastIndexOf(".");
        // 下面文件名拆分一下。
        if (index > 0) {
            fn = fullName.substring(0, index);
        }
        else {
            fn = fullName;
        }
        return fn;
    }
    
    /**
     * 把源文件拷贝到目标目录下，采用同名
     * @param sourceFileName 源文件
     * @param destPath 目标目录
     * @author zhangyz created on 2012-7-5
     */
    public static void copyFile(String sourceFileName, String destPath) {
        try {
            File source = new File(sourceFileName);
            File dest = new File((new StringBuilder(String.valueOf(destPath))).append(source.getName()).toString());
            if(!dest.exists())
                dest.createNewFile();
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(dest);
            IOUtils.copy(in, out);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void copyFile(File source, File dest) {
        try {
            if(!dest.exists()){
                dest.createNewFile();
            }
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(dest);
            IOUtils.copy(in, out);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static String[] extractPathAndFile(String fileFullName) {
        int index = fileFullName.lastIndexOf("/");
        String path = null;
        String fileName = null;
        if (index >= 0){
            if (index == fileFullName.length() - 1)
                path = fileFullName;
            else{
                path = fileFullName.substring(0, index + 1);
                fileName = fileFullName.substring(index + 1);
            }
        }
        else{
            fileName = fileFullName;
        }
        return new String[]{path, fileName};
    }
}

