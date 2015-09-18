package com.mfh.comna.bizz.netphone.dao;

import com.mfh.comna.bizz.netphone.entity.NetPhoneInfo;
import com.mfh.comn.bean.Pair;
import com.mfh.comna.comn.database.dao.BaseDbDao;

/**
 * Created by Administrator on 2014/9/22.
 * 网络电话
 */
public class NetPhoneDbDao extends BaseDbDao<NetPhoneInfo, Long> {
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("网络电话账号信息表", "emb_netphone");
    }

    @Override
    protected Class<NetPhoneInfo> initPojoClass() {
        return NetPhoneInfo.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }
}
