package com.mfh.comn.fs;

import java.util.HashMap;
import java.util.Map;

import com.mfh.comn.annotations.NoColumn;

/**
 * 基类，用于支持文件存储。
 * 文件存储字段未定,需要指定。
 * @author zhangyz created on 2012-8-20
 */
public class FileAbleField implements Cloneable{//IFileAvailableBean<PK>, 
    private transient Map<String, FileParam> fileParams = null;// 传入的文件参数,key是属性名，因而支持多个文件字段。临时变量,最终写到数据库中,并把路径写入filePath中
    public static String DEFAULT_FILEPATH_FIELD = "filePath";//缺省的文件路径字段名
    //protected String filePath;//存放文件路径,实际可以有多个文件路径字段，此处提供一个缺省的
    
    @Override
    public FileAbleField clone() {
        try {
            return (FileAbleField)super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
    //@Override
    public boolean hasFileParam(){
        if (fileParams != null && fileParams.size() > 0)
            return true;
        else
            return false;
    }

    //@Override
    public void clearFileParam(){
        if (fileParams != null){
            fileParams.clear();
            fileParams = null;
        }
    }

    @NoColumn
    //@Override
    public void setFileParam(String key, FileParam fileParam) {
        if (fileParams == null)
            fileParams = new HashMap<String, FileParam>();
        fileParams.put(key, fileParam);
    }

    //@Override
    public void addFileParam(String key, FileItemParam fileItemParam) {
        if (fileParams == null)
            fileParams = new HashMap<String, FileParam>();
        FileParam param = fileParams.get(key);
        if (param == null){
            param = new FileParam();
            fileParams.put(key, param);
        }
        param.addFileItem(fileItemParam);
    }

    @NoColumn
    //@Override
    public FileParam getFileParam(String key) {
        if (fileParams == null)
            return null;
        else
            return fileParams.get(key);
    }
}
