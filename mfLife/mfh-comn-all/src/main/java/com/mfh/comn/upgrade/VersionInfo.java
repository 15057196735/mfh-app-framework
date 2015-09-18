/*
 * 文件名称: VersionInfo.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-14
 * 修改内容: 
 */
package com.mfh.comn.upgrade;

import java.io.Serializable;
import java.util.Date;

import com.mfh.comn.annotations.Column;
import com.mfh.comn.annotations.Id;
import com.mfh.comn.bean.IStringId;

/**
 * 版本信息
 * @author zhangyz
 * @since SHK BMP 1.0
 */
public class VersionInfo implements Serializable, IStringId {

    /** serialVersionUID */
    private static final long serialVersionUID = -5140130812690754214L;

    /** 子系统所属域定义 */
    @Id(caption = "应用域")
    private String domain;

    /** 子系统当前版本号 */
    @Column(name="CURRENT_VERSION")
    private String currentVersion;

    /** 子系统前一版本号 */
    @Column(name="LAST_VERSION")
    private String lastVersion;

    /** 最后一次升级时间 */
    @Column(name="UPGRAGE_DATE")
    private Date upgradeDate;

    /** 描述信息 */
    private String comments;

    /** 默认构造函数 */
    public VersionInfo() {
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("VersionInfo{")
                .append("  domain=").append(domain)
                .append(", currentVersion=").append(currentVersion)
                .append(", lastVersion=").append(lastVersion)
                .append(", upgradeDate=").append(upgradeDate)
                .append(", comment=").append(comments)
                .append('}');
        return sb.toString();
    }

    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public Date getUpgradeDate() {
        return upgradeDate;
    }

    public void setUpgradeDate(Date upgradeDate) {
        this.upgradeDate = upgradeDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String getId() {
        return domain;
    }

    //@Override
    public void setId(String id) {
        domain = id;        
    }

}
