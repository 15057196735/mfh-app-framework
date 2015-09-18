package com.mfh.comn.net.data;

import java.util.ArrayList;
import java.util.List;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.net.QfiledBase;

/**
 * 查询输出部分的定义结构
 * @param <T>
 * @author zhangyz created on 2013-5-14
 * @since Framework 1.0
 */
public class RspQueryResult <T> implements IResponseData{
    private long totalNum = -1;//全部的结果记录数    
    private List<EntityWrapper<T>> rowDatas = null;
    private List<QfiledBase> rowFields = null;
    
    /**
     * 获取表中的全部记录数
     * @return
     * @author zhangyz created on 2014-3-11
     */
    public long getTotalNum() {
        return totalNum;
    }
    
    /**
     * 获取返回的结果集中的记录数
     * @return
     * @author zhangyz created on 2014-3-11
     */
    public int getReturnNum() {
        if (rowDatas == null)
            return 0;
        else
            return rowDatas.size();
    }
    
    /**
     * 设置结果集大小
     * @param total
     * @author zhangyz created on 2013-5-14
     */
    public void setTotalNum(long total) {
        this.totalNum = total;
    }

    /**
     * 添加一条记录
     * @param item
     * @author zhangyz created on 2013-5-14
     */
    public void addRowItem(EntityWrapper<T> item) {
        if (rowDatas == null)
            rowDatas = new ArrayList<EntityWrapper<T>> (); 
        rowDatas.add(item);
    }
    
    /**
     * 添加一个字段
     * @param field 字段定义
     * @author zhangyz created on 2013-5-14
     */
    public void addField(QfiledBase field) {
        if (rowFields == null)
            rowFields = new ArrayList<QfiledBase> ();
        rowFields.add(field);
    }
    
    public List<EntityWrapper<T>> getRowDatas() {
        return rowDatas;
    }
    
    /**
     * 获取指定行的bean值
     * @param index
     * @return
     * @author zhangyz created on 2014-3-8
     */
    public T getRowEntity(int index) {
        if (rowDatas == null){
            System.out.print("Mfh: rowDatas is null");
        }
        return rowDatas.get(index).getBean();
    }
    
    public EntityWrapper<T> getRowWrapper(int index) {
        return rowDatas.get(index);
    }
        
    public void setRowDatas(List<EntityWrapper<T>> rowDatas) {
        this.rowDatas = rowDatas;
    }
    
    public List<QfiledBase> getRowFields() {
        return rowFields;
    }
    
    public void setRowFields(List<QfiledBase> rowFields) {
        this.rowFields = rowFields;
    }
}
