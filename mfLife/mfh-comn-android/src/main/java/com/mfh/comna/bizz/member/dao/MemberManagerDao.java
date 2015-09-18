package com.mfh.comna.bizz.member.dao;

import android.text.TextUtils;

import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.bean.Pair;
import com.mfh.comna.bizz.member.entity.SubdisManager;
import com.mfh.comna.comn.database.dao.BaseDbDao;

import java.util.List;

/**
 * Created by Administrator on 2014/10/22.
 * 小区业主
 */
public class MemberManagerDao extends BaseDbDao<SubdisManager, String> {
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<String, String>("通讯录业主表", "emb_member_manager");
    }

    @Override
    protected Class<SubdisManager> initPojoClass() {
        return SubdisManager.class;
    }

    @Override
    protected Class<String> initPkClass() {
        return String.class;
    }

    /**
     * 查询小区业主
     * */
    public List<SubdisManager> queryManager(Long ownerId,String searchToken,PageInfo pageInfo) {
        String where = "ownerId = " + ownerId;
        if (!TextUtils.isEmpty(searchToken)) {
            where = where + " name = '" + searchToken + "'";
        }
        return getDb().findAllByWhere(SubdisManager.class,where,"subdisName",pageInfo);
    }

    public void clean(Long userId) {
        this.getDb().deleteByWhere(SubdisManager.class, "ownerId=" + userId.toString());
    }
}
