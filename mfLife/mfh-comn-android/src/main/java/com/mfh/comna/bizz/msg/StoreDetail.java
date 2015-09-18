package com.mfh.comna.bizz.msg;

/**
 * 店铺详情
 * Created by Administrator on 2015/5/14.
 *
 */
public class StoreDetail implements java.io.Serializable{
    private String nickname;//昵称
    private String name;//商家名称
    private String telphone;//客服电话
    private String description;//服务项目
    private String address;//地理位置
    private String latitude;//维度
    private String longitude;//经度
    private String url;//网上店铺
    private String starLevel;//星级

    public StoreDetail(){

    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }
}
