package com.mfh.comna.comn.logic;

/**
 * Created by Administrator on 2014/12/10.
 * 三层数据同步策略
 */
public abstract class ThreeLayerStrategy extends TwoLayerStrategy {
    /**
     * 将第三层数据同步到第二层   这里同步数据不需要开启异步， 此方法是在异步线程中执行。
     */
    public abstract void syncDataFromTwo_Three();

    @Override
    public void syncDataFromFrontToEnd(int fromLayerIndex) {
        switch (fromLayerIndex) {
            case 2 :
                syncDataFromOne_Two();
            case 3:
                syncDataFromTwo_Three();
                break;
        }
    }
}
