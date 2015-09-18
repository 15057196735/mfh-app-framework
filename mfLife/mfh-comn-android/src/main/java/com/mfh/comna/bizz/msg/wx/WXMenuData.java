package com.mfh.comna.bizz.msg.wx;

import java.util.List;

/**
 * Created by Administrator on 2015/5/12.
 */
public class WXMenuData {
    private String name;
    private String url;
    private List<WXMenuData> subList = null;

    public WXMenuData(){

    }

    public WXMenuData(String name, String url, List<WXMenuData> subList){
        this.name = name;
        this.url = url;
        this.subList = subList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WXMenuData> getSubList() {
        return subList;
    }

    public void setSubList(List<WXMenuData> subList) {
        this.subList = subList;
    }
}
