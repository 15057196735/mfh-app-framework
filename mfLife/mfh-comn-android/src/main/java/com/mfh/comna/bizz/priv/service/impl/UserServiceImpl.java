package com.mfh.comna.bizz.priv.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TOfficeInfo;
import com.mfh.comn.priv.bean.TRUOffice;
import com.mfh.comn.priv.bean.TRUserGroup;
import com.mfh.comn.priv.bean.TUser;
import com.mfh.comn.utils.UuidUtil;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.bizz.priv.dao.UserDao;
import com.mfh.comna.bizz.priv.dao.UserGroupDao;
import com.mfh.comna.bizz.priv.dao.UserOfficeDao;
import com.mfh.comna.bizz.priv.service.UserService;

/**
 * 用户服务类
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class UserServiceImpl extends BaseService<TUser, String, UserDao> implements UserService {
    protected UserDao userDao = null;
    //protected GroupDao groupDao = new GroupDao();
    protected UserGroupDao userGroupDao = new UserGroupDao();
    protected UserOfficeDao userOfficeDao = new UserOfficeDao();

    @Override
    protected Class<UserDao> getDaoClass() {
        return UserDao.class;
    }
    
    public UserServiceImpl() {
        super();
        userDao = (UserDao) dao;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }


    /**
     * 保存一个用户时填充额外的自动信息
     * @param user
     * @return 是新增还是修改
     * @author zhangyz created on 2012-5-17
     */
    protected boolean initSaveUser(TUser user){
        boolean isSys = false;
        if (TUser.USER_SYS.equals(user.getLoginname()))
                isSys = true;
        if (!isSys && StringUtils.isBlank(user.getEditid()))
            user.setEditid(getPrivSession().getCurrentUserId());//否则会递归调用
        if (user.getEditdate() == null)
            user.setEditdate(new Date());
        if (StringUtils.isBlank(user.getId())){//是新增
            user.setId(UuidUtil.getUuid());  
            if (!isSys && StringUtils.isBlank(user.getCreateid()))
                user.setCreateid(getPrivSession().getCurrentUserId());//否则会递归调用
            if (user.getCreatedate() == null){
                user.setCreatedate(new Date());
            }
            String loginName = user.getLoginname();
            
            //if (StringUtils.isBlank(loginName))
            //    user.setLoginname(genUniqLoginNameByCName(genLoginNameByCName(user.getFullName())));
            
            if (loginName != null && userDao.getUserByLoginname(loginName) != null)
                throw new RuntimeException("登录名已经存在");
            return true;
        }
        else{//是修改
            String loginName = user.getLoginname();
            if (StringUtils.isBlank(loginName))
                throw new RuntimeException("登录名不能为空!");
            else{
                IUser old = userDao.getEntityById(user.getId());
                if (old == null)
                    throw new RuntimeException("指定的用户编号不存在!");                           
                if (old.getLoginname().equals(loginName) == false){//说明登录名修改了
                    if (userDao.getUserByLoginname(loginName) != null)
                        throw new RuntimeException("登录名已经存在!");                        
                }
            }
            return false;
        }
    }
    
    /**
     * 新增或修改一个用户
     * @param pa_User
     * @param grantUserId
     * @return 返回是新增还是修改,true代表新增
     * @author zhangyz created on 2012-7-29
     */
    protected boolean saveUserInner(TUser pa_User, String grantUserId){
        pa_User.setCreateid(grantUserId);
        boolean bInsert = initSaveUser(pa_User);
        if (StringUtils.isBlank(pa_User.getPassword()))
            throw new RuntimeException("用户密码不能为空!");
        if (bInsert){
            userDao.save(pa_User);
        }
        else{
            userDao.update(pa_User);
        }
        return bInsert;
    }
    
    /**
     * 内部辅助函数，删除一个用户的相关联信息。
     * @param user
     * @author zhangyz created on 2012-7-17
     */
    protected void deleteUserRelationInfo(IUser user){
        String userId = user.getId();
        //先删除office端，跟这个用户的关系
        userOfficeDao.deleteUoRelationByUserId(userId);        
        //删除角色信息
        userGroupDao.deleteGroupsByUserId(userId);        
        //删除授权信息
        //PrivService.getPrivSerice().getuPrivService().deleteEntityById(new PrivPk(userId)); 
    }
    
    public void deleteByUser(IUser user){  
        deleteUserRelationInfo(user);
        userDao.deleteById(user.getId());
    }

    @Override
    public IUser createOrGetSysUser() {
        String loginName = TUser.USER_SYS;//USER_SYS
        IUser user = getUserByLoginname(loginName);
        if (user == null){
            try {
                user = (IUser)(userDao.getPojoClass()).newInstance();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("未能创建指定的用户对象:" + userDao.getPojoClass().getName(), e);
            }
            //user.setId(UuidUtil.getUuid());
            user.setType(TUser.USERTYPE_SYS);//系统管理员;
            user.setFullName("系统");
            user.setLoginname(loginName);
            user.setPassword("123456");
            save(user, "INIT");
        }        
        //默认添加一个单位;放在初始化脚本中了。
        /*fields[0] = "createId";
        params[0] = "INIT";
        define = new QueryDefine(fields,params);
        list = session.daoOffice.searchOffices(define);
        if (list.size() <= 0){
            TOfficeInfo office = new TOfficeInfo();
            office.setId("INIT");
            office.setOfficeCode("INIT");
            office.setOfficeName("顶层虚拟单位");
            office.setCreateId("INIT");
            office.setCreateDate(new Date());
            session.daoOffice.saveOffice(office, TOfficeInfo.ROOT_INIT);
        }*/
        return user;
    }

    @Override
    public IUser getUserById(String userId) {
        return userDao.getEntityById(userId);
    }

    @Override
    public String getLoginNameById(String id) {
        IUser user = userDao.getEntityById(id);
        if (user != null){
            return user.getLoginname();
        }
        else
            return null;
    }

    @Override
    public IUser getUserByLoginname(String loginName) {
        return userDao.getUserByLoginname(loginName);
    }

    @Override
    public void modifyPassword(String userId, String strOldPwd, String strNewPwd, String strGrantId) {
        TUser user = userDao.getEntityById(userId);
        if(user == null)
            throw new RuntimeException("没有找到" + userId + "用户");
        //判断该用户能否自己修改密码;
        if(userId.equals(strGrantId)){
            //if(user.getNoeditpass().equals("1"))
            //    throw new RuntimeException("不允许该用户自己修改密码!");
        }
        if(user.getPassword().equals(strOldPwd)){
            user.setPassword(strNewPwd);
        }
        else
            throw new RuntimeException("原密码输入不正确!");
        userDao.update(user);        
    }

    @Override
    public void update(IUser pa_user) {
        userDao.update((TUser)pa_user);
        
    }
    
    /**
     * 保存用户基础信息，不保存用户权限和用户组信息，
     * @param pa_User
     * @param grantUserId
     * @param rUPrivDao
     * @param daoModule
     * @throws HibernateException
     */
    @Override
    public void save(IUser pa_User, String grantUserId){
        saveUserInner((TUser)pa_User, grantUserId);
    }

    @Override
    public void setOffices(String userId, Set<TOfficeInfo> offices, String grantUserId) {
        IUser user = this.getUserById(userId);
        if(user == null)
            throw new RuntimeException("未找到"+userId+"用户！");
        setOffices(user, offices, grantUserId);
    }   

    public void setOffices(IUser user, Set<TOfficeInfo> offices, String grantUserId) {   
        try{
            userOfficeDao.deleteUoRelationByUserId(user.getId());
            userOfficeDao.saveUserOfficeInfos(user.getId(), offices);
            //this.update(user);//grantUserId
        }
        catch(Exception e){
            throw new RuntimeException("设置用户单位失败：" + e.getMessage(), e);
        }
    }  
    
    @Override
    public void addUserGroups(String userId, String[] groupIds) {
        TRUserGroup userGroup = null;
        for (int ii = 0; ii < groupIds.length; ii++){
            if (StringUtils.isBlank(groupIds[ii]))
                continue;
            userGroup = new TRUserGroup(groupIds[ii], userId);
            int count = userGroupDao.queryGroupUserCount(userGroup);
            if (count <= 0)
                userGroupDao.save(userGroup);
        }        
    }
    
    public void addUserGroups(String userId, List<String> groupIds) {
        TRUserGroup userGroup = null;
        for (int ii = 0; ii < groupIds.size(); ii++){
            if (StringUtils.isBlank(groupIds.get(ii)))
                continue;
            userGroup = new TRUserGroup(groupIds.get(ii), userId);
            int count = userGroupDao.queryGroupUserCount(userGroup);
            if (count <= 0)
                userGroupDao.save(userGroup);
        }        
    }

    @Override
    public void deleteUserGroups(String userId, String[] groupIds) {
        TRUserGroup userGroup = null;
        for (int ii = 0; ii < groupIds.length; ii++){
            userGroup = new TRUserGroup(groupIds[ii], userId);
            userGroupDao.deleteGroupUser(userGroup);
        } 
    }

    @Override
    public void addUserOffices(String userId, String[] officeIds) {
        for (int ii = 0; ii < officeIds.length; ii++){
            TRUOffice uoffice = new TRUOffice(userId, officeIds[ii]);
            if (userOfficeDao.queryUserOfficeCount(uoffice) <= 0)
                userOfficeDao.insertUserOffice(uoffice);
        }        
    }

    @Override
    public void deleteUserOffices(String userId, String[] officeIds) {
        for (int ii = 0; ii < officeIds.length; ii++){
            TRUOffice uoffice = new TRUOffice(userId, officeIds[ii]);
            userOfficeDao.deleteUserOffice(uoffice);
        }
    }
}
