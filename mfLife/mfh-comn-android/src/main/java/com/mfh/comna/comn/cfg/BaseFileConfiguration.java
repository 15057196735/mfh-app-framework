package com.mfh.comna.comn.cfg;

import android.util.Pair;

import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comn.config.SingleConfiguration;
import com.mfh.comna.bizz.BizApplication;
import com.mfh.comna.comn.database.dao.BaseFao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 单配置文件实现类基类
 * 
 * @author zhangyz created on 2013-5-26
 * @since Framework 1.0
 */
public abstract class BaseFileConfiguration implements SingleConfiguration{    
    public final static String FROM_ASSETS = "assets";  //资源文件的assets目录
    public final static String FROM_INTERN = "intern";//内部存储的私有文件目录
    public final static String FROM_EXTERN = "extern";//外部存储的文件目录
    public final static String FROM_RAW = "raw";        //资源文件的raw目录
    public final static String FROM_CLASSPATH = "classpath";
    public final static String FROM_SERVER = "server";
    
    protected String fromConfigPath;//原始来源
    protected boolean bUpdated = false;//是否发生过修改
    
    public BaseFileConfiguration() {
        super();
    }

    /**
     * 执行修改提交,子类继承。注意子类发生修改时必须更改bUpdated标志为true。
     * @param fos
     * @throws Exception
     * @author zhangyz created on 2013-5-26
     */
    protected abstract void writeCommitInner(OutputStream fos) throws Exception;
    
    private File unionFileDir(File dir, String filePath) {
        String absDirPath = dir.getAbsolutePath();
        if (absDirPath.endsWith("/") == false)
            absDirPath += "/";
        int index = filePath.lastIndexOf("/");
        if (index > 0) {
            String rDir = filePath.substring(0, index + 1);
            if (rDir != null && rDir.length() > 0)
                absDirPath += rDir;
            filePath = filePath.substring(index + 1);
        }
        File file = new File (absDirPath, filePath);
        return file;
    }
    
    public static Pair<String, String> genPreAndPath(String configPath) {
        String[] strs = StringUtils.splitByWholeSeparator(configPath, ":");
        String pre = FROM_ASSETS;
        String factPath = configPath;
        if (strs.length == 2) {
            pre = strs[0];
            factPath = strs[1];
        }        
        Pair<String, String> ret = new Pair<String, String> (pre, factPath);
        return ret;
    }
    
    /**
     * 获取本配置文件可输出的文件对象
     * @return
     * @author zhangyz created on 2013-5-26
     */
    public File getWriteAbleFile() {
        if (fromConfigPath == null)
            throw new RuntimeException("配置文件路径未指定!");

        File fileOut;
        Pair<String, String> pair = genPreAndPath(fromConfigPath);  
        String pre = pair.first;
        String factPath = pair.second;
        if (pre.equals(FROM_INTERN)) {
            File dir = BizApplication.getAppContext().getFilesDir();
            fileOut = unionFileDir(dir, factPath);
        }
        else if (pre.equals(FROM_EXTERN)) {
            File dir = BizApplication.getAppContext().getExternalFilesDir(null);
            fileOut = unionFileDir(dir, factPath);
        }
        else
            throw new RuntimeException(pre + "类型的文件不支持写入");
        
        if (fileOut.exists() == false) {
            if (fileOut.getParentFile().exists() == false)
                fileOut.getParentFile().mkdirs();
            try {
                fileOut.createNewFile();
            }
            catch (IOException e) {
                throw new RuntimeException("创建文件失败：" + e.getMessage(), e);
            }
        }        
        return fileOut;
    }
    
    /**
     * 根据配置文件的位置读取出文件流
     * @param configPath 具有文件来源前缀
     * @return
     * @throws Exception
     * @author zhangyz created on 2013-5-26
     */
    protected InputStream readStream(String configPath) throws Exception{   
        this.fromConfigPath = configPath;
        Pair<String, String> pair = genPreAndPath(configPath);        
        String pre = pair.first;
        String factPath = pair.second;
        
        try {
            if (pre.equals(FROM_ASSETS)) {
                return AppHelper.getAm().open(factPath);
            }
            else if (pre.equals(FROM_INTERN)) {
                File dir = BizApplication.getAppContext().getFilesDir();
                File file = unionFileDir(dir, factPath);
                return new FileInputStream(file);
            }
            else if (pre.equals(FROM_EXTERN)) {
                File dir = BizApplication.getAppContext().getExternalFilesDir(null);
                File file = unionFileDir(dir, factPath);
                return new FileInputStream(file);
            }
            else if (pre.equals(FROM_RAW)) {
                int rId = Integer.parseInt(factPath);
                return BizApplication.getAppContext().getResources().openRawResource(rId);
            }
            else if (pre.equals(FROM_CLASSPATH)) {//似乎不起作用
                if (factPath.startsWith("/") == false)
                    factPath = "/" + factPath;
                return BaseFileConfiguration.class.getClassLoader().getResourceAsStream(factPath);
            }
            else if (pre.equals(FROM_SERVER)) {
               return BaseFao.returnSteam(factPath);
            }
            else
                throw new RuntimeException("不支持的配置文件来源协议:" + pre);
        }
        catch(IOException ex) {
            //IO异常，说明文件可能不存在
            LoggerFactory.getLogger(((Object)this).getClass()).warn("读取配置文件" + configPath + "失败,可能损坏或不存在:" + ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public InputStream getReadStream() {
        try {
            return readStream(fromConfigPath);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void commitWrite() {
        if (!bUpdated)
            return;
        File file = this.getWriteAbleFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            // 将Properties集合保存到流中
            writeCommitInner(fos);
        }
        catch(Exception ex) {
            throw new RuntimeException("保存配置文件失败:" + ex.getMessage());
        }
        finally {
            if (fos != null){
                try {
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
