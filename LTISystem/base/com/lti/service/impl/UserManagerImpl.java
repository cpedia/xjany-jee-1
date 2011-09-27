package com.lti.service.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupPermission;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.GroupUser;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Role;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.service.bo.UserFundTable;
import com.lti.service.bo.UserMarket;
import com.lti.service.bo.UserOperation;
import com.lti.service.bo.UserPayItem;
import com.lti.service.bo.UserPermission;
import com.lti.service.bo.UserProfile;
import com.lti.service.bo.UserResource;
import com.lti.service.bo.UserTransaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.EmailLoginDetails;
import com.lti.type.PaginationSupport;
import com.lti.util.EmailUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class UserManagerImpl extends DAOManagerImpl implements UserManager, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GroupManager groupManager;

	private PortfolioManager portfolioManager;

	private StrategyManager strategyManager;

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public User getLoginUser() {
		String username = null;
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (obj instanceof UserDetails || obj instanceof EmailLoginDetails) {
			username = ((UserDetails) obj).getUsername();
		} else {
			username = obj.toString();
		}
		User u = this.get(username);
		if (u == null) {
			u = new User();
			u.setUserName("ANONYMOUS");
			u.setID(Configuration.USER_ANONYMOUS);
		}
		return u;
	}

	public long add(User user) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		// TODO Auto-generated method stub
		// judge whether the user does already exist(add by chaos)
		if (this.get(user.getUserName()) != null) {
			return -1l;
		}
		// ////////////////////////////////
		// Md5PasswordEncoder md5=new Md5PasswordEncoder();
		// user.setPassword(md5.encodePassword(user.getPassword(), null));
		user.setCreatedDate(new Date());
		getHibernateTemplate().save(user);
		mpm.createOneUserPermission(user.getID());
		return user.getID();
	}

	public PaginationSupport getUsers(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		// TODO Auto-generated method stub

		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}

	public User get(long id) {
		// TODO Auto-generated method stub

		return (User) getHibernateTemplate().get(User.class, id);
	}

	public User get(String username) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("UserName", username));
		List<com.lti.service.bo.User> bolist = findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	
	public List<User> getBySubOfName(String name){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.like("UserName", "%"+name+"%"));
		//String query = "select * from `ltisystem`.`ltisystem_user` u where u.userName like '%"+name+"%'";
		List<User> users = new ArrayList<User>();
		users = findByCriteria(detachedCriteria);
