package com.mfh.comn.code;

import java.util.List;

/**
 * 编码服务集成门户
 * 每一种编码类我们称为一个domain
 * 每一种编码有一个编码提供者CodeHouse
 * 
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public interface ICodeService {
    public final static String DOMAIN_DEFAULT = "default";//缺省的编码域名
    
    /**
     * 使用指定的实现类，初始化一个编码提供者
     * @param houseClassName
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public <T extends ICodeHouse<?>> T initCodeHouse(Class<T> houseClassName, String... domain);
    
    /**
     * 增加一个编码提供者
     * @param codeHouse
     * @param domain
     * @author zhangyz created on 2013-6-7
     */
    public void addCodeHouse(ICodeHouse<?> codeHouse, String... domain);
    
    /**
     * 根据编码值获取一个编码描述（在默认编码域内）
     * @param code 编码值
     * @return 编码描述
     * @author zhangyz created on 2014-3-17
     */
    public <T> String getValue(T code);
    
    /**
     * 在指定编码域内根据编码值获取一个编码描述
     * @param domain 编码域，哪类编码
     * @param code 编码值
     * @return 编码描述
     * @author zhangyz created on 2013-6-7
     */
    public <T> String getValue(String domain, T code);
    
    /**
     * 在指定编码域内根据编码值获取一个编码描述
     * @param domain 编码域，以该类名命名的
     * @param code 编码值,字符型
     * @return 编码描述
     * @author zhangyz created on 2013-6-7
     */
    public <T> String getValue(Class<?> domain, T code);
    
    /**
     * 简单的布尔编码转换
     * @param code 
     * @return false:否：true:是
     * @author zhangyz created on 2013-6-7
     */
    public String getBooleanValue(boolean code);
    
    /**
     * 简单的0-1布尔编码转换
     * @param code 
     * @return 0:否：>0 是
     * @author zhangyz created on 2013-6-7
     */
    public String getIntBooleanValue(int code);
    
    /**
     * 根据编码类获取其对应的简单编码提供者
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public <T> ISimpleCodeHouse<T> getSimleCodeHouse(String... domain);
    
    /**
     * 根据编码类获取其对应的树编码提供者
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    //public <T> ITreeCodeHouse<T> getTreeCodeHouse(String... domain);
    
    /**
     * 获取该编码类下所有简单编码项，如果存在
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public <T> List<ICodeItem<T>> getOptions(String domain);
    
    /**
     * 获取该编码类下指定父编码的所有简单编码项，如果存在
     * @param parentCode 父编码值
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public <T> List<ICodeItem<T>> getOptions(T parentCode, String domain);
    
    /**
     * 获取该编码类下所有树编码项，如果存在
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    //public <T> List<ITreeCodeItem<T>> getTreeOptions(String... domain);
    
    /**
     * 获取该编码类下指定父编码的所有树编码项，如果存在
     * @param parentCode 父编码值
     * @param domain
     * @return
     * @author zhangyz created on 2013-6-7
     */
    //public <T> List<ITreeCodeItem<T>> getTreeOptions(T parentCode, String... domain);
}
