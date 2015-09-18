package com.mfh.comn.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.util.Base64;
import com.mfh.comn.ConstantActions;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.utils.FileUtils;
import com.mfh.comn.utils.UuidUtil;

/**
 * 接收文件数据时的操作参数
 * 文件可能来自于网络、摄像头、录音设备等，支持大文件长时间接收。
 * 用于客户端（手机、java客户端）传递文件参数。
 * @author zhangyz created on 2012-4-17
 * @since Framework 1.0
 */
public class FileItemParam {
    public static String DEFAULT_FILEPATH_FIELD = "filePath";//缺省的文件路径字段名

    private int operKind;//操作类型
    private String operName;//文件名
    private InputStream fileContent;//文件内容
    private long size;//文件内容大小
    
    private Integer resizeWidth = 0;

    public static String DEFAULT_SUFFIX = ".dat";
    public static String MIX_SUFFIX = ".mix";//混合字段产生的文件扩展名
    
    public FileItemParam() {
        super();
    }
    
    /**
     * 直接根据文件对象构造一个插入操作的文件操作参数
     * @param file
     */
    public FileItemParam(File file) {
        this.operKind = ConstantActions.OPER_INSERT;
        this.operName = file.getName();
        checkFileName();
        try {
            this.fileContent = new java.io.FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("指定的文件不存在:" + file.getName());
        }
        this.size = file.length();
    }
    
    /**
     * 构造函数
     * @param operKind 操作类型
     * @param fileName 文件名
     * @param fileContent 文件内容
     */
    public FileItemParam(int operKind, String fileName, InputStream fileContent, long size) {
        super();
        this.operKind = operKind;
        this.operName = fileName;
        checkFileName();
        this.fileContent = fileContent;
        this.size = size;
    }
    
    public FileItemParam(int operKind, File file, String fileName) {
        super();
        this.operKind = operKind;
        if (fileName != null)
            this.operName = fileName;
        else
            this.operName = file.getName();
        checkFileName();
        try {
            this.fileContent = new java.io.FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("指定的文件不存在:" + file.getName());
        }
        this.size = file.length();
    }
    
    public FileItemParam(int operKind, String fileName, byte[] fileContent) {
        super();
        this.operKind = operKind;
        this.operName = fileName;
        checkFileName();
        if (fileContent != null){
            this.fileContent = new java.io.ByteArrayInputStream(fileContent);
            this.size = fileContent.length;
        }
    }
    
    /**
     * 构造函数,xml结构如下:
     * 若newName为空oldName不为空代表需要删除该文件;newName不为空代表覆盖或新建文件;两者相同代表修改文件。
     * <fileItem newFileName="" oldFileName="">文件字节流base64编码</fileItem>
     */    
    /*public FileItemParam(Element fileEle) {
        super();        
        operName = fileEle.attributeValue("fileName");
        String kind = fileEle.attributeValue("kind");
        operKind = ConstantActions.TranslateOperKind(kind);
        checkFileName();
        String content = fileEle.getText();
        if (content != null && content.length() > 0){
            byte[] fileBytes = Base64.decodeBase64(content.getBytes());
            size = fileBytes.length;
            fileContent = new java.io.ByteArrayInputStream(fileBytes);
        }
        else
            size = 0;
    }*/
    
    /**
     * 构造函数
     * @param operKind 操作类型
     * @param fileName 文件名
     * @param base64Content 文件内容(base64编码)
     */
    public FileItemParam(String kind, String fileName, String base64Content) {
        super();
        operKind = ConstantActions.TranslateOperKind(kind);        
        operName = fileName;
        checkFileName();
        
        if (base64Content != null && base64Content.length() > 0){
            byte[] fileBytes = Base64.decodeFast(base64Content);
            //byte[] fileBytes = Base64.decodeBase64(base64Content.getBytes());
            size = fileBytes.length;
            fileContent = new java.io.ByteArrayInputStream(fileBytes);
        }
        else
            size = 0;
    }
    
    private void checkFileName(){
        if (operName == null || operName.length() == 0){
            if (operKind == ConstantActions.OPER_INSERT)
                operName = UuidUtil.getUuid() + DEFAULT_SUFFIX;
        }
        else{
            String suffix = FileUtils.getSuffixName(operName);
            if (StringUtils.isBlank(suffix))
                operName = operName + DEFAULT_SUFFIX;//自动增加扩展名
        }
    }
    
