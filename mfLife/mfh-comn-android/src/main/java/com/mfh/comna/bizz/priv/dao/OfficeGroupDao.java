package com.mfh.comna.bizz.priv.dao;

import java.util.List;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.priv.bean.TOfficeInfo;
import com.mfh.comn.priv.bean.TRGroupOffice;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

import net.tsz.afinal.FinalDb;

/**
 * 单位和角色的关系定义
 * @param <T>
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class OfficeGroupDao extends BaseSeqAbleDao<TRGroupOffice, Integer> {
    
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("单位角色信息", "T_R_GROUP_OFFICE");
    }
    
    @Override
    protected Class<Integer> initPkClass() {
        return Integer.class;
    }

    @Override
    protected Class<TRGroupOffice> initPojoClass() {
        return TRGroupOffice.class;
    }
    
    /**
     * 根据角色号查询单位
     * create on : 2011-5-24
     * @author jinj
     * @param groupid 角色编号
     * @return
     * @throws Exception
     */
    public List<TOfficeInfo> getOfficesByGroupId(String groupId) {
        FinalDb persist = getPersist();
        List<TOfficeInfo> resList = null;//new ArrayList();
        try {
            resList = persist.findAllBySql(TOfficeInfo.class, "select office.* from T_OFFICEINFO office, T_R_GROUP_OFFICE goff where office.officeId = goff.officeId and goff.groupId=?", 
                    null, null, new String[] {groupId});
        }
        catch (Exception e) {
            throw new RuntimeException("获取单位角色转授权信息出错：" + e.getMessage(), e);
        }
        finally{
            release(persist);
        }
        return resList;
    }
    
    /**
     * 生成where条件
     * @param groupOffice
     * @return
     * @author zhangyz created on 2013-6-13
     */
    private String genPkWhereClause(TRGroupOffice groupOffice) {
        String where = "groupid = '" + groupOffice.getGroupid() 
                + "' and officeid = '" + groupOffice.getOfficeid() + "'";
        return where;
    }

    /**
     * 删除一个角色所有的所有单位关系
     * @param groupId
     * @author zhangyz created on 2012-5-25
     */
    public void deleteOfficesByGroupId(String groupId){
        this.getDb().deleteByWhere(this.pojoClass, "groupid = '" + groupId + "'");
    }
    
    /**
     * 查询角色-单位关系是否存在
     * @param groupOffice
     * @return
     * @author zhangyz created on 2012-6-25
     */
    public int queryGroupOfficeCount(TRGroupOffice groupOffice) {
        return this.getDb().findCount(pojoClass, genPkWhereClause(groupOffice));
    }
    

    /**
     * 删除一个角色和单位关系
     * @param groupOffice
     * @author zhangyz created on 2012-6-9
     */
    public void deleteGroupOffice(TRGroupOffice groupOffice) {
        this.getDb().deleteByWhere(pojoClass, genPkWhereClause(groupOffice));
    }   
    
    /**
     * 删除一个单位所有的角色关系
     * @param officeId
     * @author zhangyz created on 2012-5-25
     */
    public void deleteGroupsByOfficeId(String officeId) {
        getDb().deleteByWhere(pojoClass, "officeid = '" + officeId + "'");
    }
}
