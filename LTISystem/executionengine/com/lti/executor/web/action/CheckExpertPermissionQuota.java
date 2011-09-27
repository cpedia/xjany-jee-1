/**
 * 
 */
package com.lti.executor.web.action;

import java.util.Date;
import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

/**
 * @author CCD
 *
 */
public class CheckExpertPermissionQuota extends BasePage{

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	private void setEmailNotification(Long userID, Portfolio portfolio) {
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Long portfolioID = portfolio.getID();
		if(userManager.getEmailNotification(userID, portfolioID)!=null)
			return;
		EmailNotification en = new EmailNotification();
		en.setUserID(userID);
		en.setPortfolioID(portfolioID);
		en.setSpan(0);
		Date today = new Date();
		Date lastSentDate = portfolioManager.getRealTransactionLatestDate(portfolioID, today);
		if (lastSentDate == null) lastSentDate = today;
		en.setLastSentDate(LTIDate.clearHMSM(lastSentDate));
		userManager.addEmailNotification(en);
	}
	
	public String checkOneAdvanceUser(User user) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		String result = user.getID().toString();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		UserManager userManager = ContextHolder.getUserManager();
		Long userID = user.getID();
		boolean adjusted = false;;
		//customize portfolio
		List<UserResource> customizeList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		for(UserResource userResource : customizeList) {
			Long portfolioID = userResource.getResourceId();
			Portfolio portfolio = portfolioManager.get(portfolioID);
			if(portfolio == null) {
				try {
					userManager.deleteEmailNotification(userID, portfolioID);
					mpm.afterPortfolioUnfollowAndDelete(userID, portfolioID, null);
					adjusted = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				if(portfolio.isPlanPortfolio()){
					Long planID = portfolio.getPlanID();
					ProfileManager profileManager = (ProfileManager) ContextHolder.getPortfolioManager();
					StrategyManager strategyManager = ContextHolder.getStrategyManager();
					Profile profile = profileManager.get(portfolioID, userID, planID);
					if(profile == null){
						profile = new Profile();
						profile.setPortfolioID(portfolioID);
						profile.setPlanID(planID);
						profile.setPortfolioName(portfolio.getName());
						Strategy plan = strategyManager.get(planID);
						profile.setPlanName(plan.getName());
						profile.setUserID(userID);
						profile.setUpdateTime(portfolio.getCreatedDate());
						profile.setUserName(user.getUserName());
						Profile _default = profileManager.get(0L, userID, 0L);
						if(_default != null){
							profile.setIsGenerated(_default.getIsGenerated());
							profile.setRiskNumber(_default.getRiskNumber());
						}
						profileManager.save(profile);
					}
				}
				this.setEmailNotification(userID, portfolio);
			}
		}
		//follow portfolio
		List<UserResource> followList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		for(UserResource userResource : followList) {
			Long portfolioID = userResource.getResourceId();
			Portfolio portfolio = portfolioManager.get(portfolioID);
			if(portfolio == null) {
				try {
					userManager.deleteEmailNotification(userID, portfolioID);
					mpm.afterPortfolioUnfollow(userID, portfolio, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				this.setEmailNotification(userID, portfolio);
			}
		}
		result = userID + "";
		if(adjusted)
			result += ",adjust";
		return result;
	}
	
	public String checkAll(){
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		StringBuffer sb = new StringBuffer();
		UserManager userManager = ContextHolder.getUserManager();
		List<User> userList = userManager.getUsers();
		sb.append("start<br>");
		for(User user: userList){
			try{
				if(user.getID() == Configuration.SUPER_USER_ID)
					continue;
				if(mpm.isAdvancedUser(user.getID())){
					sb.append(checkOneAdvanceUser(user));
				}
			}catch(Exception e){
				sb.append("exception," + user.getID());
			}
			sb.append("<br>----\r\n");
		}
		return sb.toString();
	}
	
	public String clearUserList(List<Long> userIDList){
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
	
	@Override
	public String execute() throws Exception {
		info = checkAll();
		return "info.ftl";
	}
	
}
