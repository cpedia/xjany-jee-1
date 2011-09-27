package com.lti.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupPermission;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.service.bo.UserFundTable;
import com.lti.service.bo.UserMarket;
import com.lti.service.bo.UserOperation;
import com.lti.service.bo.UserPermission;
import com.lti.service.bo.UserProfile;
import com.lti.service.bo.UserResource;
import com.lti.service.bo.UserTransaction;
import com.lti.type.PaginationSupport;

public interface UserManager {

	/**
	 * add a user to database
	 * 
	 * @param user
	 * @return
	 */
	public long add(com.lti.service.bo.User user);

	/**
	 * delete a user by id
	 * 
	 * @param strID
	 */
	public void remove(long strID);

	/**
	 * update a user
	 * 
	 * @param user
	 */
	public void update(com.lti.service.bo.User user);

	/**
	 * get all users
	 * 
	 * @return
	 */
	public List<com.lti.service.bo.User> getUsers();

	/**
	 * get users by page
	 * 
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getUsers(final int pageSize, final int startIndex);

	public List<com.lti.service.bo.User> getUsersByAuthority(String authority);
	
	public List<User> getUsersByCreatedDate(String date);

	public PaginationSupport getUsersByAuthority(String authority, final int pageSize, final int startIndex);

	public List<com.lti.service.bo.User> getUsersByEnabled(boolean enabled);

	public PaginationSupport getUsersByEnabled(boolean enabled, final int pageSize, final int startIndex);

	public com.lti.service.bo.User get(long id);

	public com.lti.service.bo.User get(String name);
	
	public List<com.lti.service.bo.User> getBySubOfName(String name);

	public com.lti.service.bo.User getUserByEmail(String email);

	public User getLoginUser();

	public List<com.lti.service.bo.User> getUsersByGroup(long groupid);

	public PaginationSupport getUsersByGroup(long groupid, int pageSize, int startIndex);

	public List<com.lti.service.bo.User> getUsersByOtherGroup(long groupid);

	public PaginationSupport getUsersByOtherGroup(long groupid, int defaultPageSize, int startIndex);

	public List<User> getUsers(DetachedCriteria detachedCriteria);

	public PaginationSupport getUsers(DetachedCriteria detachedCriteria, int pageSize, int startIndex);

	/**
	 * check whether the role has the the specific role
	 * 
	 * @param role
	 * @param userID
	 * @return
	 */
	@Deprecated
	public Boolean HasRole(String role, long userID);

	/**
	 * @author q 2009/10/28 get the user's legal date to read the portfolio's
	 *         information
	 * @param userID
	 * @param date
	 * @return
	 */
	@Deprecated
	public Date getLegalDate(long userID, Date date);

	/**
	 * @author q 2009/10/28 get the user's legal date to read the portfolio's
	 *         information
	 * @param userID
	 * @param date
	 * @param portfolioID
	 * @return
	 */
	public Date getLegalDate(long userID, Date date, Long portfolioID);


	/**
	 * get the groups which the user belongs to
	 * 
	 * @param userID
	 * @return
	 */
	public List<Group> getGroupsByUser(long userID);

	/**
	 * @author cherry 2009-03-30 add the email notification setting to the
	 *         database
	 * @param en
	 */
	public void addEmailNotification(EmailNotification en);

	/**
	 * @author cherry 2009-03-30 add a number of email notification settings to
	 *         the database
	 * @param ens
	 */
	public void addEmailNotification(List<EmailNotification> ens);

	/**
	 * @author cherry 2009-03-30 get the email notification setting according to
	 *         the user ID and the portfolio ID
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public EmailNotification getEmailNotification(Long userID, Long portfolioID);
	/**
	 * get the number of users which want to be sent emails of portfolioID
	 * @param portfolioID
	 * @return
	 */
	public int getEmailNotificationsByPortfolioID(Long portfolioID);
	/**
	 * @author cherry 2009-03-30 get the email notification setting according to
	 *         the user ID
	 * @param userID
	 * @return
	 */
	public List<EmailNotification> getEmailNotificationsByUser(Long userID);

	/**
	 * @author cherry 2009-03-30 get all email notification settings in user ID
	 *         and portfolio ID
	 * @return
	 */
	public List<EmailNotification> getAllEmailNotification();

