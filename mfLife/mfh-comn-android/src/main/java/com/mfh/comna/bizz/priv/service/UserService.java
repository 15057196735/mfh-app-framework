package com.mfh.comna.bizz.priv.service;

import java.util.Set;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TOfficeInfo;
/**
 * 用户服务类接口
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public interface UserService {
    IUser createOrGetSysUser();
    IUser getUserById(String userId);
    String getLoginNameById(String id);
    IUser getUserByLoginname(String loginName); 
    /**
     * 修改密码
     * UserId:修改的用户ID;strOldPwd:原密码;strNewPwd：新密码;strGrantId：修改人的ID
     */
    void modifyPassword(String UserId, String strOldPwd, String strNewPwd, String strGrantId);
    void update(IUser pa_user);//,String grantUserId
    void save(IUser pa_User, String grantUserId);
    //List getSubUser(String CurrentUserId);
    //List<TOfficeInfo> getOffices(IUser pa_user);
    void setOffices(String userId, Set<TOfficeInfo> offices, String grantUserId);
    
    /**
     * 给用户添加多个角色
     * @param userId
     * @param groupIds
     * @author zhangyz created on 2012-5-25
     */
    public void addUserGroups(String userId, String[] groupIds);

    /**
     * 删除一个用户的多个角色
     * @param userId
     * @param groupIds
     * @author zhangyz created on 2012-6-9
     */
    public void deleteUserGroups(String userId, String[] groupIds);
    /**
     * 给一个用户增加多个单位
     * @param userId
     * @param officeIds
     * @author zhangyz created on 2012-6-9
     */
    public void addUserOffices(String userId, String[] officeIds);
    
    /**
     * 给一个用户删除多个单位
     * @param userId
     * @param officeIds
     * @author zhangyz created on 2012-6-9
     */
    public void deleteUserOffices(String userId, String[] officeIds);
    
    public void deleteByUser(IUser user);
    
}
