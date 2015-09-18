package com.mfh.comna.bizz.login.entity;

import java.util.Date;

import com.mfh.comna.MfhEntity;

/**
 * 用户类
 */
@SuppressWarnings("serial")
public class User extends MfhEntity<Long> {

    public static final int USERTYPE_SYS = 2;// 系统管理员

    public static final int USERTYPE_NOTSYS = 1;// 普通用户

    public static final int USERTYPE_TENANT = 3;//租户的系统管理员,与TUser.USERTYPE_SYS等区别;也就是sass平台的付费客户；但该客户本身还有自己的最终用户。

    public static String USER_SYS = "admin";// 系统管理员。

    private Long humanId;

    /**
     * 账户名/登录名
     */
    private String userName;

    /**
     * 电话号码
     */
    private String phonenumber;

    /**
     * 邮箱
     */
    private String email = "";

    /**
     * 加密的登录密码
     */
    private String password = "";

    /**
     * 加密的盐
     */
    private String salt;

    /**
     * 状态 1=激活，0=失效
     */
    private Integer status;

    /**
     * 用户类型
     */
    private Integer userType = USERTYPE_NOTSYS;

    //private String rids;

    //private List<Role> roleList = Lists.newArrayList(); // 有序的关联对象集合

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullName() {
        return this.userName;
    }

    public void setFullName(String name) {
        this.userName = name;
    }

    public String getLoginname() {
        return userName;
    }

    public void setLoginname(String loginname) {
        this.userName = loginname;
    }

    public Integer getUserType() {
        return this.userType;
    }

    public void setUserType(Integer type) {
        this.userType = type;
    }

    public void setCreateid(String createid) {
        this.createdBy = createid;
    }

    public String getCreateid() {
        return createdBy;
    }

    public Date getCreatedate() {
        return this.createdDate;
    }

    public void setCreatedate(Date createdate) {
        this.createdDate = createdate;
    }

    public String getTenantname() {
        return null;
    }

    public void setTenantname(String tenantname) {

    }

    public Long getHumanId() {
        return humanId;
    }

    public void setHumanId(Long humanId) {
        this.humanId = humanId;
    }
}
