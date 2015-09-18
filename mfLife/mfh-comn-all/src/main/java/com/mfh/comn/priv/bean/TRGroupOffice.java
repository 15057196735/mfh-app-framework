package com.mfh.comn.priv.bean;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mfh.comn.bean.IIntId;

/**
 * 单位角色关系定义表
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class TRGroupOffice implements Serializable, IIntId {
    /**  */
    private static final long serialVersionUID = 8606398094885046400L;
    private String groupid;
    private String officeid;
    private Integer id;

    /** full constructor */
    public TRGroupOffice(String groupid, String officeid) {
        this.groupid = groupid;
        this.officeid = officeid;
    }

    /** default constructor */
    public TRGroupOffice() {
    }
    
    public String getGroupid() {
        return groupid;
    }
    
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    
    public String getOfficeid() {
        return officeid;
    }

    public void setOfficeid(String officeid) {
        this.officeid = officeid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("groupid", getGroupid())
            .append("officeid", getOfficeid())
            .toString();
    }

    @Override
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof TRGroupOffice) ) return false;
        TRGroupOffice castOther = (TRGroupOffice) other;
        return new EqualsBuilder()
            .append(this.getGroupid(), castOther.getGroupid())
            .append(this.getOfficeid(), castOther.getOfficeid())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getGroupid())
            .append(getOfficeid())
            .toHashCode();
    }
    
    @Override
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
}
