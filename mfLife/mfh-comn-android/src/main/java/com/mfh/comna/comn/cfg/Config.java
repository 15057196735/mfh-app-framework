package com.mfh.comna.comn.cfg;

import com.mfh.comna.MfhEntity;
import com.mfh.comn.net.data.IResponseData;

/**
 * Created by Administrator on 2015/1/28.
 */
public class Config extends MfhEntity<Integer> implements IResponseData {

    private String domain = "default";
    private String configItem = "default";
    private String configValue = "default";
    private String configCaption = "default";
    private String platform = "default";
    private String buildVersion = "default";

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getConfigItem() {
        return configItem;
    }

    public void setConfigItem(String configItem) {
        this.configItem = configItem;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigCaption() {
        return configCaption;
    }

    public void setConfigCaption(String configCaption) {
        this.configCaption = configCaption;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }
}
