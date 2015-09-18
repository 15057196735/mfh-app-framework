package com.mfh.comna.bizz.member.entity;

import com.mfh.comn.bean.ILongId;

/**
 * 人的接口
 * Created by Administrator on 14-5-27.
 */
public interface Ihuman extends ILongId{
    /**
     * 获取姓名
     * @return
     */
    public String getName();

    /**
     * 获取头像
     * @return
     */
    public String getHeadimage();

    /**
     * 获取主键标识
     * @return
     */
    public Long getId();

    /**
     * 获取全局唯一通讯标识
     * @return
     */
    public String getGuid();

    /**
     * 获得身份下标
     * @return
     */
    public String getLetterIndex();
}
