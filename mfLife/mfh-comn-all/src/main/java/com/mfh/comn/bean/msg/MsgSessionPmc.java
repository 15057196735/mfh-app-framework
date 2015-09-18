/*
 * 文件名称: MsgSessionBase.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-11-6
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

/**
 * 带有物业客服会话特定场景下的会话bean，需要在会话条目中额外返回一些特点信息
 * 
 * @author zhangyz created on 2014-11-6
 */
@SuppressWarnings("serial")
public final class MsgSessionPmc extends BizSessionBean {
    
    //物业特有的
    private Long subdisid;//创建人小区ID
    private String subdisName;//创建人小区名称 
    private String addrvalsbind;//创建人绑定后的公寓短地址

    public Long getSubdisid() {
        return subdisid;
    }

    public void setSubdisid(Long subdisid) {
        this.subdisid = subdisid;
    }
    public String getAddrvalsbind() {
        return addrvalsbind;
    }

    public void setAddrvalsbind(String addrvalsbind) {
        this.addrvalsbind = addrvalsbind;
    }
    
    public String getSubdisName() {
        return subdisName;
    }
    
    public void setSubdisName(String subdisName) {
        this.subdisName = subdisName;
    }
    
    /*private Long addrvalid;    
    public Long getAddrvalid() {
        return addrvalid;
    }

    public void setAddrvalid(Long addrvalid) {
        this.addrvalid = addrvalid;
    }*/
    

    /*private String fromguid = "";  //最后说话人
    private Integer fromckId = null; //最后说话人渠道编号
    private String fromcpid = ""; 
    
    public String getFromguid() {
        return fromguid;
    }

    public void setFromguid(String fromguid) {
        this.fromguid = fromguid;
    }
    

    public Integer getFromckId() {
        return fromckId;
    }
    
    public void setFromckId(Integer fromckId) {
        this.fromckId = fromckId;
    }

    public String getFromcpid() {
        return fromcpid;
    }

    public void setFromcpid(String fromcpid) {
        this.fromcpid = fromcpid;
    }*/
    /*    
    private Long humanid;//会话创建人
    public Long getHumanid() {
        return humanid;
    }

    public void setHumanid(Long humanid) {
        this.humanid = humanid;
    }
    private String param;//最后消息的json内容
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
    
    private String localheadimageurl;
    public String getLocalheadimageurl() {
        return localheadimageurl;
    }

    public void setLocalheadimageurl(String localheadimageurl) {
        this.localheadimageurl = localheadimageurl;
    }
    private Long sessionid;// 会话编号
    public Long getSessionid() {
        return sessionid;
    }

    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }
    private Integer regstatus;//微信用户的绑定状态 
    public Integer getRegstatus() {
        return regstatus;
    }

    public void setRegstatus(Integer regstatus) {
        this.regstatus = regstatus;
    }*/

}
