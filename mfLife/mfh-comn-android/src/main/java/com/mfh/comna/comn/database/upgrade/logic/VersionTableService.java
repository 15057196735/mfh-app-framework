package com.mfh.comna.comn.database.upgrade.logic;

import com.mfh.comn.upgrade.VersionInfo;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.comn.database.upgrade.dao.VersionTableDao;

/**
 * 版本定义表服务类
 * Created by Administrator on 14-6-7.
 */
public class VersionTableService extends BaseService<VersionInfo, String, VersionTableDao>{
    @Override
    protected Class<VersionTableDao> getDaoClass() {
        return VersionTableDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }

}
