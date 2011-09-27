/**
 * 
 */
package com.lti.permission;

import java.util.Random;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;

/**
 * @author Administrator
 *
 */
public class TestMYPLANIQPermissionManager {
	private MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
	private PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
	private Long userID = 0L;
	private String getUserNameByID(Long userID){
		if(userID == 1650)
			return "Register";
		if(userID == 1651)
			return "Basic";
		if(userID == 1652)
			return "Expert";
		if(userID == 1653)
			return "Pro";
		return "Anonymous";
	}
	
	public TestMYPLANIQPermissionManager(Long userID) {
		this.userID = userID;
	}
	
	public void testCreatePlan() throws Exception{
		System.out.println(getUserNameByID(userID) + " want to create a plan");
		Random random = new Random();
		Long planID = random.nextLong();
		int operationCode = mpm.canPlanCreate(userID);
		if(operationCode ==  mpm.PERMISSION_LIMIT){
			System.out.println("you don't have the priviliage to create plan");
			return;
		}
		if(operationCode == mpm.COUNT_LIMIT){
			System.out.println("you can't create a new plan until you delete an old one");
			return;
		}
		System.out.println("create a plan with ID :" + planID);
		mpm.afterPlanCreate(userID, planID,false);
		System.out.println("create plan success, you can create " + mpm.getAllowPortfolioFollowNum(userID) + " more plan(s)");
	}
	/**
	 * 这里的portfolioID必须是已有的
	 * @param userID
	 * @param portfolioID
	 * @throws Exception 
	 */
	public void testCustomizePortfolio(Long portfolioID, Long planID) throws Exception{
		System.out.println(getUserNameByID(userID) + " want to customize a portfolio with ID :" + portfolioID);
		Random random = new Random();
		Long newPortfolioID = random.nextLong();
		int operationCode = mpm.canPortfolioCustomize(userID, portfolioID, planID);
		if(operationCode ==  mpm.PERMISSION_LIMIT){
			System.out.println("you don't have the priviliage to customize this portfolio");
			return;
		}
		if(operationCode == mpm.COUNT_LIMIT){
			System.out.println("you can't customize a new portfolio until you delete an old one");
			return;
		}
		if(operationCode == mpm.PLAN_REF_COUNT_LIMIT){
			System.out.println("you can't reference a new plan");
			return;
		}
		System.out.println("create a new portfolio with ID :" + newPortfolioID);
		mpm.afterPortfolioCustomize(userID, newPortfolioID, planID);
		System.out.println("you can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)");
	}
	
	public void testFollowPortfolio(Long portfolioID, Long planID) throws Exception {
		Portfolio portfolio = portfolioManager.get(portfolioID);
		System.out.println(getUserNameByID(userID) + " want to follow a portfolio with ID :" + portfolioID);
		int operationCode = mpm.canPortfolioFollow(userID, portfolio, planID);
		if(operationCode ==  mpm.PERMISSION_LIMIT){
			System.out.println("you don't have the priviliage to follow this portfolio");
			return;
		}
		if(operationCode == mpm.COUNT_LIMIT){
			System.out.println("you can't follow a new portfolio until you delete an old one");
			return;
		}
		if(operationCode == mpm.PLAN_REF_COUNT_LIMIT){
			System.out.println("you can't reference a new plan");
			return;
		}
		System.out.println("follow a portfolio with ID :" + portfolioID);
		mpm.afterPortfolioFollow(userID, portfolio, planID);
		System.out.println("you can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)");
	}
	
	public void testUnfollowPortfolio(Long portfolioID, Long planID) throws Exception {
		System.out.println(getUserNameByID(userID) + " want to unfollow a portfolio with ID :" + portfolioID);
		System.out.println("unfollow a portfolio with ID :" + portfolioID);
		mpm.afterPortfolioUnfollowAndDelete(userID, portfolioID, planID);
		System.out.println("you can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)");
	}
	
