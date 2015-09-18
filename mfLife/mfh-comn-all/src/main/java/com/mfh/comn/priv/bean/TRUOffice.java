/*
 * Created on 2006-11-13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mfh.comn.priv.bean;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.mfh.comn.bean.IIntId;

/**用户和单位关系实体
 * @author chenxd
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TRUOffice implements Serializable, IIntId{
	/**  */
    private static final long serialVersionUID = 6261128043922880887L;
    private String userid;
	private String officeid;
	private Integer id;
	
    public TRUOffice(String userid, String officeid){
        this.userid = userid;
        this.officeid = officeid;
    }
    public TRUOffice(){
    	
    }
    
    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getOfficeid() {
        return officeid;
    }
    
    public void setOfficeid(String officeid) {
        this.officeid = officeid;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("userid", getUserid())
            .append("officeid", getOfficeid())
            .toString();
    }

    @Override
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof TRUOffice) ) return false;
        TRUOffice castOther = (TRUOffice) other;
        return new EqualsBuilder()
            .append(this.getUserid(), castOther.getUserid())
            .append(this.getOfficeid(), castOther.getOfficeid())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserid())
            .append(getOfficeid())
            .toHashCode();
    }
    
    @Override
    public Integer getId() {
        return id;
    }
}
