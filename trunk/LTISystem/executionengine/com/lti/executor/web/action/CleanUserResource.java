/**
 * 
 */
package com.lti.executor.web.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lti.executor.web.BasePage;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.service.bo.UserPermission;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author CCD
 *
 */
public class CleanUserResource extends BasePage{

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String checkOneUser(User user) {
		String result = user.getID().toString();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		UserManager userManager = ContextHolder.getUserManager();
		Long userID = user.getID();
		//customize portfolio
		List<Portfolio> portfolioList = portfolioManager.getPortfolioListByUserID(user.getID());
		int realTimeCount = 0;
		Set<Long> customizeIDSet = new HashSet<Long>();
		if(portfolioList != null){
			realTimeCount += portfolioList.size();
			for(Portfolio portfolio: portfolioList)
				customizeIDSet.add(portfolio.getID());
		}
		//follow portfolio
		List<Long> emailList = userManager.getEmailNotificationsByUserID(userID);
		for(Long portfolioID: emailList){
			if(!customizeIDSet.contains(portfolioID))
				++realTimeCount;
		}
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		if(userPermission.getCurPortfolioFollowNum() != realTimeCount)
			result += ",fail";
		else
			result += ",success";
		result += "," + userPermission.getCurPortfolioFollowNum() + "," + realTimeCount;
		return result;
	}
	
	public String checkAll(){
		StringBuffer sb = new StringBuffer();
		UserManager userManager = ContextHolder.getUserManager();
		List<User> userList = userManager.getUsers();
		for(User user: userList){
			try{
				if(user.getID() == Configuration.SUPER_USER_ID)
					continue;
				sb.append(checkOneUser(user));
			}catch(Exception e){
				sb.append("exception," + user.getID());
			}
			sb.append("<br>\r\n");
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