	/**
	 * @author cherry 2009-03-30 update the email notification settings
	 * @param en
	 */
	public void updateEmailNotification(EmailNotification en);

	/**
	 * @author cherry 2009-03-30 update the email notification settings in
	 *         quantity
	 * @param ens
	 */
	public void updateEmailNotification(List<EmailNotification> ens);

	/**
	 * @author cherry 2009-03-30 update the last-sent-date of some user's
	 *         interested portfolios
	 * @param portfolioid
	 * @param lastSentDate
	 */
	public void updateEmailLastSentDate(Long portfolioid, Date lastSentDate) throws Exception;

	/**
	 * @author cherry 2009-03-30 delete the email notification settings
	 * @param ID
	 */
	public void deleteEmailNotification(Long ID);

	/**
	 * @author cherry 2009-04-02 delete the email notification settings
	 *         according to the user ID and the portfolio ID
	 * @param userID
	 * @param portfolioID
	 */
	public void deleteEmailNotification(Long userID, Long portfolioID);

	public void deleteEmailNotificationByUser(Long userID);

	/**
	 * get users from email notification
	 * 
	 * @return
	 */
	List<Long> getUsersFromEN();

	/**
	 * users in the groups have role for operating the portfolio or strategy
	 * 
	 * @param role
	 * @param userID
	 * @param ID
	 * @return
	 */
	public Boolean HaveRole(String role, long userID, long ID,int resourceType);

	/**
	 * update a portfolio's LastSentDate. The lastSentDate is the latest
	 * transaction day
	 * 
	 * @param userID
	 * @param portfolioID
	 * @param lastSentDate
	 * @throws Exception
	 */
	void updateEmailLastSentDate(Long userID, Long portfolioID, Date lastSentDate) throws Exception;
	
	public List findBySQL(String sql)throws Exception;

	public void executeSQL(String sql) throws Exception;

	void clearUserFundTableID(UserFundTable uft);

	int numUserFundTableID(Long userID);

	List<Long> findUserPlanID(Long userID);

	boolean hasPlan(Long userID, Long planID);

	void saveUserFundTableID(Long userID, Long planID);

	List<UserProfile> getUserProfileOutTime(Date today);

	public List<User> changeUserGroup();

	List<UserProfile> getAllUserProfile();

	List<UserProfile> getUserProfileByTime(int interval);

	List<UserTransaction> getUserTransaction(Long userID);
	
	List<UserTransaction> getUserTransactionByTimeAndTxnType(String timeCreated,String txnType,String itemName);
	
	List<UserTransaction> getUserTransactionByPaymentDateAndTxnType(String paymentDate,String txnType);

	Map<String, List<String>> getAllItem();

	void savePayItem(String itemName, List<Long> groups);

	void saveUserTransaction(UserTransaction userTran);

	void addGroupByItem(Long userID) throws Exception;

	void deletePayItem(String item_name);

	void saveOrUpdateUserProfile(UserProfile profile);

	List<UserProfile> getUserProfileByStatus(String payerStatus);
	
	List<UserProfile> getUserProfileByStatusAndItemName(String userStatus,String itemName);

	void saveUserResByPortfolioCounts(long userID, Long portfolioID);

	void saveUserResByStrategyCounts(long userID, Long strategyID,int strategyCount);

	List<Integer> getUserResTypeCounts(Long userID);

	List<UserResource> getUserResByUserID(Long userID);

	List<UserResource> getUserResByUserID(Long userID, UserResource userres);

	List<UserResource> getByURPorperty(String propertyName, Object value);

	List<UserResource> getUesrResByExample(UserResource userres);

	void updateUserResourse(UserResource userres);

	void deleteUserResource(UserResource userres);

	void saveUserResource(UserResource userres);

	UserProfile getUserProfile(Long userID);

	void deleteUserProfile(Long userID);

	void deleteUserTransaction(Long userID);


	void removeGroupByItem(Long userID) throws Exception;
	List<UserProfile> getUserProfileInTime(Date today);

	List<String> getAllItemName();

	void changUserItemName(Long userID, String itemName) throws Exception;
	
	public long saveUserOperation(UserOperation uo);
	public void updateUserOperation(UserOperation uo);
	public List<UserOperation> getUserOperations(long userid);

	public boolean existPayerId(String payerId, String itemName);

