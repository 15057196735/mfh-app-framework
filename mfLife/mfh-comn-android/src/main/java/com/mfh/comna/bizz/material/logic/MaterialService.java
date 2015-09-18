package com.mfh.comna.bizz.material.logic;

import com.mfh.comna.bizz.material.dao.MaterialDao;
import com.mfh.comna.bizz.material.dao.MaterialNetDao;
import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;

import net.tsz.afinal.http.AjaxParams;

/**
 * 素材服务类
 * Created by Administrator on 14-5-19.
 */
public class MaterialService extends BaseService<ResourceData, Long, MaterialDao> {
    private MaterialNetDao netDao = new MaterialNetDao();

    @Override
    protected Class<MaterialDao> getDaoClass() {
        return MaterialDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }

    /**
     * 从网络上读取素材
     * @param mType 素材类型
     */
    public void queryFromNet(int mType, NetProcessor.QueryRsProcessor<ResourceData> callback) {
        AjaxParams params = new AjaxParams();
        addJsonParam(params, "type", mType);//"cursubdisid", ((MfhLoginService)ls).getCurSubdis()
        PageInfo pageInfo = new PageInfo(1, 100);//要求第一页从1开始
        netDao.query(params, callback);
    }
}
