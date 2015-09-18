/*
 * 文件名称: TGroupService.java
 * 版权信息: Copyright 2001-2012 SXKJ., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2012-5-24
 * 修改内容: 
 */
package com.mfh.comna.bizz.priv.service;

import java.util.List;

import com.mfh.comn.code.bean.ParentChildItem;
import com.mfh.comn.priv.bean.TGroup;

/**
 * Service
 * 
 * @author <a href="mailto:zhangyz@shk.com">zhangyz</a> created on 2012-5-24
 * @since 
 */
public interface GroupService{
	/**
     * 插入一个角色
     * @param tGroup
     * @since
     */
	public void insertTGroup(TGroup tGroup);
		
	/**
     * 更新一个角色
     * @param tGroup
     * @since
     */
	public void updateTGroup(TGroup tGroup);
	
	/**
     * 查询角色
     * @param groupid
     * @since
     */
	public TGroup getTGroup(String groupid);
	
	/**
	 * 删除一个角色
	 * @param groupid
	 * @author zhangyz created on 2013-6-13
	 */
	public void deleteGroupById(String groupid);
	
	/**
	 * 增加一个用户角色关系
	 * @param groupId
	 * @param userId
	 */
    public void addGroupUser(String groupId, String userId);
    
    /**
     * 给角色添加多个用户
     * @param groupId
     * @param userIds
     */
    public void addGroupUsers(String groupId, String[] userIds);
    
    /**
     * 增加一个角色和单位关系
     * @param groupId
     * @param officeId
     */
    public void addGroupOffice(String groupId, String officeId);
    
    /**
     * 给角色添加多个单位
     * @param groupId
     * @param officeIds
     */
    public void addGroupOffices(String groupId, String[] officeIds);
    
    /**
     * 删除一个用户角色关系
     * @param groupId 角色,若groupId为空，则删除用户的所有角色
     * @param userId 用户，若userId为空，则删除角色的所有用户
     */
    public void deleteGroupUser(String groupId, String userId);
    
    /**
     * 给角色删除用户
     * @param groupId
     * @param userIds
     */
    public void deleteGroupUsers(String groupId, String[] userIds);
    
    /**
     * 删除一个角色和单位关系
     * @param groupId 角色,若groupId为空，则删除单位的所有角色
     * @param officeId 单位，若officeId为空，则删除角色的所有单位
     */
    public void deleteGroupOffice(String groupId, String officeId);
    
    /**
     * 给角色删除用户
     * @param groupId
     * @param officeIds 多个单位
     * @author zhangyz created on 2012-5-30
     */
    public void deleteGroupOffices(String groupId, String[] officeIds);
    
    /**
     * 获取顶层角色编码列表
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<ParentChildItem> queryRootGroups();
    
    /**
     * 获取子角色列表
     * @param parentGroupId
     * @return
     * @author zhangyz created on 2012-5-25
     */
    public List<ParentChildItem> queryGroupsByParent(String parentGroupId);
    
    /**
     * 获取一个角色下的所有用户
     * @param groupId
     * @return
     * @author zhangyz created on 2012-5-30
     */
    //public List<ParentChildItem> queryUsersByGroupId(String groupId);//, PageInfo pageInfo
    
    /**
     * 查询一个用户所拥有的角色
     * @param userId
     * @return
     * @author zhangyz created on 2012-6-6
     */
    public List<TGroup> queryGroupsByUserId(String userId);
}
