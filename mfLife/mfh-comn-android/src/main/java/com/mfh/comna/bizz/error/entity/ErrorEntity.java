package com.mfh.comna.bizz.error.entity;

import com.mfh.comna.MfhEntity;
import com.mfh.comn.bean.ILongId;

/**错误收集实体类
 * Created by 李潇阳 on 2014/11/7.
 */
public class ErrorEntity extends MfhEntity<Long> implements ILongId {

    private String errorTime = "";//出错时间
    private String stackInformation = "";//堆栈信息
    private String hardwareInformation = "";//硬件版本信息
    private String androidLevel = "";//安卓版本号
    private String loginName = "";//登陆名
    private String softVersion = "";//程序的版本号
    private Integer isUpload = 0;//是否上传，0：没有，1已经上传

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }

    public String getStackInformation() {
        return stackInformation;
    }

    public void setStackInformation(String stackInformation) {
        this.stackInformation = stackInformation;
    }

    public String getHardwareInformation() {
        return hardwareInformation;
    }

    public void setHardwareInformation(String hardwareInformation) {
        this.hardwareInformation = hardwareInformation;
    }

    public String getAndroidLevel() {
        return androidLevel;
    }

    public void setAndroidLevel(String androidLevel) {
        this.androidLevel = androidLevel;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public Integer getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(Integer isUpload) {
        this.isUpload = isUpload;
    }
}
