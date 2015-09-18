package com.mfh.comna.comn.database.dao;

import com.mfh.comn.utils.IOUtils;
import com.mfh.comn.utils.MD5;

import net.tsz.afinal.core.AsyncTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.InputStream;

/**
 * 管理本地文件与网络同步。若本地文件不存在，则从网络上下载。一旦下载后，则从本地读取。
 * Created by Administrator on 14-5-13.
 */
public class FileNetDao extends BaseFao {
    private String fileServerUrl = null;
    private String localRootDir = null;
    private boolean useLocalFirst = true;//是否优先使用本地文件

    /**
     * 构造函数
     * @param dirName 存储的相对文件目录,根目录位于程序私有目录下.下载服务器url根据默认配置
     */
    public FileNetDao(String dirName) {
        super(dirName);
        this.fileServerUrl = null;
    }

    /**
     * 构造函数
     * @param dirName 存储的相对文件目录,根目录位于程序私有目录下
     * @param fileServerUrl 文件服务url路径名，要求/结尾
     */
    public FileNetDao(String dirName, String fileServerUrl) {
        super(dirName);
        if (fileServerUrl != null && fileServerUrl.length() > 0 && fileServerUrl.endsWith("/") == false)
            fileServerUrl += "/";
        this.fileServerUrl = fileServerUrl;
    }

    /**
     * 构造函数
     * @param dirName 存储的相对文件目录
     * @param fileServerUrl 文件下载url，要求/结尾
     * @param localRootPath 存储的本地根目录,不包含/结尾
     */
    public FileNetDao(String dirName, String fileServerUrl, String localRootPath) {
        super(dirName);
        if (fileServerUrl != null && fileServerUrl.length() > 0 && fileServerUrl.endsWith("/") == false)
            fileServerUrl += "/";
        this.fileServerUrl = fileServerUrl;
        this.localRootDir = localRootPath;
    }

    public void setUseLocalFirst(boolean useLocalFirst) {
        this.useLocalFirst = useLocalFirst;
    }

    /**
     * 下载回调接口
     */
    public interface CallBack {
        /**
         * 执行下载成功处理
         * @param file
         */
        public void processFile(File file);

        /**
         * 处理异常
         * @param fileName
         * @param e
         */
        public void onFailure(String fileName, Throwable e);
    }

    @Override
    protected String getRootPath() {
        if (localRootDir != null)
            return localRootDir;
        else
            return super.getRootPath();
    }

    /**
     * 把传入的文件路径转换成本地文件名
     * @param fileName 可能是网络路径，如http://....
     * @return 下载到本地后对应的文件名（不含目录）
     */
    public static String genLocalFileName(String fileName) {
        if (fileName.indexOf("://") > 0) {
            //说明本身就是一个url串
            fileName = MD5.getMD5(fileName.getBytes());
        }
        return fileName;
    }

    /**
     * 读取并处理文件,内部会优先从本地读取，找不到再从网络下载。
     * @param fileName 文件名
     * @param callBack 获取文件后的回调处理函数
     */
    public void processFile(String fileName, final CallBack callBack) {
        final FileNetDao that = this;
        new AsyncTask<String, Integer, File>() {
            //需要异步执行
            @Override
            protected void onPostExecute(File fileImg) {
                if (fileImg != null) {
                    try {
                        callBack.processFile(fileImg);
                    }
                    catch (Throwable e) {
                        LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
                    }
                }
            }

            @Override
            protected File doInBackground(String... params) {
                String fileName = params[0];
                if (StringUtils.isBlank(fileName))
                    return null;
                String localFileName = genLocalFileName(fileName);
                try {
                    File fileImg = that.readFile(localFileName);
                    if (!useLocalFirst || !fileImg.exists()) {
                        //先从网络下载
                        String fullUrl = getFullFileUrl(fileName);
                        InputStream is = returnSteam(fullUrl);
                        IOUtils.copy(is, fileImg);
                    }
                    return fileImg;
                } catch (Throwable e) {
                    callBack.onFailure(fileName, e);
                    LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
                    return null;
                }
            }
        }.execute(fileName);
    }

    /**
     * 获取完整的文件地址
     * @param fileName
     * @return
     */
    public String getFullFileUrl(String fileName) {
        if (fileServerUrl == null) {
            fileServerUrl = "";
        }
        if (fileName.startsWith("http"))
            return fileName;
        else
            return fileServerUrl + fileName;
    }
}
