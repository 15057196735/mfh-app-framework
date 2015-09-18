package com.mfh.comna.comn.database.dao;


import com.mfh.comna.bizz.BizApplication;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件访问基础对象
 * 
 * @author zhangyz created on 2013-6-10
 * @since Framework 1.0
 */
public class BaseFao {
    private static Map<String, File> fileCache = new HashMap<> ();//文件目录名、文件对象缓冲，此处的目录名是相对目录名
    public static String temp_dir = "temp";//临时目录

    private String dirName;

    public BaseFao(String dirName) {
        this.dirName = dirName;
    }

    /**
     * 读取指定文件,注意可能不存在。
     * @param fileName
     * @return
     */
    public File readFile(String fileName) {
        File file = new File(getFileDir(), fileName);
        return file;
    }

    public File getFileDir() {
        return getFileDir(dirName);
    }

    /**
     * 指定根目录，默认位于程序的私有目录下.不包含/结尾
     * @return
     */
    protected String getRootPath() {
        return BizApplication.getAppContext().getFilesDir().getAbsolutePath();
    }

    /**
     * 或者指定相对目录的目录File对象
     * @param dirRelativeName 相对目录名，相对于应用程序的私根目录。目录名可以是temp、或temp/temp2之类。
     * @return 目录对象。内部使用了缓存,效率高的。
     * 若为null，代表使用根目录。
     * @author zhangyz created on 2013-6-7
     */
    public File getFileDir(String dirRelativeName) {
        File file = fileCache.get(dirRelativeName);
        if (file == null) {
            if (dirRelativeName == null || dirRelativeName.length() == 0)
                file = BizApplication.getAppContext().getFilesDir();
            else {
                String dirPath = getRootPath();
                if (dirRelativeName.startsWith(File.separator) == false)
                    dirPath += File.separator + dirRelativeName;
                else
                    dirPath += dirRelativeName;
                
                file = new File(dirPath);
                if (file.exists() == false)
                    file.mkdirs();
            }
            fileCache.put(dirRelativeName, file);
        }        
        return file;
    }
    
    /**
     * 获取应用程序的私人目录,以/号结尾
     * @return
     * @author zhangyz created on 2013-6-7
     */
    protected File getFileDirs() {
        return BizApplication.getAppContext().getFilesDir();
    }

    /**
     * 生成一个新的临时文件
     * @param suffixType 文件扩展名
     * @param tempDir 临时目录
     * @return
     */
    public static File getNewTempFile(String suffixType, String... tempDir) {
        File dir;
        if (tempDir != null && tempDir.length > 0){
            dir = new BaseFao(tempDir[0]).getFileDir();
        }
        else{
            dir = new BaseFao(temp_dir).getFileDir();
        }

        // 获取当前时间，进一步转化为字符串
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if (suffixType != null && suffixType.length() > 0)
            fileName += ("." + suffixType);

        File fileTmp = new File(dir, fileName);
//        if(!fileTmp.exists()){
//            fileTmp.mkdir();
//        }
        return fileTmp;
    }

    /**
     * 从网络上读取文件流
     * @param url
     * @return
     * @throws Exception
     */
    public static InputStream returnSteam(String url) throws  Exception{
        URL myFileUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        return is;
    }
}
