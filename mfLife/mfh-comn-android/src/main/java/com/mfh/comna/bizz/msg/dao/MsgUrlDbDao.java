package com.mfh.comna.bizz.msg.dao;

import com.mfh.comn.bean.Pair;
import com.mfh.comna.bizz.msg.entity.MsgUrl;
import com.mfh.comna.comn.database.dao.BaseDbDao;

/**
 * Created by 李潇阳 on 14-8-6.
 */
public class MsgUrlDbDao extends BaseDbDao<MsgUrl, Long> {
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("消息的url地址表", "tb_emb_msg");
    }

    @Override
    protected Class<MsgUrl> initPojoClass() {
        return MsgUrl.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }
}
