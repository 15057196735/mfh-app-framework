/*
 * 文件名称: UpgradeConfigParseHelper.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-22
 * 修改内容: 
 */
package com.mfh.comn.upgrade;

import com.mfh.comn.config.UConfig;
import com.mfh.comn.utils.W3cDomUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * 升级配置解析助手类
 * @since SHK BMP 1.0
 */
public class UpgradeConfigParseHelper {
    
    /** 日志记录器 */
    private Logger logger = LoggerFactory.getLogger(UpgradeConfigParseHelper.class);
    private List<UpgradeConfigInfo> upgradeConfigInfos = null;

    /** 私有构造函数, 防止实例化 */
    public UpgradeConfigParseHelper() {
        
    }
    
    /**
     * 执行数据库升级
     * @param updateVersionOnly 可空，若为true代表只更新版本号本上
     * @author zhangyz created on 2014-3-21
     */
    public void doDbUpdate(UConfig uc, UpgradeSupport support, Boolean... updateVersionOnly) {
        if (uc == null)
            return;
        /**读取升级配置*/
        List<UpgradeConfigInfo> uciList = getUpgradeConfigInfos(uc);
        DbUpgrade ddu = null;
        String className = null;
        for (UpgradeConfigInfo uci : uciList) {
            className = uci.getClassName();
            if (StringUtils.isBlank(className)){
                ddu = new BaseDbUpgrade();
            }
            else{
                try {
                    ddu = (DbUpgrade)Class.forName(className).newInstance();
                }
                catch (Exception e) {
                    throw new RuntimeException("指定的数据库升级实现类:" + className + "不存在!");
                }
            }
            ddu.init(uci, support);

            if (updateVersionOnly != null && updateVersionOnly.length > 0
                    && updateVersionOnly[0]) {
                ddu.directToNewVersion();
            }
            else {
                ddu.upgrade();
            }
        }
    }

    /**
     * 获取当前的数据库版本
     * @param uc
     * @return
     */
    public int getNewVersion(UConfig uc) {
        List<UpgradeConfigInfo> uciList = getUpgradeConfigInfos(uc);
        if (uciList == null || uciList.size() == 0)
            return 0;
        return uciList.get(0).getVersin();
    }

    /**
     * 获取升级配置信息
     * @return 升级配置信息
     * @throws Exception
     * @author LuoJingtian created on 2011-12-22 
     * @since SHK BMP 1.0
     */
    public List<UpgradeConfigInfo> getUpgradeConfigInfos(UConfig uc) {
        if (upgradeConfigInfos == null) {
            upgradeConfigInfos = new ArrayList<>();
            try {
                Document doc = uc.getMergeDocument();
                NodeList nodeList = doc.getDocumentElement().getElementsByTagName("upgrade-config");
                UpgradeConfigInfo upgradeConfigInfo;
                for (int i=0; i< nodeList.getLength(); i++) {
                    upgradeConfigInfo = new UpgradeConfigInfo();

                    String domain = W3cDomUtils.getStringValueByXPath(nodeList.item(i), "domain");
                    upgradeConfigInfo.setDomain(domain);
                    upgradeConfigInfo.setDescription(W3cDomUtils.getStringValueByXPath(nodeList.item(i), "description"));

                    Integer version = DbVersion.getDomainVersion(domain);
                    if (version == null) {
                        version = Integer.parseInt(W3cDomUtils.getStringValueByXPath(nodeList.item(i), "version"));
                    }
                    upgradeConfigInfo.setVersin(version);

                    upgradeConfigInfo.setScriptFilePath(W3cDomUtils.getStringValueByXPath(nodeList.item(i), "script-file-path"));
                    upgradeConfigInfo.setScriptFilePrefix(W3cDomUtils.getStringValueByXPath(nodeList.item(i), "script-file-prefix"));
                    String dsId = W3cDomUtils.getStringValueByXPath(nodeList.item(i), "datasource-id");
                    upgradeConfigInfo.setDataSourceId(dsId);
                    upgradeConfigInfo.setClassName(W3cDomUtils.getStringValueByXPath(nodeList.item(i), "className"));
                    upgradeConfigInfos.add(upgradeConfigInfo);
                }
            }
            catch (Exception e) {
                logger.error("读取升级配置信息失败", e);
                throw new RuntimeException(e);
            }
        }
        return upgradeConfigInfos;
    }
}
