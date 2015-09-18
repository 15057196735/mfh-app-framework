package com.mfh.comna.api.widgets;

/**
 * Created by Administrator on 2015/4/21.
 */
public class SettingsItemData {
    private int resId;
    private String title;
    private String detail;

    public SettingsItemData(int resId, String title, String detail){
        this.resId = resId;
        this.title = title;
        this.detail = detail;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
