package com.mfh.comn.priv.bean;


public class RelOfficePk {
    private String officeId;
    private String subOfficeId;
    
    
    public RelOfficePk(String officeId, String subOfficeId) {
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

}