	public UserOperation getUserOperation(long id);

	List<Transaction> showEmailTransaction(Date tdate);

	public User getUserBySubscrId(String subScrID);

	List<Object[]> showEmailPortfolio();

	Date getLastSentDate(Long portfolioID);

	void saveMarketEmail(UserMarket userMarket);

	void deleteMarketEmail(UserMarket userMarket);

	List<UserMarket> getMarketEmailbyProperty(String propertyName,
			Object value, boolean flag);

	List<UserMarket> getMEmailsbyProperty(String[] propertyNames,
			String[] values, String[] judge);

	void saveMarkerEmails(List<UserMarket> umlist);

	boolean hasMarketEmail(String userEmail);

	String[] getMarketEmailsByKey(String[] Keys);

	List<String> getEmailsGroupKeys();

	void updateMarketEmail(UserMarket userMarket);

	public void saveUserResByPlanCreate(long userID,long planID);

	boolean canPlanCreate(long userID, int planNum);

	boolean canPortfolioUse(long userID,int portfolioCount);

	boolean canPlanUse(long userID, long strategyID, int strategyCount);

	int getCurrentPortfolioNumber(long userID);

	int getCurrentPlanUsedNumber(long userID);

	int getCurrentPlanCreatedNumber(long userID);

	/****************************20110118创建，权限管理**************************/
	UserPermission getUserPermissionByUserID(Long userID);
	
	void saveUserPermission(UserPermission userPermission);
	
	void updateUserPermission(UserPermission userPermission);
	
	void saveOrUpdateUserPermission(UserPermission userPermission);
	
	GroupPermission getGroupPermissionByGroupID(Long groupID);
	
	void saveGroupPermission(GroupPermission groupPermission);
	
	void updateGroupPermission(GroupPermission groupPermission);
	
	void saveOrUpdateGroupPermission(GroupPermission groupPermission);
	
	List<GroupPermission> getGroupPermissionList();
	
	void saveOrUpdateUserPermissionList(List<UserPermission> userPermissionList);
	
	/***********从UserResource表里获得各种ID列表**********/
	
	void adjustCurPlanCreateNum(Long userID, int adjustNum) throws Exception;
	
	void adjustCurPlanRefNum(Long userID, int adjustNum) throws Exception;
	
	void adjustCurPlanFundTableNum(Long userID, int adjustNum) throws Exception;
	
	void adjustCurPortfolioFollowNum(Long userID, int adjustNum) throws Exception;
	
	void adjustCurPortfolioRealTimeNum(Long userID, int adjustNum) throws Exception;
	
	void saveUserResource(Long userID, Long resourceID, int resourceType);
	
	void saveUserResource(Long userID, Long resourceID, int resourceType, Date updateTime);
	
	List<Long> getResourceIDListByUserIDAndResourceType(Long userID, int resourceType);
	
	UserResource getUserResourceByUserIDAndResourceIDAndResourceType(Long userID, Long resourceID, int resourceType);
	
	List<UserResource> getUserResourceByResourceIDAndResourceType(Long resourceID, int resourceType);
	
	List<UserResource> getUserResourceByUserIDAndResourceType(Long userID, int resourceType);

	List<UserResource> getUserResourceByResourceType(int resourceType);
	
	List<UserResource> getUserResourceByResourceTypeAndExpiredTime(int resourceType, Date expiredTime);
	
	List<Long> getEmailNotificationsByUserID(Long userID);
	
	List<UserResource> getFollowPortfolioResourcesByPortfolioID(Long portfolioID);
	
	UserResource getCustomizePortfolioResourceByPortfolioID(Long portfolioID);

	List<Long> getResourceIDListByUserIDAndResourceTypeAndRelationID(Long userID, int resourceType, long relationID);

	UserResource getUserResourceByUserIDAndResourceIDAndResourceTypeAndRelationID(Long userID, Long resourceID, int resourceType, long relationID);
	List<Long> getUnSendEmailPortfolioIDList(Date endDate);
	public List<User> numUser(long i);
	public List<UserTransaction> findUserbyInviteId(long i);

	User getUserByCharCode(String cc);

	void adjustCurConsumerPlanNum(Long userID, int adjustNum) throws Exception;

	List<Long> getUserIDListByPlanIDs(Long[] portfolioids);
}
