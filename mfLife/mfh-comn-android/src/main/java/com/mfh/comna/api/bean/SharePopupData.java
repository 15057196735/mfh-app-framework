package com.mfh.comna.api.bean;

/**
 * Created by Administrator on 2015/5/12.
 */
public class SharePopupData {
    public static final int TAG_SHARE   = 0x01;
    public static final int TAG_HOME    = 0X02;

    private String name;
    private int resId;
    private int tag;

    public SharePopupData(){

    }

    public SharePopupData(String name, int resId, int tag){
        this.name = name;
        this.resId = resId;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}
