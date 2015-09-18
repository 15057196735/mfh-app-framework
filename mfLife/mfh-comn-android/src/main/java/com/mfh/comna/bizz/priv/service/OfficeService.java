package com.mfh.comna.bizz.priv.service;

import java.util.List;

import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.priv.bean.IUser;
import com.mfh.comn.priv.bean.TOfficeInfo;

/**
 * 单位服务类
 * 
 * @author zhangyz created on 2013-6-13
 * @since Framework 1.0
 */
public interface OfficeService {
    
    //以下三个方法是通过不同的关键字查找单位，每个单位的code和id是唯一的，名字允许重复，按实际业务要求
    public TOfficeInfo getOfficeByCode(String code);
    public TOfficeInfo getOfficeById(String id);

    public List<TOfficeInfo> getOfficeByName(String name);
    /**
     * 根据用户号，获取该用户所在的office集合
     * @param userId
     * @return
     * @throws Exception
     */
    public List<TOfficeInfo> getOfficeByUserId(String userId);
    
    /**
     * 获取一个单位的根单位
     * @param officeId
     * @return
     * @author zhangyz created on 2012-8-26
     */
    public String getRootOfficeById(String officeId);
    
    /**
     * 获取一个单位的所有子单位
     * @param parentOfficeId
     * @return
     */
    public List<TOfficeInfo> getOfficesByParent(String parentOfficeId);
    
    /**
     * 根据角色号查询单位
     * create on : 2011-5-24
     * @author jinj
     * @param groupid 角色编号
     * @return
     * @throws Exception
     */
    public List<TOfficeInfo> getOfficesByGroupId(String groupId);
    
    //public List<TOfficeInfo> searchOffices(QueryDefine define);

    public List<IUser> getUsers(TOfficeInfo office);//获取单位下的用户集合

    public List<IUser> getUsers(String officeId);//获取单位下的用户集合

    /**
     * 查询顶层单位编码集合
     * @return
     * @author zhangyz created on 2012-8-1
     */
    public List<ParentChildItem> queryRootOffices();
    
    /**
     * 查询指定单位的子单位编码集合
     * @param parentGroupId
     * @return
     * @author zhangyz created on 2012-8-1
     */
    public List<ParentChildItem> queryOfficeCodesByParent(String parentGroupId);

    public void updateOffice(TOfficeInfo office, String editerId);//修改单位

    public void saveOffice(TOfficeInfo off, String createUserId);//保存单位

    public void addSubOffices(String officeId, String[] subOfficeIds);//设置子单位
    
    public void deleteSubOffices(String officeId, String[] subOfficeIds);//删除一个单位的指定子单位

    public void addUserOffices(String officeId, String[] userIds);//设置单位用户
    
    public void addOfficeGroups(String officeId, String[] groupIds);    //增加单位--角色关系
    
    public void deleteUserOffices(String officeId, String[] userIds);//删除一个单位用户关系
    
    public void deleteOfficeGroups(String officeId, String[] groupIds);//删除单位--角色关系
    
    public void deleteUoRelationByUserId(String userId);//删除一个用户的所有单位关系
    
    public void deleteUoRelationByOfficeId(String officeId);//删除一个单位的所有用户关系
    
    public void deleteEntityById(String id);
}
