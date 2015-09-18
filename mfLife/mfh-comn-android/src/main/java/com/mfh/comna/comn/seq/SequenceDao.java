/*
 * 文件名称: SequenceDao.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-28
 * 修改内容: 
 */
package com.mfh.comna.comn.seq;

import com.mfh.comn.bean.Pair;
import com.mfh.comna.comn.database.dao.BaseDbDao;

/**
 * 序列业务接口
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-28
 * @since SHK BMP 1.0
 */
public class SequenceDao extends BaseDbDao<Sequence, String> {
    
    /**
     * 获取序列
     * @param sequenceName 序列名称
     * @author LuoJingtian created on 2011-12-28 
     * @since SHK BMP 1.0
     */
    public Sequence getSequence(String sequenceName) {
        return getEntityById(sequenceName);
    }

    @Override
    protected Class<String> initPkClass() {
        return String.class;
    }
    
    /**
     * 更新序列
     * @param sequence 序列对象
     * @author LuoJingtian created on 2011-12-28 
     * @since SHK BMP 1.0
     */
    public void updateSequence(Sequence sequence) {
        update(sequence);
    }
    
    /**
     * 插入序列初始值
     * @param sequence
     */
    public void insertSequence(Sequence sequence){
        save(sequence);
    }

    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("序列", null);
    }

    @Override
    protected Class<Sequence> initPojoClass() {
        return Sequence.class;
    }
}
