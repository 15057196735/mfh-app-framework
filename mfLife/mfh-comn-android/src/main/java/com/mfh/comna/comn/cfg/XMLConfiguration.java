package com.mfh.comna.comn.cfg;

import com.mfh.comn.Constants;
import com.mfh.comn.utils.IOUtils;
import com.mfh.comn.utils.W3cDomUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.w3c.dom.Document;

/**
 * xml配置文件实现对象
 * 
 * @author zhangyz created on 2013-5-25
 * @since Framework 1.0
 */
public class XMLConfiguration extends BaseFileConfiguration{

    private Document doc;
    
    public XMLConfiguration() {
        super();
    }

    public Document getDocument() {
        return doc;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(String s) {
        return false;
    }

    @Override
    public void addProperty(String s, Object obj) {
        this.bUpdated = true;
        
    }

    @Override
    public void setProperty(String s, Object obj) {
        this.bUpdated = true;
        
    }

    @Override
    public void clearProperty(String s) {
        this.bUpdated = true;
        
    }

    @Override
    public void clear() {
        this.bUpdated = true;
        
    }

    @Override
    public Object getProperty(String s) {
        return null;
    }

    @Override
    public Iterator<?> getKeys() {
        return null;
    }

    @Override
    public Properties getProperties(String s) {
        return null;
    }

    @Override
    public boolean getBoolean(String s) {
        return false;
    }

    @Override
    public boolean getBoolean(String s, boolean flag) {
        return false;
    }

    @Override
    public Boolean getBoolean(String s, Boolean boolean1) {
        return null;
    }

    @Override
    public byte getByte(String s) {
        return 0;
    }

    @Override
    public byte getByte(String s, byte byte0) {
        return 0;
    }

    @Override
    public Byte getByte(String s, Byte byte1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getDouble(String s) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDouble(String s, double d) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Double getDouble(String s, Double double1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getFloat(String s) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFloat(String s, float f) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Float getFloat(String s, Float float1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getInt(String s) {
        return 0;
    }

    @Override
    public int getInt(String s, int i) {
        return 0;
    }

    @Override
    public Integer getInteger(String s, Integer integer) {
        return null;
    }

    @Override
    public long getLong(String s) {
        return 0;
    }

    @Override
    public long getLong(String s, long l) {
        return 0;
    }

    @Override
    public Long getLong(String s, Long long1) {
        return null;
    }

    @Override
    public short getShort(String s) {
        return 0;
    }

    @Override
    public short getShort(String s, short word0) {
        return 0;
    }

    @Override
    public Short getShort(String s, Short short1) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String s) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String s, BigDecimal bigdecimal) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String s) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String s, BigInteger biginteger) {
        return null;
    }

    @Override
    public String getString(String s) {
        return null;
    }
    @Override
    public String getStringNotNull(String s) {
        return null;
    }

    @Override
    public String getString(String s, String s1) {
        return null;
    }
    
    @Override
    public List<?> getList(String s) {
        return null;
    }

    /*@Override
    public List<?> getList(String s, List<?> list) {
        return null;
    }*/

    @Override
    public void refresh() {
        
    }
    
    @Override
    public boolean loadFromFile(String configPath) {
        this.fromConfigPath = configPath;
        try {
            InputStream in = readStream(configPath);
            if (in == null) {
                return true;
            }
            else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copy(in, out);
                String strXml = new String(out.toByteArray(), Constants.defaultCode);
                out.close();
                doc = W3cDomUtils.fromXmlString(strXml);
                return true;
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("读取配置文件失败:" + ex.getMessage());
        }
        
    }

    @Override
    public boolean loadFromURI(URI configPath) {
        return true;
        
    }

    @Override
    protected void writeCommitInner(OutputStream fos) throws Exception {
        
    }

    /**
     * (non-Javadoc)
     * @see com.mfh.comn.config.IConfiguration#getProperties()
     */
    @Override
    public Properties getProperties() {
        return null;
    }    
}
