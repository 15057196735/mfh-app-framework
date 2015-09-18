package com.mfh.comna.bizz.member.dao;

import android.text.TextUtils;

import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comna.comn.database.dao.BaseNetDao;
import com.mfh.comna.comn.database.dao.DaoUrl;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.JsonParser;
import com.mfh.comna.network.NetFactory;
import com.mfh.comna.bizz.member.entity.Human;

import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 通讯录成员网络dao类
 * Created by Administrator on 14-5-22.
 */
public class MemberNetDao extends BaseNetDao<Human, Long> {

    @Override
    protected void initUrlInfo(DaoUrl daoUrl) {
        daoUrl.setListUrl("/priv/subdist");
        // /pmcAdmin/listAllEmpHumanIdOfPmcId  //userId=206&subdisId=99
    }

    @Override
    protected Class<Human> initPojoClass() {
        return Human.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }

    /**
     * 获取本物业公司内，与当前楼管员的同事信息
     * @param userId
     * @param callBack
     */
    public void listAllEmpHumanIdOfPmcId(Long userId, NetProcessor.QueryRsProcessor<Human> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId.toString());
        super.query(params, callBack, "/pmcAdmin/listAllEmpHumanIdOfPmcId");
    }

    /**
     * 查询所有满分的联系人
     * */
    public void getCompanyList(Long userId, NetProcessor.QueryRsProcessor<Human> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId.toString());
        super.query(params, callBack, "/companyHuman/listAllHumanIdOfCompanyId");
    }

    /**
     * 获取楼管员管辖的业主human列表
     * @param userId 楼管员用户号
     * @param subdisId 小区号，过滤作用，可以为null
     * @param updatedDate 更新日期
     * @param callBack
     */
    public void getPownerOfApartManager(Long userId, String subdisId,
                NetProcessor.QueryRsProcessor<Human> callBack, String... updatedDate) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId.toString());
        if (subdisId != null)
            params.put("subdisId", subdisId);
        if (updatedDate != null && updatedDate.length > 0)
            params.put("startCursor", updatedDate[0]);
        //callBack.setPageInfo(new PageInfo(0, 100));
        //params.put("pageInfo", new PageInfo(0, 100).toString());
        super.query(params, callBack, "/priv/subdist/powners");
    }

    /**
     * 获取服务指定小区列表的相关厂家客服人员列表
     * @param subdisIds 小区编号，多个的话逗号分隔
     * @param callBack
     */
    public void getSellSupportsByPowner(String subdisIds, NetProcessor.QueryRsProcessor<Human> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("subdisIds", subdisIds);

        //兼容老的服务器端，后面可以去掉
        String[] segs = StringUtils.splitByWholeSeparator(subdisIds, ",");
        params.put("subdisId", segs[0]);
        super.query(params, callBack, "/priv/subdist/sellSupports");
    }

    public List<EntityWrapper<Human>> listAllEmpHumanIdOfPmcId(Long userId, String roleType) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId.toString());
        params.put("roleType",roleType);
        String result = (String) NetFactory.getHttp().postSync(NetFactory.getServerUrl() + "/pmcAdmin/listAllEmpHumanIdOfPmcId", params);
        List<EntityWrapper<Human>> humans = JsonParser.parseArray(result, Human.class);
        return humans;
    }

    public List<EntityWrapper<Human>> getPownerOfApartManager(Long userId, String subdisId, String roleType, String... updatedDate) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId.toString());
        if (subdisId != null)
            params.put("subdisId", subdisId);
        if (updatedDate != null && updatedDate.length > 0)
            params.put("startCursor", updatedDate[0]);
        params.put("page", "1");
        params.put("rows","100000000");
        params.put("roleType",roleType);
        String result = (String) NetFactory.getHttp().postSync(NetFactory.getServerUrl() +"/priv/subdist/powners", params);
        List<EntityWrapper<Human>> humans = JsonParser.parseArray(result, Human.class);
        return humans;
    }

    public List<EntityWrapper<Human>> getSellSupportsByPowner(String subdisIds, String roleType) {
        AjaxParams params = new AjaxParams();
        params.put("subdisIds", subdisIds);

        params.put("roleType",roleType);
        if (TextUtils.isEmpty(subdisIds)) {
            String[] segs = StringUtils.splitByWholeSeparator(subdisIds, ",");
            params.put("subdisId", segs[0]);
        }
        String result = (String) NetFactory.getHttp().postSync(NetFactory.getServerUrl() +"/priv/subdist/sellSupports", params);
        List<EntityWrapper<Human>> humans = JsonParser.parseArray(result, Human.class);
        return humans;
    }

    public List<EntityWrapper<Human>> getCompanyList(Long userId, String roleType) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId.toString());
        params.put("roleType",roleType);
        String result = (String) NetFactory.getHttp().postSync(NetFactory.getServerUrl() +"/companyHuman/listAllHumanIdOfCompanyId", params);
        List<EntityWrapper<Human>> humans = JsonParser.parseArray(result, Human.class);;
        return humans;
    }

}
