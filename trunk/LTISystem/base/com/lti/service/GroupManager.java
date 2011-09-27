package com.lti.service;

import java.util.*;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.Group;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.GroupUser;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Role;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.type.PaginationSupport;

public interface GroupManager {

	/** *********************************************************** */
	/* ==Group method==Start */
	/** *********************************************************** */

	long add(Group group);

	/**
	 * remove a group
	 * 
	 * @param id
	 */
	void remove(long id);

	Group get(Long id);

	Long save(Group g);

	void saveOrUpdate(Group g);

	void update(Group g);

	Group get(String name);

	/**
	 * add user to a group
	 * 
	 * @param groupid
	 * @param userid
	 * @return
	 */
	long addGroupUser(long groupid, long userid);

	void removeUser(long groupid, long userid);

	List<Group> getGroups();

	PaginationSupport getGroups(int pageSize, int startIndex);

	List<Group> getGroups(String name);

	PaginationSupport getGroups(String name, int pageSize, int startIndex);

	List<Group> getUserGroups(long userid);

	List<User> getUsers(long groupid);

	Long[] getGroupIDs();

	List<Group> getGroups(Object[] groupIDs);

	Object[] getGroupIDs(long userid);

	PaginationSupport getUsers(long groupid, int permission, int pageSize, int startIndex);

	/** *********************************************************** */
	/* ==Group method==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==Role method==Start */
	/** *********************************************************** */

	/**
	 * get the specific role according to the ID
	 * 
	 * @author cherry 2009-3-7
	 * @param roleID
	 * @return
	 */
	Role getRole(long roleID);

	/**
	 * get the specific role with name
	 * 
	 * @param name
	 * @return
	 */
	Role getRoleByName(String name);

	/**
	 * get all roles in the database
	 * 
	 * @return
	 */
	List<Role> getAllRoles();

	/**
	 * add the specific role
	 * 
	 * @author cherry 2009-3-7
	 * @param role
	 */
	void addRole(Role role) throws Exception;

	/**
	 * update a role
	 * 
	 * @param role
	 * @throws Exception
	 */
	void updateRole(Role role) throws Exception;

	/**
	 * delete the specific role according to the id
	 * 
	 * @author cherry 2009-3-7
	 * @param roleID
	 */
	void deleteRole(long roleID) throws Exception;

	PaginationSupport getRoles(int pageSize, int startIndex);

	PaginationSupport getRoles(String name, int pageSize, int startIndex);

	/** *********************************************************** */
	/* ==Role method==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==group_role relation method==Start */
	/** *********************************************************** */

	/**
	 * delete a group_role relation by groupid
	 * 
	 * @param groupid
	 */
	void delete(long groupid);

	/**
	 * get all groups and roles relation
	 * 
	 * @author cherry 2009-3-7
	 * @return
	 */
	List<GroupRole> getAllGroupRoles();

	/**
	 * get group_role relation by id
	 * 
	 * @author cherry 2009-3-7
	 * @param id
	 * @return
	 */
	GroupRole getGroupRole(long id);

	/**
	 * get group_role relation
	 * 
	 * @param groupID
	 * @param roleID
	 * @param resourceID
	 * @param resourceType
	 * @return
	 */
	GroupRole getGroupRole(Long groupID, Long roleID, Long resourceID, int resourceType);

	/**
	 * get the specific group's group_role relationships
	 * 
	 * @author cherry 2009-3-7
	 * @param groupID
	 * @return
	 */
	List<GroupRole> getGroupRolesByGroup(long groupID);

	/**
	 * get the specific role's group_role relationships
	 * 
	 * @author cherry 2009-3-7
	 * @param roleID
	 * @return
	 */
	List<GroupRole> getGroupRolesByRole(long roleID);

	/**
	 * get a group's roles
	 * 
	 * @param groupID
	 * @return
	 */
	List<Role> getRolesByGroupID(long groupID);

	/**
	 * get roles by a list of groups
	 * 
	 * @param groupIDs
	 * @return
	 */
	List<Role> getRolesByGroups(Object[] groupIDs);

	/**
	 * add a group_role relation
	 * 
	 * @author cherry 2009-3-7
	 * @param gr
	 */
	void addGroupRole(GroupRole gr) throws Exception;

	/**
	 * add a group_role relation according to the groupID and role ID
	 * resourceID=0 and resourceType=0
	 * 
	 * @param groupID
	 * @param roleID
	 * @throws Exception
	 */
	void addGroupRole(long groupID, long roleID) throws Exception;

