package com.mfh.comn.config;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

/**
 * 单个配置文件实现类接口(与统一配置框架配合实现)
 * 
 * @author zhangyz created on 2013-5-26
 * @since Framework 1.0
 */
public interface SingleConfiguration extends IConfiguration{    
    /**
     * 从文件中初始化
     * @param configPath
     * @author zhangyz created on 2013-5-25
     */
    public boolean loadFromFile(String configPath);
    
    /**
     * 从url中初始化
     * @param configPath
     * @author zhangyz created on 2013-5-25
     */
    public boolean loadFromURI(URI configPath);
    
    /**
     * 获取配置文件可写的对象
     * @return
     * @author zhangyz created on 2013-5-26
     */
    public File getWriteAbleFile();
    
    /**
     * 获取配置文件流
     * @return
     * @author zhangyz created on 2013-5-26
     */
    public InputStream getReadStream();
    
    /**
     * 重新刷新一下
     * 
     * @author zhangyz created on 2013-5-26
     */
    public void refresh();
}
