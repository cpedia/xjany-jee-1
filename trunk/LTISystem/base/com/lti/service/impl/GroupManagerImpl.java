package com.lti.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.GroupManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.GroupUser;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Role;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.PaginationSupport;

public class GroupManagerImpl extends DAOManagerImpl implements GroupManager {

	/** *********************************************************** */
	/* fields start */
	/** *********************************************************** */
	/** *********************************************************** */
	/* fields end */
	/** *********************************************************** */

	/** *********************************************************** */
	/* set method for spring Start */
	/** *********************************************************** */
	/** *********************************************************** */
	/* set method for spring End */
	/** *********************************************************** */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** *********************************************************** */
	/* ==basic method== Start */
	/** *********************************************************** */

	@Override
	public void remove(long id) {
		Object obj = getHibernateTemplate().get(Group.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public Group get(Long id) {
		return (Group) getHibernateTemplate().get(Group.class, id);
	}

	@Override
	public Long save(Group g) {
		getHibernateTemplate().save(g);
		return g.getID();
	}

	@Override
	public void saveOrUpdate(Group g) {
		getHibernateTemplate().saveOrUpdate(g);
	}

	@Override
	public void update(Group g) {
		getHibernateTemplate().update(g);
	}

	@Override
	public Group get(String name) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Group.class);

		detachedCriteria.add(Restrictions.eq("Name", name));

		List<Group> bolist = findByCriteria(detachedCriteria);

		if (bolist.size() >= 1)
			return bolist.get(0);

		else
			return null;
	}

	@Override
	public long add(Group group) {

		if (this.get(group.getName()) != null) {

			return -1l;
		}

		this.save(group);

		return group.getID();
	}

	@Override
	public void delete(long groupid) {
		// break all the relations with the group
		// and delete the group
		// but it do not delete any other things
		deleteByHQL("from GroupRole where groupid=" + groupid);

		deleteByHQL("from GroupUser gu where gu.GroupID=" + groupid);

		this.remove(groupid);
	}

	@Override
	public long addGroupUser(long groupid, long userid) {

		GroupUser groupUser = new GroupUser();

		groupUser.setGroupID(groupid);

		groupUser.setUserID(userid);

		getHibernateTemplate().save(groupUser);

		return groupUser.getID();
	}

	@Override
	public void removeUser(long groupid, long userid) {
		deleteByHQL("from GroupUser gu where gu.GroupID=" + groupid + " and gu.UserID=" + userid);
	}

	@Override
	public void removeByUserID(long userid) {
		deleteByHQL("from GroupUser gu where gu.UserID=" + userid);
	}

