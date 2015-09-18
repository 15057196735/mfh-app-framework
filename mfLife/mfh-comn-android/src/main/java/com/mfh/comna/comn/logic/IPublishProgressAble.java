package com.mfh.comna.comn.logic;

/**
 * 提供一个方法，供第三方来发布进度
 * 
 * @author zhangyz created on 2013-5-15
 * @since Framework 1.0
 */
public interface IPublishProgressAble<T> {
    /**
     * 是否还在执行
     * @return
     * @author zhangyz created on 2013-5-15
     */
    public boolean isProgress();
    
    /**
     * 通知时间间隔，秒
     * @return
     * @author zhangyz created on 2013-5-15
     */
    public int getRate();
    
    /**
     * 供发布进度
     * @param param 进度参数
     * @author zhangyz created on 2013-5-15
     */
    public void publishProgressByService(T... param);
}
