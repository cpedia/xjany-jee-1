/**
 * 
 */
package com.lti.permission;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioFollowDate;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author CCD
 *
 */
public class SetupUserPermission {
	MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
	UserManager userManager;
	PortfolioManager portfolioManager;
	StrategyManager strategyManager;
	public SetupUserPermission(){
		userManager = ContextHolder.getUserManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		strategyManager = ContextHolder.getStrategyManager();
	}
	//pro, expert, basic, register, anonymous
	//1、根据用户的最高组别为每个用户生成一条user permission记录
	public void setupInitialUserPermissionForAllUser() {
		mpm.initialAllUserPermission();
	}
	//2、数据转换, 避免重复
	//2.1将每个用户创建的plan添加plan create资源记录，调整计数
	public void changeUserCreatePlan(List<Strategy> strategyList) throws Exception {
		for(Strategy strategy : strategyList){
			try{
				Long userID = strategy.getUserID();
				if(!mpm.isAdmin(userID) && strategy.is401K()){//如果不是admin或并且是401k的才考虑
					if(!mpm.hasUserPlanCreate(userID, strategy.getID())){//不重复创建
						userManager.saveUserResource(userID, strategy.getID(), Configuration.USER_RESOURCE_PLAN_CREATE);
						mpm.adjustPlanCreateNum(userID, 1);
					}
				}
			}catch(Exception e){
				System.out.println("Plan,userID,planID," + strategy.getID());
			}
		}
	}
	//2.2将每个用户创建的portfolio添加customize、 follow、 realtime, plan ref 资源记录，调整记数
	public void changeUserCustomizePortfolio(List<User> userList) throws Exception {
		for(User user : userList){
			Long userID = user.getID();
			if(!mpm.isAdmin(userID)){
				List<Portfolio> userPortfolioList = portfolioManager.getPortfolioListByUserID(userID);
				if(userPortfolioList != null){
					int adjustNum = 0;
					for(Portfolio portfolio : userPortfolioList){
						try{
							Long portfolioID = portfolio.getID();
							Long planID = null;
							String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
							if(planIDStr != null && !planIDStr.equals(""))
								planID = Long.valueOf(planIDStr);
							userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
							userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
							userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
							mpm.afterPlanRef(userID, planID);
							++adjustNum;
						}catch(Exception e){
							System.out.println("Customize,UserID,PortfolioID," + userID + "," + portfolio.getID());
						}
					}
					mpm.adjustPortfolioFollowNum(userID, adjustNum);
					mpm.adjustPortfolioRealTimeNum(userID, adjustNum);
				}
			}
		}
	}
	//2.3将每个用户设置email alert的portfolio添加follow、 realtime, plan ref 资源记录，调整记数
	public void changeUserFollowPortfolio(List<User> userList) throws Exception {
		for(User user: userList){
			Long userID = user.getID();
			if(!mpm.isAdmin(userID)){
				List<Long> emailPortfolioIDList = userManager.getEmailNotificationsByUserID(userID);
				if(emailPortfolioIDList != null){
					int adjustNum = 0;
					for(Long portfolioID : emailPortfolioIDList){
						try{
							if(!mpm.hasUserPortfolioFollow(userID, portfolioID)){
								Portfolio portfolio = portfolioManager.get(portfolioID);
								Long planID = null;
								String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
								if(planIDStr != null && !planIDStr.equals(""))
									planID = Long.valueOf(planIDStr);
								userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
								userManager.saveUserResource(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_REALTIME);
								mpm.afterPlanRef(userID, planID);
								++adjustNum;
							}
						}catch(Exception e){
							System.out.println("Follow,userID,PortfolioID," + userID + "," + portfolioID);
						}
					}
					mpm.adjustPortfolioFollowNum(userID, adjustNum);
					mpm.adjustPortfolioRealTimeNum(userID, adjustNum);
				}
			}
		}
	}
	//2.4将原有的fundtable资源转化为新资源
	public void changeUserPlanFundTable(List<User> userList) {
		for(User user : userList){
			Long userID = user.getID();
			try{
				if(!mpm.isAdmin(userID)){
					List<Long> planIDList = userManager.findUserPlanID(userID);
					if(planIDList != null){
						for(Long planID : planIDList){
							userManager.saveUserResource(userID, planID, Configuration.USER_RESOURCE_PLAN_CREATE);
						}
						mpm.adjustPlanFundTableNum(userID, planIDList.size());
					}
				}
			}catch(Exception e){
				System.out.println("FundTable,userID," + userID);
			}
			
		}
	}
	
	public void changeStartToFollowDate(List<PortfolioFollowDate> portfolioFollowDateList) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(portfolioFollowDateList != null){
			for(PortfolioFollowDate portfolioFollowDate : portfolioFollowDateList){
				Long portfolioID = portfolioFollowDate.getPortfolioID();
				String datesStr = portfolioFollowDate.getDateString();
				String[] dates = datesStr.split("#");
				try {
					Date lastFollowDate = df.parse(dates[dates.length - 1]);
					UserResource userResource = userManager.getCustomizePortfolioResourceByPortfolioID(portfolioID);
					if(userResource != null) {//是这个用户创建的
						userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userResource.getUserID(), portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
						if(userResource != null){
							userResource.setUpdateTime(lastFollowDate);
							userManager.updateUserResourse(userResource);
						}
					}
				} catch (ParseException e) {
					System.out.println("Follow, Date," + portfolioFollowDate.getPortfolioID());
				}
			}
		}
	}
	
	public void setup() throws Exception{
		setupInitialUserPermissionForAllUser();
		List<Strategy> strategyList = strategyManager.getStrategies();
		List<User> userList = userManager.getUsers();
		this.changeUserCreatePlan(strategyList);
		this.changeUserCustomizePortfolio(userList);
		this.changeUserFollowPortfolio(userList);
		this.changeUserPlanFundTable(userList);
		this.setupFollowDate();
	}
	
	public void setupFollowDate() {
		List<PortfolioFollowDate> portfolioFollowDateList = portfolioManager.getPortfolioFollowDates();
		changeStartToFollowDate(portfolioFollowDateList);
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		portfolioManager.changeUserResourceWhenCanclePaypal(1305L, new Date());
	}

}
