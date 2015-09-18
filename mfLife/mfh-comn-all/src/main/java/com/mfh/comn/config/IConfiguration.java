package com.mfh.comn.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 下面来自于 org.apache.commons.configuration.Configuration,为了运行在android平台上，故没有使用整个jar包。
 * 
 * @author zhangyz created on 2013-5-26
 * @since Framework 1.0
 */
public interface IConfiguration {
    
    //public abstract Configuration subset(String s);

    public abstract boolean isEmpty();

    public abstract boolean containsKey(String s);

    public abstract void addProperty(String s, Object obj);

    public abstract void setProperty(String s, Object obj);

    public abstract void clearProperty(String s);

    public abstract void clear();

    public abstract Object getProperty(String s);

    //public abstract Iterator<?> getKeys(String s);

    public abstract Iterator<?> getKeys();

    public Properties getProperties(String s);
    
    public Properties getProperties();

    public abstract boolean getBoolean(String s);

    public abstract boolean getBoolean(String s, boolean flag);

    public abstract Boolean getBoolean(String s, Boolean boolean1);

    public abstract byte getByte(String s);

    public abstract byte getByte(String s, byte byte0);

    public abstract Byte getByte(String s, Byte byte1);

    public abstract double getDouble(String s);

    public abstract double getDouble(String s, double d);

    public abstract Double getDouble(String s, Double double1);

    public abstract float getFloat(String s);

    public abstract float getFloat(String s, float f);

    public abstract Float getFloat(String s, Float float1);

    public abstract int getInt(String s);

    public abstract int getInt(String s, int i);

    public abstract Integer getInteger(String s, Integer integer);

    public abstract long getLong(String s);

    public abstract long getLong(String s, long l);

    public abstract Long getLong(String s, Long long1);

    public abstract short getShort(String s);

    public abstract short getShort(String s, short word0);

    public abstract Short getShort(String s, Short short1);

    public abstract BigDecimal getBigDecimal(String s);

    public abstract BigDecimal getBigDecimal(String s, BigDecimal bigdecimal);

    public abstract BigInteger getBigInteger(String s);

    public abstract BigInteger getBigInteger(String s, BigInteger biginteger);

    public abstract String getString(String s);
    
    /**
     * 若为空抛出异常
     * @param s
     * @return
     * @author zhangyz created on 2014-4-22
     */
    public abstract String getStringNotNull(String s);

    public abstract String getString(String s, String s1);

    //public abstract String[] getStringArray(String s);

    public abstract List<?> getList(String s);

    //public abstract List<?> getList(String s, List<?> list);
    
    /**
     * 修改属性配置后保存一下
     * 
     * @author zhangyz created on 2013-5-26
     */
    public void commitWrite();
}
