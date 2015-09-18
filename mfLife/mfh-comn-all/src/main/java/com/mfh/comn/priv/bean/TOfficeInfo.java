/**
 * 
 * 注意参见PrivClientSession类中的changeOfficeValue方法,增加属性时注意同步修改。
 */
package com.mfh.comn.priv.bean;

import java.util.Date;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.mfh.comn.bean.IStringId;

@SuppressWarnings("serial")
public class TOfficeInfo implements IStringId {
    private String id;

    private String officeCode;

    private String officeName;

    private String createId;

    private Date createDate;

    private String editId;

    private Date editDate;
    
    private String tenantName;//租户信息，也即根单位编号
    
    public static String ROOT_INIT = "INIT";
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEditId() {
        return editId;
    }

    public void setEditId(String editId) {
        this.editId = editId;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
    
    public String getTenantName() {
        return tenantName;
    }
    
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    /**
     * @stereotype constructor
     */
    public TOfficeInfo(String officeCode, String officeName) {
        this.officeCode = officeCode;
        this.officeName = officeName;
    }

    public TOfficeInfo() {
    }

    public boolean equals(Object object) {
        if (!(object instanceof TOfficeInfo))
            return false;
        TOfficeInfo office = (TOfficeInfo) object;
        return new EqualsBuilder().appendSuper(super.equals(object)).append(this.id, office.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }
}
