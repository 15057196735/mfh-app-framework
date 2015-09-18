package com.mfh.comna.bizz.msg.entity;

import com.alibaba.fastjson.JSONObject;
import com.mfh.comna.MfhEntity;
import com.mfh.comn.annotations.Table;
import com.mfh.comn.bean.IStringId;

/**
 * 消息表
 * Created by Administrator on 14-5-6.
 */
@Table(name="emb_msg")
public class EmbMsg extends MfhEntity<String> implements IStringId{
    //发送方
    private Long fromguid; //发送者逻辑编号，不能为空
    private Integer fromchannelid; //发送渠道编号
    private Integer fromChannelType; //发送渠道类型
    private String channelpointid;//发送者物理端点号

    //接收方
    private Long sessionid; //会话编号，关联会话表

    /**
     * 下面的都是EmbMsgBean里面的内容
     */
    private Long guid;//创建人guid
    private String techType; //消息技术类型
    private String param; //消息内容
    private Integer isRead = 0;//是否已读:0-未读  1-已读
    private Integer isdel = 0; //删除标志
    //private Integer unread = 0; //是否未读，0-未读  1-已读
    private String tagOne;//消息标签1，备用
    private String tagTwo; //消息标签2，备用
    private String tagThree; //消息标签3，备用
    private Long extparam;//额外业务关联信息
    private Integer bind ;//0:未绑定 1:绑定
    private String localheadimageurl;//头像
    private String pointName; //端点名 可空 对应point_type=1有用
    private String msgInfo = getMsgInfoFromParam();//具体的消息内容，json格式

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getParam() {
        return param;
    }


    public void setParam(String param) {
        this.param = param;
    }


    public Long getFromguid() {
        return fromguid;
    }


    public void setFromguid(Long fromguid) {
        this.fromguid = fromguid;
    }


    public String getTechType() {
        return techType;
    }


    public void setTechType(String techType) {
        this.techType = techType;
    }


    public Long getSessionid() {
        return sessionid;
    }


    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }


    public Integer getIsdel() {
        return isdel;
    }


    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }


    public Integer getFromchannelid() {
        return fromchannelid;
    }


    public void setFromchannelid(Integer fromchannelid) {
        this.fromchannelid = fromchannelid;
    }


    public String getChannelpointid() {
        return channelpointid;
    }


    public void setChannelpointid(String channelpointid) {
        this.channelpointid = channelpointid;
    }


    public String getTagOne() {
        return tagOne;
    }


    public void setTagOne(String tagOne) {
        this.tagOne = tagOne;
    }


    public String getTagTwo() {
        return tagTwo;
    }


    public void setTagTwo(String tagTwo) {
        this.tagTwo = tagTwo;
    }


    public String getTagThree() {
        return tagThree;
    }


    public void setTagThree(String tagThree) {
        this.tagThree = tagThree;
    }

    /** 保存格式化后的时间显示 */
    private String formatCreateTime;

    public Long getExtparam() {
        return extparam;
    }

    public void setExtparam(Long extparam) {
        this.extparam = extparam;
    }

    public Integer getBind() {
        return bind;
    }

    public void setBind(Integer bind) {
        this.bind = bind;
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public String getLocalheadimageurl() {
        return localheadimageurl;
    }

    public void setLocalheadimageurl(String localheadimageurl) {
        this.localheadimageurl = localheadimageurl;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getFormatCreateTime() {
        return formatCreateTime;
    }

    public void setFormatCreateTime(String formatCreateTime) {
        this.formatCreateTime = formatCreateTime;
    }

    public String getMsgInfo() {
        if (param != null) {
            return getMsgInfoFromParam();
        }
        return msgInfo;
    }

    private String getMsgInfoFromParam() {
        if (param != null) {
            JSONObject jsonObject = JSONObject.parseObject(param);
//            JSONObject msgBean = jsonObject.getJSONObject("msgBean");
//            JSONObject msgBody = jsonObject.getJSONObject("body");
            if(jsonObject != null){
                return JSONObject.toJSONString(jsonObject);
            }
        }
        return null;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

   /* public static int MSG_TYPE_SYSTEM = 0;//系统消息
    public static int MSG_TYPE_USER = 1;//非系统消息
    public static int MSG_TYPE_TIP = 2;//客户端插入的提示消息

    private Long sessionId;
    private String formatCreateTime;
    private Integer isRead = 0;//是否已读
    private String remark;
    private String headImageUrl;
    private String nickName;
    private String humanName;
    private String signName; //消息签名(如\\n-------------------------\\n 未知-未知 天平花园物业\)
    private String guid;//说话人通讯标识
    private Integer msgType = MSG_TYPE_USER;//消息类型//0 系统消息  1非系统消息; 2、客户端插入的提示消息
    private String ownerId;

    private String msgInfo;//具体的消息内容，json格式
    //private String mediaType;//发言的消息媒体类型：text/image/tuwen
    //private String msgContent;//消息内容

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getFormatCreateTime() {
        return formatCreateTime;
    }

    public void setFormatCreateTime(String formatCreateTime) {
        this.formatCreateTime = formatCreateTime;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signname) {
        this.signName = signname;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }*/

    /**
     * 获取消息Id（bug补丁）
     * @return
     */
   /* public Long getMsgId() {
        Object objId = id;
        if (objId instanceof String)
            return Long.parseLong(objId.toString());
        else
           return (Long)objId;
    }*/
}
