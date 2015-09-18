package com.mfh.comna.bizz.priv.dao;

import java.util.List;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TUser;
import com.mfh.comna.comn.database.dao.BaseSeqAbleDao;

/**
 * 用户dao对象
 * 
 * @author zhangyz created on 2013-6-12
 * @since Framework 1.0
 */
public class UserDao extends BaseSeqAbleDao<TUser, String> {
    
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<String, String>("用户", "T_USER");
    }

    @Override
    protected Class<TUser> initPojoClass() {
        return TUser.class;
    }

    @Override
    protected Class<String> initPkClass() {
        return String.class;
    }
    
    /**
     * 通过用户名模糊查询指定的用户
     * @param name
     * @param pageInfo
     * @return
     * @author zhangyz created on 2013-5-10
     */
    public List<TUser> queryUserInfos(String name, PageInfo pageInfo) {
        String where = "type <> '" + TUser.USERTYPE_SYS + "'";
        if (name != null && name.length() > 0) {
            where = " and (firstname like '%" + name + "%' or lastname like '%" + name + "%')";
        }
        List<TUser> users = this.getDb().findAllByWhere(TUser.class, 
                where, "letterIndex", pageInfo);        
        return users;
    }

    /**
     * 根据登录名查找指定的用户
     * @param loginName
     * @return
     * @author zhangyz created on 2013-6-12
     */
    public IUser getUserByLoginname(String loginName) {
        List<TUser> users = this.getDb().findAllByWhere(this.pojoClass, "loginname='" + loginName + "'");
        if (users == null || users.size() == 0)
            return null;
        else
            return users.get(0);
    }
}
