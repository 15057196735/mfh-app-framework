package com.mfh.comna.bizz.priv.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.mfh.comn.bean.IObject;
import com.mfh.comn.code.UnionCode;
import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.priv.bean.TGroup;
import com.mfh.comn.priv.bean.TRGroupOffice;
import com.mfh.comn.priv.bean.TRUserGroup;
import com.mfh.comn.utils.UuidUtil;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.bizz.priv.dao.GroupDao;
import com.mfh.comna.bizz.priv.dao.OfficeGroupDao;
import com.mfh.comna.bizz.priv.dao.UserGroupDao;
import com.mfh.comna.bizz.priv.service.GroupService;

/**
 * 角色服务类
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class GroupServiceImpl extends BaseService<TGroup, String, GroupDao> implements GroupService{
    private UserGroupDao userGroupDao = new UserGroupDao();
    private OfficeGroupDao officeGroupDao = new OfficeGroupDao();
    
    protected void initSave(IObject item){
        //super.initSave(item);
        TGroup tGroup = (TGroup)item;
        if (StringUtils.isNotBlank(tGroup.getPgroupid())){
            tGroup.setPgroupid(UnionCode.getLastCodeInLastTable(tGroup.getPgroupid()));
        }
    }

    @Override
    protected Class<GroupDao> getDaoClass() {
        return GroupDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }

    /**
     * 插入一个角色
     * @param tGroup
     * @author zhangyz created on 2012-5-24 
     * @since
     */
     @Override
    public void insertTGroup(TGroup tGroup) {
         initSave(tGroup);
        if (StringUtils.isBlank(tGroup.getId()))
            tGroup.setId(UuidUtil.getUuid());
        if (tGroup.getUpdateDate() == null)
            tGroup.setUpdateDate(new Date());
        if (StringUtils.isBlank(tGroup.getCreateid()))
            tGroup.setCreateid(this.getPrivSession().getCurrentUserId());
        if (StringUtils.isBlank(tGroup.getUpdatorId()))
            tGroup.setUpdatorId(this.getPrivSession().getCurrentUserId());
        getDao().save(tGroup);
    }
    
    /**
     * 删除一个角色
     * @param id
     * @author zhangyz created on 2012-5-24
     * @since
     */
     @Override
     public void deleteGroupById(String groupid) {
        userGroupDao.deleteUsersByGroupId(groupid);
        /*PrivPk pk = new PrivPk();
        pk.setMasterId(groupid);
        gpService.deleteEntityById(pk);*/
        
        getDao().deleteById(groupid);
    }
    
    /**
     * 更新一个角色
     * @param tGroup
     * @author zhangyz created on 2012-5-24 
     * @since
     */
    @Override
    public void updateTGroup(TGroup tGroup) {
        initSave(tGroup);
        if (tGroup.getId().equals(tGroup.getPgroupid()))
            throw new RuntimeException("父角色不能是自己!");
        tGroup.setUpdateDate(new Date());
        tGroup.setUpdatorId(this.getPrivSession().getCurrentUserId());
        getDao().update(tGroup);
    }
    
    /**
     * 查询一个角色
     * @param groupid
     * @author zhangyz created on 2012-5-24
     * @since
     */
    @Override
    public TGroup getTGroup(String groupid) {
        return getDao().getEntityById(groupid);
    }
    
    @Override
    public void addGroupUser(String groupId, String userId){
        TRUserGroup userGroup = new TRUserGroup(groupId, userId);
        if (userGroupDao.queryGroupUserCount(userGroup) <= 0)
            userGroupDao.save(userGroup);
    }

    @Override
    public void deleteGroupUser(String groupId, String userId){
        if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(userId)){
            TRUserGroup userGroup = new TRUserGroup(groupId, userId);
            userGroupDao.deleteGroupUser(userGroup);
        }
        else if (StringUtils.isNotBlank(userId)){
            userGroupDao.deleteGroupsByUserId(userId);
        }
        else
            userGroupDao.deleteUsersByGroupId(groupId);
    }

    @Override
    public void deleteGroupUsers(String groupId, String[] userIds){
        TRUserGroup userGroup;
        for (String id : userIds){
            userGroup = new TRUserGroup(groupId, id);
            userGroupDao.deleteGroupUser(userGroup);
        }        
    }

    @Override
    public void addGroupUsers(String groupId, String[] userIds){
        TRUserGroup userGroup;
        for (String id : userIds){
            userGroup = new TRUserGroup(groupId, id);
            if (userGroupDao.queryGroupUserCount(userGroup) <= 0)
                userGroupDao.save(userGroup);
        }
    }
    
    public void addGroupUsers(String groupId, List<String> userIds){
        TRUserGroup userGroup = null;
        for (int ii = 0; ii < userIds.size(); ii++){
            userGroup = new TRUserGroup(groupId, userIds.get(ii));
            if (userGroupDao.queryGroupUserCount(userGroup) <= 0)
                userGroupDao.save(userGroup);
        }
    }

    @Override
    public void addGroupOffice(String groupId, String officeId) {
        TRGroupOffice groupOffice = new TRGroupOffice(groupId, officeId);
        if (officeGroupDao.queryGroupOfficeCount(groupOffice) <= 0)
            officeGroupDao.save(groupOffice);        
    }

    @Override
    public void addGroupOffices(String groupId, String[] officeIds) {
        TRGroupOffice groupOffice = null;
        for (String officeId : officeIds){
            groupOffice = new TRGroupOffice(groupId, officeId);
            if (officeGroupDao.queryGroupOfficeCount(groupOffice) <= 0)
                officeGroupDao.save(groupOffice);
        }        
    }
    
    public void addGroupOffices(String groupId, List<String> officeIds) {
        TRGroupOffice groupOffice = null;
        for (int ii = 0; ii < officeIds.size(); ii++){
            groupOffice = new TRGroupOffice(groupId, officeIds.get(ii));
            if (officeGroupDao.queryGroupOfficeCount(groupOffice) <= 0)
                officeGroupDao.save(groupOffice);
        }        
    }

    @Override
    public void deleteGroupOffice(String groupId, String officeId) {
        if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(officeId)){
            TRGroupOffice groupOffice = new TRGroupOffice(groupId, officeId);
            officeGroupDao.deleteGroupOffice(groupOffice);
        }
        else if (StringUtils.isNotBlank(officeId)){
            officeGroupDao.deleteGroupsByOfficeId(officeId);
        }
        else
            officeGroupDao.deleteOfficesByGroupId(groupId);
    }

    @Override
    public void deleteGroupOffices(String groupId, String[] officeIds) {
        TRGroupOffice groupOffice = null;
        for (String officeId : officeIds){
            groupOffice = new TRGroupOffice(groupId, officeId);
            officeGroupDao.deleteGroupOffice(groupOffice);
        }        
    }
    
    public void deleteGroupOffices(String groupId, List<String> officeIds) {
        TRGroupOffice groupOffice = null;
        for (int ii = 0; ii < officeIds.size(); ii++){
            groupOffice = new TRGroupOffice(groupId, officeIds.get(ii));
            officeGroupDao.deleteGroupOffice(groupOffice);
        }        
    }
    
    @Override
    public List<ParentChildItem> queryRootGroups(){
        List<ParentChildItem> options = getDao().queryRootGroupCodes();
        //fillOddLevelInfo(options, 0, null, true);
        return options;
    }

    @Override
    public List<ParentChildItem> queryGroupsByParent(String parentGroupId){
        List<ParentChildItem> options = getDao().queryChildCodes(parentGroupId);
        //fillOddLevelInfo(options, 0, null, true);
        return options;
    }
    
    // -------------------------------- 以下为Gettter/Setter方法 -------------------------------- //    
    
    public String getValue(String code) {
        TGroup group = getDao().getEntityById(code);
        if (group == null)
            return null;
        else
            return group.getGname();
    }
    
    /*@Override
    public List<ParentChildItem> queryUsersByGroupId(String groupId) {//, PageInfo pageInfo
        List<String> userIds = userGroupDao.queryUserIdsByGroupId(groupId);
        if (userIds == null || userIds.size() <= 0)
            return null;
        List<ParentChildItem> ret = new ArrayList<ParentChildItem>(userIds.size());
        ICodeValue glpUs = (ICodeValue)ContextHolder.getBean(UserService.class.getName());
        String code;
        for (int ii = 0; ii < userIds.size(); ii++){
            code = userIds.get(ii);
            ParentChildItem item = ParentChildItem.makeOption(code, glpUs.getValue(code), Constant.LEVEL_USER, true);
            ret.add(item);
        }
        return ret;
    }*/

    @Override
    public List<TGroup> queryGroupsByUserId(String userId) {
        return userGroupDao.queryGroupsByUserId(userId);
    }
}
