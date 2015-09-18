package com.mfh.comn.priv.bean;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mfh.comn.annotations.NoColumn;
import com.mfh.comn.bean.IStringId;
/**
 * 系统用户对象
 * 
 * @author zhangyz created on 2013-5-9
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class TUser implements IUser, IStringId {
    public static final String USERTYPE_SYS = "2";//系统管理员
	public static final String USERTYPE_NOTSYS = "1";//普通用户    
    public static final String USERTYPE_TENANT = "3";//系统租户,与TUser.USERTYPE_SYS等区别;也就是sass平台的付费客户；但该客户本身还有自己的最终用户。
    
    public static String USER_SYS = "sys";//系统管理员。
    
    /** identifier field */
	protected String id;

    /** persistent field */
	protected String loginname;

    /** persistent field */
	protected String firstname;

    /** persistent field */
	protected String lastname;
    
    /** persistent field */
    protected String password;
    
    /** nullable persistent field */
    protected Date createdate;

    /** nullable persistent field */
    protected String createid;

    /** nullable persistent field */
    protected Date editdate;

    /** nullable persistent field */
    protected String editid;

    /** persistent field */
    protected String musteditpass = "0";

    /** persistent field */
    protected String noeditpass ="0";

    /** persistent field */
    protected int state = 1;

    /** persistent field */
    protected String type = USERTYPE_NOTSYS;	//1、非系统管理员,2、系统管理员。基本不会用到，兼容老版本。

    protected String tenantname;
    
    private String letterIndex; //  首字母索引
    
    //头像信息放在本地文件，以ID串命名。
    
    /** full constructor */
    public TUser(String firstname,String lastname,Date createdate, 
    		String createid, Date editdate, String editid,String loginname,
			String musteditpass, String noeditpass, String password,int state, 
			String type) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.createdate = createdate;
        this.createid = createid;
        this.editdate = editdate;
        this.editid = editid;
        this.loginname = loginname;
        this.musteditpass = musteditpass;
        this.noeditpass = noeditpass;
        this.password = password;
        this.state = state;
        this.type = type;
        //this.id = UuidUtil.getUuid();
    }

    public TUser(String userId) {
    	this.state = 1;
    	this.type = USERTYPE_NOTSYS;
    	this.musteditpass = "0";
        this.noeditpass = "0";
        this.id = userId;
    }
    
    /** default constructor */
    public TUser() {
    	this.state = 1;
    	this.type = USERTYPE_NOTSYS;
    	this.musteditpass = "0";
        this.noeditpass = "0";
    }

    /** common constructor */
    public TUser(String loginname,String password,String firstname, String lastname,String type) {
        this.loginname = loginname;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.type = type;
        this.state = 1;
    	this.musteditpass = "0";
        this.noeditpass = "0";
    }

    /** minimal constructor */
    public TUser(String loginname,String password) {
        this.loginname = loginname;
        this.password = password;
        this.state = 1;
    	this.type = USERTYPE_NOTSYS;
    	this.musteditpass = "0";
        this.noeditpass = "0";
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    /*public void setId(Serializable id) {
        this.id = (String)id;
    }*/
    
    /**
     * 得到该用户的全名,姓在前,名在后.若为空，则返回登录名。
     * @return String;
     */
    @NoColumn
    public String getFullName(){
        String fullName = "";
    	if(firstname != null){
    	    fullName = firstname;
    	}
    	if(lastname != null)
    	    fullName = fullName + lastname;
    	if(fullName.equals(""))
    	    fullName = loginname;
    	return fullName;
    }

    @NoColumn
    @Override
    public void setFullName(String name){
        ;
    }
    
    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public Date getCreatedate() {
        return this.createdate;
    }

    @Override
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @Override
    public String getLoginname() {
        return this.loginname;
    }

    @Override
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    @Override
    public void setCreateid(String createid) {
        this.createid = createid;
    }

    @Override
    public String getCreateid() {
        return this.createid;
    }
    
    public Date getEditdate() {
        return this.editdate;
    }

    public void setEditdate(Date editdate) {
        this.editdate = editdate;
    }

    public String getEditid() {
        return this.editid;
    }

    public void setEditid(String editid) {
        this.editid = editid;
    }

    //为兼容旧版的数据库，保留，不提倡使用
    public String getMusteditpass() {
        return this.musteditpass;
    }

    public void setMusteditpass(String musteditpass) {
        this.musteditpass = musteditpass;
    }

    public String getNoeditpass() {
        return this.noeditpass;
    }

    public void setNoeditpass(String noeditpass) {
        this.noeditpass = noeditpass;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        if (StringUtils.isBlank(type))
            return;//加的补丁，客户端没有设置
        this.type = type;
    }
    	
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj==this)
            return true;
    	if(!(obj instanceof TUser))
    		return false;
    	TUser user = (TUser)obj;
    	return new EqualsBuilder().append(user.getId(),this.getId()).isEquals();
    }   
    
    public String getTenantname() {
        return tenantname;
    }
    
    public void setTenantname(String tenantname) {
        this.tenantname = tenantname;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

    //用于修改密码的场合。
    @NoColumn
	public String getPasswordOld() {
		return null;//password;
	}

    @NoColumn
	public void setPasswordOld(String passwordOld) {
		//this.password = passwordOld;
	}

    @NoColumn
    public String getPasswordNew(){
    	return null;
    }

    @NoColumn
    public void setPasswordNew(String newWord){
    	;
    }
    
    public String getLetterIndex() {
        return letterIndex;
    }
    
    public void setLetterIndex(String letterIndex) {
        this.letterIndex = letterIndex;
    }
    
    /**
     * 对象克隆的方法，上面要继承C
     */
   /* public Object clone()
    {
    	try{
    		//sava the object into a byte arr
    		ByteArrayOutputStream bout = new ByteArrayOutputStream();
    		ObjectOutputStream out = new ObjectOutputStream(bout);
    		out.writeObject(this);
    		out.close();
    		
    		//read a cloned object form the byte arr above
    		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
    		ObjectInputStream in = new ObjectInputStream(bin);
    		Object obj = in.readObject();
    		in.close();
    		
			return obj;
    	}catch(Exception e){
    		return null;
    	}
    }
*/
}
