package com.mfh.comn.priv.bean;

import java.util.Date;


public interface IUser {
    public static final String USERTYPE_SYS = "2";//系统管理员
    public static final String USERTYPE_NOTSYS = "1";//非系统管理员
    
    public String getId();//获取用户标识
    public void setId(String id);
    public String getFullName();//获取用户姓名
    public void setFullName(String name);
    public String getLoginname();//获取登录名
    public void setLoginname(String loginname);
    public String getType();//获取用户类型，USERTYPE_SYS or USERTYPE_NOTSYS
    public void setType(String type);
    public String getPassword();//用户密码
    public void setPassword(String password) ;
    public int getState();//用户状态
    public void setState(int state);
    
    public void setCreateid(String createid);
    public String getCreateid();

    public Date getCreatedate();
    public void setCreatedate(Date createdate);
    
    /**
     * 用于多租户
     * @return
     * @author zhangyz created on 2012-12-28
     */
    public String getTenantname();    
    public void setTenantname(String tenantname) ;
}
