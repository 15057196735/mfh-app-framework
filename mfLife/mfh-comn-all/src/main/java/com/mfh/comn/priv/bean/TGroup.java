package com.mfh.comn.priv.bean;

import java.util.Date;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mfh.comn.annotations.Column;
import com.mfh.comn.bean.IStringId;

/**
 * 角色定义类
 * 
 * @author zhangyz created on 2012-7-1
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class TGroup implements IStringId {
    private String id;

    /** nullable persistent field */
    private Date createdate;

    /** nullable persistent field */
    private String createid;

    /** nullable persistent field */
    private Date updateDate;

    /** nullable persistent field */
    private String updatorId;

    /** nullable persistent field */
    private String gdesc;

    /** persistent field */
    private String gname;

    /** persistent field */
    private int gstate = 1;

    /** 为null代表这个角色对所有租户都适用 */
    private String tenantName;

    /*private Set modules;

    private Set users;

    private Set privs;*/

    private String pgroupid;

    /** full constructor */
    public TGroup(Date createdate, String createid, Date editdate, String editid, String gdesc, String gname,
            int gstate, String pgroupid) {
        this.createdate = createdate;
        this.createid = createid;
        this.updateDate = editdate;
        this.updatorId = editid;
        this.gdesc = gdesc;
        this.gname = gname;
        this.gstate = gstate;
        this.pgroupid = pgroupid;
    }

    /** default constructor */
    public TGroup() {
        this.gstate = 1;
    }

    /** common constructor */
    public TGroup(String gname) {
        this.gstate = 1;
        this.gname = gname;
    }

    /** minimal constructor */
    public TGroup(String gname, int gstate) {
        this.gname = gname;
        this.gstate = gstate;
    }
    
    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String groupId) {
        this.id = groupId;
    }
    
    public Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreateid() {
        return this.createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    @Column(name="EDITDATE")
    public Date getUpdateDate() {
        return this.updateDate;
    }

    @Column(name="EDITDATE")
    public void setUpdateDate(Date editdate) {
        this.updateDate = editdate;
    }

    @Column(name="EDITID")
    public String getUpdatorId() {
        return this.updatorId;
    }

    @Column(name="EDITID")
    public void setUpdatorId(String editid) {
        this.updatorId = editid;
    }

    public String getGdesc() {
        return this.gdesc;
    }

    public void setGdesc(String gdesc) {
        this.gdesc = gdesc;
    }

    public String getGname() {
        return this.gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getGstate() {
        return this.gstate;
    }

    public void setGstate(int gstate) {
        this.gstate = gstate;
    }

    public String getPgroupid() {
        return pgroupid;
    }

    public void setPgroupid(String pgroupid) {
        this.pgroupid = pgroupid;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TGroup))
            return false;
        TGroup group = (TGroup) obj;

        return new EqualsBuilder().append(group.getId(), this.getId()).isEquals();
        // 考虑到ID是存入数据库后生成的，新建的对象不一定会存在，而gname是设定为唯一的，所以用gname而不用id
        // return(group.getGname().equals(this.gname));
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
}
