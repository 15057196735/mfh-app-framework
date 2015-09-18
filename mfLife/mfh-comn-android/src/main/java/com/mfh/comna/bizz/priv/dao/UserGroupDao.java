package com.mfh.comna.bizz.priv.dao;

import java.util.List;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.priv.bean.TGroup;
import com.mfh.comn.priv.bean.TRUserGroup;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

/**
 * 用户角色关联的dao操作，用户有哪些角色
 * 
 * @author zhangyz created on 2013-5-10
 * @since Framework 1.0
 */
public class UserGroupDao extends BaseSeqAbleDao<TRUserGroup, Integer> {
    
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<>("工作岗位", "T_R_USER_GROUP");
    }

    @Override
    protected Class<Integer> initPkClass() {
        return Integer.class;
    }

    @Override
    protected Class<TRUserGroup> initPojoClass() {
        return TRUserGroup.class;
    }

    private String genWhereClause(TRUserGroup userGroup) {
        String where = "groupid = '" + userGroup.getGroupid() + "' and userid = '" + userGroup.getUserid() + "'";
        return where;
    }
    
    /**
     * 查询角色-用户关系是否存在
     * @param userGroup
     * @return
     * @author zhangyz created on 2012-6-25
     */
    public int queryGroupUserCount(TRUserGroup userGroup) {
        return this.getDb().findCount(this.pojoClass, genWhereClause(userGroup));
    }
    
    /**
     * 删除一个角色和用户关系
     * @param userGroup
     * @author zhangyz created on 2012-5-30
     */
    public void deleteGroupUser(TRUserGroup userGroup) {
        getDb().deleteByWhere(pojoClass, genWhereClause(userGroup));
    }  

    /**
     * 删除一个角色所有的所有用户关系
     * @param groupId
     * @author zhangyz created on 2012-5-25
     */
    public void deleteUsersByGroupId(String groupId) {
        getDb().deleteByWhere(pojoClass, "groupid = '" + groupId + "'");
    }    

    
    /**
     * 删除一个用户所有的角色关系
     * @param userId
     * @author zhangyz created on 2012-5-25
     */
    public void deleteGroupsByUserId(String userId) {
        getDb().deleteByWhere(pojoClass, "userid = '" + userId + "'");
    }

    /**
     * 查询一个角色下的所有用户
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<String> queryUserIdsByGroupId(String groupId) {
        return getDb().findAllBySql(String.class, 
                "select userid as id from T_R_USER_GROUP where groupid = ?", new String[]{groupId});
    }
    
    /**
     * 查询一个用户所拥有的角色编号
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<String> queryGroupIdsByUserId(String userId){
        String sql = "select groupid from T_R_USER_GROUP where userid = ?";
        return getDb().findAllBySql(String.class, sql, new String[]{userId});
    }
    
    /**
     * 查询一个用户所拥有的角色(即头衔)信息
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<TGroup> queryGroupsByUserId(String userId) {
        String sql = "select gp.* from T_GROUP gp, T_R_USER_GROUP ru where ru.userid=? and ru.groupid = gp.id";
        return getDb().findAllBySql(TGroup.class, sql, new String[]{userId});
    }
        
    /**
     * 获取某用户的头衔列表
     * @param userId
     * @return
     * @author zhangyz created on 2013-5-10
     */
   /* public List<String> queryUserGroups(String userId) {
        TRUserGroup group;
        if (userId.startsWith("654321")) {
            group = new TRUserGroup();
            group.setGroupid("");
            group.setUserid(userId);
            group.setRemark("杭州鼎夏科技 总经理");
        }
        else {
            group = new TRUserGroup();
            group.setGroupid("");
            group.setUserid(userId);
            group.setRemark("杭州鼎夏科技 副总经理");
        }
        List<String> ret = new ArrayList<String> ();
        ret.add(group.getRemark());
        return ret;
    }*/
}