	/** *********************************************************** */
	/* ==basic method== End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==List method== Start */
	/** *********************************************************** */
	@Override
	public List<Group> getGroups() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Group.class);

		List<Group> bolist = findByCriteria(detachedCriteria);

		return bolist;
	}

	@Override
	public List<Group> getGroups(Object[] groupIDs) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Group.class);

		detachedCriteria.add(Restrictions.in("ID", groupIDs));

		List<Group> bolist = findByCriteria(detachedCriteria);

		return bolist;
	}

	@Override
	public Long[] getGroupIDs() {

		List<Group> bolist = this.getGroups();

		if (bolist == null || bolist.size() == 0)
			return null;

		Long[] groupIDs = new Long[bolist.size()];
		for (int i = 0; i < bolist.size(); i++) {
			groupIDs[i] = bolist.get(i).getID();
		}
		return groupIDs;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PaginationSupport getGroups(int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Group.class);

		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	@Override
	public List<Group> getGroups(String name) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Group.class);

		detachedCriteria.add(Restrictions.like("Name", "%" + name + "%"));

		List<Group> bolist = findByCriteria(detachedCriteria);

		return bolist;
	}

	@Override
	public PaginationSupport getGroups(String name, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Group.class);

		detachedCriteria.add(Restrictions.like("Name", "%" + name + "%"));

		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	@Override
	public List<Group> getUserGroups(long userid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupUser.class);

		detachedCriteria.add(Restrictions.eq("UserID", (java.lang.Long) userid));

		List<GroupUser> gulist = findByCriteria(detachedCriteria);

		List<Group> bolist = new ArrayList<Group>();

		for (int i = 0; i < gulist.size(); i++) {

			Long groupid = gulist.get(i).getGroupID();

			if (groupid != null) {

				Group group = this.get(groupid);

				if (group != null)
					bolist.add(group);
			}

		}

		return bolist;
	}

	@Override
	public Object[] getGroupIDs(long userid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupUser.class);

		detachedCriteria.add(Restrictions.eq("UserID", (java.lang.Long) userid));

		List<GroupUser> gulist = findByCriteria(detachedCriteria);

		if (gulist == null || gulist.size() == 0)
			return null;

		Object[] ids = new Object[gulist.size()];

		for (int i = 0; i < gulist.size(); i++) {

			Long groupid = gulist.get(i).getGroupID();

			ids[i] = groupid;

		}
		return ids;

	}

	@Override
	public List<User> getUsers(long groupid) {

		DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(GroupUser.class);

		detachedCriteria1.add(Restrictions.eq("GroupID", (java.lang.Long) groupid));

		List<GroupUser> resources = findByCriteria(detachedCriteria1);

		List<User> users = new ArrayList<User>();

		for (int i = 0; i < resources.size(); i++) {

			Long userid = resources.get(i).getUserID();

			if (userid != null) {

				User user = (User) getHibernateTemplate().get(User.class, userid);

				if (user != null)
					users.add(user);

			}

		}
		return users;
	}

	@Override
	public List<User> getUsersByGroupID(Long groupID) {
		String sql = "FROM User where ID in (select UserID from GroupUser where GroupID=" + groupID + ")";
		List<User> userlist = new ArrayList<User>();
		try {
			userlist = super.findByHQL(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userlist;
	}

	@Override
	public PaginationSupport getUsers(long groupid, int permission, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(GroupUser.class);

		detachedCriteria1.add(Restrictions.eq("GroupID", (java.lang.Long) groupid));

		PaginationSupport ps = findPageByCriteria(detachedCriteria1, pageSize, startIndex);

		List<GroupUser> resources = ps.getItems();

		List<User> users = new ArrayList<User>();

		for (int i = 0; i < resources.size(); i++) {

			Long userid = resources.get(i).getUserID();

			if (userid != null) {

				User user = (User) getHibernateTemplate().get(User.class, userid);

				if (user != null)
					users.add(user);

			}

		}

		ps.setItems(users);

		return ps;
	}

	@Override
	public void addGroupRole(GroupRole gr)  {
		getHibernateTemplate().save(gr);
	}

	@Override
	public void addGroupRole(long groupID, long roleID) throws Exception {
		// TODO Auto-generated method stub
		GroupRole gr = new GroupRole();
		gr.setGroupID(groupID);
		gr.setRoleID(roleID);
		gr.setResourceID(0L);
		gr.setResourceType(Configuration.RESOURCE_TYPE_ROLE);
		addGroupRole(gr);
	}

	@Override
	public void addGroupRoles(List<GroupRole> grs) throws Exception {
		// TODO Auto-generated method stub
		if (grs == null)
			return;
		for (int i = 0; i < grs.size(); i++) {
			this.addGroupRole(grs.get(i));
		}
	}

	@Override
	public void addRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().save(role);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(role);
	}

	@Override
	public void deleteGroupRole(long id) throws Exception {
		// TODO Auto-generated method stub
		try {
			deleteByHQL("from GroupRole where id=" + id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteGroupRole(long groupID, long roleID) throws Exception {
		// TODO Auto-generated method stub
		try {
			deleteByHQL("from GroupRole where groupid=" + groupID + "and roleid=" + roleID);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteGroupRole(long groupID, long roleID, int resourceType) throws Exception {
		// TODO Auto-generated method stub
		try {
			deleteByHQL("from GroupRole where groupid=" + groupID + "and roleid=" + roleID + "and resourceType=" + resourceType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Role> getAllRoles() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		return findByCriteria(detachedCriteria);
	}

	@Override
	public PaginationSupport getRoles(int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);

		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	@Override
	public PaginationSupport getRoles(String name, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);

		detachedCriteria.add(Restrictions.like("name", "%" + name + "%"));

		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	@Override
	public void deleteGroupRoleByGroup(long groupID) throws Exception {
		// TODO Auto-generated method stub
		List<GroupRole> grs = getGroupRolesByGroup(groupID);
		if (grs != null) {
			for (int i = 0; i < grs.size(); i++) {
				GroupRole gr = grs.get(i);
				deleteGroupRole(gr.getID());
			}
		}
	}

	@Override
	public void deleteGroupRoleByRole(long roleID) throws Exception {
		// TODO Auto-generated method stub
		List<GroupRole> grs = getGroupRolesByRole(roleID);
		if (grs != null) {
			for (int i = 0; i < grs.size(); i++) {
				GroupRole gr = grs.get(i);
				deleteGroupRole(gr.getID());
			}
		}
	}

	@Override
	public void deleteRole(long roleID) throws Exception {
		// TODO Auto-generated method stub
		try {
			deleteByHQL("from Role where id=" + roleID);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public GroupRole getGroupRole(long ID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("ID", ID));
		List<GroupRole> grs = findByCriteria(detachedCriteria);
		if (grs != null && grs.size() != 0)
			return grs.get(0);
		else
			return null;
	}

	@Override
	public List<GroupRole> getAllGroupRoles() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		return findByCriteria(detachedCriteria);
	}

	@Override
	public List<GroupRole> getGroupRolesByGroup(long groupID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("GroupID", groupID));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public List<GroupRole> getGroupRolesByRole(long roleID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("RoleID", roleID));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public List<GroupRole> getGroupRolesByResource(long resourceID, int resourceType) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("ResourceID", resourceID));
		detachedCriteria.add(Restrictions.eq("ResourceType", resourceType));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public List<GroupRole> getGroupRolesByResource(int resourceType) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("ResourceType", resourceType));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public List<GroupRole> getGroupRoles(Long groupID, Long roleID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("GroupID", groupID));
		detachedCriteria.add(Restrictions.eq("RoleID", roleID));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public Role getRole(long roleID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		detachedCriteria.add(Restrictions.eq("ID", roleID));
		List<Role> roles = findByCriteria(detachedCriteria);
		if (roles != null && roles.size() != 0)
			return roles.get(0);
		else
			return null;
	}

	@Override
	public List<Role> getRolesByGroupID(long groupID) {
		// TODO Auto-generated method stub
		List<GroupRole> grs = getGroupRolesByGroup(groupID);
		if (grs == null)
			return null;
		List<Role> roles = new ArrayList<Role>();
		for (int i = 0; i < grs.size(); i++) {
			Role r = getRole(grs.get(i).getRoleID());
			if (roles.contains(r) != true) {
				roles.add(r);
			}
		}
		return roles;
	}

	@Override
	public List<Role> getRolesByGroups(Object[] groupIDs) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.in("GroupID", groupIDs));
		List<GroupRole> grs = findByCriteria(detachedCriteria);
		if (grs == null)
			return null;
		List<Role> roles = new ArrayList<Role>();
		List<Long> roleIDs = new ArrayList<Long>();
		for (int i = 0; i < grs.size(); i++) {
			GroupRole gr = grs.get(i);
			Role r = getRole(gr.getRoleID());
			if (r == null)
				continue;
			if (roleIDs.contains(r.getID()))
				continue;
			roles.add(r);
			roleIDs.add(r.getID());
		}
		return roles;
	}

	@Override
	public Role getRoleByName(String name) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		detachedCriteria.add(Restrictions.eq("name", name));
		List<Role> roles = findByCriteria(detachedCriteria);
		if (roles != null && roles.size() != 0)
			return roles.get(0);
		else
			return null;
	}

	@Override
	public void addGroupRole(Long groupID, Long roleID, Long resourceID, int resourceType) {
		if (this.getGroupRole(groupID, roleID, resourceID, resourceType) != null) {
			return;
		}
		GroupRole gr = new GroupRole();
		gr.setGroupID(groupID);
		gr.setRoleID(roleID);
		gr.setResourceID(resourceID);
		gr.setResourceType(resourceType);
		addGroupRole(gr);

	}

	@Override
	public void deleteGroupRole(Long groupID, Long roleID, Long resourceID, int resourceType) throws Exception {
		deleteByHQL("from GroupRole gr where gr.GroupID=" + groupID + " and gr.RoleID=" + roleID + " and gr.ResourceID=" + resourceID + " and gr.ResourceType=" + resourceType);
	}

	@Override
	public GroupRole getGroupRole(Long groupID, Long roleID, Long resourceID, int resourceType) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.eq("GroupID", (java.lang.Long) groupID));
		detachedCriteria.add(Restrictions.eq("RoleID", (java.lang.Long) roleID));
		detachedCriteria.add(Restrictions.eq("ResourceID", (java.lang.Long) resourceID));
		detachedCriteria.add(Restrictions.eq("ResourceType", (java.lang.Integer) resourceType));
		List<GroupRole> grs = findByCriteria(detachedCriteria);
		if (grs != null && grs.size() != 0)
			return grs.get(0);
		else
			return null;

	}

	@Override
	public List<Group> getResourceGroups(Long resourceID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria.add(Restrictions.eq("ResourceID", (java.lang.Long) resourceID));

		List<GroupRole> gulist = findByCriteria(detachedCriteria);

		List<Group> bolist = new ArrayList<Group>();

		for (int i = 0; i < gulist.size(); i++) {

			Long groupid = gulist.get(i).getGroupID();

			if (groupid != null) {

				Group group = this.get(groupid);

				if (group != null)
					bolist.add(group);
			}

		}

		return bolist;
	}

	@Override
	public List<Role> getResourceRoles(Long resourceID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria.add(Restrictions.eq("ResourceID", (java.lang.Long) resourceID));

		List<GroupRole> grlist = findByCriteria(detachedCriteria);

		List<Role> rlist = new ArrayList<Role>();

		for (int i = 0; i < grlist.size(); i++) {

			Long roleid = grlist.get(i).getRoleID();

			if (roleid != null) {

				Role role = this.getRole(roleid);

				if (role != null)
					rlist.add(role);
			}

		}

		return rlist;
	}

	@Override
	public void deleteResource(Long groupID, Long roleID, Long resourceID) {
		// TODO Auto-generated method stub
		deleteByHQL("from GroupRole gr where gr.GroupID=" + groupID + " and gr.RoleID=" + roleID + " and gr.ResourceID=" + resourceID);
	}

	@Override
	public void deleteResource(Long groupID, Long resourceID) {
		// TODO Auto-generated method stub
		deleteByHQL("from GroupRole gr where gr.GroupID=" + groupID + " and gr.ResourceID=" + resourceID);
	}

	@Override
	public PaginationSupport getPortfolios(Long groupID, int permission, Integer pageSize, Integer startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria1.add(Restrictions.eq("GroupID", (java.lang.Long) groupID));

		PaginationSupport ps = findPageByCriteria(detachedCriteria1, pageSize, startIndex);

		List<GroupRole> resources = ps.getItems();

		List<Portfolio> portfolios = new ArrayList<Portfolio>();

		for (int i = 0; i < resources.size(); i++) {

			if (resources.get(i).getResourceType() == Configuration.RESOURCE_TYPE_PORTFOLIO) {
				Long portfolioid = resources.get(i).getResourceID();

				if (portfolioid != null) {

					Portfolio portfolio = (Portfolio) getHibernateTemplate().get(Portfolio.class, portfolioid);

					if (portfolio != null)
						portfolios.add(portfolio);

				}
			}

		}

		ps.setItems(portfolios);

		return ps;
	}

	@Override
	public PaginationSupport getStrategies(Long groupID, int permission, Integer pageSize, Integer startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria1.add(Restrictions.eq("GroupID", (java.lang.Long) groupID));

		PaginationSupport ps = findPageByCriteria(detachedCriteria1, pageSize, startIndex);

		List<GroupRole> resources = ps.getItems();

		List<Strategy> strategies = new ArrayList<Strategy>();

		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i).getResourceType() == Configuration.RESOURCE_TYPE_STRATEGY) {
				Long strategyid = resources.get(i).getResourceID();

				if (strategyid != null) {

					Strategy strategy = (Strategy) getHibernateTemplate().get(Strategy.class, strategyid);

					if (strategy != null)
						strategies.add(strategy);

				}
			}
		}

		ps.setItems(strategies);

		return ps;
	}

	@Override
	public PaginationSupport getResources(Long roleID, int permission, Integer pageSize, Integer startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria1.add(Restrictions.eq("RoleID", (java.lang.Long) roleID));

		detachedCriteria1.add(Restrictions.or(Restrictions.eq("ResourceType", Configuration.RESOURCE_TYPE_URL), Restrictions.eq("ResourceType", Configuration.RESOURCE_TYPE_METHOD)));

		PaginationSupport ps = findPageByCriteria(detachedCriteria1, pageSize, startIndex);

		List<GroupRole> resources = ps.getItems();
		System.out.println(resources.size());
		return ps;
	}

	@Override
	public PaginationSupport getGroupRoles(Long groupID, Integer pageSize, Integer startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria1.add(Restrictions.eq("GroupID", (java.lang.Long) groupID));

		PaginationSupport ps = findPageByCriteria(detachedCriteria1, pageSize, startIndex);

		return ps;
	}

	@Override
	public void updateGroupRole(GroupRole gr) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(gr);
	}

	@Override
	public PaginationSupport getGroupRoles(String name, Integer pageSize, Integer startIndex) {
		// TODO Auto-generated method stub

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);

		detachedCriteria.add(Restrictions.like("Name", "%" + name + "%"));

		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	@Override
	public void deleteGroupRolesByResource(Long resourceID, int resourceType) throws Exception {
		// TODO Auto-generated method stub
		List<GroupRole> grs = getGroupRolesByResource(resourceID, resourceType);
		if (grs != null) {
			for (int i = 0; i < grs.size(); i++) {
				GroupRole gr = grs.get(i);
				// if (gr.getResourceType() == resourceType) {
				deleteGroupRole(gr.getID());
				// }
			}
		}
	}

	@Override
	public List<Group> getGroupsByRoleID(Long roleID) {
		// TODO Auto-generated method stub
		List<GroupRole> grs = getGroupRolesByGroup(roleID);
		if (grs == null)
			return null;
		List<Group> groups = new ArrayList<Group>();
		for (int i = 0; i < grs.size(); i++) {
			Group g = get(grs.get(i).getGroupID());
			if (groups.contains(g) != true) {
				groups.add(g);
			}
		}
		return groups;
	}

	@Override
	public List<Long> getResourceIDByGroups(Object[] groupIDs, int resource) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupRole.class);
		detachedCriteria.add(Restrictions.in("GroupID", groupIDs));
		List<GroupRole> grs = findByCriteria(detachedCriteria);
		List<Long> resourceIDs = new ArrayList<Long>();
		if (grs != null) {
			for (int i = 0; i < grs.size(); i++) {
				GroupRole gr = grs.get(i);
				Long id = gr.getResourceID();
				if (id == null)
					continue;
				if (gr.getResourceType() == resource)
					resourceIDs.add(id);
			}
		}
		return resourceIDs;
	}

	@Override
	public List<Role> getRolesByResourceID(Long resourceID, int resourceType) {
		// TODO Auto-generated method stub
		List<GroupRole> grs = getGroupRolesByResource(resourceID, resourceType);
		if (grs == null)
			return null;
		List<Role> roles = new ArrayList<Role>();
		for (int i = 0; i < grs.size(); i++) {
			Role r = getRole(grs.get(i).getRoleID());
			if (roles.contains(r) != true) {
				roles.add(r);
			}
		}
		return roles;
	}

	@Override
	public boolean hasrole(long roleID, Object[] groupIDs, long resourceID, int resourceType) {
		// TODO Auto-generated method stub
		if (getGroupRolesByResource(resourceID, resourceType) == null)
			return false;
		List<GroupRole> grs = getGroupRolesByResource(resourceID, resourceType);
		for (int i = 0; i < grs.size(); i++) {
			for (int j = 0; j < groupIDs.length; j++)
				if (groupIDs == null)
					return false;
				else if (grs.get(i).getGroupID().equals(groupIDs[j]) && grs.get(i).getRoleID().equals(roleID))
					return true;
		}
		return false;
	}

	@Override
	public boolean hasrole(long roleID, Object[] groupIDs, int resourceType) {
		// TODO Auto-generated method stub
		if (getGroupRolesByResource(resourceType) == null)
			return false;
		List<GroupRole> grs = getGroupRolesByResource(resourceType);
		for (int i = 0; i < grs.size(); i++) {
			for (int j = 0; j < groupIDs.length; j++)
				if (groupIDs == null)
					return false;
				else if (grs.get(i).getGroupID().equals(groupIDs[j]) && grs.get(i).getRoleID().equals(roleID))
					return true;
		}
		return false;
	}

	@Override
	public Long[] parseIDs(String s) {
		String[] ss = s.split(",");
		Long[] ids = new Long[ss.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Long.parseLong(ss[i]);
		}
		return ids;
	}

	@Override
	public Long[] getPublicGroupIDs(Long userID) {
		// TODO Auto-generated method stub
		String groupIDs;
		if (userID.equals(Configuration.USER_ANONYMOUS) || userID == Configuration.USER_ANONYMOUS) {
			groupIDs = get(Configuration.GROUP_ANONYMOUS).getID() + "";
		} else {
			groupIDs = get(Configuration.GROUP_ANONYMOUS).getID() + ",";
			groupIDs += get(Configuration.GROUP_MEMBER).getID();

		}
		Long[] gIDs = parseIDs(groupIDs);
		return gIDs;
	}

	@Override
	public void changeGroupRoles(long resourceid, long[] groupids, long roleid) {
		deleteByHQL("from GroupRole where roleid=" + roleid + " and resourceid=" + resourceid);
		int resourceType = Configuration.RESOURCE_TYPE_PORTFOLIO;
		if (roleid == Configuration.ROLE_STRATEGY_READ_ID || roleid == Configuration.ROLE_STRATEGY_CODE_ID)
			resourceType = Configuration.RESOURCE_TYPE_STRATEGY;

		if (groupids != null) {
			for (int i = 0; i < groupids.length; i++) {
				if(groupids[i]!=-1)this.addGroupRole(groupids[i], roleid, resourceid, resourceType);
			}
		}

	}

	@Override
	public void changeGroupRoles(long resourceid, String[] groupnames, long roleid) {
		if(groupnames!=null){
			long[] groupids=new long[groupnames.length];
			for(int i=0;i<groupnames.length;i++){
				String gr=groupnames[i];
				Group gp=this.get(gr.trim());
				if(gp!=null){
					groupids[i]=gp.getID();
				}else{
					groupids[i]=-1;
				}
			}
			changeGroupRoles(resourceid, groupids, roleid);
		}else{
			changeGroupRoles(resourceid, new long[]{-1l}, roleid);
		}

	}

	@Override
	public String[] getGroupRoleNameArray(long resourceid, long roleid) {
		List<GroupRole> grs=this.findByHQL("from GroupRole where roleid="+roleid+" and resourceid="+resourceid);
		if(grs==null||grs.size()==0)return null;
		String[] strs=new String[grs.size()];
		for(int i=0;i<grs.size();i++){
			GroupRole gr=grs.get(i);
			Group g=this.get(gr.getGroupID());
			if(g!=null){
				strs[i]=g.getName();
			}else{
				strs[i]=gr.getGroupID()+"";
			}
		}
		return strs;
	}

	@Override
	public GroupUser getGroupUser(Long grid, Long userID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupUser.class);
		detachedCriteria.add(Restrictions.eq("GroupID", grid));
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		List<GroupUser> grs = findByCriteria(detachedCriteria);
		if (grs != null && grs.size() != 0)
			return grs.get(0);
		else
			return null;
	}
	
	public static void main(String[] args){
		GroupManager gm=ContextHolder.getGroupManager();
		System.out.println(gm.getGroupUser(23434l, 9l).getID());
		
	}

