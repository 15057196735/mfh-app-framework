package com.mfh.comna.bizz.priv.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TOfficeInfo;
import com.mfh.comn.priv.bean.TRGroupOffice;
import com.mfh.comn.priv.bean.TRUOffice;
import com.mfh.comn.utils.UuidUtil;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.bizz.priv.dao.OfficeDao;
import com.mfh.comna.bizz.priv.dao.OfficeGroupDao;
import com.mfh.comna.bizz.priv.dao.OfficeRelationDao;
import com.mfh.comna.bizz.priv.dao.UserOfficeDao;
import com.mfh.comna.bizz.priv.service.OfficeService;
import com.mfh.comna.comn.seq.SequenceService;

/**
 * 单位服务实现类
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public class OfficeServiceImpl extends  BaseService<TOfficeInfo, String, OfficeDao> implements OfficeService{
    private OfficeDao officeDao = new OfficeDao();
    private OfficeRelationDao orDao = new OfficeRelationDao();
    private UserOfficeDao userOfficeDao = new UserOfficeDao();
    private OfficeGroupDao officeGroupDao = new OfficeGroupDao();
    protected SequenceService seqService = ServiceFactory.getSequenceService();
     
    @Override
    protected Class<OfficeDao> getDaoClass() {
        return OfficeDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }


    /**
     * 保存TOfficeInfo对象
     * @param off
     * @param createUserId 为当前用户ID
     */
    public void saveOffice(TOfficeInfo off, String createUserId){
        /*考虑到一级单位是没有父单位的，无法精确判断，只能在页面中限制
         * QueryDefine define = new QueryDefine(new String[0],new Object[0]);
        List listOff = searchOffices(define);
        if(listOff.size()>0&&(setPOff==null||setPOff.size()==0))
            throw new Exception("必须指定新建单位的父单位");*/
        //严格来说，这里还要判断父单位集中的各单位是否存在父子关系－－如存在就要抛出异常，但考虑到页面中应该会限制，暂不检验
        try {
            if (StringUtils.isBlank(off.getId()))
                off.setId(UuidUtil.getUuid());
            off.setCreateId(createUserId);
            Date date = new Date();
            if (off.getCreateDate() == null){
                off.setCreateDate(date);
            }
            off.setEditDate(date);
            if (StringUtils.isBlank(off.getOfficeCode())){
                Long offId = seqService.getNextSeqLongValue(officeDao.getTableName());
                off.setOfficeCode(Long.toString(offId));
            }
            else{
                //if (StringUtils.isNumeric(off.getOfficeCode()) == false)
                //    throw new RuntimeException("单位编码必须为数值型");
            }
            
            OfficeDao dao = getOfficeDao();
            dao.save(off);
            orDao.addSubOffice(TOfficeInfo.ROOT_INIT, off.getId());//同步添加至根单位下,根单位可以是包括所有租户。
        }
        catch (Exception he) {
            throw new RuntimeException("单位信息保存出错:" + he.getMessage());
        }
    }

    private OfficeDao getOfficeDao() {
        return officeDao;
    }
    /**
     * 删除单位
     * @param officeID
     */
    @Override
    public void deleteEntityById(String id){
        this.getOfficeDao().deleteById(id);//需要改进..,不是全执行sql语句,这样一些额外逻辑没办法扩展了
    }
    
    @Override
    public TOfficeInfo getOfficeById(String id){
        return (TOfficeInfo)officeDao.getEntityById(id);
    }

    
    @Override
    public void deleteUoRelationByUserId(String userId){
        userOfficeDao.deleteUoRelationByUserId(userId);
    }

    @Override
    public void deleteUoRelationByOfficeId(String officeId){
        userOfficeDao.deleteUoRelationByOfficeId(officeId);        
    }
    
    /**
     * 更新单位记录。这里的TOfficeInfo对象要确定其id不能为空
     * @param office
     * @param editerId
     */
    public void updateOffice(TOfficeInfo office, String editerId) {
        if (office.getId() == null)
            throw new RuntimeException("要修改的TOfficeInfo对象ID不能为空！");
        try {
            //该单位的修改人和修改时间信息都要保存下来
            office.setEditId(editerId);
            Date date = new Date();
            office.setEditDate(date);
            TOfficeInfo oldOff = (TOfficeInfo)officeDao.getEntityById(office.getId());
            //该单位的创建人和创建时间信息都要保存下来
            office.setCreateDate(oldOff.getCreateDate());
            office.setCreateId(oldOff.getCreateId());
            if (StringUtils.isBlank(office.getOfficeCode())){
                Long offId = this.seqService.getNextSeqLongValue(officeDao.getTableName());
                office.setOfficeCode(Long.toString(offId));
            }
            else if (StringUtils.isNumeric(office.getOfficeCode()) == false){
                if (TOfficeInfo.ROOT_INIT.equals(office.getOfficeCode()) == false)
                    throw new RuntimeException("单位编码必须为数值型");
            }
            officeDao.update(office);
        }
        catch (Exception he) {
            throw new RuntimeException("单位信息修改出错:" + he.getMessage() ,he);
        }
    }

    /**
     * 设置单位用户
     * @param officeId
     * @param users 用户集
     */
    public void setUsers(String officeId, Set<IUser> users) {
        throw new RuntimeException("设置单位用户出错不支持");
    }
    
    @Override
    public void deleteUserOffices(String officeId, String[] userIds){
        TRUOffice userOffice = null;
        for (int ii = 0; ii < userIds.length; ii++){
            if (userIds[ii] == null)
                continue;
            userOffice = new TRUOffice(userIds[ii], officeId);
            userOfficeDao.deleteUserOffice(userOffice);
        }        
    }

    public void deleteUserOffices(String officeId, List<String> userIds){
        TRUOffice userOffice = null;
        for (int ii = 0; ii < userIds.size(); ii++){
            if (userIds.get(ii) == null)
                continue;
            userOffice = new TRUOffice(userIds.get(ii), officeId);
            userOfficeDao.deleteUserOffice(userOffice);
        }        
    }
    
    @Override
    public void addUserOffices(String officeId, String[] userIds){
        TRUOffice userOffice = null;
        for (int ii = 0; ii < userIds.length; ii++){
            if (userIds[ii] == null)
                continue;
            userOffice = new TRUOffice(userIds[ii], officeId);
            if (userOfficeDao.queryUserOfficeCount(userOffice) <= 0)
                userOfficeDao.insertUserOffice(userOffice);
        }
    }
    
    public void addUserOffices(String officeId, List<String> userIds){
        TRUOffice userOffice = null;
        for (int ii = 0; ii < userIds.size(); ii++){
            if (userIds.get(ii) == null)
                continue;
            userOffice = new TRUOffice(userIds.get(ii), officeId);
            if (userOfficeDao.queryUserOfficeCount(userOffice) <= 0)
                userOfficeDao.insertUserOffice(userOffice);
        }
    }

    @Override
    public void addOfficeGroups(String officeId, String[] groupIds) {
        TRGroupOffice groupOffice = null;
        for (int ii = 0; ii < groupIds.length; ii++){
            if (groupIds[ii] == null)
                continue;
            groupOffice = new TRGroupOffice(groupIds[ii], officeId);
            if (officeGroupDao.queryGroupOfficeCount(groupOffice) <= 0)
                officeGroupDao.save(groupOffice);
        }        
    }

    @Override
    public void deleteOfficeGroups(String officeId, String[] groupIds) {
        TRGroupOffice groupOffice = null;
        for (int ii = 0; ii < groupIds.length; ii++){
            if (groupIds[ii] == null)
                continue;
            groupOffice = new TRGroupOffice(groupIds[ii], officeId);
            officeGroupDao.deleteGroupOffice(groupOffice);
        }        
    }

    @Override
    public void addSubOffices(String officeId, String[] subOfficeIds) {
        for (int ii = 0; ii < subOfficeIds.length; ii++){
            if (subOfficeIds[ii] == null)
                continue;
            orDao.addSubOffice(officeId, subOfficeIds[ii]);
        }
    }
    
    public void addSubOffices(String officeId, List<String> subOfficeIds) {
        for (int ii = 0; ii < subOfficeIds.size(); ii++){
            if (subOfficeIds.get(ii) == null)
                continue;
            orDao.addSubOffice(officeId, subOfficeIds.get(ii));
        }
    }

    protected void deleteSubOffice(String officeId, String subOfficeId){
        orDao.deleteSubOffice(officeId, subOfficeId);
    }

    @Override
    public void deleteSubOffices(String officeId, String[] subOfficeIds) {
        for (int ii = 0; ii < subOfficeIds.length; ii++){
            if (subOfficeIds[ii] == null)
                continue;
            deleteSubOffice(officeId, subOfficeIds[ii]);
        }
    }
    
    @Override
    public List<TOfficeInfo> getOfficeByName(String name) {
        return this.getOfficeDao().getOfficeByName(name);
    }

    @Override
    public TOfficeInfo getOfficeByCode(String code){
        return this.getOfficeDao().getOfficeByCode(code);
    }
    
    @Override
    public List<TOfficeInfo> getOfficesByGroupId(String groupId) {
        return officeGroupDao.getOfficesByGroupId(groupId);
    }

    @Override
    public List<TOfficeInfo> getOfficesByParent(String parentOfficeId){
        return getOfficeDao().getOfficesByParentId(parentOfficeId);
    }
    
    @Override
    public List<ParentChildItem> queryRootOffices(){
        List<ParentChildItem> options = orDao.queryChildCodes(TOfficeInfo.ROOT_INIT);
        //fillOddLevelInfo(options, 0, null, true);
        return options;
    }

    @Override
    public String getRootOfficeById(String officeId){
        return getOfficeDao().getRootOfficeId(officeId);
    }

    @Override
    public List<ParentChildItem> queryOfficeCodesByParent(String parentOfficeId){
        List<ParentChildItem> options = orDao.queryChildCodes(parentOfficeId);
        //fillOddLevelInfo(options, 0, null, true);
        return options;
    }

    @Override
    public List<IUser> getUsers(String officeId){
        return userOfficeDao.getUsersByOfficeId(officeId);
    }

    @Override
    public List<IUser> getUsers(TOfficeInfo off){
        return userOfficeDao.getUsersByOfficeId(off.getId());
    }

    @Override
    public List<TOfficeInfo> getOfficeByUserId(String userId) {
        return userOfficeDao.getOfficesByUserId(userId);
    }

    /*@Override
    public List<TOfficeInfo> searchOffices(QueryDefine define) {
        return this.getOfficeDao().searchOffices(define);
    }*/

    public String getValue(String code) {
        TOfficeInfo info = (TOfficeInfo)officeDao.getEntityById(code);
        if (info == null)
            return null;
        else
            return info.getOfficeName();
    }
}
