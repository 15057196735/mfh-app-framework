package com.mfh.comna.bizz.material.dao;

import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comna.comn.database.dao.BaseNetDao;
import com.mfh.comna.comn.database.dao.DaoUrl;

/**
 * 请求后台素材资源
 * Created by Administrator on 14-5-19.
 */
public class MaterialNetDao extends BaseNetDao<ResourceData, Long> {

    @Override
    protected void initUrlInfo(DaoUrl daoUrl) {
        daoUrl.setListUrl("/resourcedata/getResourceDataBySubidAndType");
    }

    @Override
    protected Class<ResourceData> initPojoClass() {
        return ResourceData.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }
}
