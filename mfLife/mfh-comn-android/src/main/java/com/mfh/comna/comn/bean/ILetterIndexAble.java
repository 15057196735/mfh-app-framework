package com.mfh.comna.comn.bean;

import com.mfh.comn.bean.IObject;

/**
 * 支持按字母顺序索引，（大写字母)
 * 
 * @author zhangyz created on 2013-5-10
 * @since Framework 1.0
 */
public interface ILetterIndexAble<T> extends IObject<T>{
    String getLetterIndex();
}
