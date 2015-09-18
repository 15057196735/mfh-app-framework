package com.mfh.comna.comn.logic;

import java.util.Map;
/**
 * list列表适配器能支持按字母索引
 * 
 * @author zhangyz created on 2013-5-13
 * @since Framework 1.0
 */
public interface IAdapterLetterIndexAble {
    /**
     * 获取指定字母对应的首行条目位置,key:是索引字母； value是对应的首个条目的位置。
     * @return
     * @author zhangyz created on 2013-5-13
     */
    public Map<String, Integer> getSelector();
}
