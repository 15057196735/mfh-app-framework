/*
 * 文件名称: DefaultDbUpgrade.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-24
 * 修改内容: 
 */
package com.mfh.comn.upgrade;

import com.mfh.comn.Constants;
import com.mfh.comn.utils.IOUtils;
import com.mfh.comn.utils.PathUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 抽象的数据库升级实现
 * 注意实际的数据库可能是jdbc关系数据库，也可能是其他数据存储实现。
 * @since SHK BMP 1.0
 */
public class BaseDbUpgrade implements DbUpgrade {

    private UpgradeSupport upgradeSupport = null;

    /** 数据库升级配置信息 */
    protected UpgradeConfigInfo upgradeConfigInfo;

    /** 日志记录器 */
    protected Logger logger = LoggerFactory.getLogger(BaseDbUpgrade.class);

    @Override
    public void init(UpgradeConfigInfo uci, UpgradeSupport support) {
        this.upgradeConfigInfo = uci;
        this.upgradeSupport = support;
        try {
            upgradeSupport.init(uci.getDataSourceId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造函数
     */
    public BaseDbUpgrade() {
        
    }
    
    /**
     * 获取升级的实际数据源
     * @return
     * @author zhangyz created on 2013-6-20
     */
    protected String getUpgradeDsName() {
        return upgradeConfigInfo.getDataSourceId();
    }
    
    /**
     * 获取一个升级文件读取器
     */
    @Override
    public BufferedReader getUpgradeSqlScriptsReader(int version) {
        BufferedReader br = null;
        InputStream isSql = null;        
        String scriptRelativePath = getScriptRelativePath(version);
        try {
            isSql = upgradeSupport.getUpgradeSqlScriptsStream(scriptRelativePath);
            br = new BufferedReader(new InputStreamReader(isSql, Constants.defaultCode));
            return br;
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(scriptRelativePath + "升级脚本文件编码错误:", e);
        }
    }

    /**
     * 获取指定版本的数据库升级脚本的相对路径
     * @param version 版本
     * @return 数据库升级脚本的相对路径
     * @author LuoJingtian created on 2012-1-18 
     * @since SHK BMP 1.0
     */
    private String getScriptRelativePath(int version) {
        String scriptFilePath = upgradeConfigInfo.getScriptFilePath();
        String scriptFilePrefix = upgradeConfigInfo.getScriptFilePrefix();
        scriptFilePath = PathUtils.appendEndFileSeparator(scriptFilePath);
        
        String dsType = this.upgradeSupport.getDsType();//根据数据源Id得到数据源的类型，以拼出升级文件目录
        if (StringUtils.isNotBlank(dsType)) {
            scriptFilePath = scriptFilePath + dsType + "/";
        } else {
            throw new RuntimeException("找不到升级脚本目录，数据源ID为:"+upgradeConfigInfo.getDataSourceId() + ", 数据源类型为:" + dsType);
        }
        
        String filePath = scriptFilePath + scriptFilePrefix + "_" + version + "_" + (version + 1) + ".sql";
        return filePath;
    }
    
    /**
     * 某版本升级前执行的方法
     * @return true:成功执行了升级前执行的方法，且需要升级
     * false:成功执行了升级前执行的方法，且不需要升级。便于子类做些检测
     * 
     * @author zhangyz created on 2012-11-3
     */
    protected boolean doBeforeUpdateVersion(int curVersion){
        return true;
    }
    
    /**
     * 某版本升级后执行的方法
     * 
     * @author zhangyz created on 2012-11-3
     */
    protected void doAfterUpdateVersion(int curVersion){
        
    }

    @Override
    public void directToNewVersion() {
        try {
            int newVersion = upgradeConfigInfo.getVersin();
            if (!upgradeSupport.existTable()) {
                // 创建表
                upgradeSupport.createVersionTable();
            }
            updateVersion(newVersion);
        }
        catch (Exception e) {
            rollbackQuietly();
            logger.error("执行数据库版本初始化失败:" + e.getMessage(), e);
            throw new RuntimeException("执行数据库版本初始化." + upgradeConfigInfo.getDomain(), e);
        }
        finally {
            upgradeSupport.close();
        }
    }

    /**
     * 升级主方法，sql语句以分号分隔，不需要创建T_COM_VERSION的sql
     */
    @Override
    public void upgrade() {
        try {
            int newVersion = upgradeConfigInfo.getVersin();
            int curVersion = 0;
            if (upgradeSupport.existTable()) {
                curVersion = queryCurrentVersion();
            }
            else {
                // 创建表
                upgradeSupport.createVersionTable();
            }

            while(curVersion < newVersion) {
                if (doBeforeUpdateVersion(curVersion)) {
                    upgradeVersion(curVersion);
                    upgradeSupport.commit();//先提交，否则后面可能的逻辑因为查询数据库可能会造成等待死锁。缺点是可能产生不一致。
                    logger.info("---" + upgradeConfigInfo.getDomain() + "数据库由" + (curVersion) + "_" + (curVersion + 1) + "升级成功");
                    
                    doAfterUpdateVersion(curVersion);
                    upgradeSupport.commit();
                }
                
                if (curVersion == 0)// 某个升级文件的第一次升级
                    insertFirstVersion();
                else 
                    updateVersion(curVersion + 1);
                
                curVersion++;
            }
        }
        catch (Exception e) {
            rollbackQuietly();
            logger.error("执行数据库升级失败:" + e.getMessage(), e);
            throw new RuntimeException("执行数据库升级失败." + upgradeConfigInfo.getDomain(), e);
        }
        finally {            
            upgradeSupport.close();
        }
    }
    
    /**
     * 判断当前Sql是否为建版本控制表，true表示是，false表示否
     * @param sql 要进行判断的sql
     * @return
     * <ul>
     * <li>true:是建版本控制表语句</li>
     * <li>false:不是建版本控制表语句</li>
     * </ul>
     * @author huangwb created on 2012-1-12 
     * @since SHK BMP 1.0
     */
    private boolean isCreateVersionTableSql(String sql) {
        if (StringUtils.isNotBlank(sql)) {
            if (sql.toUpperCase().contains("CREATE TABLE T_COM_VERSION")) {
                return true;
            }
        } 
        return false;
    }

    /**
     * 1.升级一个版本
     * 2.修改版本信息
     * 3.提示:如果升级成功,而修改版本不成功,这时会造成,下次升级出错
     * 
     * @param version
     * @return
     * @throws Exception
     * @author huangwb created on 2012-1-4 
     * @since SHK BMP 1.0
     */
    private void upgradeVersion(int version) throws Exception {
        BufferedReader reader = null;
        String sql = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = getUpgradeSqlScriptsReader(version);
            List<String> sqls = new ArrayList<>();
            sql = readSql(reader);
            
            while (StringUtils.isNotBlank(sql)) {
                if (isCreateVersionTableSql(sql)) {//忽略创建版本表的sql
                    // 创建版本表的sql,不做处理
                }
                else {
                    logger.info(sql);
                    sqls.add(sql);
                    sb.append(sql).append(";");
                }
                sql = readSql(reader);
            }

            if (sqls.size() > 0) {
                this.upgradeSupport.upgradeVersion(sqls);
            }
        }
        catch (Exception e) {
            String msg = e.getMessage();
            logger.error("---" + upgradeConfigInfo.getDomain() + "数据库由" + (version) + "_" + (version + 1)
                    + "升级失败，SQL为：" + sb.toString(), e);
            //throw e;
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    /**
     * 读取一条sql语句
     * @param reader 升级文件读取
     * @return
     * @throws IOException
     * @author huangwb created on 2012-1-4 
     * @since SHK BMP 1.0
     */
    private String readSql(BufferedReader reader) throws IOException  {
        StringBuffer sqlBuf = new StringBuffer();
        String sql = null;
        String line = reader.readLine();
        while(line != null){
            line = line.trim();
            if(StringUtils.isNotBlank(line) && !line.startsWith("--")) { 
                sqlBuf.append(line).append(" ");
                if (line.endsWith(";")) {//一个完整的sql;
                    sql = sqlBuf.toString().trim();
                    sql = replaceBlankspace(sql.substring(0, sql.length() - 1));//去掉最后的分号，不然升级不了
                    if (StringUtils.isNotBlank(sql)) {
                        break;
                    }
                    else {
                        sqlBuf.delete(0,sqlBuf.length());
                    }
                }
                
            }
            line = reader.readLine();
        }
        return replaceBlankspace(sql);
    }

    
    /**
     * 回滚数据库操作
     * @author huangwb created on 2012-1-12 
     * @since SHK BMP 1.0
     */
    private void rollbackQuietly() {
        if (upgradeSupport != null) {
            try {
                upgradeSupport.rollback();
            }
            catch (Exception e1) {
                logger.error("执行数据库升级失败", e1);
                throw new RuntimeException("执行数据库升级失败.", e1);
            }
        }
    }
    
    /**
     * 获取域的当前数据升级脚本版本号
     * @return
     * @author huangwb created on 2011-12-30 
     * @since SHK BMP 1.0
     */
    private int queryCurrentVersion() {
        VersionInfo versionInfo = upgradeSupport.queryVersion(upgradeConfigInfo.getDomain());
        String curVersionStr = "0";
        int curVersion = 0;
        if (null != versionInfo) {
            curVersionStr = versionInfo.getCurrentVersion();
            if (StringUtils.isNotBlank(curVersionStr)) {
                curVersion = Integer.parseInt(curVersionStr);
            } else {//前次脚本升级时版本号写入有错
                logger.error("前次脚本升级时版本号为空");
                throw new RuntimeException("前次脚本升级时版本号为空");
            }
        }
        return curVersion;
    }
    
    /**
     * 初始化版本表为第一个版本
     * @return
     * @author huangwb created on 2012-1-4 
     * @since SHK BMP 1.0
     */
    private int insertFirstVersion() {
        VersionInfo versionInfo =  new VersionInfo();
        versionInfo.setDomain(upgradeConfigInfo.getDomain());
        versionInfo.setCurrentVersion("1");
        versionInfo.setLastVersion("0");
        versionInfo.setUpgradeDate(new Date());
        versionInfo.setComments(upgradeConfigInfo.getDescription());
        upgradeSupport.saveVersion(versionInfo);
        return 0;
    }
    
    /**
     * 更新数据库版本
     * @param currentVersion 当前要更新到的版本号
     * @author huangwb created on 2012-1-12 
     * @since SHK BMP 1.0
     */
    private void updateVersion(int currentVersion) {
        VersionInfo versionInfo = upgradeSupport.queryVersion(upgradeConfigInfo.getDomain());
        if (versionInfo == null) {
            insertFirstVersion();
            versionInfo = upgradeSupport.queryVersion(upgradeConfigInfo.getDomain());
        }
        if (versionInfo == null)
            throw new RuntimeException("指定的版本应用域不存在:" + upgradeConfigInfo.getDomain());
        versionInfo.setCurrentVersion(String.valueOf(currentVersion));
        upgradeSupport.updateVersion(versionInfo);
    }

    /**
     * 替换空白字符为空格
     * @param source 要被替换操作的字符串
     * @return
     * @author huangwb created on 2012-1-4 
     * @since SHK BMP 1.0
     */
    private String replaceBlankspace(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("\\s", " ").replaceAll(" +", " ");
        } else {
            return source;
        }
    }
}
