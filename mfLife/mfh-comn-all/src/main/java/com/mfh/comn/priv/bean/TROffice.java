package com.mfh.comn.priv.bean;
import com.mfh.comn.bean.IIntId;

/**
 * 单位关系dao
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class TROffice implements IIntId {
    private Integer id;
    private String officeId;
    private String subOfficeId;
    private String relationType;
    
    public TROffice() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TROffice(String officeId, String subOfficeId) {
        super();
        this.officeId = officeId;
        this.subOfficeId = subOfficeId;
    }

    public String getOfficeId() {
        return officeId;
    }
    
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
    
    public String getSubOfficeId() {
        return subOfficeId;
    }
    
    public void setSubOfficeId(String subOfficeId) {
        this.subOfficeId = subOfficeId;
    }
    
    public String getRelationType() {
        return relationType;
    }
    
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
}
