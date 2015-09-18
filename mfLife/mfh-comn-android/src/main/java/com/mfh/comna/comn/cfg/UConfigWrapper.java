package com.mfh.comna.comn.cfg;

import com.mfh.comn.config.ConfigsParseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class UConfigWrapper {

    /** 日志记录器 */
    protected Logger logger = null;
    
    /** 默认构造函数*/
    public UConfigWrapper() {
        super();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public UConfigWrapper(String configAlias) {
        this.configAlias = configAlias;
        logger = LoggerFactory.getLogger(this.getClass());        
    }
    
    protected String configAlias = ConfigsParseHelper.configAlias;
    
    /**
     * 初始化
     * @param stream
     * @author zhangyz created on 2013-5-25
     */
    public void init (InputStream stream) {
        ConfigsParseHelper.init(configAlias, stream);
    }
    
    /**
     * 获取指定配置路径的文件对象
     * @param configLocation
     * @return
     * @author zhangyz created on 2012-12-26
     */
    public static File getConfigFile(String configLocation){
        return new File(configLocation);
    }
    
    /**
     * 把配置的路径统一处理
     * @param configLocation
     * @return
     * @author zhangyz created on 2012-3-22
     */
    public static URL getConfigUrl(String configLocation){
        try {
            return new URL(configLocation);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
