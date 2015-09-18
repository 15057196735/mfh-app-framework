package com.mfh.comn.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * io工具类，没有使用apache的io，是因为手机上希望小巧
 * 
 * @author zhangyz created on 2013-5-26
 * @since Framework 1.0
 */
public class IOUtils {
    
    protected static Logger logger = LoggerFactory.getLogger(IOUtils.class);
    
    /**
     * 安静的关闭IO流, 如果关闭IO流异常时使用默认的日志记录器写入默认的错误信息到日志.
     * @param closeable IO流对象
     */
    public static void closeQuietly(Closeable closeable) {
        closeQuietly(closeable, logger, "关闭IO流发生异常");
    }
    
    /**
     * 安静的关闭IO流, 如果关闭IO流异常时使用给定日志记录器写入默认的错误信息到日志.
     * @param closeable IO流对象
     * @param logger 日志记录器
     */
    public static void closeQuietly(Closeable closeable, Logger logger) {
        closeQuietly(closeable, logger, "关闭IO流发生异常");
    }
    
    /**
     * 安静的关闭IO流, 如果关闭IO流异常时使用给定日志记录器写入默认的错误信息到日志.
     * @param closeable IO流对象
     * @param message 关闭IO流异常时写入错误日志的信息
     */
    public static void closeQuietly(Closeable closeable, String message) {
        closeQuietly(closeable, logger, message);
    }
    
    /**
     * 安静的关闭IO流, 如果关闭IO流异常时使用给定日志记录器写入给定的错误信息到日志.
     * @param closeable IO流对象
     * @param logger 日志记录器
     * @param message 关闭IO流异常时写入错误日志的信息
     */
    public static void closeQuietly(Closeable closeable, Logger logger, String message) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException e) {
                logger.warn(message, e);
            }
        }
    }

    /**
     * 拷贝输入流到输出流
     * @param in
     * @param fileOut
     * @author zhangyz created on 2013-5-26
     */
    public static void copy (InputStream in, File fileOut) {
        try {
            fileOut.createNewFile();
            copy(in, new FileOutputStream(fileOut));
        }
        catch (Exception e) {
            throw new RuntimeException("写文件出错" + fileOut.getAbsolutePath() + ":" + e.getMessage() ,e);
        }
    }

    /**
     * 实现流的拷贝，拷贝完成后都进行了关闭。
     * @param in
     * @param out
     * @author zhangyz created on 2013-6-7
     */
    public static void copy (InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[1024];
            int length = 0; //字节
            while((length= in.read(buffer)) > 0){
                out.write(buffer,0,length);
            }
            out.flush();
        }
        catch(Exception e) {
            throw new RuntimeException("拷贝文件失败:" + e.getMessage(), e);
        }
        finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
