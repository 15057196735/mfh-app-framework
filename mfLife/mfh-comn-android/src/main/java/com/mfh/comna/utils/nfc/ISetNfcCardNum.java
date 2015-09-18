package com.mfh.comna.utils.nfc;

/**
 * Nfc的接口类，由需要监听NFC事件的Activity来实现，用于根据获取的Token来设置IC卡号
 * @author yxm
 * @version 1.0
 */
public interface ISetNfcCardNum {
    public void setNfcCardNum(String Token);//设置IC卡号
}