	/**
	 * add a number of group_role relations
	 * 
	 * @param grs
	 * @throws Exception
	 */
	void addGroupRoles(List<GroupRole> grs) throws Exception;

	/**
	 * delete the group_role relation according to the id
	 * 
	 * @author cherry 2009-3-7
	 * @param id
	 */
	void deleteGroupRole(long id) throws Exception;

	/**
	 * delete the group_role relations according to the groupID and role ID
	 * 
	 * @param groupID
	 * @param roleID
	 * @throws Exception
	 */
	void deleteGroupRole(long groupID, long roleID) throws Exception;

	/**
	 * delete the group_role relations belonging to the specific group
	 * 
	 * @author cherry 2009-3-7
	 * @param groupID
	 */
	void deleteGroupRoleByGroup(long groupID) throws Exception;

	/**
	 * delete the group_role relations belonging to the specific role
	 * 
	 * @author cherry 2009-3-7
	 * @param roleID
	 */
	void deleteGroupRoleByRole(long roleID) throws Exception;

	void deleteByHQL(String string);

	List<Group> getResourceGroups(Long resourceID);

	List<Role> getResourceRoles(Long resourceID);

	void addGroupRole(Long groupID, Long roleID, Long resourceID, int resourceType) throws Exception;

	void deleteResource(Long groupID, Long roleID, Long resourceID);

	void deleteResource(Long groupID, Long resourceID);

	PaginationSupport getPortfolios(Long groupID, int permission, Integer pageSize, Integer startIndex);

	PaginationSupport getStrategies(Long groupID, int permission, Integer pageSize, Integer startIndex);

	PaginationSupport getResources(Long roleID, int permission, Integer pageSize, Integer startIndex);

	PaginationSupport getGroupRoles(Long groupID, Integer pageSize, Integer startIndex);

	void updateGroupRole(GroupRole gr);

	PaginationSupport getGroupRoles(String name, Integer pageSize, Integer startIndex);

	List<GroupRole> getGroupRolesByResource(long resourceID, int resourceType);

	List<Group> getGroupsByRoleID(Long roleID);

	/**
	 * delete group_role relations according to resourceID and resourceType
	 * 
	 * @param resourceID
	 * @param resourceType
	 * @throws Exception
	 */
	void deleteGroupRolesByResource(Long resourceID, int resourceType) throws Exception;

	List<Long> getResourceIDByGroups(Object[] groupIDs, int resource);

	List<Role> getRolesByResourceID(Long resourceID, int resourceType);

	void deleteGroupRole(long groupID, long roleID, int resourceType) throws Exception;

	List<GroupRole> getGroupRoles(Long groupID, Long roleID);

	/**
	 * 
	 * @param roleID
	 * @param groupIDs
	 * @param resourceID
	 * @return
	 */
	boolean hasrole(long roleID, Object[] groupIDs, long resourceID, int resourceType);

	/**
	 * parse String ids to Long[] id
	 * 
	 * @param s
	 * @return
	 */
	Long[] parseIDs(String s);

	/**
	 * ANONYMOUS_user :GROUP_ANONYMOUS Login_user: GROUP_ANONYMOUS,GROUP_LEVEL1
	 * 
	 * @param userID
	 * @return
	 */
	Long[] getPublicGroupIDs(Long userID);

	List<GroupRole> getGroupRolesByResource(int resourceType);

	boolean hasrole(long roleID, Object[] groupIDs, int resourceType);

	void removeByUserID(long userid);

	List<User> getUsersByGroupID(Long groupID);

	void deleteGroupRole(Long groupId, Long roleid, Long resourceid, int resourceType) throws Exception;

	/** *********************************************************** */
	/* ==group_role relation method==End */
	/** *********************************************************** */

	/**
	 * 清除原先Resource的Role为roleid的Group Role信息
	 * 并添加新Group Role
	 * @param resourceid
	 * @param groupids
	 * @param roleid
	 * @author Michael Chua
	 */
	public void changeGroupRoles(long resourceid,long[] groupids,long roleid);
	/**
	 * 传入的group信息为string数组
	 * @param resourceid
	 * @param groupnames
	 * @param roleid
	 */
	public void changeGroupRoles(long resourceid,String[] groupnames,long roleid);
	/**
	 * 获取Resource的Role为roleid的Group Role信息
	 * @param resourceid
	 * @param roleid
	 * @return
	 */
	public String[] getGroupRoleNameArray(long resourceid,long roleid);

	GroupUser getGroupUser(Long long1, Long userID);
	
	public String addPortfolioID(String portfolioID);
	
}
