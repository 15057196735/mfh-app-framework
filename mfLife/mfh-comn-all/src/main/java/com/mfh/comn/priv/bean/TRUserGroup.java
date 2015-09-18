package com.mfh.comn.priv.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.mfh.comn.bean.IIntId;

/**
 * 用户角色关系定义表
 * 
 * @author zhangyz created on 2013-6-12
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class TRUserGroup implements IIntId {
    private String groupid;//角色编号
    private String userid;//用户编号
    private String remark;//备注
    private Integer id;//自增主键
    
    /** full constructor */
    public TRUserGroup(String groupid, String userid) {
        this.groupid = groupid;
        this.userid = userid;
    }

    /** default constructor */
    public TRUserGroup() {
    }
    
    public String getGroupid() {
        return groupid;
    }
    
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    
    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("userid", this.getUserid())
            .append("groupid", this.getGroupid())
            .toString();
    }

    @Override
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof TRUserGroup) ) return false;
        TRUserGroup castOther = (TRUserGroup) other;
        return new EqualsBuilder()
            .append(this.getUserid(), castOther.getUserid())
            .append(this.getGroupid(), castOther.getGroupid())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserid())
            .append(getGroupid())
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
