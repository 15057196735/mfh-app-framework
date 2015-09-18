package com.mfh.comn.net.data;

import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.net.QfiledBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李潇阳 on 14-8-23.
 */
public class RspCodeDomain <T> implements IResponseData {
    private long totalNum = -1;
    private List<ParentChildItem<T>> rowDatas = null;
    private List<QfiledBase> rowFields = null;



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

    public void setRowDatas(List<ParentChildItem<T>> rowDatas) {
        this.rowDatas = rowDatas;
    }


    /**
     * 添加一条记录
     * @param item
     * @author zhangyz created on 2013-5-14
     */
    public void addRowItem(ParentChildItem<T> item) {
        if (rowDatas == null)
            rowDatas = new ArrayList<ParentChildItem<T>>();
        rowDatas.add(item);
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long total) {
        this.totalNum = total;
    }

    public List<QfiledBase> getRowFields() {
        return rowFields;
    }

    public void setRowFields(List<QfiledBase> rowFields) {
        this.rowFields = rowFields;
    }
}
