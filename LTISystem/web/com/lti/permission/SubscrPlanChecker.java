package com.lti.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class SubscrPlanChecker {

	/**
	 * check the user's permission to create plan
	 * 
	 * @param userid
	 * @return
	 */
	public boolean hasPlanCreateRole(long userID) {
		return userID!=Configuration.USER_ANONYMOUS.longValue();
	}

	/**
	 * check the number of the user's creating plan,if it's out of limit, return
	 * false.
	 * 
	 * @param userID
	 * @return
	 */
	public boolean checkCreateNumber(long userID) {
		Map<String, Boolean> pnMap = new HashMap<String, Boolean>();
		if (userID == Configuration.SUPER_USER_ID) {
			return true;
		}

		GroupManager gm = ContextHolder.getGroupManager();
		UserManager userManager = ContextHolder.getUserManager();
		Object[] groups = gm.getGroupIDs(userID);
		List list = Arrays.asList(groups);
		List<Object> groupList = new ArrayList<Object>(list);
		boolean planBoolean = false;
		if (groupList.contains(Configuration.GROUP_MPIQ_P_ID)) {
			planBoolean = userManager.canPlanCreate(userID,
					Configuration.PLAN_CREATE10);
		} else if (groupList.contains(Configuration.GROUP_MPIQ_E_ID)) {
			planBoolean = userManager.canPlanCreate(userID,
					Configuration.PLAN_CREATE5);
		} else {
			planBoolean = false;
		}
		return planBoolean;
	}
	
	/**
	 * The max number of the plan which the owner use
	 * @param userID
	 * @return
	 */
	public int getMaxPlanUsedNumber(long userID) {
		GroupManager gm = ContextHolder.getGroupManager();
		Object[] groups = gm.getGroupIDs(userID);
		List list = Arrays.asList(groups);
		List<Object> groupList = new ArrayList<Object>(list);
		int number = 0;
		if(userID == Configuration.SUPER_USER_ID){
			number = 0;
		}
		else if (groupList.contains(Configuration.GROUP_MPIQ_P_ID)) {
			number = Configuration.PLAN_USE20;
		} 
		else if (groupList.contains(Configuration.GROUP_MPIQ_E_ID)) {
			number = Configuration.PLAN_USE10;
		}
		else
			number = 0;
		return number;
	}

	/**
	 * The max number of the plan which the owner create
	 * @param userID
	 * @return
	 */
	public int getMaxPlanCreatedNumber(long userID) {
		GroupManager gm = ContextHolder.getGroupManager();
		Object[] groups = gm.getGroupIDs(userID);
		List list = Arrays.asList(groups);
		List<Object> groupList = new ArrayList<Object>(list);
		int number = 0;
		if(userID == Configuration.SUPER_USER_ID){
			number = 0;
		}
		else if (groupList.contains(Configuration.GROUP_MPIQ_P_ID)) {
			number = Configuration.PLAN_CREATE10;
		} 
		else if (groupList.contains(Configuration.GROUP_MPIQ_E_ID)) {
			number = Configuration.PLAN_CREATE5;
		}
		else{
			number = 0;
		}
		return number;
	}

	/**
	 * The max number of the portfolio which the owner create/use 
	 * @param userID
	 * @return
	 */
	public int getMaxPortfolioNumber(long userID) {
		GroupManager gm = ContextHolder.getGroupManager();
		Object[] groups = gm.getGroupIDs(userID);
		List list = Arrays.asList(groups);
		List<Object> groupList = new ArrayList<Object>(list);
		int number = 0;
		if(userID == Configuration.SUPER_USER_ID){
			number = 0;
		}
		else if (groupList.contains(Configuration.GROUP_MPIQ_P_ID)) {
			number = Configuration.PORTFOLIO_USE20;
		} 
		else if (groupList.contains(Configuration.GROUP_MPIQ_E_ID)) {
			number = Configuration.PORTFOLIO_USE10;
		} 
		else if (groupList.contains(Configuration.GROUP_MPIQ_B_ID) || groupList.contains(Configuration.GROUP_MPIQ_ID)){
			number = Configuration.PORTFOLIO_USE5;
		}
		else
			number = 0;
		return number;
	}

	/**
	 * The number of the portfolio which the owner have created/used currently
	 * @param userID
	 * @return
	 */
	public int getCurrentPortfolioNumber(long userID) {
		UserManager userManager = ContextHolder.getUserManager();
		return userManager.getCurrentPortfolioNumber(userID);
	}

	/**
	 * The plans which is used include the system plans and the owner's plans
	 * @param userID
	 * @return
	 */
	public int getCurrentPlanUsedNumber(long userID) {
		UserManager userManager = ContextHolder.getUserManager();
		return userManager.getCurrentPlanUsedNumber(userID);
	}
	
	/**
	 * The number of the plan which the owner have created currently
	 * @param userID
	 * @return
	 */
	public int getCurrentPlanCreatedNumber(long userID) {
		UserManager userManager = ContextHolder.getUserManager();
		return userManager.getCurrentPlanCreatedNumber(userID);
	}

	/**
	 * finish create a plan, save it into the database.
	 * @param userID
	 * @param planID
	 */
	public void savePlanCreate(long userID, long planID) {
		if (hasPlanCreateRole(userID) && userID != Configuration.SUPER_USER_ID) {
			UserManager userManager = ContextHolder.getUserManager();
			userManager.saveUserResByPlanCreate(userID, planID);
		}
	}

	/**
	 * check the user whether has the role to use another plan or portfolio.
	 * 
	 * @param userID
	 * @param planID
	 * @param portfolioID
	 * @return
	 */
	public Map<String, Boolean> hasPlanOrPortfolioUseRole(long userID,
			long planID) {
		Map<String, Boolean> spMap = new HashMap<String, Boolean>();
		if (userID == Configuration.SUPER_USER_ID) {
			spMap.put(Configuration.ROLE_PLAN_CREATE, true);
			spMap.put(Configuration.ROLE_PLAN_USE, true);
			spMap.put(Configuration.ROLE_PORTFOLIO_USE, true);
			return spMap;
		}
		
		GroupManager gm = ContextHolder.getGroupManager();
		UserManager userManager = ContextHolder.getUserManager();
		
		Object[] groups = gm.getGroupIDs(userID);
		List list = Arrays.asList(groups);
		List<Object> groupList = new ArrayList<Object>(list);
		
		List<Integer> planIDs = userManager.getUserResTypeCounts(userID);
		int sys_plan = planIDs.get(0);
		int cre_plan = planIDs.get(1);
		int total_plan = sys_plan + cre_plan;
		if (groupList.contains(Configuration.GROUP_MPIQ_ID)) {
			if (groupList.contains(Configuration.GROUP_MPIQ_P_ID)) {
				if (total_plan + 1 <= Configuration.PLAN_USE20) {
					if (cre_plan + 1 <= Configuration.PLAN_CREATE10
							&& sys_plan + 1 <= Configuration.PLAN_CREATE10)
						spMap.put(Configuration.ROLE_PLAN_CREATE, true);
					else
						spMap.put(Configuration.ROLE_PLAN_CREATE, false);
				} else
					spMap.put(Configuration.ROLE_PLAN_CREATE, false);
				boolean planuse = userManager.canPlanUse(userID, planID,
						Configuration.PLAN_USE20);
				boolean portfoliouse = userManager.canPortfolioUse(userID,
						Configuration.PORTFOLIO_USE20);
				if (portfoliouse == false)
					spMap.put(Configuration.ROLE_PLAN_USE, false);
				else
					spMap.put(Configuration.ROLE_PLAN_USE, planuse);
				spMap.put(Configuration.ROLE_PORTFOLIO_USE, portfoliouse);
				spMap.put("MPIQ_P", true);
			} else if (groupList.contains(Configuration.GROUP_MPIQ_E_ID)) {
				if (total_plan + 1 <= Configuration.PLAN_USE10) {
					if (cre_plan + 1 <= Configuration.PLAN_CREATE5
							&& sys_plan + 1 <= Configuration.PLAN_CREATE5)
						spMap.put(Configuration.ROLE_PLAN_CREATE, true);
					else
						spMap.put(Configuration.ROLE_PLAN_CREATE, false);
				} else
					spMap.put(Configuration.ROLE_PLAN_CREATE, false);
				boolean planuse = userManager.canPlanUse(userID, planID,
						Configuration.PLAN_USE10);
				boolean portfoliouse = userManager.canPortfolioUse(userID,
						Configuration.PORTFOLIO_USE10);
				if (portfoliouse == false)
					spMap.put(Configuration.ROLE_PLAN_USE, false);
				else
					spMap.put(Configuration.ROLE_PLAN_USE, planuse);
				spMap.put(Configuration.ROLE_PORTFOLIO_USE, portfoliouse);
				spMap.put("MPIQ_E", true);
			} else if (groupList.contains(Configuration.GROUP_MPIQ_B_ID)) {
				boolean planuse = userManager.canPlanUse(userID, planID,
						Configuration.PLAN_USE5);
				boolean portfoliouse = userManager.canPortfolioUse(userID,
						Configuration.PORTFOLIO_USE5);
				spMap.put(Configuration.ROLE_PLAN_CREATE, false);
				if (portfoliouse == false)
					spMap.put(Configuration.ROLE_PLAN_USE, false);
				else
					spMap.put(Configuration.ROLE_PLAN_USE, planuse);
				spMap.put(Configuration.ROLE_PORTFOLIO_USE, portfoliouse);
				spMap.put("MPIQ_B", true);
			} else {
				boolean planuse = userManager.canPlanUse(userID, planID,
						Configuration.PLAN_USE5);
				boolean portfoliouse = userManager.canPortfolioUse(userID,
						Configuration.PORTFOLIO_USE5);
				spMap.put(Configuration.ROLE_PLAN_CREATE, false);
				if (portfoliouse == false)
					spMap.put(Configuration.ROLE_PLAN_USE, false);
				else
					spMap.put(Configuration.ROLE_PLAN_USE, planuse);
				spMap.put(Configuration.ROLE_PORTFOLIO_USE, portfoliouse);
				spMap.put("MPIQ", true);
			}
		} else {
			spMap.put(Configuration.ROLE_PLAN_CREATE, false);
			spMap.put(Configuration.ROLE_PLAN_USE, false);
			spMap.put(Configuration.ROLE_PORTFOLIO_USE, false);
			spMap.put("Unregistered", true);
		}
		return spMap;
	}

	/**
	 * if the permission of ROLE_PLAN_USE and ROLE_PORTFOLIO_USE is broke, return the max number for the checker and the message
	 * @param userID
	 * @param planID
	 * @return
	 */
	public int[] hasRole(long userID, long planID) {
		Map<String, Boolean> spMap = this.hasPlanOrPortfolioUseRole(userID,
				planID);
		int planMaxNumber = 0;
		int portfoliomaxNumber = 0;
		if (!spMap.get(Configuration.ROLE_PLAN_USE)) {
			if (spMap.containsKey("MPIQ_P")) {
				if (spMap.get("MPIQ_P"))
					planMaxNumber = 20;
			} else if (spMap.containsKey("MPIQ_E")) {
				if (spMap.get("MPIQ_E"))
					planMaxNumber = 10;
			} else if (spMap.containsKey("MPIQ_B")) {
				if (spMap.get("MPIQ_B"))
					planMaxNumber = 5;
			} else if (spMap.containsKey("MPIQ")) {
				if (spMap.get("MPIQ"))
					planMaxNumber = 5;
			}
		}
		if (!spMap.get(Configuration.ROLE_PORTFOLIO_USE)) {
			if (spMap.containsKey("MPIQ_P")) {
				if (spMap.get("MPIQ_P"))
					portfoliomaxNumber = 20;
			} else if (spMap.containsKey("MPIQ_E")) {
				if (spMap.get("MPIQ_E"))
					portfoliomaxNumber = 10;
			} else if (spMap.containsKey("MPIQ_B")) {
				if (spMap.get("MPIQ_B"))
					portfoliomaxNumber = 5;
			} else if (spMap.containsKey("MPIQ")) {
				if (spMap.get("MPIQ"))
					portfoliomaxNumber = 5;
			}
		}
		int[] max = { planMaxNumber, portfoliomaxNumber };
		return max;
	}

	/**
	 * after use the plan or portfolio, save it into the database.
	 * 
	 * @param userID
	 * @param planID
	 * @param portfolioID
	 */
	public void savePlanAndPortUse(long userID, long planID, long portfolioID) {
		if (userID == Configuration.SUPER_USER_ID) {
			return;
		} else {
			Map<String, Boolean> spMap = this.hasPlanOrPortfolioUseRole(userID,
					planID);
			UserManager userManager = ContextHolder.getUserManager();
			if (spMap.containsKey("MPIQ_P")) {
				if (spMap.get("MPIQ_P")) {
					if (spMap.get(Configuration.ROLE_PORTFOLIO_USE))
						userManager.saveUserResByPortfolioCounts(userID,
								portfolioID);
					if (spMap.get(Configuration.ROLE_PLAN_USE))
						userManager.saveUserResByStrategyCounts(userID, planID,
								Configuration.PORTFOLIO_USE20);
				}
			} else if (spMap.containsKey("MPIQ_E")) {
				if (spMap.get("MPIQ_E")) {
					if (spMap.get(Configuration.ROLE_PORTFOLIO_USE))
						userManager.saveUserResByPortfolioCounts(userID,
								portfolioID);
					if (spMap.get(Configuration.ROLE_PLAN_USE))
						userManager.saveUserResByStrategyCounts(userID, planID,
								Configuration.PORTFOLIO_USE10);
				}
			} else if (spMap.containsKey("MPIQ_B")) {
				if (spMap.get("MPIQ_B")) {
					if (spMap.get(Configuration.ROLE_PORTFOLIO_USE))
						userManager.saveUserResByPortfolioCounts(userID,
								portfolioID);
					if (spMap.get(Configuration.ROLE_PLAN_USE))
						userManager.saveUserResByStrategyCounts(userID, planID,
								Configuration.PORTFOLIO_USE5);
				}
			} else if (spMap.containsKey("MPIQ")) {
				if (spMap.get("MPIQ")) {
					if (spMap.get(Configuration.ROLE_PORTFOLIO_USE))
						userManager.saveUserResByPortfolioCounts(userID,
								portfolioID);
					if (spMap.get(Configuration.ROLE_PLAN_USE))
						userManager.saveUserResByStrategyCounts(userID, planID,
								Configuration.PORTFOLIO_USE5);
				}
			} else {
				return;
			}
		}
	}
}