	public void testViewFundTable(Long planID) throws Exception {
		System.out.println(getUserNameByID(userID) + " want to view a fundtable with planID :" + planID);
		int operationCode = mpm.canPlanFundTable(userID, planID);
		if(operationCode == mpm.COUNT_LIMIT){
			System.out.println("you can't view a new fundtable more");
			return;
		}
		System.out.println("view a fundtable with planID :" + planID);
		mpm.afterPlanFundTable(userID, planID);
		System.out.println("you can view " + mpm.getAllowPlanFundTableNum(userID) + " more fundtable(s)");
	}
	
	public void testDeletePortfolio(Long portfolioID, Long planID) throws Exception {
		System.out.println(getUserNameByID(userID) + " want to delete a portfolio with ID :" + portfolioID);
		System.out.println("delete a portfolio with ID :" + portfolioID);
		mpm.afterPortfolioDelete(userID, portfolioID, planID);
		System.out.println("you can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)");
	}
	
	public boolean testChangeMHP(Long userID){
		return mpm.canChangeMinimumHoldingPeriods(userID);
	}
	
	public boolean testCreatePlan(Long userID){
		return mpm.canChangeMinimumHoldingPeriods(userID);
	}
	
	public boolean testFollowTAA(Long userID){
		return mpm.canFollowTAA(userID);
	}
	
	public boolean testFollowSAA(Long userID){
		return mpm.canFollowSAA(userID);
	}
	
	public boolean testFollowADV(Long userID){
		return mpm.canFollowADV(userID);
	}
	
	public boolean testPortfolioCompare(Long userID){
		return mpm.canPortfolioCompare(userID);
	}
	
	public boolean testPlanCompare(Long userID){
		return mpm.canPlanCompare(userID);
	}
	
	public boolean testPlanRollover(Long userID){
		return mpm.canPlanRollover(userID);
	}
	
	public boolean testCustomerReport(Long userID){
		return mpm.canCustomerReport(userID);
	}
	
	public void testPermission(){
		System.out.println(getUserNameByID(userID));
		System.out.println("Follow SAA : " + testFollowSAA(userID));
		System.out.println("Follow TAA : " + testFollowTAA(userID));
		System.out.println("Follow ADV : " + testFollowADV(userID));
		System.out.println("Change MHP : " + testChangeMHP(userID));
		System.out.println("Create Plan : " + testCreatePlan(userID));
		System.out.println("Portfolio Compare : " + testPortfolioCompare(userID));
		System.out.println("Plan Compare : " + testPlanCompare(userID));
		System.out.println("Plan Rollover : " + testPlanRollover(userID));
		System.out.println("Customer Report : " + testCustomerReport(userID));
		System.out.println();
	}
	/**
	 * 各级别用户权限测试并输出
	 */
	public void testAllLevelPermission(){
		userID = 0L;//anonymous
		testPermission();
		userID = 1650L;//register
		testPermission();
		userID = 1651L;//basic
		testPermission();
		userID = 1652L;//expert
		testPermission();
		userID = 1653L;//pro
		testPermission();
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TestMYPLANIQPermissionManager tm = new TestMYPLANIQPermissionManager(1652L);
		tm.testCustomizePortfolio(12464L, 725L);
//		tm.testFollowPortfolio(21186L, 674L);
//		tm.testUnfollowPortfolio(17301L, 1148L);
//		tm.testCustomizePortfolio(12494L, 727L);
//		tm.testCustomizePortfolio(15160L, 679L);
//		tm.testCustomizePortfolio(15166L, 679L);
//		tm.testFollowPortfolio(17301L, 1148L);
		//tm.testCreatePlan();
		//customize TAA
		//tm.testCustomizePortfolio(17295L, 1148L);
		//customize SAA
		//tm.testFollowPortfolio(17301L, 1148L);
		//tm.testFollowPortfolio(12494L, 727L);
		//tm.testCustomizePortfolio(15160L, 679L);
		//tm.testFollowPortfolio(15166L, 679L);
		//tm.testCustomizePortfolio(15166L, 679L);
		//tm.testDeletePortfolio(12494L, 679L);
		//tm.testDeletePortfolio(1158490006685838588L, 1148L);
		
		
	}

}
