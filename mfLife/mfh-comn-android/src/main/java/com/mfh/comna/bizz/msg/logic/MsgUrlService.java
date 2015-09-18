package com.mfh.comna.bizz.msg.logic;

import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.bizz.msg.dao.MsgUrlDbDao;
import com.mfh.comna.bizz.msg.entity.MsgUrl;

/**
 * Created by 李潇阳 on 14-8-6.
 */
public class  MsgUrlService extends BaseService<MsgUrl, Long, MsgUrlDbDao> {

    @Override
    protected Class<MsgUrlDbDao> getDaoClass() {
        return MsgUrlDbDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }


}