//		try {
//			users = super.findBySQL(query);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return users;
	}

	public com.lti.service.bo.User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("EMail", email));
		List<com.lti.service.bo.User> bolist = findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	@Override
	public com.lti.service.bo.User getUserByCharCode(String cc) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("CharCode", cc));
		List<com.lti.service.bo.User> bolist = findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		List<User> bolist = findByCriteria(detachedCriteria);
		return bolist;
	}

	public List<User> getUsers(DetachedCriteria detachedCriteria) {
		// TODO Auto-generated method stub
		return findByCriteria(detachedCriteria);
	}

	public PaginationSupport getUsers(int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	public void remove(long id) {
		// TODO Auto-generated method stub
		Object obj = getHibernateTemplate().get(User.class, id);
		getHibernateTemplate().delete(obj);
		super.deleteByHQL("from GroupUser gu " + "where gu.UserID=" + id);
		deleteByHQL("from" + Configuration.TABLE_EMAILNOTIFICATION + "where userID=" + id);
	}

	public void update(User user) {
		// TODO Auto-generated method stub
		// Md5PasswordEncoder md5=new Md5PasswordEncoder();
		// user.setPassword(md5.encodePassword(user.getPassword(), null));
		getHibernateTemplate().update(user);
	}

	public List<User> getUsersByAuthority(String authority) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("Authority", authority));
		List<com.lti.service.bo.User> bolist = findByCriteria(detachedCriteria);
		return bolist;
	}

	public List<User> getUsersByCreatedDate(String date) {
		// TODO Auto-generated method stub
		String query = "select * from `ltisystem`.`ltisystem_user` u where u.createdDate like '"+date+"%'";
		List<User> users = new ArrayList<User>();
		try {
			users = super.findBySQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
	
	public PaginationSupport getUsersByAuthority(String authority, int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("Authority", authority));
		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		return ps;
	}

	public List<User> getUsersByEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("Enabled", enabled));
		List<User> bolist = findByCriteria(detachedCriteria);
		return bolist;
	}

	public PaginationSupport getUsersByEnabled(boolean enabled, int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.eq("Enabled", enabled));
		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		return ps;
	}

	public List<User> getUsersByGroup(long groupid) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupUser.class);
		detachedCriteria.add(Restrictions.eq("GroupID", (java.lang.Long) (groupid)));
		List<GroupUser> groupUsers = findByCriteria(detachedCriteria);
		List<com.lti.service.bo.User> bolist = new ArrayList<com.lti.service.bo.User>();
		if (groupUsers != null) {
			for (int i = 0; i < groupUsers.size(); i++) {
				User user = this.get(groupUsers.get(i).getUserID());
				bolist.add(user);
			}
		}
		return bolist;

	}

	public PaginationSupport getUsersByGroup(long groupid, int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupUser.class);
		detachedCriteria.add(Restrictions.eq("GroupID", (java.lang.Long) (groupid)));
		PaginationSupport groupUsers = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		List<com.lti.service.bo.User> boList = new ArrayList<com.lti.service.bo.User>();
		if (groupUsers != null && groupUsers.getItems() != null) {
			for (int i = 0; i < groupUsers.getItems().size(); i++) {
				User user = this.get(((GroupUser) groupUsers.getItems().get(i)).getUserID());
				boList.add(user);
			}
		}
		groupUsers.setItems(boList);
		return groupUsers;
	}

	public List<User> getUsersByOtherGroup(long groupid) {
		String hql = "select u from User u,GroupUser gu " + "where gu.GroupID!=" + groupid + " and gu.UserID=u.ID";
		List<User> boList = findByHQL(hql);
		return boList;
	}

	public PaginationSupport getUsersByOtherGroup(long groupid, int pageSize, int startIndex) {
		String hql = "select u from User u,GroupUser gu " + "where gu.GroupID!=" + groupid + " and gu.UserID=u.ID";

		PaginationSupport groupUsers = findPageByHQL(hql, pageSize, startIndex);

		return groupUsers;

	}

	@Override
	public Date getLegalDate(long userID, Date date) {
		// TODO Auto-generated method stub
		Object[] groupIDs = groupManager.getGroupIDs(userID);
		Long roleID = groupManager.getRoleByName(Configuration.ROLE_PORTFOLIO_REALTIME).getID();
		Boolean realtime = groupManager.hasrole(roleID, groupIDs, Configuration.RESOURCE_TYPE_ROLE);
		if (realtime == true) {
			date = LTIDate.clearHMSM(date);
			return date;
		}
		Date legalDate = LTIDate.getHoldingDateMonthEnd(date);
		return legalDate;

		// throw new RuntimeException("it had been removed");
	}

	@Deprecated
	@Override
	public Date getLegalDate(long userID, Date date, Long portfolioID) {
		// TODO Auto-generated method stub
		Boolean realtime = HaveRole(Configuration.ROLE_PORTFOLIO_REALTIME, userID, portfolioID, Configuration.RESOURCE_TYPE_PORTFOLIO);
		if (realtime == true) {
			date = LTIDate.clearHMSM(date);
			return date;
		}
		Date legalDate = LTIDate.getHoldingDateMonthEnd(date);
		return legalDate;
	}

	// please remove it
	@Override
	public Boolean HasRole(String role, long userID) {
		if(userID==Configuration.SUPER_USER_ID)return true;
		Object[] groupIDs = groupManager.getGroupIDs(userID);
		if(groupIDs==null||groupIDs.length==0)return false;
		Role r = groupManager.getRoleByName(role);
		String hql = "from GroupRole gr where gr.RoleID=" + r.getID() + " and ";
		hql += "(";
		for (int i = 0; i < groupIDs.length; i++) {
			if (i != 0)
				hql += " or ";
			hql += "gr.GroupID=" + groupIDs[i];
		}
		hql += ")";

		List<GroupRole> grs = super.findByHQL(hql, 1);
		if (grs != null & grs.size() > 0)
			return true;
		return false;
	}

	private String buildGroupCondition(Object[] groupIDs) {

		StringBuffer sb = new StringBuffer();
		sb.append("groupid in (");
		for (int i = 0; i < groupIDs.length; i++) {
			sb.append(groupIDs[i]);
			if (i != groupIDs.length - 1)
				sb.append(",");
		}
		sb.append(",");
		sb.append(Configuration.GROUP_ANONYMOUS_ID);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Boolean HaveRole(String role, long userID, long ID, int resourceType) {
		if (userID == Configuration.SUPER_USER_ID)
			return true;

		Object[] groupIDs = groupManager.getGroupIDs(userID);
		if (groupIDs == null || groupIDs.length == 0){
			groupIDs=new Long[]{Configuration.GROUP_ANONYMOUS_ID};
		}

		// test owner
		if (resourceType == Configuration.RESOURCE_TYPE_PORTFOLIO&& !role.equals(Configuration.ROLE_PORTFOLIO_REALTIME)) {
			String sql = "select * from " + Configuration.TABLE_PORTFOLIO + " where ";
			sql += "id=" + ID;
			sql += " and userid=" + userID;
			sql += " limit 0,1";
			List list = null;
			try {
				list = super.findBySQL(sql);
			} catch (Exception e) {
			}
			if (list != null && list.size() == 1)
				return true;
		} else if (resourceType == Configuration.RESOURCE_TYPE_STRATEGY) {
			String sql = "select * from " + Configuration.TABLE_STRATEGY + " where ";
			sql += "id=" + ID;
			sql += " and userid=" + userID;
			sql += " limit 0,1";
			List list = null;
			try {
				list = super.findBySQL(sql);
			} catch (Exception e) {
			}
			if (list != null && list.size() == 1)
				return true;
		}

		Role r = groupManager.getRoleByName(role);
		if (r == null)
			return false;

		if (resourceType == Configuration.RESOURCE_TYPE_ROLE) {
			String sql = "select * from " + Configuration.TABLE_GROUP_ROLE + " where resourceType=" + resourceType;
			sql += " and roleid=" + r.getID();
			sql += " and " + buildGroupCondition(groupIDs);
			sql += " limit 0,1";
			List list = null;
			try {
				list = super.findBySQL(sql);
			} catch (Exception e) {
			}
			if (list != null && list.size() == 1)
				return true;
		} else {
			String sql = "select * from " + Configuration.TABLE_GROUP_ROLE + " where resourceType=" + resourceType;
			sql += " and roleid=" + r.getID();
			sql += " and resourceid=" + ID;
			sql += " and " + buildGroupCondition(groupIDs);
			sql += " limit 0,1";
			List list = null;
			try {
				list = super.findBySQL(sql);
			} catch (Exception e) {
			}
			if (list != null && list.size() == 1)
				return true;
		}

		return false;

	}

	@Override
	public List<Group> getGroupsByUser(long userID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupUser.class);
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		List<GroupUser> gus = findByCriteria(detachedCriteria);
		if (gus == null || gus.size() == 0)
			return null;
		List<Group> groups = new ArrayList<Group>();
		List<Long> groupIDs = new ArrayList<Long>();
		for (int i = 0; i < gus.size(); i++) {
			GroupUser gu = gus.get(i);
			if (!groupIDs.contains(gu.getGroupID())) {
				groupIDs.add(gu.getGroupID());
				Group g = groupManager.get(gu.getGroupID());
				groups.add(g);
			}
		}
		return groups;
	}

	@Override
	public void addEmailNotification(EmailNotification en) {
		// TODO Auto-generated method stub
		Long userID = en.getUserID();
		Long portfolioID = en.getPortfolioID();
		EmailNotification e = this.getEmailNotification(userID, portfolioID);
		if (e == null)
			getHibernateTemplate().save(en);
		else {
			e.setLastSentDate(en.getLastSentDate());
			e.setSpan(en.getSpan());
			getHibernateTemplate().update(e);
		}
	}

	@Override
	public void addEmailNotification(List<EmailNotification> ens) {
		// TODO Auto-generated method stub
		if (ens == null || ens.size() < 1)
			return;
		for (int i = 0; i < ens.size(); i++) {
			EmailNotification en = ens.get(i);
			addEmailNotification(en);
		}
	}

	@Override
	public void deleteEmailNotification(Long ID) {
		// TODO Auto-generated method stub
		deleteByHQL("from EmailNotification where id=" + ID);
	}

	@Override
	public void deleteEmailNotificationByUser(Long userID) {
		// TODO Auto-generated method stub
		deleteByHQL("from EmailNotification where userid=" + userID);
	}

	@Override
	public void deleteEmailNotification(Long userID, Long portfolioID) {
		// TODO Auto-generated method stub
		deleteByHQL("from EmailNotification where userid=" + userID + " and portfolioid=" + portfolioID);
	}

	@Override
	public EmailNotification getEmailNotification(Long userID, Long portfolioID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmailNotification.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		List<EmailNotification> ens = findByCriteria(detachedCriteria);
		if (ens != null && ens.size() > 0)
			return ens.get(0);
		else
			return null;
	}

	public int getEmailNotificationsByPortfolioID(Long portfolioID) {
		String sql = "select count(*) from " + Configuration.TABLE_EMAILNOTIFICATION + " where portfolioid = " + portfolioID;
		List<BigInteger> countList = null;
		try {
			countList = (List<BigInteger>) this.findBySQL(sql);
		} catch (Exception e) {
			return 0;
		}
		if (countList != null && countList.size() > 0)
			return countList.get(0).intValue();
		return 0;
	}

	@Override
	public List<EmailNotification> getAllEmailNotification() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmailNotification.class);
		detachedCriteria.addOrder(Order.asc("UserID"));
		detachedCriteria.addOrder(Order.desc("PortfolioID"));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public List<Long> getUsersFromEN() {
		// TODO Auto-generated method stub
		String hql = "select Distinct en.UserID from EmailNotification en";
		List<Long> boList = findByHQL(hql);
		return boList;
	}

	@Override
	public List<EmailNotification> getEmailNotificationsByUser(Long userID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmailNotification.class);
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public void updateEmailNotification(EmailNotification en) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(en);
	}

	@Override
	public void updateEmailNotification(List<EmailNotification> ens) {
		// TODO Auto-generated method stub
		if (ens == null || ens.size() < 1)
			return;
		for (int i = 0; i < ens.size(); i++) {
			EmailNotification en = ens.get(i);
			updateEmailNotification(en);
		}
	}

	@Override
	public void updateEmailLastSentDate(Long userID, Long portfolioID, Date lastSentDate) throws Exception {
		// TODO Auto-generated method stub
		lastSentDate = LTIDate.clearHMSM(lastSentDate);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// String lastSentDateStr = sdf.format(lastSentDate);
		// try {
		// String sql = "update EmailNotification set
		// LastSentDate='"+lastSentDateStr+"' where UserID=" + userID+"and
		// PortfolioID="+portfolioID;
		// executeSQL(sql);
		//	
		// // super.executeSQL("update " + Configuration.TABLE_EMAILNOTIFICATION
		// + " set lastsentdate='"+lastSentDateStr+"' where userid=" + userID+"
		// and portfolioid="+portfolioID);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//		

		deleteEmailNotification(userID, portfolioID);
		EmailNotification en = new EmailNotification();
		en.setPortfolioID(portfolioID);
		en.setUserID(userID);
		en.setSpan(0);
		en.setLastSentDate(lastSentDate);
		try {
			addEmailNotification(en);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	@Override
	public void updateEmailLastSentDate(Long portfolioid, Date lastSentDate) throws Exception {
		// TODO Auto-generated method stub
		lastSentDate = LTIDate.clearHMSM(lastSentDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastSentDateStr = sdf.format(lastSentDate);
		String sql = "update " + Configuration.TABLE_EMAILNOTIFICATION + " set lastsentdate='" + lastSentDateStr + "'";
		sql += " where portfolioid=" + portfolioid;
		executeSQL(sql);
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	@Override
	public void saveUserFundTableID(Long userID, Long planID) {
		if(hasPlan(userID, planID))return;
		
		UserFundTable uft = new UserFundTable();
		uft.setUserID(userID);
		uft.setPlanID(planID);
		getHibernateTemplate().save(uft);
		int planNum = this.numUserFundTableID(userID);
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.get(userID);
		user.setPlanIDNum(planNum);
		getHibernateTemplate().update(user);
	}

	@Override
	public void clearUserFundTableID(UserFundTable uft) {
		Long userID = uft.getUserID();
		Long planID = uft.getUserID();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		UserManager userManager = ContextHolder.getUserManager();
		Strategy plan = strategyManager.get(planID);
		User user = userManager.get(userID);
		if (user == null || plan == null)
			getHibernateTemplate().delete(uft);
	}

	@Override
	public int numUserFundTableID(Long userID) {
		int planNum = 0;
		String sql = "SELECT COUNT(PlanID) From UserFundTable WHERE UserID=" + userID;
		List num = new ArrayList<Long>();
		try {
			num = this.findByHQL(sql);
			planNum = Integer.parseInt(num.get(0).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planNum;
	}

	@Override
	public List<Long> findUserPlanID(Long userID) {
		String sql = "SELECT PlanID From UserFundTable WHERE UserID=" + userID;
		List<Long> planIDs = new ArrayList<Long>();
		try {
			planIDs = this.findByHQL(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planIDs;
	}

	@Override
	public boolean hasPlan(Long userID, Long planID) {
		boolean hasplan = false;
		Long p = 0l;
		List<Long> planIDs = this.findUserPlanID(userID);
		for (int i = 0; i < planIDs.size(); i++) {
			p = planIDs.get(i).longValue();
			if (planID.equals(p))
				hasplan = true;
		}
		return hasplan;
	}

	@Override
	public void saveUserResource(UserResource userres) {
		getHibernateTemplate().save(userres);
	}

	@Override
	public void deleteUserResource(UserResource userres) {
		getHibernateTemplate().delete(userres);
	}

	@Override
	public void updateUserResourse(UserResource userres) {
		getHibernateTemplate().update(userres);
	}

	@Override
	public List<UserResource> getUesrResByExample(UserResource userres) {
		List<UserResource> results = getHibernateTemplate().findByExample(userres);
		return results;
	}

	@Override
	public List<UserResource> getByURPorperty(String propertyName, Object value) {
		String queryString = "from UserResource as ur where ur." + propertyName + "= ?";
		return getHibernateTemplate().find(queryString, value);
	}

	@Override
	public List<UserResource> getUserResByUserID(Long userID, UserResource userres) {
		userres.setUserID(userID);
		List<UserResource> urlist = this.getUesrResByExample(userres);
		return urlist;
	}

	@Override
	public List<UserResource> getUserResByUserID(Long userID) {
		String queryString = "select * from ltisystem_userpayment where userID=" + userID;
		List<UserResource> urlist = new ArrayList<UserResource>();
		try {
			urlist = super.findBySQL(queryString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlist;
	}

	@Override
	public List<Integer> getUserResTypeCounts(Long userID) {
		List<Integer> counts = new ArrayList<Integer>();
		String queryString = "select distinct resourceId FROM UserResource ur where ur.userID=" + userID+" and resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN;
		String queryString2 = "select distinct resourceId FROM UserResource ur where ur.userID=" + userID+" and resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE;
		List<Integer> slist = getHibernateTemplate().find(queryString);
		List<Integer> clist = getHibernateTemplate().find(queryString2);
		counts.add(slist.size());// plan_system
		counts.add(clist.size());// plan_create_himself
		return counts;
	}
	
	@Override
	public int getCurrentPortfolioNumber(long userID){
		String squery = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_PORTFOLIO + " and userID=" + userID;
		List<Integer> slist = null;
		slist = super.findByHQL(squery);
		int number = 0;
		if(slist!=null && slist.size()>=0)
			number = slist.size();
		return number;
	}

	@Override
	public int getCurrentPlanUsedNumber(long userID){
		String squery = "select distinct resourceId from UserResource where (resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN + 
						" or resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE + 
						") and userID=" + userID;
		List<Integer> slist = null;
		slist = super.findByHQL(squery);
		int number = 0;
		if(slist!=null && slist.size()>=0)
			number = slist.size();
		return number;
	}
	
	@Override
	public int getCurrentPlanCreatedNumber(long userID){
		String squery = "select distinct resourceId from UserResource where (resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE + 
		                " or resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE + 
						") and userID=" + userID;
		List<Integer> slist = null;
		slist = super.findByHQL(squery);
		int number = 0;
		if(slist!=null && slist.size()>=0)
			number = slist.size();
		return number;
	}

	/**
	 * after use plan, save in the database
	 */
	@Override
	public void saveUserResByStrategyCounts(long userID, Long strategyID,int strategyCount) {
		String squery = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN + " and userID=" + userID;
		String squery2 = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE + " and userID=" + userID;
		String squery3 = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE + " and userID=" + userID;
		List<Long> sys_planIDs = super.findByHQL(squery);
		List<Long> cre_planIDs = super.findByHQL(squery2);
		List<Long> creuse_planIDs = super.findByHQL(squery3);
		if(cre_planIDs.contains(strategyID)){
			// user create and do not customize
			if(sys_planIDs.size()+creuse_planIDs.size()+1 <= strategyCount){
				String squery4 = "from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE + " and resourceId=" + strategyID;
				UserResource ur = (UserResource)super.findByHQL(squery4).get(0);
				ur.setResourceType(Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE);
				getHibernateTemplate().update(ur);
			}
		}
		else{
			//new one
			if(sys_planIDs.contains(strategyID)&&sys_planIDs.size()+creuse_planIDs.size()+1 <= strategyCount){
				UserResource userRes = new UserResource();
				userRes.setUserID(userID);
				userRes.setResourceType(Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN);
				userRes.setResourceId(strategyID);
				getHibernateTemplate().save(userRes);
			}
		}
	}
	
	/**
	 * before use plan, check the permission
	 */
	@Override
	public boolean canPlanUse(long userID, long strategyID,int strategyCount){
		boolean flag = false;
		String squery = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN + " and userID=" + userID;
		String squery2 = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE + " and userID=" + userID;
		String squery3 = "select distinct resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE + " and userID=" + userID;
		List<Long> sys_planIDs = super.findByHQL(squery);
		List<Long> cre_planIDs = super.findByHQL(squery2);
		List<Long> creuse_planIDs = super.findByHQL(squery3);
		if(sys_planIDs.contains(strategyID)){
			flag = true;
		}
		else if(cre_planIDs.contains(strategyID)){
			// user create and do not customize
			if(sys_planIDs.size()+creuse_planIDs.size()+1 <= strategyCount){
				flag = true;
			}
			else flag = false;
		}
		else if(creuse_planIDs.contains(strategyID)){
			// user create and customize
			flag = true;
		}
		else{
			//new one
			if(sys_planIDs.size()+creuse_planIDs.size()+1 <= strategyCount){
				flag = true;
			}
			else flag = false;
		}
		return flag;
	}
	
	/**
	 * before create plan, check the permission
	 */
	@Override
	public boolean canPlanCreate(long userID,int planNum){
		boolean flag = false;
		String squery = "select resourceId from UserResource where (resourceType=" + Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE + 
		                " or resourceType="+Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE+") and userID=" + userID;
		List<Long> creuse_planIDs = super.findByHQL(squery);
		int PN = creuse_planIDs.size();
		if(PN + 1 <= planNum)
			flag = true;
		else 
			flag = false;
		return flag;
	}
	
	/**
	 * after create plan, save in the database
	 */
	@Override
	public void saveUserResByPlanCreate(long userID,long planID) {
		UserResource userRes = new UserResource();
		userRes.setUserID(userID);
		userRes.setResourceType(Configuration.USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE);
		userRes.setResourceId(planID);
		getHibernateTemplate().save(userRes);
	}
	
	/**
	 * before create portfolio, check the permission
	 */
	@Override
	public boolean canPortfolioUse(long userID,int portfolioCount){
		boolean flag = false;
		String squery = "select resourceId from UserResource where resourceType=" + Configuration.USER_RESOURCE_PORTFOLIO + " and userID=" + userID;
		List<Long> portfolioIDs = super.findByHQL(squery);
		int PN = portfolioIDs.size();
		if(PN + 1 <= portfolioCount)
			flag = true;
		else 
			flag = false;
		return flag;
	}
	
	/**
	 * after createportfolio, save in the database
	 */
	@Override
	public void saveUserResByPortfolioCounts(long userID, Long portfolioID) {
		UserResource userRes = new UserResource();
		userRes.setUserID(userID);
		userRes.setResourceType(Configuration.USER_RESOURCE_PORTFOLIO);
		userRes.setResourceId(portfolioID);
		getHibernateTemplate().save(userRes);
	}

	// get all the user's profile in the same pay_status
	@Override
	public List<UserProfile> getUserProfileByStatus(String userStatus) {
		String query = "select * from `ltisystem`.`ltisystem_userprofile` up where up.userStatus='" + userStatus + "'";
		List<UserProfile> payments = new ArrayList<UserProfile>();
		try {
			payments = super.findBySQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return payments;
	}

	public List<UserProfile> getUserProfileByStatusAndItemName(String userStatus,String itemName) {
		String query = "select * from `ltisystem`.`ltisystem_userprofile` up where up.userStatus='" + userStatus + "' and up.item_Name like '"+ "%"+itemName+"%'";
		List<UserProfile> payments = new ArrayList<UserProfile>();
		try {
			payments = super.findBySQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return payments;
	}
	
	@Override
	public List<UserProfile> getAllUserProfile() {
		String query = "from UserProfile";
		List<UserProfile> payments = getHibernateTemplate().find(query);
		for (int i = 0; i < payments.size(); i++) {
			User user = ContextHolder.getUserManager().get(payments.get(i).getUserID());
			payments.get(i).setUserName(user.getUserName());
		}
		return payments;
	}

	@Override
	// get the user's payment which come to the enddate
	public List<UserProfile> getUserProfileByTime(int interval) {
		String query = "SELECT * FROM `ltisystem`.`ltisystem_userprofile` up WHERE  TO_DAYS(NOW())-TO_DAYS(up.payment_startdate)>=" + interval;
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		try {
			profiles = super.findBySQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return profiles;
	}

	@Override
	public List<UserProfile> getUserProfileOutTime(Date today) {
		String date = com.lti.util.LTIDate.parseDateToString(today);
		String hql = "SELECT p FROM UserProfile p WHERE p.paymentEnddate < '" + date + "'";
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		profiles = super.findByHQL(hql);
		for (int i = 0; i < profiles.size(); i++) {
			User user = ContextHolder.getUserManager().get(profiles.get(i).getUserID());
			profiles.get(i).setUserName(user.getUserName());
		}
		return profiles;
	}

	/**
	 * if the profile out of the limited time,changes user's group
	 */
	@Override
	public List<User> changeUserGroup() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ca=Calendar.getInstance();
		ca.add(Calendar.DAY_OF_YEAR, -3);
		Date today = ca.getTime();
		List<UserProfile> profiles = this.getUserProfileOutTime(today);
		List<User> users = new ArrayList<User>();
		StringBuffer sb=new StringBuffer();
		sb.append("Date:"+df.format(new Date())+"<br>\r\n");
		if (profiles != null && profiles.size() > 0) {
			for (int i = 0; i < profiles.size(); i++) {
				
				Long userid = profiles.get(i).getUserID();
				User user = get(userid);
				sb.append("Change user["+user.getUserName()+"]<br>\r\n");
				users.add(user);
				String item_name = profiles.get(i).getItemName();
				sb.append("Item: "+item_name+"<br>\r\n");
				profiles.get(i).setUserStatus(UserProfile.PAYMENT_STATUS_EXPIRED);
				saveOrUpdateUserProfile(profiles.get(i));
				List<Object> groupIDs = new ArrayList<Object>();// need a new
																// table
				String query = "SELECT p.groupId FROM `ltisystem`.`ltisystem_userpayitem` p WHERE p.Item_name='" + item_name + "'";
				try {
					groupIDs = super.findBySQL(query);
					if (groupIDs != null && groupIDs.size() > 0) {
						for (int j = 0; j < groupIDs.size(); j++) {
							Long groupid = ((BigInteger) groupIDs.get(j)).longValue();
							sb.append("Remove group["+groupid+"]<br>\r\n");
							groupManager.removeUser(groupid, user.getID());
						}
					}
				} catch (Exception e) {
					sb.append(StringUtil.getStackTraceString(e));
					e.printStackTrace();
				}
				sb.append("<br>\r\n");
				sb.append("<br>\r\n");
				sb.append("<br>\r\n");
			}
		}else{
			sb.append("Change nothing.<br>\r\n");
		}
		try {
			sendEmail("Paypal check", sb.toString());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return users;
	}
	public static void sendEmail(String subject,String text) throws Exception{
		if(com.lti.util.IPUtil.getLocalIP().startsWith("70.38.112"))
			EmailUtil.sendMail(new String[] { "caixg@ltisystem.com", "jany.wuhui@gmail.com","ccd1010@163.com" }, subject+"From "+com.lti.util.IPUtil.getLocalIP(), text);
	}
	@Override
	public void removeGroupByItem(Long userID) throws Exception{
		String query = "SELECT item_name FROM `ltisystem`.`ltisystem_userprofile` up WHERE up.UserID=" + userID;
		List<String> itemNames = super.findBySQL(query);
		//if(itemNames.size()==1)
		//	groupManager.removeByUserID(userID);
		for (int j = 0; j < itemNames.size(); j++) {
			String sql = "SELECT GroupID FROM UserPayItem p WHERE p.itemName='" + itemNames.get(j) + "'";
			List<Long> groupIDs = getHibernateTemplate().find(sql);
			List<GroupUser> groupUsers = new ArrayList<GroupUser>();
			for (int i = 0; i < groupIDs.size(); i++) {
				groupManager.removeUser(groupIDs.get(i), userID);
			}
		}
	}
	
	@Override
	public void changUserItemName(Long userID,String itemName) throws Exception{
		UserProfile profile = new UserProfile();
		profile = this.getUserProfile(userID);
		profile.setItemName(itemName);
		this.saveOrUpdateUserProfile(profile);
		getHibernateTemplate().update(profile);
		String sql = "SELECT GroupID FROM UserPayItem p WHERE p.itemName='" + itemName + "'";
		List<Long> groupIDs = getHibernateTemplate().find(sql);
		List<GroupUser> groupUsers = new ArrayList<GroupUser>();
		for (int i = 0; i < groupIDs.size(); i++) {
			GroupUser groupUser = new GroupUser();
			groupUser.setUserID(userID);
			groupUser.setGroupID(groupIDs.get(i));
			groupUsers.add(groupUser);
		}
		getHibernateTemplate().saveOrUpdateAll(groupUsers);
	}

	// item's content must different??
	@Override
	public void addGroupByItem(Long userID) throws Exception{
		String query = "SELECT item_name FROM `ltisystem`.`ltisystem_userprofile` up WHERE up.UserID=" + userID;
		List<String> itemNames = super.findBySQL(query);
		for (int j = 0; j < itemNames.size(); j++) {
			String sql = "SELECT GroupID FROM UserPayItem p WHERE p.itemName='" + itemNames.get(j) + "'";
			List<Long> groupIDs = getHibernateTemplate().find(sql);
			List<GroupUser> groupUsers = new ArrayList<GroupUser>();
			for (int i = 0; i < groupIDs.size(); i++) {
				GroupUser gr=this.groupManager.getGroupUser(groupIDs.get(i),userID);
				if(gr!=null){
					continue;
				}
				GroupUser groupUser = new GroupUser();
				groupUser.setUserID(userID);
				groupUser.setGroupID(groupIDs.get(i));
				groupUsers.add(groupUser);
			}
			getHibernateTemplate().saveOrUpdateAll(groupUsers);
		}
	}

	@Override
	public void saveOrUpdateUserProfile(UserProfile profile) {
		if (profile != null)
			getHibernateTemplate().saveOrUpdate(profile);
	}

	@Override
	public UserProfile getUserProfile(Long userID) {
		UserProfile userprofile = (UserProfile) getHibernateTemplate().get(UserProfile.class, userID);
		return userprofile;
	}

	@Override
	public void deleteUserProfile(Long userID) {
		UserProfile userprofile = new UserProfile();
		userprofile = this.getUserProfile(userID);
		getHibernateTemplate().delete(userprofile);
	}

	@Override
	public void savePayItem(String itemName, List<Long> groupIDs) {
		List<UserPayItem> payitems = new ArrayList<UserPayItem>();
		for (int i = 0; i < groupIDs.size(); i++) {
			UserPayItem payitem = new UserPayItem();
			payitem.setItemName(itemName);
			payitem.setGroupID(groupIDs.get(i));
			payitems.add(payitem);
		}
		getHibernateTemplate().saveOrUpdateAll(payitems);

	}
	
	@Override
	public List<UserProfile> getUserProfileInTime(Date today){
		String date = com.lti.util.LTIDate.parseDateToString(today);
		String hql = "SELECT p FROM UserProfile p WHERE p.paymentEnddate > '"+date+"'";
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		profiles = super.findByHQL(hql);
		for(int i=0;i<profiles.size();i++){
			User user =  ContextHolder.getUserManager().get(profiles.get(i).getUserID());
			profiles.get(i).setUserName(user.getUserName());
		}
		return profiles;
	}


	@Override
	public void deletePayItem(String item_name){
		String query = "FROM UserPayItem up WHERE up.itemName='"+item_name+"'";
		deleteByHQL(query);
	} 
	@Override
	public Map<String, List<String>> getAllItem() {
		Map<String, List<String>> payitems = new HashMap<String, List<String>>();
		String query = "select distinct Item_Name from `ltisystem`.`ltisystem_userpayitem`";
		List<String> items = new ArrayList<String>();
		try {
			items = super.findBySQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GroupManager groupManager = ContextHolder.getGroupManager();
		for (int i = 0; i < items.size(); i++) {
			String sql = "select groupId from `ltisystem`.`ltisystem_userpayitem` where Item_Name='" + items.get(i) + "'";
			List<Object> groupIDs = new ArrayList<Object>();
			try {
				groupIDs = super.findBySQL(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> groupNames = new ArrayList<String>();
			for (int j = 0; j < groupIDs.size(); j++) {
				Long id = ((BigInteger) groupIDs.get(j)).longValue();
				groupNames.add(groupManager.get(id).getName());
			}
			payitems.put(items.get(i), groupNames);
		}
		return payitems;
	}
	
	@Override
	public List<String> getAllItemName(){
		String query = "select distinct Item_Name from `ltisystem`.`ltisystem_userpayitem`";
		List<String> items = new ArrayList<String>();
		try {
			items = super.findBySQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public void saveUserTransaction(UserTransaction userTran) {
		getHibernateTemplate().save(userTran);
	}

	/**
	 * all=false for display the transaction all=true for get the complete
	 * transaction
	 */
	@Override
	public List<UserTransaction> getUserTransaction(Long userID) {
		List<UserTransaction> userTrans = new ArrayList<UserTransaction>();
		String query = "select u from UserTransaction u where u.userID=" + userID;
		try {
			userTrans = super.findByHQL(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userTrans;
	}
	
	public List<UserTransaction> getUserTransactionByTimeAndTxnType(String timeCreated,String txnType,String itemName) {
		List<UserTransaction> userTrans = new ArrayList<UserTransaction>();
		String query = "select distinct  u.UserID,date_format(u.time_created,'%Y-%m-%d') from `ltisystem`.`ltisystem_usertransaction` u where u.time_created like '" + timeCreated+"%'"+" and u.txn_type='"+txnType+"' and u.item_name like '"+"%"+itemName+"%'";
		try {
			userTrans = super.findBySQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTrans;
	}

	public List<UserTransaction> getUserTransactionByPaymentDateAndTxnType(String paymentDate,String txnType) {
		List<UserTransaction> userTrans = new ArrayList<UserTransaction>();
		String query = "select distinct new UserTransaction(u.userID,u.paymentDate,u.paymentGross) from UserTransaction u where u.paymentDate like '" + paymentDate+"%'"+" and u.txnType='"+txnType+"'";
		try {
			userTrans = super.findByHQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTrans;
	}
	@Override
	public void deleteUserTransaction(Long userID) {
		String query = "delete from ltisystem_usertransaction where userID=" + userID;
		try {
			executeSQL(query);
		} catch (Exception e) {
			throw new RuntimeException("Cannot delete the paypal transactions of user "+userID+" .",e);
		}
	}

	@Override
	public boolean existPayerId(String payerId,String itemName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserTransaction.class);
		detachedCriteria.add(Restrictions.eq("payerId", payerId));
		detachedCriteria.add(Restrictions.eq("itemName", itemName));
		List list = findByCriteria(detachedCriteria, 1, 0);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}
	
	@Override
	public List<Transaction> showEmailTransaction(Date tdate){
		String date = com.lti.util.LTIDate.parseDateToString(tdate);
		String hql = "FROM Transaction where Date >= '"+date+"' and PortfolioID in (select distinct PortfolioID from EmailNotification)";
		List<Transaction> trans = new ArrayList<Transaction>();
		try {
			trans = super.findByHQL(hql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trans;
	}
	
	@Override
	public List<Object[]> showEmailPortfolio(){
		List<Object[]> portfolioIDs = null;
		List<Portfolio> portfolios = new ArrayList<Portfolio>();
		String sql = "select distinct(portfolioID),lastsentdate from " + Configuration.TABLE_EMAILNOTIFICATION;
		try {
			portfolioIDs = super.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return portfolioIDs;
	}
	
	@Override
	public Date getLastSentDate(Long portfolioID){
		String sql = "select distinct(LastSentDate) from " + Configuration.TABLE_EMAILNOTIFICATION + " where portfolioID="+portfolioID;
		List<Object> dates= new ArrayList<Object>();
		try {
			dates = super.findBySQL(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Date)dates.get(0);
	}
	
	@Override
	public void saveMarketEmail(UserMarket userMarket){
		getHibernateTemplate().saveOrUpdate(userMarket);
	}
	
	@Override
	public void updateMarketEmail(UserMarket userMarket){
		getHibernateTemplate().update(userMarket);
	}
	
	@Override
	public void saveMarkerEmails(List<UserMarket> umlist){
		super.saveAll(umlist);
//		for(int i=0;i<umlist.size();i++){
//			getHibernateTemplate().save(umlist.get(i));
//			//System.out.println("complete:"+i+"//"+umlist.get(i).getUserEmail());
//		}
	}
	
	@Override
	public void deleteMarketEmail(UserMarket userMarket){
		getHibernateTemplate().delete(userMarket);
	}
	
	@Override
	public boolean hasMarketEmail(String userEmail){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserMarket.class);
		detachedCriteria.add(Restrictions.eq("userEmail", userEmail));
		List<com.lti.service.bo.UserProfile> list = findByCriteria(detachedCriteria);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}
	
	@Override
	public List<UserMarket> getMarketEmailbyProperty(String propertyName, Object value,boolean flag){
		String queryString = null;
		if(flag){
			queryString = "from UserMarket as um where um."+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		}else{
			queryString = "from UserMarket as um where um."+ propertyName + " like '%"+value+"%'"; 
			return getHibernateTemplate().find(queryString);
		}
	}
	
	@Override
	public List<UserMarket> getMEmailsbyProperty(String[] propertyNames, String[] values,String[] judge){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserMarket.class);
		List<Criterion>  criterions =new ArrayList<Criterion>();
		int length = propertyNames.length;
		if(propertyNames==null || judge==null)
			return null;
		for(int i=0;i<length;i++){
			if(!propertyNames[i].equals("null") && values[i]!=null && !values[i].trim().equals("")){
				Criterion criterion = Restrictions.like(propertyNames[i], values[i],MatchMode.ANYWHERE);
				criterions.add(criterion);
			}
		}
		if(criterions.size()==0)
			return null;
		Criterion criterionfinal = null;
		for(int i=0;i<criterions.size();i++){
			if(judge[i].equals("null"))
				criterionfinal = criterions.get(i);
			else if(judge[i].equals("and"))
				criterionfinal = Restrictions.and(criterionfinal,criterions.get(i));
			else if(judge[i].equals("or"))
				criterionfinal = Restrictions.or(criterionfinal,criterions.get(i));
			else if(judge[i].equals("not"))
				criterionfinal = Restrictions.not(criterions.get(i));
		}
		detachedCriteria.add(criterionfinal);
		List<UserMarket> usermarkets = findByCriteria(detachedCriteria);
		return usermarkets;
	}
	
	@Override
	public List<String> getEmailsGroupKeys(){
		String sql="SELECT distinct group_key1,group_key2,group_key3,group_key4,group_key5 FROM `ltisystem`.`ltisystem_usermarket`";
		List<Object[]> obKeys = new ArrayList<Object[]>();
		try {
			obKeys = super.findBySQL(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> keySet = new HashSet<String>();
		for(int i=0;i<obKeys.size();i++){
			for(int j=0;j<obKeys.get(i).length;j++){
				if(obKeys.get(i)[j]!=null&&!obKeys.get(i)[j].toString().trim().equals("")){
					keySet.add(obKeys.get(i)[j].toString());
				}
			}
		}
		return new ArrayList<String>(keySet);
	}
	
	@Override
	public String[] getMarketEmailsByKey(String[] Keys){
		String keyName = "group_key";
		List<Object> emails = new ArrayList<Object>();
		for(int j=0;j<Keys.length;j++){
			if(Keys[j].equals("MPIQ_B")){
				List<Object> blist = new ArrayList<Object>();
				String qurey = "select email from `ltisystem`.`ltisystem_user` as u, `ltisystem`.`ltisystem_group_user` as g where u.ID = g.userID and g.groupID=8";
				try {
					blist = super.findBySQL(qurey);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				emails.addAll(blist);
			}
			else if(Keys[j].equals("VF_B")){
				List<Object> blist = new ArrayList<Object>();
				String qurey = "select email from `ltisystem`.`ltisystem_user` as u, `ltisystem`.`ltisystem_group_user` as g where u.ID = g.userID and g.groupID=11";
				try {
					blist = super.findBySQL(qurey);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				emails.addAll(blist);
			}
			else{
				for(int i=1;i<=5;i++){
					keyName = keyName+i;
					String sql="SELECT user_email FROM `ltisystem`.`ltisystem_usermarket` where "+keyName+"='"+Keys[j]+"'";
					List<Object> list = new ArrayList<Object>();
					try {
						list = super.findBySQL(sql);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(list!=null && list.size()>0)
						emails.addAll(list);
					keyName = "group_key";
				}	
			}
		}
		Set<Object> eSet = new HashSet<Object>();
		eSet.addAll(emails);
		emails = new ArrayList<Object>(eSet);
		return (String[])emails.toArray(new String[emails.size()]);
	}
	
	public static void main(String[] args) {
		// UserManager um = (UserManager)
		// com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("userManager");
		// try {
		// long portfolioID = 100l;//4634:3;
		// int count = um.getEmailNotificationsByPortfolioID(portfolioID);
		// System.out.println(count);
		// //um.updateEmailLastSentDate(347L, 4540L, new Date());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
//		Date today = new Date();
		UserManager userManager = ContextHolder.getUserManager();
//		List<UserProfile> pa = userManager.getUserProfileByStatus("normal");
//		System.out.println("getUserProfileByStatus:" + pa);
//		List<User> users = userManager.changeUserGroup();
//		System.out.println("changeUserGroup:" + users);
		// userManager.BuyPayItem(508l);
		
		Date testDate = LTIDate.getDate(2010,1,1);
		List<Transaction> trans = new ArrayList<Transaction>();
		trans = userManager.showEmailTransaction(testDate);
	}

	@Override
	public List<UserOperation> getUserOperations(long userid) {
		return super.findByHQL("from UserOperation uo where uo.userID="+userid);
	}

	@Override
	public long saveUserOperation(UserOperation uo) {
		getHibernateTemplate().save(uo);
		return uo.getId();
	}

	@Override
	public void updateUserOperation(UserOperation uo) {
		getHibernateTemplate().update(uo);
	}

	@Override
	public UserOperation getUserOperation(long id) {
		return (UserOperation) getHibernateTemplate().get(UserOperation.class, id);
	}

	@Override
	public User getUserBySubscrId(String subScrID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserProfile.class);
		detachedCriteria.add(Restrictions.eq("subscrId", subScrID));
		List<com.lti.service.bo.UserProfile> bolist = findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return this.get(bolist.get(0).getUserID());
		else
			return null;
	}

	
	/**************************** 20110118 **************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public UserPermission getUserPermissionByUserID(Long userID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserPermission.class);
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		List<UserPermission> userPermissionList = this.findByCriteria(detachedCriteria, 1, 0);
		if(userPermissionList != null && userPermissionList.size() == 1){
			return userPermissionList.get(0);
		}
		return null;
	}

	@Override
	public void saveUserPermission(UserPermission userPermission) {
		this.getHibernateTemplate().save(userPermission);
	}

	@Override
	public void updateUserPermission(UserPermission userPermission) {
		this.getHibernateTemplate().update(userPermission);
	}

	@Override
	public void saveOrUpdateUserPermission(UserPermission userPermission) {
		this.getHibernateTemplate().saveOrUpdate(userPermission);
	}
	
	@Override
	public GroupPermission getGroupPermissionByGroupID(Long groupID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupPermission.class);
		detachedCriteria.add(Restrictions.eq("GroupID", groupID));
		List<GroupPermission> groupPermissionList = this.findByCriteria(detachedCriteria, 1, 0);
		if(groupPermissionList != null && groupPermissionList.size() == 1)
			return groupPermissionList.get(0);
		return null;
	}
	
	@Override
	public void saveGroupPermission(GroupPermission groupPermission) {
		this.getHibernateTemplate().save(groupPermission);
	}
	
	@Override
	public void updateGroupPermission(GroupPermission groupPermission) {
		this.getHibernateTemplate().update(groupPermission);
	}
	
	@Override
	public void saveOrUpdateGroupPermission(GroupPermission groupPermission) {
		this.getHibernateTemplate().saveOrUpdate(groupPermission);
	}
	
	@Override
	public List<GroupPermission> getGroupPermissionList() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GroupPermission.class);
		return this.findByCriteria(detachedCriteria);
	}
	
	@Override
	public void saveOrUpdateUserPermissionList(List<UserPermission> userPermissionList) {
		this.saveOrUpdateAll(userPermissionList);
	}
	/**************************** 20110118 *************************
	 * @throws Exception */

	@Override
	public void adjustCurPlanCreateNum(Long userID, int adjustNum) throws Exception {
		String sql = "update ltisystem.ltisystem_userpermission set CurPlanCreateNum = CurPlanCreateNum + " + adjustNum + " where UserID = " + userID;
		super.executeSQL(sql);
	}
	@Override
	public void adjustCurConsumerPlanNum(Long userID, int adjustNum) throws Exception {
		String sql = "update ltisystem.ltisystem_userpermission set CurConsumerPlanNum = CurConsumerPlanNum + " + adjustNum + " where UserID = " + userID;
		super.executeSQL(sql);
	}
	@Override
	public void adjustCurPlanFundTableNum(Long userID, int adjustNum) throws Exception {
		String sql = "update ltisystem.ltisystem_userpermission set CurPlanFundTableNum = CurPlanFundTableNum + " + adjustNum + " where UserID = " + userID;
		super.executeSQL(sql);
	}

	@Override
	public void adjustCurPlanRefNum(Long userID, int adjustNum) throws Exception {
		String sql = "update ltisystem.ltisystem_userpermission set CurPlanRefNum = CurPlanRefNum + " + adjustNum + " where UserID = " + userID;
		super.executeSQL(sql);
	}

	@Override
	public void adjustCurPortfolioFollowNum(Long userID, int adjustNum) throws Exception {
		String sql = "update ltisystem.ltisystem_userpermission set CurPortfolioFollowNum = CurPortfolioFollowNum + " + adjustNum + " where UserID = " + userID;
		super.executeSQL(sql);
	}

	@Override
	public void adjustCurPortfolioRealTimeNum(Long userID, int adjustNum) throws Exception {
		String sql = "update ltisystem.ltisystem_userpermission set CurPortfolioRealTimeNum = CurPortfolioRealTimeNum + " + adjustNum + " where UserID = " + userID;
		super.executeSQL(sql);
	}

	@Override
	public void saveUserResource(Long userID, Long resourceID, int resourceType) {
		UserResource userResource = new UserResource();
		userResource.setUserID(userID);
		userResource.setResourceId(resourceID);
		userResource.setResourceType(resourceType);
		this.saveUserResource(userResource);
	}
	
	@Override
	public void saveUserResource(Long userID, Long resourceID, int resourceType, Date updateTime) {
		UserResource userResource = new UserResource();
		userResource.setUserID(userID);
		userResource.setResourceId(resourceID);
		userResource.setResourceType(resourceType);
		userResource.setUpdateTime(updateTime);
		this.saveUserResource(userResource);
	}
	@Override
	public List<Long> getResourceIDListByUserIDAndResourceType(Long userID, int resourceType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("userID", userID));
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		detachedCriteria.setProjection(Projections.property("resourceId"));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public List<Long> getUserIDListByPlanIDs(Long[] portfolioids) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.in("resourceId", portfolioids));
		detachedCriteria.add(Restrictions.eq("resourceType", Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW));
		detachedCriteria.setProjection(Projections.property("userID"));
		return this.findByCriteria(detachedCriteria);
	}
	
	@Override
	public List<Long> getResourceIDListByUserIDAndResourceTypeAndRelationID(Long userID, int resourceType,long relationID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("userID", userID));
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		detachedCriteria.add(Restrictions.eq("relationID", relationID));
		detachedCriteria.setProjection(Projections.property("resourceId"));
		return this.findByCriteria(detachedCriteria);
	}
	
	@Override
	public UserResource getUserResourceByUserIDAndResourceIDAndResourceType(Long userID, Long resourceID, int resourceType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("userID", userID));
		detachedCriteria.add(Restrictions.eq("resourceId", resourceID));
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		List<UserResource> userResourceList = this.findByCriteria(detachedCriteria, 1, 0);
		if(userResourceList != null && userResourceList.size() == 1)
			return userResourceList.get(0);
		return null;
	}
	
	
	
	@Override
	public List<UserResource> getUserResourceByUserIDAndResourceType(Long userID, int resourceType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("userID", userID));
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		return findByCriteria(detachedCriteria);
	}
	
	@Override
	public List<UserResource> getUserResourceByResourceIDAndResourceType(Long resourceID, int resourceType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("resourceId", resourceID));
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		return findByCriteria(detachedCriteria);
	}
	
	@Override
	public UserResource getUserResourceByUserIDAndResourceIDAndResourceTypeAndRelationID(Long userID, Long resourceID, int resourceType,long relationID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("userID", userID));
		detachedCriteria.add(Restrictions.eq("resourceId", resourceID));
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		detachedCriteria.add(Restrictions.eq("relationID", relationID));
		List<UserResource> userResourceList = this.findByCriteria(detachedCriteria, 1, 0);
		if(userResourceList != null && userResourceList.size() == 1)
			return userResourceList.get(0);
		return null;
	}
	
	
	
	
	@Override
	public List<Long> getEmailNotificationsByUserID(Long userID) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmailNotification.class);
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		detachedCriteria.setProjection(Projections.distinct(Projections.property("PortfolioID")));
		return findByCriteria(detachedCriteria);
	}
	
	@Override
	public List<UserResource> getFollowPortfolioResourcesByPortfolioID(Long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("resourceId", portfolioID));
		detachedCriteria.add(Restrictions.eq("resourceType", Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW));
		return this.findByCriteria(detachedCriteria);
	}
	
	@Override
	public UserResource getCustomizePortfolioResourceByPortfolioID(Long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("resourceId", portfolioID));
		detachedCriteria.add(Restrictions.eq("resourceType", Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE));
		List<UserResource> userResourceList = this.findByCriteria(detachedCriteria);
		if(userResourceList != null && userResourceList.size() > 0)
			return userResourceList.get(0);
		return null;
	}

	@Override
	public List<UserResource> getUserResourceByResourceType(int resourceType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("resourceType", Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE));
		return this.findByCriteria(detachedCriteria);
	}
	
	@Override
	public List<Long> getUnSendEmailPortfolioIDList(Date endDate){
		List<Long> portfolioIDList = new ArrayList<Long>();
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<EmailNotification> emailList = userManager.getAllEmailNotification();
		for(EmailNotification en : emailList){
			Long portfolioID = en.getPortfolioID();
			Portfolio portfolio = portfolioManager.get(portfolioID);
			boolean needSend = false;//endDate当天需不需要发
			if(portfolio != null){
				if(portfolio.isPlanPortfolio()){//如果是TAA或SAA的话，则需要根据frequency去判断
					String frequency = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("Frequency");
					if(frequency == null || frequency.equals(""))
						frequency = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RebalanceFrequency");
					if(frequency == null || frequency.equals(""))
						frequency = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("CheckFrequency");
					if(frequency.equalsIgnoreCase("WEEKLY")){
						needSend = LTIDate.isLastNYSETradingDayOfWeek(endDate);
						//needSend = LTIDate.isLastSecondNYSETradingDayOfWeek(endDate);
					}else if(frequency.equalsIgnoreCase("MONTHLY")){
						needSend = LTIDate.isLastNYSETradingDayOfMonth(endDate);
					}else if(frequency.equalsIgnoreCase("QUARTERLY")){
						needSend = LTIDate.isLastNYSETradingDayOfQuarter(endDate);
					}else if(frequency.equalsIgnoreCase("YEARLY")){
						needSend = LTIDate.isLastNYSETradingDayOfYear(endDate);
					}
				}else{//是advance portfolio，要根据是否有transaction判断
					List<Transaction> transactionList = portfolioManager.getTransactionsByPortfolioIDAndTypeAndDate(portfolioID, Configuration.TRANSACTION_TYPE_REAL, endDate);
					if(transactionList != null && transactionList.size() > 0)
						needSend = true;
				}
			}
			//需要发但没发
			if(needSend && LTIDate.before(en.getLastSentDate(), endDate))
				portfolioIDList.add(portfolioID);
		}
		return portfolioIDList;
	}
	
	//用户数量
	public List<User> numUser(long i){
		List<User> list = new ArrayList<User>();
		list = getHibernateTemplate().find("from User user where user.InviteCodeID="+i);
		return list;
	}
	//根据邀请码ID找出所有USER
	public List<UserTransaction> findUserbyInviteId(long i){
		List<UserTransaction> list = new ArrayList<UserTransaction>();
		list= getHibernateTemplate().find("from UserTransaction userTransaction where userTransaction.userID="+i);
		System.out.print(list.size());
		return list;
	}

	@Override
	public List<UserResource> getUserResourceByResourceTypeAndExpiredTime(int resourceType, Date expiredTime) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserResource.class);
		detachedCriteria.add(Restrictions.eq("resourceType", resourceType));
		detachedCriteria.add(Restrictions.le("expiredTime", expiredTime));
		return this.findByCriteria(detachedCriteria);
	}
}
