/*
 * 文件名称: SqlliteUpgradeSupport.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-6-6
 * 修改内容: 
 */
package com.mfh.comna.comn.database.upgrade;

import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comn.Constants;
import com.mfh.comn.upgrade.UpgradeSupport;
import com.mfh.comn.upgrade.VersionInfo;
import com.mfh.comna.comn.database.dao.BaseDbDao;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.comn.database.upgrade.logic.VersionTableService;

import net.tsz.afinal.FinalDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 针对安卓sqllite的数据库升级
 * 不是采用jdbc的
 * @author zhangyz created on 2014-6-6
 */
public class SqlliteUpgradeSupport implements UpgradeSupport {
    private FinalDb db = null;
    private VersionTableService vtService = ServiceFactory.getService(VersionTableService.class);

    @Override
    public String getDsType() {
        return Constants.SQLLITE;
    }

    @Override
    public void init(String dsId) throws Exception {
        db = BaseDbDao.getDb();
    }

    @Override
    public InputStream getUpgradeSqlScriptsStream(String scriptRelativePath) {
        try {
            return AppHelper.getAm().open(scriptRelativePath);
        } catch (IOException e) {
            throw new RuntimeException("读取脚本文件失败:" + scriptRelativePath);
        }
    }

    @Override
    public boolean existTable() {
        return db.tableIsExist(tableVersionName);
    }

    @Override
    public void createVersionTable() throws Exception {
        String genSql = genCreateTableSql();
        db.exeSql(genSql);
    }

    /**
     * 生成建表sql语句
     * @return
     */
    private static String genCreateTableSql() {
        String creatTableSql = "CREATE TABLE " + tableVersionName + " (DOMAIN VARCHAR(64) NOT NULL,CURRENT_VERSION VARCHAR(64) NOT NULL,"
                + "LAST_VERSION VARCHAR(64) NOT NULL,UPGRAGE_DATE DATETIME NOT NULL,COMMENTS VARCHAR(256),PRIMARY KEY (DOMAIN))";
        
        return creatTableSql;
    }
    
    @Override
    public void upgradeVersion(List<String> sqls) throws Exception {
        for (String sqlItem : sqls) {
            db.exeSql(sqlItem);
        }
    }

    @Override
    public VersionInfo queryVersion(String tableDomain) {
        VersionInfo ret = vtService.getDao().getEntityById(tableDomain);
        if (ret != null && ret.getDomain() == null)//bug,因为domain是主键，但名字不是id
            ret.setDomain(tableDomain);
        return ret;
    }

    @Override
    public void updateVersion(VersionInfo versionInfo) {
        vtService.getDao().update(versionInfo);
    }

    @Override
    public void saveVersion(VersionInfo versionInfo) {
        vtService.getDao().saveOrUpdate(versionInfo);
    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }
}
