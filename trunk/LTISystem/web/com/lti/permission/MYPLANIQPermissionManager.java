package com.lti.permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupPermission;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserPermission;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
/**
 * 全局默认userID不为空，即用户必须已经登录
 * @author Administrator
 *
 */
public class MYPLANIQPermissionManager {
	/**
	 * 成功允许
	 */
	public final int SUCCESS = 0;
	/**
	 * 无权限
	 */
	public final int PERMISSION_LIMIT = 1;
	/**
	 * 功能本身数目限制
	 */
	public final int COUNT_LIMIT = 2;
	/**
	 * PLAN引用限制
	 */
	public final int PLAN_REF_COUNT_LIMIT = 3;
	/**
	 * 对SAA的限制
	 */
	public final int PERMISSION_SAA_LIMIT = 4;
	/**
	 * 对TAA的限制
	 */
	public final int PERMISSION_TAA_LIMIT = 5;
	/**
	 * 对SAA的限制
	 */
	public final int PERMISSION_ADV_LIMIT = 6;
	
	/**
	 * 对大众plan数目的限制
	 */
	public final int PERMISSION_CONSUMER_LIMIT = 6;
	public MYPLANIQPermissionManager(){
		userManager = ContextHolder.getUserManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		groupManager = ContextHolder.getGroupManager();
		strategyManager = ContextHolder.getStrategyManager();
	}
	UserManager userManager;
	PortfolioManager portfolioManager;
	GroupManager groupManager;
	StrategyManager strategyManager;
	/********************************** begin: private method **********************************/
	/**
	 * 返回大数
	 * @param num1
	 * @param num2
	 * @return
	 */
	private int max(int num1, int num2){
		return num1 < num2? num2: num1;
	}
	/**
	 * 是否拥有该资源
	 * @param userID
	 * @param resourceID
	 * @param resourceType
	 * @return
	 */
	private boolean hasUserResource(Long userID, Long resourceID, int resourceType){
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, resourceID, resourceType);
		if(userResource != null)
			return true;
		return false;
	}
	/**
	 * 策略是否为401k
	 * @param planID
	 * @return
	 */
	private boolean is401kPlan(Long planID){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Strategy strategy = strategyManager.get(planID);
		return strategy.is401K();
	}
	/********************************** end: private method **********************************/
	
	/**
	 * plan是否是用户创建
	 * @param userID
	 * @param planID
	 * @return
	 */
	public boolean hasUserPlanCreate(Long userID, Long planID){
		return hasUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_CREATE);
	}
	/**
	 * plan是否已被用户引用
	 * @param userID
	 * @param planID
	 * @return
	 */
	public boolean hasUserPlanRef(Long userID, Long planID){
		return hasUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_REFERENCE);
	}
	/**
	 * plan的fund table是否已被用户查看
	 * @param userID
	 * @param planID
	 * @return
	 */
	public boolean hasUserPlanFundTable(Long userID, Long planID){
		return hasUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
	}
	/**
	 * portfolio是否是用户customize
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public boolean hasUserPortfolioCustomize(Long userID, Portfolio portfolio){
		return hasUserResource(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
	}
	/**
	 * portfolio是否是用户follow
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public boolean hasUserPortfolioFollow(Long userID, Portfolio portfolio){
		return hasUserPortfolioFollow(userID, portfolio.getID());
	}
	public boolean hasUserPortfolioFollow(Long userID, Long portfolioID) {
		return hasUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
	}
	/**
	 * portfolio是否是realtime
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public boolean hasUserPortfolioRealTime(Long userID, Portfolio portfolio) {
		return hasUserResource(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
	}
	/**
	 * 判断是否是管理员
	 * @param userID
	 * @return
	 */
	public boolean isAdmin(Long userID) {
		return userID.equals(Configuration.SUPER_USER_ID);
	}
	/**
	 * 判断是否是portfolio owner
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public boolean isPortfolioOwner(Long userID, Portfolio portfolio) {
		return hasUserPortfolioCustomize(userID, portfolio);
	}
	/**
	 * 是否有权利follow这个portfolio，这里没有进行数目控制
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public int hasFollowPortfolioPermission(Long userID, Portfolio portfolio){
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		Long strategyID = portfolio.getStrategies().getAssetAllocationStrategy().getID();
		if(strategyID.equals(Configuration.STRATEGY_TAA_ID)){
			if(!groupPermission.getFollowTAA())
				return PERMISSION_TAA_LIMIT;
		}else if(strategyID.equals(Configuration.STRATEGY_SAA_ID)){
			if(!groupPermission.getFollowSAA())
				return PERMISSION_SAA_LIMIT;
		}else{
			if(!groupPermission.getFollowADV())
				return PERMISSION_ADV_LIMIT;
		}
		return SUCCESS;
	}
	/**
	 * 是否有customize一个portfolio的权限，这里没有进行数目控制
	 * 根据strategyID
	 * @param userID
	 * @param strategyID
	 * @return
	 */
	public int hasCustomizePortfolioPermission(Long userID, Long strategyID){
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(strategyID.equals(Configuration.STRATEGY_SAA_ID)){
			if(!groupPermission.getFollowSAA())
				return PERMISSION_SAA_LIMIT;
		}else if(strategyID.equals(Configuration.STRATEGY_TAA_ID)) {
			if(!groupPermission.getFollowTAA())
				return PERMISSION_TAA_LIMIT;
		}else if(!groupPermission.getFollowADV())
				return PERMISSION_ADV_LIMIT;
		return SUCCESS;
	}
	
	public boolean isAdvancedUser(Long userID){
		Long groupID = getUserHighestGroupIDByUserID(userID);
		if(groupID != null && (groupID == Configuration.GROUP_MPIQ_E_ID || groupID == Configuration.GROUP_MPIQ_P_ID))
			return true;
		return false;
	}
	
	/**
	 * 获得用户权限组里面最高级别的group
	 * GROUP_MPIQ_P_ID 		professor
	 * GROUP_MPIQ_E_ID 		expert
	 * GROUP_MPIQ_B_ID 		basic
	 * GROUP_MPIQ_ID   		register
	 * GROUP_ANONYMOUS_ID 	anonymous
	 * @param userID
	 * @return
	 */
	public Group getUserHighGroupByUserID(Long userID){
		Long highGroupID = Configuration.GROUP_ANONYMOUS_ID;
		Object[] groups = groupManager.getGroupIDs(userID);
		List userGroupIDList = Arrays.asList(groups);
		if(userGroupIDList.contains(Configuration.GROUP_MPIQ_P_ID))
			highGroupID = Configuration.GROUP_MPIQ_P_ID;
		else if(userGroupIDList.contains(Configuration.GROUP_MPIQ_E_ID))
			highGroupID = Configuration.GROUP_MPIQ_E_ID;
		else if(userGroupIDList.contains(Configuration.GROUP_MPIQ_B_ID))
			highGroupID = Configuration.GROUP_MPIQ_B_ID;
		else if(userGroupIDList.contains(Configuration.GROUP_MPIQ_ID))
			highGroupID = Configuration.GROUP_MPIQ_ID;
		return groupManager.get(highGroupID);
	}
	/**
	 * 
	 * @param userID
	 * @return
	 */
	public Long getUserHighestGroupIDByUserID(Long userID){
		Long highGroupID = Configuration.GROUP_ANONYMOUS_ID;
		Object[] groups = groupManager.getGroupIDs(userID);
		if(groups != null){
			List userGroupIDList = Arrays.asList(groups);
			if(userGroupIDList.contains(Configuration.GROUP_MPIQ_P_ID))
				return Configuration.GROUP_MPIQ_P_ID;
			else if(userGroupIDList.contains(Configuration.GROUP_MPIQ_E_ID))
				return Configuration.GROUP_MPIQ_E_ID;
			else if(userGroupIDList.contains(Configuration.GROUP_MPIQ_B_ID))
				return Configuration.GROUP_MPIQ_B_ID;
			else if(userGroupIDList.contains(Configuration.GROUP_MPIQ_ID))
				return Configuration.GROUP_MPIQ_ID;
		}
		return Configuration.GROUP_ANONYMOUS_ID;
	}
	/**
	 * 根据用户ID获得最高组权限
	 * @param userID
	 * @return
	 */
	public GroupPermission getGroupPermissionByUserID(Long userID){
		Long groupID = this.getUserHighestGroupIDByUserID(userID);
		return userManager.getGroupPermissionByGroupID(groupID);
	}
	/**
	 * 根据用户的最高组别权限来设置用户权限
	 * @param userID
	 * @param gropuPermission
	 */
	private void createOneUserPermission(Long userID, GroupPermission groupPermission){
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null) userPermission = new UserPermission();
		userPermission.setUserID(userID);
		userPermission.setCurPlanCreateNum(0);
		userPermission.setCurPlanFundTableNum(0);
		userPermission.setCurPlanRefNum(0);
		userPermission.setCurPortfolioFollowNum(0);
		userPermission.setCurPortfolioRealTimeNum(0);
		userPermission.setMaxPlanCreateNum(groupPermission.getMaxPlanCreateNum());
		userPermission.setMaxPlanFundTableNum(groupPermission.getMaxPlanFundTableNum());
		userPermission.setMaxPlanRefNum(groupPermission.getMaxPlanRefNum());
		userPermission.setMaxPortfolioFollowNum(groupPermission.getMaxPortfolioFollowNum());
		userPermission.setMaxPortfolioRealTimeNum(groupPermission.getMaxPortfolioRealTimeNum());
		userPermission.setMaxConsumerPlanNum(groupPermission.getMaxConsumerPlanNum());
		userManager.saveOrUpdateUserPermission(userPermission);
	}
	/**
	 * 根据用户的最高组别权限来设置用户权限
	 * 每次创建用户时执行
	 * @param userID
	 */
	public void createOneUserPermission(Long userID) {
		Long groupID = getUserHighestGroupIDByUserID(userID);
		createOneUserPermission(userID, userManager.getGroupPermissionByGroupID(groupID));
	}
	/**
	 * 根据用户的最高组别权限来设置用户权限,并不改变各数目控制的当前值，只改变最大值
	 * 每次用户改变最高级别权限时执行
	 * @param userID
	 * @param groupPermission
	 */
	private void changeOneUserPermission(Long userID, GroupPermission groupPermission) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		userPermission.setMaxPlanCreateNum(groupPermission.getMaxPlanCreateNum());
		userPermission.setMaxPlanFundTableNum(groupPermission.getMaxPlanFundTableNum());
		userPermission.setMaxPlanRefNum(groupPermission.getMaxPlanRefNum());
		userPermission.setMaxPortfolioFollowNum(groupPermission.getMaxPortfolioFollowNum());
		userPermission.setMaxPortfolioRealTimeNum(groupPermission.getMaxPortfolioRealTimeNum());
		userManager.updateUserPermission(userPermission);
	}
	/**
	 * 根据用户的最高组别权限来设置用户权限,并不改变各数目控制的当前值，只改变最大值
	 * 每次用户改变最高级别权限时执行
	 * @param userID
	 */
	public void changeOneUserPermission(Long userID) {
		Long groupID = getUserHighestGroupIDByUserID(userID);
		changeOneUserPermission(userID, userManager.getGroupPermissionByGroupID(groupID));
	}
	/**
	 * 获得用户初始化权限
	 * 每次创建用户或改变用户级别时执行
	 * @param userID
	 * @param gropuPermission
	 */
	public UserPermission getOneUserPermission(Long userID, GroupPermission groupPermission){
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null) userPermission = new UserPermission();
		userPermission.setUserID(userID);
		userPermission.setCurPlanCreateNum(0);
		userPermission.setCurPlanFundTableNum(0);
		userPermission.setCurPlanRefNum(0);
		userPermission.setCurPortfolioFollowNum(0);
		userPermission.setCurPortfolioRealTimeNum(0);
		userPermission.setMaxPlanCreateNum(groupPermission.getMaxPlanCreateNum());
		userPermission.setMaxPlanFundTableNum(groupPermission.getMaxPlanFundTableNum());
		userPermission.setMaxPlanRefNum(groupPermission.getMaxPlanRefNum());
		userPermission.setMaxPortfolioFollowNum(groupPermission.getMaxPortfolioFollowNum());
		userPermission.setMaxPortfolioRealTimeNum(groupPermission.getMaxPortfolioRealTimeNum());
		return userPermission;
	}
	/**
	 * 根据用户组织初始化所有的用户权限，并且数据清0，慎用
	 */
	public void initialAllUserPermission(){
		List<UserPermission> userPermissionList = new ArrayList<UserPermission>();
		Map<Long, GroupPermission> groupPermissionMap = new HashMap<Long, GroupPermission>();
		List<GroupPermission> groupPermissionList = userManager.getGroupPermissionList();
		for(GroupPermission gp : groupPermissionList)
			groupPermissionMap.put(gp.getGroupID(), gp);
		for(Long gpID: groupPermissionMap.keySet()){
			System.out.println(gpID);
		}
		if(groupPermissionMap.size() == 0)
			System.out.println("null");
		List<User> userList = userManager.getUsers();
		for(User user : userList){
			Long groupID = getUserHighestGroupIDByUserID(user.getID());
			GroupPermission groupPermission = groupPermissionMap.get(groupID);
			if(groupPermission == null){
				groupPermission = groupPermissionMap.get(1L);
				System.out.println("Abnormal User:" + user.getID() + "," + groupID);
			}
			userPermissionList.add(getOneUserPermission(user.getID(), groupPermission));
		}
		userManager.saveOrUpdateUserPermissionList(userPermissionList);
	}
	/**
	 * 为个别用户设置特殊权限
	 * @param userID
	 * @param maxPlanCreateNum
	 * @param maxPlanRefNum
	 * @param maxPlanFundTableNum
	 * @param maxPortfolioFollowNum
	 * @param maxPortfolioRealTimeNum
	 */
	public void setPersonalUserPermission(Long userID, int maxPlanCreateNum, int maxPlanRefNum, int maxPlanFundTableNum, int maxPortfolioFollowNum, int maxPortfolioRealTimeNum,int maxConsumerPlanNum) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null) userPermission = new UserPermission();
		userPermission.setMaxPlanCreateNum(maxPlanCreateNum);
		userPermission.setMaxPlanFundTableNum(maxPlanFundTableNum);
		userPermission.setMaxPlanRefNum(maxPlanRefNum);
		userPermission.setMaxPortfolioFollowNum(maxPortfolioFollowNum);
		userPermission.setMaxPortfolioRealTimeNum(maxPortfolioRealTimeNum);
		userPermission.setMaxConsumerPlanNum(maxConsumerPlanNum);
	}
	/****************************************************begin 判断是否具有操作权限的API****************************************************/
	public int canPlanCreate(Long userID){
		return canPlanCreate(userID,false);
	}
	
	/**
	 * 创建plan
	 * @param userID
	 * @return
	 */
	public int canPlanCreate(Long userID,boolean consumer){
		if(isAdmin(userID)) return SUCCESS;
		if(!canCreatePlan(userID,consumer))
			return PERMISSION_LIMIT;
		
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(!consumer){
			if(userPermission == null || userPermission.getCurPlanCreateNum() >= userPermission.getMaxPlanCreateNum())
				return COUNT_LIMIT;
			return SUCCESS;
		}else{
			if(userPermission == null || userPermission.getCurConsumerPlanNum() >= userPermission.getMaxConsumerPlanNum())
				return COUNT_LIMIT;
			return SUCCESS;
		}
		
	}
	/**
	 * 创建portfolio时引用的不属于自己创建的策略
	 * @param userID
	 * @param planID
	 * @return
	 */
	public int canPlanRef(Long userID, Long planID){
		if(isAdmin(userID)) return SUCCESS;
		if(planID == null || !is401kPlan(planID)) return SUCCESS;
		//首先考虑该plan是否是用户创建的，是的话不用考虑Ref计数，其次考虑该plan是否被引用过，是的话不用考虑Ref计数
		if(hasUserPlanCreate(userID, planID) || hasUserPlanRef(userID, planID)) return SUCCESS;
		//再次考虑数目限制
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null || userPermission.getCurPlanRefNum() >= userPermission.getMaxPlanRefNum())
			return COUNT_LIMIT;
		return SUCCESS;
	}
	/**
	 * 查看fund table
	 * @param userID
	 * @param planID
	 * @return
	 */
	public int canPlanFundTable(Long userID, Long planID){
		if(isAdmin(userID)) return SUCCESS;
		//首先考虑该plan是否是用户创建的，是的话不用考虑fund table计数，其次考虑该plan的fund table是否已被用户查看，即查看同个plan不重复计数
		if(hasUserPlanCreate(userID, planID) || hasUserPlanFundTable(userID, planID)) return SUCCESS;
		//再次考虑数目限制
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null || userPermission.getCurPlanFundTableNum() >= userPermission.getMaxPlanFundTableNum())
			return COUNT_LIMIT;
		return SUCCESS;
	}
	/**
	 * 创建portfolio
	 * @param userID
	 * @param planID
	 * @return
	 */
	public int canPortfolioCustomize(Long userID, Portfolio portfolio, Long planID){
		if(isAdmin(userID)) return SUCCESS;
		//首先考虑是否允许
		int operationCode = hasFollowPortfolioPermission(userID, portfolio);
		if( operationCode != SUCCESS) return operationCode;
		//其次考虑数目限制
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null || userPermission.getCurPortfolioFollowNum() >= userPermission.getMaxPortfolioFollowNum())
			return COUNT_LIMIT;
		//再次考虑plan引用限制
		if(canPlanRef(userID, planID) != SUCCESS) return PLAN_REF_COUNT_LIMIT;
		return SUCCESS;
	}
	/**
	 * 创建portfolio，根据strategyID
	 * @param userID
	 * @param planID
	 * @param strategyID
	 * @return
	 */
	public int canPortfolioCustomize(Long userID, Long planID, Long strategyID){
		if(isAdmin(userID)) return SUCCESS;
		//首先考虑是否允许
		int operationCode = hasCustomizePortfolioPermission(userID, strategyID);
		if( operationCode != SUCCESS) return operationCode;
		//其次考虑数目限制
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null || userPermission.getCurPortfolioFollowNum() >= userPermission.getMaxPortfolioFollowNum())
			return COUNT_LIMIT;
		//再次考虑plan引用限制
		if(canPlanRef(userID, planID) != SUCCESS) return PLAN_REF_COUNT_LIMIT;
		return SUCCESS;
	}
	/**
	 * Follow portfolio
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public int canPortfolioFollow(Long userID, Portfolio portfolio, Long planID){
		if(isAdmin(userID)) return SUCCESS;
		//首先考虑是否允许该功能
		int operationCode = hasFollowPortfolioPermission(userID, portfolio);
		if( operationCode != SUCCESS) return operationCode;
		//其次判断这个portfolioID 是不是用户本人customize的，如果是的话不用判断数目
		if(hasUserPortfolioCustomize(userID, portfolio))
			return SUCCESS;
		//再次进行数目控制
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null || userPermission.getCurPortfolioFollowNum() >= userPermission .getMaxPortfolioFollowNum())
			return COUNT_LIMIT;
		//最后考虑plan引用限制
		if(canPlanRef(userID, planID) != SUCCESS) return PLAN_REF_COUNT_LIMIT;
		return SUCCESS;
	}
	/**
	 * 查看portfolio实时信息
	 * 暂时不用
	 * @param userID
	 * @param portfolioID
	 * @return
	 */
	public int canPortfolioRealTime(Long userID, Portfolio portfolio){
		if(isAdmin(userID)) return SUCCESS;
		if(hasUserPortfolioRealTime(userID, portfolio))
			return SUCCESS;
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission == null || userPermission.getCurPortfolioRealTimeNum() >= userPermission.getMaxPortfolioRealTimeNum())
			return COUNT_LIMIT;
		return SUCCESS;
	}
	/****************************************************end 判断是否具有操作权限的API****************************************************/
	
	/****************************************************begin 调整数目并返回剩余操作权限的API****************************************************/
	/**
	 * 调整创建plan的数目
	 * @param userID
	 * @param adjustNum
	 * @throws Exception 
	 */
	public void adjustPlanCreateNum(Long userID, int adjustNum) throws Exception{
		if(isAdmin(userID)) return;
		userManager.adjustCurPlanCreateNum(userID, adjustNum);
	}
	/**
	 * 调整plan引用的数目
	 * @param userID
	 * @param adjustNum
	 * @throws Exception 
	 */
	public void adjustPlanRefNum(Long userID, int adjustNum) throws Exception{
		if(isAdmin(userID)) return;
		userManager.adjustCurPlanRefNum(userID, adjustNum);
	}
	/**
	 * 调整查看fund table的数目
	 * @param userID
	 * @param adjustNum
	 * @throws Exception 
	 */
	public void adjustPlanFundTableNum(Long userID, int adjustNum) throws Exception{
		if(isAdmin(userID)) return;
		userManager.adjustCurPlanFundTableNum(userID, adjustNum);
	}
	/**
	 * 调整follow portfolio的数目(这里的follow 包括customize)
	 * @param userID
	 * @param adjustNum
	 * @throws Exception 
	 */
	public void adjustPortfolioFollowNum(Long userID, int adjustNum) throws Exception{
		if(isAdmin(userID)) return;
		userManager.adjustCurPortfolioFollowNum(userID, adjustNum);
	}
	/**
	 * 调整查看portfolio实时信息的数目
	 * @param userID
	 * @param adjustNum
	 * @throws Exception 
	 */
	public void adjustPortfolioRealTimeNum(Long userID, int adjustNum) throws Exception{
		if(isAdmin(userID)) return;
		userManager.adjustCurPortfolioRealTimeNum(userID, adjustNum);
	}
	/****************************************************end 调整数目并返回剩余操作权限的API****************************************************/
	/****************************************************begin 计算剩余操作权限数目的API****************************************************/
	/**
	 * @param userID
	 * @return 返回还可以查看portfolio实时信息的数目
	 */
	public int getAllowPortfolioRealTimeNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return max(userPermission.getMaxPortfolioRealTimeNum() - userPermission.getCurPortfolioRealTimeNum(), 0);
	}
	/**
	 * 返回用户最多可以查看portfolio实时信息的数目
	 * @param userID
	 * @return 
	 */
	public int getMaxPortfolioRealTimeNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getMaxPortfolioRealTimeNum();
	}
	/**
	 * 返回用户现在已查看portfolio实时信息的数目
	 * @param userID
	 * @return 
	 */
	public int getCurPortfolioRealTimeNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getCurPortfolioRealTimeNum();
	}
	/**
	 * 返回还可以follow或customize portfolio的数目
	 * @param userID
	 * @return
	 */
	public int getAllowPortfolioFollowNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return max(userPermission.getMaxPortfolioFollowNum() - userPermission.getCurPortfolioFollowNum(), 0);
	}
	/**
	 * 返回用户最多可以follow或customize portfolio的数目
	 * @param userID
	 * @return
	 */
	public int getMaxPortfolioFollowNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getMaxPortfolioFollowNum();
	}
	/**
	 * 返回用户现在已经follow或customize portfolio的数目
	 * @param userID
	 * @return
	 */
	public int getCurPortfolioFollowNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getCurPortfolioFollowNum();
	}
	/**
	 * 返回还可以创建多少个plan
	 * @param userID
	 * @return 
	 */
	public int getAllowPlanCreateNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return max(userPermission.getMaxPlanCreateNum() - userPermission.getCurPlanCreateNum(), 0);
	}
	/**
	 * 返回用户最多可以创建plan个数
	 * @param userID
	 * @return
	 */
	public int getMaxPlanCreateNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getMaxPlanCreateNum();
	}
	/**
	 * 返回用户现在已创建plan个数
	 * @param userID
	 * @return
	 */
	public int getCurPlanCreateNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getCurPlanCreateNum();
	}
	/**
	 * 返回还可以引用多少个plan
	 * @param userID
	 * @return
	 */
	public int getAllowPlanRefNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return max(userPermission.getMaxPlanRefNum() - userPermission.getCurPlanRefNum(), 0);
	}
	/**
	 * 返回用户现在已引用多少个plan
	 * @param userID
	 * @return
	 */
	public int getMaxPlanRefNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getMaxPlanRefNum();
	}
	/**
	 * 返回用户最多可以引用多少个plan
	 * @param userID
	 * @return
	 */
	public int getCurPlanRefNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getCurPlanRefNum();
	}
	/**
	 * 返回还可以查看fundtable的数目
	 * @param userID
	 * @return
	 */
	public int getAllowPlanFundTableNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return max(userPermission.getMaxPlanFundTableNum() - userPermission.getCurPlanFundTableNum(), 0);
	}
	/**
	 * 返回用户最多可以查看fundtable的数目
	 * @param userID
	 * @return
	 */
	public int getMaxPlanFundTableNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getMaxPlanFundTableNum();
	}
	/**
	 * 返回用户现在已查看fundtable的数目
	 * @param userID
	 * @return
	 */
	public int getCurPlanFundTableNum(Long userID) {
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		return userPermission.getCurPlanFundTableNum();
	}
	/****************************************************begin 相应操作完成后修改user resource并修改操作权限的API****************************************************/
	/**
	 * 创建plan后的操作
	 * 创建用户资源并且更新计数
	 * @param userID
	 * @param planID 新创建的plan的ID
	 * @return 返回还可以创建plan的数目
	 * @throws Exception 
	 */
	public void afterPlanCreate(Long userID, Long planID,boolean consumer) throws Exception {
		if(isAdmin(userID)) return;
		
		if(consumer){
			
			userManager.adjustCurConsumerPlanNum(userID, 1);
		}else{
			userManager.saveUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_CREATE);
			
			adjustPlanCreateNum(userID, 1);
		}
	}
	public void afterPlanCreate(Long userID, Long planID) throws Exception {
		this.afterPlanCreate(userID, planID,false);
	}
	public void afterPlanDelete(Long userID, Long planID)throws Exception{
		this.afterPlanDelete(userID, planID, false);
	}
	/**
	 * 删除plan后的操作（afterPortfoDelete 让相关删除portfolio的操作去完成）
	 * 还没测试完全
	 * @param userID
	 * @param planID
	 * @throws Exception 
	 */
	public void afterPlanDelete(Long userID, Long planID,boolean consumer) throws Exception {
		if(isAdmin(userID)) return;
		if(consumer){
			userManager.adjustCurConsumerPlanNum(userID, -1);
		}else{
			UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_CREATE);
			if(userResource != null){//避免程序不小心多次删除造成计数器错误
				userManager.deleteUserResource(userResource);
				adjustPlanCreateNum(userID, -1);
			}
		}
		
		
	}
	/**
	 * 查看完一个plan 的 fund table后的操作
	 * 判断是否需要创建用户资源并更新计数
	 * @param userID
	 * @param planID
	 * @throws Exception
	 */
	public void afterPlanFundTable(Long userID, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		//先考虑该plan是否已被用户创建的，是的话不用添加新资源并且不用考虑fund table计数
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_CREATE);
		if(userResource != null)//是用户创建
			return;
		//其次考虑该plan的fund table是否已被查看，是的话则不用添加新资源并且不用考虑fund table计数
		userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
		if(userResource != null)//fund table已经被用户查看过
			return;
		userManager.saveUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
		adjustPlanFundTableNum(userID, 1);
	}
	/**
	 * 删除某个用户查看过某个fundtable 的记录
	 * @param userID
	 * @param planID
	 * @throws Exception
	 */
	public void afterPlanFundTableCancel(Long userID, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
		if(userResource != null) {//fund table已经被用户查看过
			userManager.deleteUserResource(userResource);
			adjustPlanFundTableNum(userID, -1);
		}
	}
	/**
	 * Customize 或 Follow portfolio时引用到plan后的操作
	 * 判断是否需要创建用户资源并更新计数
	 * @param userID
	 * @param planID
	 * @throws Exception
	 */
	public void afterPlanRef(Long userID, Long planID) throws Exception {
		if(isAdmin(userID) || planID == null) return;
		//先考虑该plan是否已被用户创建的，是的话不用添加新资源并且不用考虑plan referenct计数
		if(this.hasUserPlanCreate(userID, planID))
			return;
		//其次考虑该plan的是否已被引用，是的话则不用添加新资源并且不用考虑plan referenct计数
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_REFERENCE);
		if(userResource != null){//已经被用户引用过，资源计数器加1
			userResource.setResourceCount(userResource.getResourceCount() + 1);
			userManager.updateUserResourse(userResource);
			return;
		}
		userManager.saveUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_REFERENCE);
		adjustPlanRefNum(userID, 1);
	}
	/**
	 * Unfollow 或 delete portfolio时取消引用plan后的操作
	 * @param userID
	 * @param planID
	 * @throws Exception
	 */
	public void afterPlanUnref(Long userID, Long planID) throws Exception {
		if(isAdmin(userID) || planID == null) return;
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_REFERENCE);
		if(userResource != null){
			if(userResource.getResourceCount() > 1){//引用计数减1
				userResource.setResourceCount(userResource.getResourceCount() - 1);
				userManager.updateUserResourse(userResource);
			}else{//直接删除引用，并且调整计数
				userManager.deleteUserResource(userResource);
				adjustPlanRefNum(userID, -1);
			}
		}
	}
	/**
	 * 创建portfolio后的操作
	 * 这里要添加的资源包括portfolio customize, portfolio follow, portfolio realtime, 可能要添加的是plan reference
	 * 同时必须在旧权限系统里面设置为realtime，to be finished
	 * @param userID
	 * @param portfolioID
	 * @param planID 401k planID,如果是validfi策略的话，为null
	 * @throws Exception 
	 */
	public void afterPortfolioCustomize(Long userID, Long portfolioID, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
		adjustPortfolioFollowNum(userID, 1);
		adjustPortfolioRealTimeNum(userID, 1);
		//创建plan reference 资源并且增加计数
		afterPlanRef(userID, planID);
	}
	/**
	 * 删除portfolio后的操作
	 * 可能要删除的资源, portfolio customize, portfolio follow, portfolio realtime, plan reference, 同时可能要为对应的计数器减1
	 * @param userID
	 * @param portfolioID
	 * @throws Exception
	 */
	public void afterPortfolioDelete(Long userID, Long portfolioID, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		afterPortfolioUnfollowAndDelete(userID, portfolioID, planID);
	}
	/**
	 * unfollow portfolio并且删除portfolio后的操作
	 * 可能要删除的资源, portfolio customize, portfolio follow, portfolio realtime, plan reference？？？？, 同时可能要为对应的计数器减1
	 * @param userID
	 * @param portfolioID
	 * @throws Exception
	 */
	public void afterPortfolioUnfollowAndDelete(Long userID, Long portfolioID, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		boolean isCustomizePortfolio = false;
		boolean isFollowPortfolio = false;
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		if(userResource != null){
			userManager.deleteUserResource(userResource);
			isCustomizePortfolio = true;
		}
		userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		if(userResource != null){
			userManager.deleteUserResource(userResource);
			isFollowPortfolio = true;
		}
		//必须是customize或follow一个为true时计数可减1
		if(isCustomizePortfolio || isFollowPortfolio)//避免重复减数
			adjustPortfolioFollowNum(userID, -1);
		userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
		if(userResource != null){//避免程序不小心多次删除
			userManager.deleteUserResource(userResource);
			adjustPortfolioRealTimeNum(userID, -1);
		}
		if(isCustomizePortfolio || isFollowPortfolio)
			afterPlanUnref(userID, planID);
	}
	/**
	 * follow portfolio后的操作
	 * 可能要创建的资源, portfolio follow, portfolio realtime, plan reference, 同时可能要为对应的计数器加1
	 * 同时必须在旧权限系统里面设置为realtime，to be finished
	 * @param userID
	 * @param portfolioID
	 * @param planID 401k planID,如果是validfi策略的话，为null
	 * @throws Exception 
	 */
	public void afterPortfolioFollow(Long userID, Portfolio portfolio, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		boolean isCustomizePortfolio = hasUserPortfolioCustomize(userID, portfolio);
		//创建portfolio follow 资源并且增加计数
		if(!hasUserPortfolioFollow(userID, portfolio)){//避免程序不小心多次添加
			userManager.saveUserResource(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW, portfolio.getEndDate());
			if(!isCustomizePortfolio)//不是customize portfolio,此时计数加1
				adjustPortfolioFollowNum(userID, 1);
		}
		//创建portfolio realtime 资源并且增加计数
		if(!hasUserPortfolioRealTime(userID, portfolio)){//避免程序不小心多次添加
			userManager.saveUserResource(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_REALTIME, portfolio.getEndDate());
			adjustPortfolioRealTimeNum(userID, 1);
		}
		//创建plan reference 资源并且增加计数
		if(!isCustomizePortfolio)
			afterPlanRef(userID, planID);
		//如果存在关于该portfolio的过期资源，则删除
		UserResource expiredCustomizeResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE_EXPIRED);
		if(expiredCustomizeResource != null)
			userManager.deleteUserResource(expiredCustomizeResource);
		UserResource expiredFollowResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW_EXPIRED);
		if(expiredFollowResource != null)
			userManager.deleteUserResource(expiredFollowResource);
	}
	/**
	 * unfollow portfolio但没有删除portfolio后的操作
	 * 可能要删除的资源, portfolio follow, portfolio realtime, plan reference？？？？, 同时可能要为对应的计数器减1
	 * 同时必须在旧权限系统里面设置为delaytime，to be finished
	 * 暂时不用
	 * @param userID
	 * @param portfolioID
	 * @throws Exception
	 */
	public void afterPortfolioUnfollow(Long userID, Portfolio portfolio, Long planID) throws Exception {
		if(isAdmin(userID)) return;
		boolean isCustomizePortfolio = hasUserPortfolioCustomize(userID, portfolio);
		if(hasUserPortfolioFollow(userID, portfolio)){//避免程序不小心多次删除
			UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
			userManager.deleteUserResource(userResource);
			if(!isCustomizePortfolio)//不是customize portfolio,此时计数减1
				adjustPortfolioFollowNum(userID, -1);
		}
		if(hasUserPortfolioRealTime(userID, portfolio)){//避免程序不小心多次删除
			UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
			userManager.deleteUserResource(userResource);
			adjustPortfolioRealTimeNum(userID, -1);
		}
		if(!isCustomizePortfolio)//如果不是customize portoflio, plan ref 得调整
			afterPlanUnref(userID, planID);
	}
	/**
	 * 设置portfolio 为realtime后的操作
	 * 创建portfolio realtime 资源，并且调整计数
	 * 暂时不用
	 * @param userID
	 * @param portfolioID
	 * @throws Exception
	 */
	public void afterPortfolioRealTime(Long userID, Portfolio portfolio) throws Exception {
		if(isAdmin(userID)) return;
		if(!hasUserPortfolioRealTime(userID, portfolio)){//避免程序不小心多次添加
			userManager.saveUserResource(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
			adjustPortfolioRealTimeNum(userID, 1);
		}
	}
	/**
	 * 取消一个portfolio realtime后的操作
	 * 删除portfolio realtime 资源，并且调整计数
	 * 暂时不用
	 * @param userID
	 * @param portfolioID
	 * @throws Exception
	 */
	public void afterPortfolioDelayTime(Long userID, Portfolio portfolio) throws Exception {
		if(isAdmin(userID)) return;
		if(hasUserPortfolioRealTime(userID, portfolio)){//避免程序不小心多次删除造成计数错误
			UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
			userManager.deleteUserResource(userResource);
			adjustPortfolioRealTimeNum(userID, -1);
		}
	}
	
	/****************************************************end 相应操作完成后修改user resource并修改操作权限的API****************************************************/
	
	/****************************************************begin 检查定性操作权限的API****************************************************/
	/**
	 * 是否有改变MHP(minimum holding periods)功能权限
	 * @param userID
	 * @return
	 */
	public boolean canChangeMinimumHoldingPeriods(Long userID){
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getChangeMHP()) 
			return true;
		return false;
	}
	/**
	 * 是否有创建plan功能权限
	 * @param userID
	 * @return
	 */
	public boolean canCreatePlan(Long userID,boolean consumer) {
		if(consumer&&userID.longValue()!=Configuration.USER_ANONYMOUS){
			return true;
		}
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getCreatePlan())
			return true;
		return false;
	}
	public boolean canCreatePlan(Long userID){
		return this.canCreatePlan(userID, false);
	}
	/**
	 * 是否有portfolio compare功能权限
	 * @param userID
	 * @return
	 */
	public boolean canPortfolioCompare(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getPortfolioCompare())
			return true;
		return false;
	}
	/**
	 * 是否有plan compare功能权限
	 * @param userID
	 * @return
	 */
	public boolean canPlanCompare(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getPlanCompare())
			return true;
		return false;
	}
	/**
	 * 是否有plan rollover功能权限
	 * @param userID
	 * @return
	 */
	public boolean canPlanRollover(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getPlanCompare())
			return true;
		return false;
	}
	/**
	 * 是否有customer report功能权限
	 * @param userID
	 * @return
	 */
	public boolean canCustomerReport(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getCustomerReport())
			return true;
		return false;
	}
	/**
	 * 是否有follow或customize TAA的功能权限
	 * @param userID
	 * @param planID
	 * @return
	 */
	public boolean canFollowTAA(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getFollowTAA())
			return true;
		return false;
	}
	/**
	 * 是否有follow或customize SAA的功能权限
	 * @param groupID
	 * @return
	 */
	public boolean canFollowSAA(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getFollowSAA())
			return true;
		return false;
	}
	/**
	 * 是否有follow或customize ADV的功能权限
	 * @param groupID
	 * @return
	 */
	public boolean canFollowADV(Long userID) {
		GroupPermission groupPermission = getGroupPermissionByUserID(userID);
		if(groupPermission != null && groupPermission.getFollowADV())
			return true;
		return false;
	}
	/****************************************************end 检查定性操作权限的API****************************************************/
	/**
	 * 返回用户当前已经查看的fund table的ID列表
	 */
	public List<Long> getCurPlanFundTableIDList(Long userID){
		return userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
	}
}
