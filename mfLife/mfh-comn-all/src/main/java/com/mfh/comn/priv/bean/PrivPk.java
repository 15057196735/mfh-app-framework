package com.mfh.comn.priv.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * 授权信息主键
 * 
 * @author zhangyz created on 2012-5-25
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class PrivPk implements Serializable{
    private String masterId;
    private String moduleno;
    private String dataNo;
    
    public PrivPk() {
        super();
    }
    
    public PrivPk(String masterId) {
        super();
        this.masterId = masterId;
    }
    
    public PrivPk(String masterId, String moduleno, String dataNo) {
        super();
        this.masterId = masterId;
        this.moduleno = moduleno;
        this.dataNo = dataNo;
    }
    
    public PrivPk(String masterId, String moduleno) {
        super();
        this.masterId = masterId;
        this.moduleno = moduleno;
    }

    public String getMasterId() {
        return masterId;
    }
    
    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }
    
    public String getModuleno() {
        return moduleno;
    }
    
    public void setModuleno(String moduleno) {
        this.moduleno = moduleno;
    }
    
    public String getDataNo() {
        return dataNo;
    }
    
    public void setDataNo(String dataNo) {
        this.dataNo = dataNo;
    }
    

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if (!(other instanceof PrivPk))
            return false;
        PrivPk castOther = (PrivPk) other;
        return new EqualsBuilder().append(this.getMasterId(), castOther.getMasterId())
                .append(this.getModuleno(), castOther.getModuleno()).append(this.getDataNo(), castOther.getDataNo())
                // .append(this.getDataKind(), castOther.getDataKind())
                .isEquals();
    }
}
