package com.mfh.comna.comn.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 属性文件配置文件对象
 * 
 * @author zhangyz created on 2013-5-25
 * @since Framework 1.0
 */
public class PropertiesConfiguration extends BaseFileConfiguration{   
    private Properties prop;
    
    /*@Override
    public UConfig subset(String s) {
        return null;
    }*/

    @Override
    public boolean isEmpty() {
        return prop.isEmpty();
    }

    @Override
    public boolean containsKey(String s) {
        return prop.containsKey(s);
    }

    @Override
    public void addProperty(String s, Object obj) {
        prop.put(s, obj);   
        bUpdated = true;     
    }

    @Override
    public void setProperty(String s, Object obj) {
        prop.setProperty(s, obj.toString());  
        bUpdated = true;      
    }
    
    @Override
    public void clearProperty(String s) {
        prop.remove(s);      
        bUpdated = true;  
    }

    @Override
    public void clear() {
        prop.clear();  
        bUpdated = true;
    }

    @Override
    public Object getProperty(String s) {
        return prop.getProperty(s);
    }

    /*@Override
    public Iterator<?> getKeys(String s) {
        throw new RuntimeException("不支持此方法!");
    }*/

    @Override
    public Iterator<?> getKeys() {
        return prop.keySet().iterator();
    }

    @Override
    public Properties getProperties(String s) {
        throw new RuntimeException("不支持此方法!");
    }

    @Override
    public boolean getBoolean(String s) {
        return getBoolean(s, false);
    }

    @Override
    public boolean getBoolean(String s, boolean flag) {
        String value = prop.getProperty(s);
        if (value == null)
            return flag;
        return Boolean.valueOf(value);
    }

    @Override
    public Boolean getBoolean(String s, Boolean boolean1) {
        String value = prop.getProperty(s);
        if (value == null)
            return boolean1;
        return Boolean.valueOf(value);
    }

    @Override
    public byte getByte(String s) {
        byte bb = 0;
        return getByte(s ,bb);
    }

    @Override
    public byte getByte(String s, byte byte0) {
        String value = prop.getProperty(s);
        if (value == null)
            return byte0;
        else
            return value.getBytes()[0];
    }

    @Override
    public Byte getByte(String s, Byte byte1) {
        String value = prop.getProperty(s);
        if (value == null)
            return byte1;
        else
            return value.getBytes()[0];
    }

    @Override
    public double getDouble(String s) {
        return getDouble (s, 0);
    }

    @Override
    public double getDouble(String s, double d) {
        String value = prop.getProperty(s);
        if (value == null)
            return d;
        return Double.valueOf(value);
    }

    @Override
    public Double getDouble(String s, Double double1) {
        String value = prop.getProperty(s);
        if (value == null)
            return double1;
        return Double.valueOf(value);
    }

    @Override
    public float getFloat(String s) {
        return getFloat(s, 0);
    }

    @Override
    public float getFloat(String s, float f) {
        String value = prop.getProperty(s);
        if (value == null)
            return f;
        return Float.valueOf(value);
    }

    @Override
    public Float getFloat(String s, Float float1) {
        String value = prop.getProperty(s);
        if (value == null)
            return float1;
        return Float.valueOf(value);
    }

    @Override
    public int getInt(String s) {
        return getInt (s, 0);
    }

    @Override
    public int getInt(String s, int i) {
        String value = prop.getProperty(s);
        if (value == null)
            return i;
        return Integer.valueOf(value);
    }

    @Override
    public Integer getInteger(String s, Integer integer) {
        String value = prop.getProperty(s);
        if (value == null)
            return integer;
        return Integer.valueOf(value);
    }

    @Override
    public long getLong(String s) {
        return getLong(s, 0);
    }

    @Override
    public long getLong(String s, long l) {
        String value = prop.getProperty(s);
        if (value == null)
            return l;
        return Long.valueOf(value);
    }

    @Override
    public Long getLong(String s, Long long1) {
        String value = prop.getProperty(s);
        if (value == null)
            return long1;
        return Long.valueOf(value);
    }

    @Override
    public short getShort(String s) {
        return getShort(s, (short)0);
    }

    @Override
    public short getShort(String s, short word0) {
        String value = prop.getProperty(s);
        if (value == null)
            return word0;
        return Short.valueOf(value);
    }

    @Override
    public Short getShort(String s, Short short1) {
        String value = prop.getProperty(s);
        if (value == null)
            return short1;
        return Short.valueOf(value);
    }

    @Override
    public BigDecimal getBigDecimal(String s) {
        String value = prop.getProperty(s);
        if (value == null)
            return BigDecimal.valueOf(0);
        return BigDecimal.valueOf(Double.valueOf(value));
    }

    @Override
    public BigDecimal getBigDecimal(String s, BigDecimal bigdecimal) {
        String value = prop.getProperty(s);
        if (value == null)
            return bigdecimal;
        return BigDecimal.valueOf(Double.valueOf(value));
    }

    @Override
    public BigInteger getBigInteger(String s) {
        String value = prop.getProperty(s);
        if (value == null)
            return BigInteger.valueOf(0);
        return BigInteger.valueOf(Long.valueOf(value));
    }

    @Override
    public BigInteger getBigInteger(String s, BigInteger biginteger) {
        String value = prop.getProperty(s);
        if (value == null)
            return biginteger;
        return BigInteger.valueOf(Long.valueOf(value));
    }

    @Override
    public String getString(String s) {
        return prop.getProperty(s);
    }

    @Override
    public String getStringNotNull(String s) {
        String ret = prop.getProperty(s);
        if (ret == null || ret.length() == 0)
            throw new RuntimeException(s + "配置属性不存在!");
        return ret;
    }

    @Override
    public String getString(String s, String s1) {
        String value = prop.getProperty(s);
        if (value == null)
            return s1;
        return value;
    }

    /*@Override
    public String[] getStringArray(String s) {
        String value = prop.getProperty(s);
        if (value == null)
            return null;
        else
            return new String[] {value};
    }*/

    @Override
    public List<?> getList(String s) {
        String value = prop.getProperty(s);
        if (value == null)
            return null;
        else {
            List<String> ret = new ArrayList<String>();
            ret.add(value);
            return ret;
        }
    }

    /*@Override
    public List<?> getList(String s, List<?> list) {
        String value = prop.getProperty(s);
        if (value == null)
            return list;
        else {
            List<String> ret = new ArrayList<String>();
            ret.add(value);
            return ret;
        }
    }*/

    @Override
    public void refresh() {
        try {
            InputStream in = readStream(fromConfigPath);
            loadInner(in);
        }
        catch(Exception ex) {
            throw new RuntimeException("读取配置文件失败:" + ex.getMessage());
        }
    }

    @Override
    protected void writeCommitInner(OutputStream fos) throws Exception{
        prop.store(fos, "");
    }
    
    @Override
    public boolean loadFromFile(String configPath) {        
        this.fromConfigPath = configPath;
        prop = new Properties(); 
        try {
            InputStream in = readStream(configPath);
            if (in == null)
                return false;
            else {
                loadInner(in);
                return true;
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("读取配置文件失败:" + ex.getMessage());
        }
    }
    
    //执行具体的读取逻辑
    private void loadInner(InputStream in) throws Exception{
        try {
            if (prop == null)
                prop = new Properties();
            else
                prop.clear();
            prop.load(in);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean loadFromURI(URI configPath) {
        return true;
        
    }

    /**
     * (non-Javadoc)
     * @see com.mfh.comn.config.IConfiguration#getProperties()
     */
    @Override
    public Properties getProperties() {
        // TODO Auto-generated method stub
        return null;
    }
}
