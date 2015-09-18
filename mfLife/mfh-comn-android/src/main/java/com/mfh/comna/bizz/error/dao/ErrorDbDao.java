package com.mfh.comna.bizz.error.dao;

import com.mfh.comn.bean.Pair;
import com.mfh.comna.bizz.error.entity.ErrorEntity;
import com.mfh.comna.comn.database.dao.BaseDbDao;

import java.util.List;

/**错误搜集的本地dao
 * Created by 李潇阳 on 2014/11/7.
 */
public class ErrorDbDao extends BaseDbDao<ErrorEntity, Long> {
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("错误收集表","tb_error_collect");
    }

    @Override
    protected Class<ErrorEntity> initPojoClass() {
        return ErrorEntity.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }

    /**
     * 获得所有的未上传的错误
     * @return
     */
    public List<ErrorEntity> getAllUnUploadError() {
        return getDb().findAllByWhere(ErrorEntity.class, "isUpload=0", "errorTime");
    }
}
