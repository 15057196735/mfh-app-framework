package com.mfh.comna.bizz.priv.dao;

import java.util.List;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.priv.bean.TGroup;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

/**
 * 角色定义信息
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class GroupDao extends BaseSeqAbleDao<TGroup, String> {

    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("角色信息", "T_GROUP");
    }

    @Override
    protected Class<TGroup> initPojoClass() {
        return TGroup.class;
    }

    @Override
    protected Class<String> initPkClass() {
        return String.class;
    }
    
    /**
     * 查询一个用户创建的所有角色
     * @param userId
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<TGroup> queryUserCreatedGroups(String userId) {
        return getDb().findAllByWhere(pojoClass, "createid = '" + userId + "'");
    }
    
    /**
     * 查询一个角色的所有子角色
     * @param groupId
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<TGroup> queryChilds(String groupId) {
        return this.getDb().findAllByWhere(this.pojoClass, "pgroupid='" + groupId + "'");
    }
    
    /**
     * 查询一个角色的所有自角色，按照编码形式
     * @param groupId
     * @return
     * @author zhangyz created on 2013-6-13
     */
    public List<ParentChildItem> queryChildCodes(String groupId) {
        String sql = "select groupid as id, gname as name from T_GROUP where pgroupid = ?";
        return getDb().findAllBySql(ParentChildItem.class, sql, new String[] {groupId});
    }
    
    /**
     * 查询所有顶层角色
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<ParentChildItem> queryRootGroupCodes() {
        String sql = "select groupid as id, gname as name from T_GROUP where pgroupid is null or pgroupid = ''";
        return getDb().findAllBySql(ParentChildItem.class, sql, null);        
    }
    
    /**
     * 查询所有顶层角色
     * @return
     * @author zhangyz created on 2013-6-13
     */
    public List<TGroup> queryRootGroups() {
        String sql = "select * from T_GROUP where pgroupid is null or pgroupid = ''";
        return getDb().findAllBySql(TGroup.class, sql, null);
    }    
}
