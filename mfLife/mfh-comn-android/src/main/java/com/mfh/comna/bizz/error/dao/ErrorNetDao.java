package com.mfh.comna.bizz.error.dao;

import com.mfh.comna.bizz.error.entity.ErrorEntity;
import com.mfh.comna.comn.database.dao.BaseNetDao;
import com.mfh.comna.comn.database.dao.DaoUrl;

/**
 * Created by 李潇阳 on 2014/11/7.
 */
public class ErrorNetDao extends BaseNetDao<ErrorEntity, Long> {
    @Override
    protected void initUrlInfo(DaoUrl daoUrl) {
        daoUrl.setCreateUrl("");
    }

    @Override
    protected Class<ErrorEntity> initPojoClass() {
        return ErrorEntity.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }
}
