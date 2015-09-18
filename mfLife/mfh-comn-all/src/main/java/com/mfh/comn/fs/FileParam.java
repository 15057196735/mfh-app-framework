package com.mfh.comn.fs;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.mfh.comn.utils.FileUtils;

/**
 * 文件路径字段对应的传入参数
 * 注意：一个文件路径字段可以存储多个文件路径
 * @author zhangyz created on 2012-4-17
 * @since Framework 1.0
 */
public class FileParam {
    private List<FileItemParam> fileItems = null;
    public static String FILES_DIVIDE = ",";
    
    /**
     * 获取组合的文件名,不包括扩展名
     * @return
     * @author zhangyz created on 2012-4-26
     */
    public String getFileName(){
        if (fileItems == null || fileItems.size() <= 0)
            return null;
        String ret = fileItems.get(0).getFileName();
        ret = FileUtils.getOnlyFileName(ret);
        if (fileItems.size() > 1)
            ret += "等";
        return ret;
    }
    
    /**
     * 判断上传文件的类型是否合法
     * @param types 合法的类型集
     * @return
     * @author zhangyz created on 2012-4-26
     */
    public void checkType(String types){
        if (fileItems == null || fileItems.size() <= 0 || types == null || types.length() == 0)
            return;
        for (int ii = 0; ii < fileItems.size(); ii++){
            String suffix = FileUtils.getSuffixName(fileItems.get(ii).getFileName());
            if (suffix.length() == 0)
                throw new RuntimeException("未指定文件类型");
            if (types.indexOf(suffix.toLowerCase()) < 0)
                throw new RuntimeException("不支持的文件类型:" + suffix);
        }
    }
    
    /**
     * 根据前缀，自动生成唯一的文件名
     * @param preId 一般是uuid
     * @return
     * @author zhangyz created on 2012-4-26
     */
    public String getAutoFileName(String preId){
        if (fileItems == null || fileItems.size() <= 0)
            return null;
        StringBuilder builder = new StringBuilder();
        String oneName;
        for (int ii = 0; ii < fileItems.size(); ii++){
            oneName = fileItems.get(ii).getAutoFileName(preId, ii);
            if (ii > 0)
                builder.append(FILES_DIVIDE);
            builder.append(oneName);
        }
        return builder.toString();
    }
    
    /**
     * 增加一个文件参数
     * @param item
     * @author zhangyz created on 2012-4-17
     */
    public void addFileItem(FileItemParam item){
        if (fileItems == null)
            fileItems = new ArrayList<FileItemParam>();
        fileItems.add(item);
    }
    
    public void clear(){
        if (fileItems != null)
            fileItems.clear();
    }
    
    public static String[] getSingleFileNames(String mutilPath){
        return StringUtils.splitByWholeSeparator(mutilPath, FILES_DIVIDE);
    }
    
    /**
     * 获取有效的全文件路径名
     * @param mutilPath 存在的文件全路径名列表，可能包含路径，但路径分隔符都是/
     * @param fileName 指定的文件名；若为空，取第一个
     * @return 文件路径和文件名
     * @author zhangyz created on 2012-4-17
     */
    public static String[] getFileName(String mutilPath, String fileName){
        String[] files = StringUtils.splitByWholeSeparator(mutilPath, FILES_DIVIDE);
        if (files == null || files.length == 0)
            return null;
        if (fileName == null)
            return FileUtils.extractPathAndFile(files[0]);
        String[] pathFile;
        for (String file : files){
            pathFile = FileUtils.extractPathAndFile(file);
            if (fileName.equals(pathFile[1])){
                /*if (pathFile[0] == null)
                    return fileName;
                else
                    return pathFile[0] + fileName;*/
                return new String[]{pathFile[0], fileName};
            }
        }
        return null;
    }
    
    /**
     * 简单地将文件内容保存到本地,覆盖原有文件。
     */
    /*public void operatorFile(IFileOperator fileOper) {
        if (fileItems == null || fileItems.size() == 0)
            return;

        FileItemParam fileItem = null;
        for (int ii = 0; ii < fileItems.size(); ii++){
            fileItem = fileItems.get(ii);
            fileOper.updateFile(null, fileItem.getFileName(), fileItem.getFileContent());
        }        
    }*/
    
    /**
     * 获取总文件大小
     * @return
     * @author zhangyz created on 2012-4-23
     */
    public long getTotalSize(){
        if (fileItems == null || fileItems.size() == 0)
            return 0;
        else{
            long iReturn = 0;
            for (FileItemParam item : fileItems)
                iReturn += item.getSize();
            return iReturn;
        }
    }
    
    public List<FileItemParam> getFileItems() {
        return fileItems;
    }

    /**
     * 获取总文件数
     */
    public int getTotalNum(){
        if (fileItems == null || fileItems.size() == 0)
            return 0;
        else{
            return fileItems.size();
        }        
    }
    
    /**
     * 获取指定索引的文件项
     * @param index
     * @return
     * @author zhangyz created on 2012-5-12
     */
    public FileItemParam getFileItem(int index){
        if (fileItems == null || fileItems.size() == 0)
            return null;
        else
            return fileItems.get(index);
    }
    
    /**
     * 多个文件名合并辅助类
     * 
     * @author zhangyz created on 2012-4-17
     * @since Framework 1.0
     */
    public static class FileNameMerger{
        private int count = 0;
        private StringBuilder builder = new StringBuilder();
        
        /**
         * 增加一个文件项名
         * @param name
         * @author zhangyz created on 2012-4-17
         */
        public void addFileItemName(String name){
            if (name != null){
                if (count > 0)
                    builder.append(FILES_DIVIDE);
                else
                    builder.setLength(0);// 初始化。
                builder.append(name);
                count++;
            }
        }
        
        /**
         * 获取总文件名
         * @return
         * @author zhangyz created on 2012-4-17
         */
        public String getFullName(){
            return builder.toString();
        }
    }
}
