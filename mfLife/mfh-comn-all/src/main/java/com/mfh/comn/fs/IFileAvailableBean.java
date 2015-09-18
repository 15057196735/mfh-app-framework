package com.mfh.comn.fs;

import com.mfh.comn.bean.IObject;

/**
 * 能够接收文件数据参数的bean
 * 
 * @author zhangyz created on 2012-4-17
 * @since Framework 1.0
 */
public interface IFileAvailableBean<PK> extends IObject<PK>{
    /**
     * 暂存一下文件流参数
     * @param key 是哪个字段的
     * @param fileParam 
     * @author zhangyz created on 2012-4-23
     */
    public void setFileParam(String key, FileParam fileParam);

    /**
     * 增加一个文件输入
     * @param key 针对哪个字段
     * @param fileItemParam
     * @author zhangyz created on 2012-4-26
     */
    public void addFileParam(String key, FileItemParam fileItemParam);
    
    /**
     * 是否上传过文件
     * @return
     * @author zhangyz created on 2012-9-1
     */
    public boolean hasFileParam();
    
    public void clearFileParam();
    
    /**
     * 获取文件流参数
     * @param key 针对哪个参数的
     * @return
     * @author zhangyz created on 2012-4-23
     */
    public FileParam getFileParam(String key);
    
    /**
     * 获取首个文件字段的值.主要是为了向前兼容，并加快速度
     * @return
     * @author zhangyz created on 2013-8-31
     */
    public String getFilePath();
    
    public void setFilePath(String path);
}
