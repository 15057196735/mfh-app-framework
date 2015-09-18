package com.mfh.comn;

/**
 * 基础平台中定义的action操作类型
 * 
 * @author zhangyz created on 2013-2-28
 * @since Framework 1.0
 */
public enum ActionType {
    insert, //插入
    delete, //删除
    update, //修改
    data,   //查询数据
    page,   //查询网页(包括数据)
    code,   //查询编码
    export, //导出数据
    search, //检索数据
    save,   //保存数据
    saveAs, move
}
