package com.mfh.comna.comn.logic;

/**
 * Created by Administrator on 2014/12/10.
 * 数据同步的基类
 */
public abstract class DataSyncStrategy<T> {
    /**
     * 指定层向上层同步
     * @param fromLayerIndex 指定层      2代表syncDataFromOne_Tow   3代表syncDataFromTwo_Three
     */
    public abstract void syncDataFromFrontToEnd(int fromLayerIndex);

//    /**
//     * 从最近一层快速读取数据
//     * @return
//     */
//    public abstract List<KvBean<T>> readListPageDataOfNearLayer();

    /**
     * 同步间隔时间
     * */
    public long getIntervaldTime(){
        return 30 * 60 * 1000; //默认30分钟
    }
}
