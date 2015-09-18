package com.mfh.comna.bizz.netphone.entity;

import com.mfh.comna.MfhEntity;
import com.mfh.comn.bean.ILongId;

/**
 * Created by Administrator on 2014/9/22.
 * 网络电话账号信息
 */
public class NetPhoneInfo extends MfhEntity<Long> implements ILongId {

    private String subAccountSid;  //子账号
    private String voipAccount;    //voip账号
    private String friendlyName;  //
    private String voipPwd;
    private String subtoken;
    private String guid;
    private String channelkey;
    private String extparam;

    public String getSubAccountSid() {
        return subAccountSid;
    }

    public void setSubAccountSid(String subAccountSid) {
        this.subAccountSid = subAccountSid;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getVoipPwd() {
        return voipPwd;
    }

    public void setVoipPwd(String voipPwd) {
        this.voipPwd = voipPwd;
    }

    public String getSubtoken() {
        return subtoken;
    }

    public void setSubtoken(String subtoken) {
        this.subtoken = subtoken;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getChannelkey() {
        return channelkey;
    }

    public void setChannelkey(String channelkey) {
        this.channelkey = channelkey;
    }

    public String getExtparam() {
        return extparam;
    }

    public void setExtparam(String extparam) {
        this.extparam = extparam;
    }
}
