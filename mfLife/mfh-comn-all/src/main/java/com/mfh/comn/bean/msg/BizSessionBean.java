package com.mfh.comn.bean.msg;

import com.mfh.comn.bean.ILongId;

/**
 * 针对业务需要的会话包装bean，只针对会话本身
 * 
 * @author zhangyz created on 2014-11-6
 */
@SuppressWarnings("serial")
public class BizSessionBean implements ILongId {//extends MfhEntity<Long>
    private Long id; //会话编号
    private Integer type;//会话类别，MsgConstant.SESSION_TYPE_GROUP
    private Integer sessionBizType;//会话的业务类型
    private Long lastupdate;//最后一条消息发生时间该会话中最后更新时刻,用于增量同步
    private Long sessionTagOne;//会话的业务属性,为空代表还是陌生用户，不是自己的会员客户

    protected String guid;    //创建者
    private String headimageurl;//创建人头像
    private String humanname;//会话创建人员姓名
    private String mobile = "";//会话创建人手机
    
    private Long unreadcount = 0L;//未读条数    

    //这部分属于消息部分，全部移除到另外的一个独立变量中
    /*private String content;//最后一条消息的内容 
    private String spokesman;//最后说话人名称    
    protected Date createtime;//最后一条消息的创建时间
    protected Long createUnixTime = 0L;
    protected String formatCreateTime;//最后一条消息格式化后的时间显示

    public String getSpokesman() {
        return spokesman;
    }

    public void setSpokesman(String spokesman) {
        this.spokesman = spokesman;
    }

    public Long getCreateUnixTime() {
        return createUnixTime;
    }

    public void setCreateUnixTime(Long createUnixTime) {
        this.createUnixTime = createUnixTime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        if (createtime != null) {
            setCreateUnixTime(createtime.getTime());
        }
        this.createtime = createtime;
    }

    public String getFormatCreateTime() {
        return formatCreateTime;
    }

    public void setFormatCreateTime(String formatCreateTime) {
        this.formatCreateTime = formatCreateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    */

    public Integer getSessionBizType() {
        return sessionBizType;
    }
    
    public void setSessionBizType(Integer sessionBizType) {
        this.sessionBizType = sessionBizType;
    }
    
    public Long getSessionTagOne() {
        return sessionTagOne;
    }
    
    public void setSessionTagOne(Long sessionTagOne) {
        this.sessionTagOne = sessionTagOne;
    }
    
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHumanname() {
        return humanname;
    }

    public void setHumanname(String humanname) {
        this.humanname = humanname;
    }

    public String getHeadimageurl() {
        return headimageurl;
    }

    public void setHeadimageurl(String headimageurl) {
        this.headimageurl = headimageurl;
    }

    public Long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
        
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public Long getUnreadcount() {
        return unreadcount;
    }

    public void setUnreadcount(Long unreadcount) {
        this.unreadcount = unreadcount;
    }

}
