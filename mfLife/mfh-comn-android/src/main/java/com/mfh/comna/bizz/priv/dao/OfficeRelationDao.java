package com.mfh.comna.bizz.priv.dao;

import java.util.List;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.priv.bean.TOfficeInfo;
import com.mfh.comn.priv.bean.TROffice;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

import net.tsz.afinal.FinalDb;

/**
 * 单位关系维护dao对象
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class OfficeRelationDao extends BaseSeqAbleDao<TROffice, Integer> {

    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("单位关系", "T_R_OFFICES");
    }

    @Override
    protected Class<Integer> initPkClass() {
        return Integer.class;
    }

    @Override
    protected Class<TROffice> initPojoClass() {
        return TROffice.class;
    }
    
    /**
     * 判断指定的单位是否为根单位
     * @param officeId
     * @return
     * @author zhangyz created on 2012-8-27
     */
    public boolean isRootOffice(String officeId){
        String parent = getParentOfficeId(officeId);
        if (parent == null)
            return true;
        if (TOfficeInfo.ROOT_INIT.equals(parent))
            return true;
        else
            return false;
    }
    
    /**
     * 获取一个单位的父单位
     * @param officeId
     * @return
     * @author zhangyz created on 2012-8-26
     */
    public String getParentOfficeId(String officeId){
        this.getDb().checkTableExist(TROffice.class);
        return this.getDb().findBySql(String.class, "select officeId from T_R_OFFICES where subOfficeId = ?"
                , new String[] {officeId});
    }
    
    //private static String SUBOFFICEQSQL = "select count(*) from T_R_OFFICES rf where rf.officeId = ? and rf.subOfficeId = ?";
    //private static String SUBOFFICEISQL = "insert into T_R_OFFICES (officeId, subOfficeId) values(?, ?)";
    
    private String genPkWhereClause(TROffice rOffice) {
        String where = "officeId = '" + rOffice.getOfficeId() + "' and subOfficeId = '" + rOffice.getSubOfficeId() + "'";
        return where;
    }
    /**
     * 给一个单位添加子单位，如果已经存在则退出
     * @param officeId
     * @param subOfficeId
     * @author zhangyz created on 2012-6-29
     */
    public void addSubOffice(String officeId, String subOfficeId){
        FinalDb persist = getPersist();
        try {
            TROffice rf = new TROffice(officeId, subOfficeId);
            Integer subOffices = persist.findCount(this.pojoClass, genPkWhereClause(rf)); //persist.findBySql(Integer.class, SUBOFFICEQSQL, params);
            if (subOffices != null && subOffices.intValue() >= 1)//已经存在
                return;            
            this.save(rf);
            
            if (!officeId.equals(TOfficeInfo.ROOT_INIT)) {                
                rf = new TROffice(TOfficeInfo.ROOT_INIT, subOfficeId);//从虚拟目录下移除
                persist.deleteByWhere(pojoClass, genPkWhereClause(rf));
            }
        }
        finally{
            release(persist);
        }        
    }
    
    //private static String SUBOFFICEDSQL1 = "delete from T_R_OFFICES where officeId = ?";
    //private static String SUBOFFICEDSQL2 = "delete from T_R_OFFICES where officeId = ? and subOfficeId = ?";
    
    /**
     * 删除一个单位的指定子单位
     * @param officeId 父单位
     * @param subOfficeId 子单位，若为空，代表删除该父单位的所有单位
     * @author zhangyz created on 2012-6-29
     */
    public void deleteSubOffice(String officeId, String subOfficeId){
        FinalDb persist = getPersist();
        try {
            if (subOfficeId == null) {
                persist.deleteByWhere(this.pojoClass, "officeId = '" + officeId + "'");
                //persist.exeSql(SUBOFFICEDSQL1, new String[] {officeId});
            }
            else {         
                TROffice rf = new TROffice(officeId, subOfficeId);
                persist.deleteByWhere(this.pojoClass, this.genPkWhereClause(rf));// .exeSql(SUBOFFICEDSQL2, new Object[]{officeId, subOfficeId});
            }
        }
        finally{
            release(persist);
        }
    }
    
    private static String PCSQL = "select office.id as id, office.officeName as name, rf.officeId as pid from T_OFFICEINFO office Left Join T_R_OFFICES rf on office.id = rf.subOfficeId where rf.officeId = ?";  
    
    /**
     * 查询指定单位的子单位编码集合
     * @param pid
     * @return
     * @author zhangyz created on 2012-8-1
     */
    public List<ParentChildItem> queryChildCodes(String pid){
        FinalDb persist = getPersist();
        try {
            persist.checkTableExist(TROffice.class);
            //直接使用ParentChildItem,会很慢,因为不是实际的表.v_user是视图。
            List<ParentChildItem> list =  persist.findAllBySql(ParentChildItem.class, PCSQL, null, null, new String[] {pid});
            return list;
        }
        finally{
            release(persist);
        }        
    }
}
