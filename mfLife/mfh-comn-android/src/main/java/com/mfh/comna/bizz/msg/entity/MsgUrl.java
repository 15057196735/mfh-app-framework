package com.mfh.comna.bizz.msg.entity;

import com.mfh.comna.MfhEntity;
import com.mfh.comn.bean.ILongId;


/**
 * Created by 李潇阳 on 14-8-6.
 */
public class MsgUrl extends MfhEntity<Long> implements ILongId {

    /*消息会话的展示url*/
    private String sessionListUrl;

    /*消息会话的创建url*/
    private String sessionCreateUrl;

    /*消息会话的删除url*/
    private String sessionDeleteUrl;

    /*消息会话的修改Url*/
    private String sessionUpdateUrl;

    /*具体消息的展示Url*/
    private String msgListUrl;

    /*具体消息的创建Url*/
    private String msgCreateUrl;

    /*具体消息的删除的Url*/
    private String msgDeleteUrl;

    /*具体消息的修改Url*/
    private String msgUpdateUrl;

    /*多人对话展示通讯录url*/
    private String groupListUrl;

    /*多人对话添加url*/
    private String groupAddUrl;

    /*多人对话删除url*/
    private String groupDeleteUrl;

    public String getSessionListUrl() {
        return sessionListUrl;
    }

    public void setSessionListUrl(String sessionListUrl) {
        this.sessionListUrl = sessionListUrl;
    }

    public String getSessionCreateUrl() {
        return sessionCreateUrl;
    }

    public void setSessionCreateUrl(String sessionCreateUrl) {
        this.sessionCreateUrl = sessionCreateUrl;
    }

    public String getSessionDeleteUrl() {
        return sessionDeleteUrl;
    }

    public void setSessionDeleteUrl(String sessionDeleteUrl) {
        this.sessionDeleteUrl = sessionDeleteUrl;
    }

    public String getSessionUpdateUrl() {
        return sessionUpdateUrl;
    }

    public void setSessionUpdateUrl(String sessionUpdateUrl) {
        this.sessionUpdateUrl = sessionUpdateUrl;
    }

    public String getMsgListUrl() {
        return msgListUrl;
    }

    public void setMsgListUrl(String msgListUrl) {
        this.msgListUrl = msgListUrl;
    }

    public String getMsgCreateUrl() {
        return msgCreateUrl;
    }

    public void setMsgCreateUrl(String msgCreateUrl) {
        this.msgCreateUrl = msgCreateUrl;
    }

    public String getMsgDeleteUrl() {
        return msgDeleteUrl;
    }

    public void setMsgDeleteUrl(String msgDeleteUrl) {
        this.msgDeleteUrl = msgDeleteUrl;
    }

    public String getMsgUpdateUrl() {
        return msgUpdateUrl;
    }

    public void setMsgUpdateUrl(String msgUpdateUrl) {
        this.msgUpdateUrl = msgUpdateUrl;
    }

    public String getGroupListUrl() {
        return groupListUrl;
    }

    public void setGroupListUrl(String groupListUrl) {
        this.groupListUrl = groupListUrl;
    }

    public String getGroupAddUrl() {
        return groupAddUrl;
    }

    public void setGroupAddUrl(String groupAddUrl) {
        this.groupAddUrl = groupAddUrl;
    }

    public String getGroupDeleteUrl() {
        return groupDeleteUrl;
    }

    public void setGroupDeleteUrl(String groupDeleteUrl) {
        this.groupDeleteUrl = groupDeleteUrl;
    }
}
