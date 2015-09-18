package com.mfh.comna.bizz.priv.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TOfficeInfo;
import com.mfh.comn.priv.bean.TRUOffice;
import com.mfh.comn.priv.bean.TUser;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

import net.tsz.afinal.FinalDb;

/**
 * 用户单位关系dao对象
 * @param <T>
 * @author zhangyz created on 2013-6-12
 * @since Framework 1.0
 */
public class UserOfficeDao extends BaseSeqAbleDao<TRUOffice, Integer> {
    private UserDao userDao = null;
    private OfficeDao officeDao = null;
       
    public UserDao getUserDao() {
        if (userDao == null)
            userDao = new UserDao();
        return userDao;
    }

    @Override
    protected Class<Integer> initPkClass() {
        return Integer.class;
    }
    
    public OfficeDao getOfficeDao() {
        if (officeDao == null)
            officeDao = new OfficeDao();
        return officeDao;
    }

    @Override
    protected Pair<String,String> initTableChName() {
        return new Pair<String, String>("用户单位", "T_R_OFFICE_USER");
    }

    @Override
    protected Class<TRUOffice> initPojoClass() {
        return TRUOffice.class;
    }
    
    private String genWhereClause(TRUOffice userOffice) {
        String where = "userid='" + userOffice.getUserid() 
                + "' and officeid='" + userOffice.getOfficeid() + "'";
        return where;
    }

    /**
     * 删除一个用户单位关系
     * @param userOffice
     * @author zhangyz created on 2012-8-1
     */
    public void deleteUserOffice(TRUOffice userOffice){
        getDb().deleteByWhere(TRUOffice.class, genWhereClause(userOffice));
    }
    
    /**
    * 删除一个单位的用户关系
    * @param userId
    * @author zhangyz created on 2012-8-1
    */
   public void deleteUoRelationByOfficeId(String officeId){
       getDb().deleteByWhere(this.pojoClass, "officeId='" + officeId + "'");
   }
   
   /**
    * 删除一个用户的单位关系
    * @param userId
    * @author zhangyz created on 2013-6-13
    */
   public void deleteUoRelationByUserId(String userId) {
       getDb().deleteByWhere(this.pojoClass, "userid='" + userId + "'");
   }
    
    /**
     * 查询指定的用户单位关系是否存在
     * @param userOffice
     * @return
     * @author zhangyz created on 2012-6-25
     */
    public int queryUserOfficeCount(TRUOffice userOffice){        
        return this.getDb().findCount(TRUOffice.class, genWhereClause(userOffice));
    }
    
    /**
     * 插入一个用户单位关系
     * @param userOffice
     * @author zhangyz created on 2012-8-1
     */
    public void insertUserOffice(TRUOffice userOffice){
        this.save(userOffice);
    }
    
    /**
     * 实现接口，根据用户号获取其所对应的部门信息和单位编号。
     * TOfficeInfo.id为部门信息
     * TOfficeInfo.tenantName为单位编号
     */
    public List<TOfficeInfo> getOfficesByUserId(String userId){
        String sql = "select * from " + getOfficeDao().getTableName() + " office, " + this.getTableName() +  " ru where ru.userid=? and ru.officeid = office.id";
        return this.getDb().findAllBySql(TOfficeInfo.class, sql, null, null, new String[] {userId});
    }
    
    /**
     * 根据单位获取对应的用户
     * @param officeId
     * @return
     * @author zhangyz created on 2012-8-1
     */
    public List<IUser> getUsersByOfficeId(String officeId){
        String sql = "select * from " + getUserDao().getTableName() + " us, " 
                + this.getTableName() +  " ru where ru.officeid=? and ru.userid = us.id";
        List<TUser> tusers = this.getDb().findAllBySql(TUser.class, sql, null, null, new String[] {officeId});
        List<IUser> users = new ArrayList<IUser>();
        for (TUser user : tusers){
            users.add(user);
        }
        return users;
    }
    
    /**
     * 保存一个用户所有的单位信息
     * @param userId
     * @param offices
     * @author zhangyz created on 2012-4-2
     */
    public void saveUserOfficeInfos(String userId, Set<TOfficeInfo> offices){
        if (offices.size() <= 0)
            return;
        FinalDb persist = getPersist();
        Iterator<TOfficeInfo> iter = offices.iterator();
        //String sql = "insert into T_R_OFFICE_USER (OFFICEID,USERID) values(?, ?)";
        while (iter.hasNext()){
            TOfficeInfo office = iter.next();
            TRUOffice ru = new TRUOffice();
            ru.setUserid(userId);
            ru.setOfficeid(office.getId());
            persist.save(ru);
        }
        release(persist);
    }
}
