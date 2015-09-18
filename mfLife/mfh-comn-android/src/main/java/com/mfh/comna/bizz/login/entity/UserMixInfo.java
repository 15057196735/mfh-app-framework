/*
 * 文件名称: PmcUser.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-14
 * 修改内容: 
 */
package com.mfh.comna.bizz.login.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * 登录时返回的信息
 * @author zhangyz created on 2014-3-14
 */
@SuppressWarnings("serial")
public class UserMixInfo extends User {
    private UserAttribute userAttribute;//各类用户混合信息
    private String sessionId;//会话Id
    private Integer humanAuthFlag = 0;//认证标志 1-个人认证  2-企业认证  3-全是
    private List<UserComInfo> comInfos = null;//公司相关信息
    private List<String> cookiees; //仅返回给手机客户端的cookiees

    public UserAttribute getUserAttribute() {
        return userAttribute;
    }

    public void setUserAttribute(UserAttribute userAttribute) {
        this.userAttribute = userAttribute;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getCookiees() {
        return cookiees;
    }

    /**
     * 增加一个cookiee
     * @param cookiee
     * @author zhangyz created on 2015-1-18
     */
    public void addCookiess(String cookiee) {
        cookiees = new ArrayList<String>();
        cookiees.add(cookiee);
    }

    public void setCookiees(List<String> cookiees) {
        this.cookiees = cookiees;
    }




    public Integer getHumanAuthFlag() {
        return humanAuthFlag;
    }

    public void setHumanAuthFlag(Integer humanAuthFlag) {
        this.humanAuthFlag = humanAuthFlag;
    }

    public List<UserComInfo> getComInfos() {
        return comInfos;
    }

    public void setComInfos(List<UserComInfo> comInfos) {
        this.comInfos = comInfos;
    }

    /**
     * 增加一个公司
     * @param com
     * @author zhangyz created on 2015-1-21
     */
    public  void addComInfo(UserComInfo com) {
        if (comInfos == null)
            comInfos = new ArrayList<UserComInfo>();
        comInfos.add(com);
    }

}
