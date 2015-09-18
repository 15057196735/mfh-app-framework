package com.mfh.comna.bizz.member.dao;

import com.mfh.comna.comn.database.dao.BaseNetDao;
import com.mfh.comna.comn.database.dao.DaoUrl;
import com.mfh.comna.comn.logic.AsyncTaskCallBack;
import com.mfh.comna.network.NetFactory;
import com.mfh.comna.bizz.member.entity.SubdisManager;

import net.tsz.afinal.http.AjaxParams;


/**
 * Created by Administrator on 2014/10/23.
 * 获取物业管理小区楼栋号
 */
public class MemberManagerNetDao extends BaseNetDao<SubdisManager,String> {
    @Override
    protected void initUrlInfo(DaoUrl daoUrl) {
        daoUrl.setListUrl("/pmcAdmin/listServiceAddrOneByUserId");
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
     * 查询小区物业管理的楼栋号
     * */
    public void queryManagerHouseNumber(Long userId, String subdisId, AsyncTaskCallBack<String> callback,String... factUrl) {
        AjaxParams params = new AjaxParams();
        if (userId != null)
            params.put("userId", userId.toString());
        if (subdisId != null)
            params.put("subdisId", subdisId);
        NetFactory.getHttp().post(getFullUrl(DaoUrl.DaoType.list, factUrl), params, callback);
    }
}
