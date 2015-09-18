/*
 * 文件名称: MsgParameterBean.java
 * 版权信息: Copyright 2013-2015 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2015-3-20
 * 修改内容: 
 */
package com.mfh.comn.bean.msg;

import com.mfh.comn.bean.msg.MsgParameter;

/**
 * 返回给客户端的针对消息的进一步封装，包括中文名、描述之类的
 * @author zhangyz created on 2015-3-20
 */
@SuppressWarnings("serial")
public final class MsgParameterBean extends MsgParameter {    
    
    public MsgParameterBean() {
        super();
    }

    public MsgParameterBean(MsgData msgBean) {
        super(msgBean);
    }

    /**
     * 构造函数
     * @param msgParam
     */
    public MsgParameterBean(MsgParameter msgParam) {
        super();
        this.setFrom(msgParam.getFrom());
        this.setTo(msgParam.getTo());
        this.setMsgBean(msgParam.getMsgBean());
        this.setMeta(msgParam.getMeta());
    }    
    
    private String headimageurl;//发送者头像
    private String spokesman; //发送者姓名
    private String formatCreateTime; //保存格式化后的时间显示
    protected Long createUnixTime = 0L;
    
    public Long getCreateUnixTime() {
        return createUnixTime;
    }

    public void setCreateUnixTime(Long createUnixTime) {
        this.createUnixTime = createUnixTime;
    }
    
    public String getHeadimageurl() {
        return headimageurl;
    }
    
    public void setHeadimageurl(String headimageurl) {
        this.headimageurl = headimageurl;
    }    
    
    public String getSpokesman() {
        return spokesman;
    }
    
    public void setSpokesman(String spokesman) {
        this.spokesman = spokesman;
    }

    public String getFormatCreateTime() {
        return formatCreateTime;
    }
    
    public void setFormatCreateTime(String formatCreateTime) {
        this.formatCreateTime = formatCreateTime;
    } 

}
