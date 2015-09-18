package com.mfh.comna.bizz.material.dao;

import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comn.bean.Pair;
import com.mfh.comna.comn.database.dao.BaseDbDao;

/**
 * 素材本地数据库Dao
 * Created by Administrator on 14-5-19.
 */
public class MaterialDao extends BaseDbDao<ResourceData, Long> {

    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("素材定义表", "wx_resource_data");
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
