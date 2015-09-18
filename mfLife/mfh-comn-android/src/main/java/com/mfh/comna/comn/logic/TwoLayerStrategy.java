package com.mfh.comna.comn.logic;

/**
 * Created by Administrator on 2014/12/10.
 * 两层数据同步策略
 */
public abstract class TwoLayerStrategy extends DataSyncStrategy {
    /**
     * 将第二层数据同步到第一层, 这里同步数据不需要开启异步， 此方法是在异步线程中执行
     */
    public abstract void syncDataFromOne_Two();

    @Override
    public void syncDataFromFrontToEnd(int fromLayerIndex) {
        syncDataFromOne_Two();
    }
}
