package com.mfh.comn.fs;

/**
 * 基类，用于支持文件存储。并且默认已经提供了一个filePath字段。
 * @author zhangyz created on 2012-5-13
 */
@SuppressWarnings("serial")
public abstract class FileTempField extends FileAbleField implements Cloneable{
    public static String DEFAULT_FILEPATH_FIELD = "filePath";//缺省的文件路径字段名
    //protected String filePath;//存放文件路径,实际可以有多个文件路径字段，此处提供一个缺省的
       
    @Override
    public FileTempField clone() {
        return (FileTempField)super.clone();
    }
    
    /*public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }*/
}