//	public Integer checkPortfolioID(int id,String portfolioID)
//	{
//		Session session = null;
//		session = getHibernateTemplate().getSessionFactory().openSession();
//		Transaction tx = null;
//		tx = session.beginTransaction();
//		Query query = session.createSQLQuery("select * from ltisystem_emailnotification e where e.UserID=? and e.PortfolioID in(?)");
//		query.setParameter(0, id);
//		query.setParameter(1, portfolioID);
//		int i = query.executeUpdate();
//		session.flush();
//		tx.commit();
//		session.clear();
//		session.close();
//		System.out.println("list size:" + query.list().size());
//		return  i;
//	}
	public Integer checkPortfolioID(String portfolioID)
	{
		Session session = null;
		session = getHibernateTemplate().getSessionFactory().openSession();
		Long longID = Long.parseLong(portfolioID);
		Query query = session.createQuery("from EmailNotification e where e.UserID=1 and e.PortfolioID=:portfolioID");
		query.setParameter("portfolioID", longID);
		int i = query.list().size();
		session.flush();
		session.clear();
		session.close();
		return i;
	}
	/*
	 * 
	 * whj
	 */
	public String addPortfolioID(String portfolioID)
	{
		String message ="";
		try
		{
			String[] strAry = portfolioID.split(",");
			for(int i = 0; i < strAry.length; i++)
			{
				
				Date time = new Date();
				String form = String.format("%tF", time); 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dateFormat.parse(form);
				if(checkPortfolioID(strAry[i]) == 0)
				{
					EmailNotification e = new EmailNotification();
					e.setUserID(1L);
					e.setPortfolioID(Long.parseLong(strAry[i]));
					e.setLastSentDate(date);
					e.setSpan(0);
					getHibernateTemplate().save(e);
					message = "success";
					continue;
				}else if(checkPortfolioID(strAry[i]) == 1)
				{
					message = "skip";
					continue;
				}
				else
				{
					message = "error";
					continue;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return message;
	}


	/** *********************************************************** */
	/* ==List method== END */
	/** *********************************************************** */

}
