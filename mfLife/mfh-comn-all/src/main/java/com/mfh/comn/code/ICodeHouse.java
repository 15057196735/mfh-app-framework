package com.mfh.comn.code;

/**
 * 支持编码转换的接口
 * (注：该接口名字最好改成ICodeChange)
 * @param <T>
 * @author zhangyz created on 2014-6-19
 */
public abstract interface ICodeHouse<T> {

    /**
     * 根据编码值获取对应的描述,用于数据查询时对结果集中的某些列进行编码转换
     * @param code 编码值(统一编码格式,但一般都是最底层的编码值，因为它们才需要进行编码转换)
     * @return
     * @author zhangyz created on 2012-4-6
     */
    public String getValue(T code);

}