    /**
     * 自动生成文件名
     * @param preId 前缀
     * @param index 序号，图片集中的第几个，0代表第一个
     * @return
     * @author zhangyz created on 2012-5-8
     */
    public String getAutoFileName(String preId, int index){
        StringBuilder builder = new StringBuilder();
        String oldName;
        oldName = getFileName();
        String suffix = null;
        if (oldName != null)
            suffix = FileUtils.divideFileName(oldName)[1];
        else
            suffix = DEFAULT_SUFFIX;
        /*if (index > 0)
            builder.append(HtxtHelper.FILES_DIVIDE);*/
        builder.append(preId);
        if (index > 0)
            builder.append("_").append(index);
        builder.append(suffix);            
        return builder.toString();
    }

    /**
     * 生成一个与已有文件名不重名的新文件名
     * @param preId 文件名前缀
     * @param oldFileItems 旧文件名集,包括路径和文件名两部分.
     * @return 新文件名，不含目录。
     * @author zhangyz created on 2012-5-8
     */
    public String genNewFileItemName(String preId, List<Pair<String, String>> oldFileItems){
        int index = 0;
        String preToken;
        do{
            if (index == 0)
                preToken = preId;
            else
                preToken = preId + "_" + index;
            boolean bFind = false;
            if (oldFileItems != null){
                for (int ii = 0; ii < oldFileItems.size(); ii++){
                    if (oldFileItems.get(ii).getV2().startsWith(preToken)){
                        bFind = true;
                        break;
                    }
                }
            }
            if (!bFind){
                return getAutoFileName(preId, index);
            }                
            index ++;
        }
        while (true);
    }
    
    /**
     * 是否新增
     * @return
     * @author zhangyz created on 2012-4-17
     */
    public boolean isInsert(){
        return operKind == ConstantActions.OPER_INSERT;
    }
    
    /**
     * 是否修改
     * @return
     * @author zhangyz created on 2012-4-17
     */
    public boolean isUpdate(){
        return operKind == ConstantActions.OPER_UPDATE;        
    }
    
    /**
     * 是删除
     * @return
     * @author zhangyz created on 2012-4-17
     */
    public boolean isDelete(){
        return operKind == ConstantActions.OPER_DELETE;        
    }
    
    public boolean isIgnore(){
        return operKind == ConstantActions.OPER_IGNORE;
    }
    
    /**
     * 判断是否可以没有实际文件内容（在某些操作类型下）
     * @return
     * @author zhangyz created on 2012-5-12
     */
    public boolean canNoContent(){
        return (isDelete() || isIgnore());
    }
    
    public int getOperKind() {
        return operKind;
    }
    
    public void setOperKind(int operKind) {
        this.operKind = operKind;
    }
    
    public String getFileName() {
        return operName;
    }
    
    public void setFileName(String fileName) {
        this.operName = fileName;
    }
    
    public InputStream getFileContent() {
        return fileContent;
    }
    
    public void setFileContent(InputStream fileContent) {
        this.fileContent = fileContent;
    }
    
    public long getSize() {
        return size;
    }
    
    /**
     * 补充设置文件大小
     * @param size
     * @author zhangyz created on 2014-4-6
     */
    public void initSize(long size) {
        if (this.size <= 0)
            this.size = size;
    }

    /**
     * 获取接收到的文件的扩展名
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public String getSuffix() {
        return FileUtils.getSuffixName(operName);
    }
    
    /**
     * 存储的内容是否为文件名
     * @param filePathName
     * @return
     * @author zhangyz created on 2012-8-16
     */
    public static boolean isMixFileName(String filePathName){
        if (StringUtils.isNotBlank(filePathName) && filePathName.endsWith(FileItemParam.MIX_SUFFIX)){
            if (filePathName.length() == 40)
                return true;
            else{
                int index = filePathName.lastIndexOf("/");
                if (index > 0){
                    filePathName = filePathName.substring(index + 1);
                    if (filePathName.length() == 40)
                        return true;
                }
                //还不行说明是新的数字命名的文件名
                String onlyFileName = FileUtils.getOnlyFileName(filePathName);//如最新的试题回答表的id是long型
                if (StringUtils.isNumeric(onlyFileName))
                    return true;
            }
            return false;
        }
        else
            return false;
    }
    
    public Integer getResizeWidth() {
        return resizeWidth;
    }

    
    public void setResizeWidth(Integer resizeWidth) {
        this.resizeWidth = resizeWidth;
    }
    
    
}
