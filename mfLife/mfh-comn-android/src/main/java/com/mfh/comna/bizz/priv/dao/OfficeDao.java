package com.mfh.comna.bizz.priv.dao;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.priv.bean.TOfficeInfo;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

/**
 * 单位dao对象
 * @param <T>
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class OfficeDao extends BaseSeqAbleDao<TOfficeInfo, String> {

    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("单位信息", "T_OFFICEINFO");
    }

    @Override
    protected Class<TOfficeInfo> initPojoClass() {
        return TOfficeInfo.class;
    }

    @Override
    protected Class<String> initPkClass() {
        return String.class;
    }

    /**
     * 根据名称查询单位列表
     * @param name
     * @return
     * @author zhangyz created on 2012-8-1
     */
    public List<TOfficeInfo> getOfficeByName(String name) {
        return this.getDb().findAllByWhere(this.pojoClass, "officeName='" + name + "'");
    }
    
    /**
     * 根据单位编码获取单位
     * @param code
     * @return
     * @author zhangyz created on 2012-8-1
     */
    public TOfficeInfo getOfficeByCode(String code){
        List<TOfficeInfo> offices = this.getDb().findAllByWhere(this.pojoClass, 
                "officeCode='" + code + "'");
        if (offices != null && offices.size() > 0)
            return offices.get(0);
        else
            return null;
    }

    private static String ORSQL = "select office.* from T_OFFICEINFO office Left Join T_R_OFFICES rf on office.id = rf.subOfficeId where rf.officeId = ?";
    /**
     * 根据单位号查询其所有子单位
     * @param pid
     * @return
     */
    public List<TOfficeInfo> getOfficesByParentId(String pid){
        return this.getDb().findAllBySql(this.pojoClass, ORSQL, null, null, new String[] {pid});
    }

    /**
     * 获取一个单位的根单位编码值
     * @param officeId
     * @return
     * @author zhangyz created on 2012-8-26
     */
    public TOfficeInfo getRootOfficeInfo(String officeId){        
        String rootId = getRootOfficeId(officeId);
        if (rootId == null)
            return null;
        TOfficeInfo rootOffice = (TOfficeInfo)getEntityById(rootId);
        return rootOffice;
    }
    
    /**
     * 获取一个单位的根单位编号
     * @param officeId
     * @return
     * @author zhangyz created on 2012-8-26
     */
    public String getRootOfficeId(String officeId){
        if (TOfficeInfo.ROOT_INIT.equals(officeId))
            return null;
        String curId = officeId;
        do{
            String parent = getDb().findBySql(String.class, "select officeId from T_R_OFFICES where subOfficeId = ?"
                    , new String[] {curId});
            if (StringUtils.isBlank(parent) || parent.equals(TOfficeInfo.ROOT_INIT))
                return curId;
            else
                curId = parent;
        }
        while (true);
    }
}
