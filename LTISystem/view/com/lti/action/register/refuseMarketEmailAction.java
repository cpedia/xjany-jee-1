package com.lti.action.register;

import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.UserMarket;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class refuseMarketEmailAction extends ActionSupport implements Action{
	
	private String userEmail;
	private UserManager userManager;
	private String resultString;
	private String action;
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String execute() throws Exception {
		if(action.equalsIgnoreCase("show")){
			email=userEmail;
			return "out";
		}
		if(action.equalsIgnoreCase("do")){
			Date logdate = new Date();
			boolean hasME = userManager.hasMarketEmail(userEmail);
			if(!hasME){
				resultString = "This email does not exist!";
				return Action.SUCCESS;
			}else{
				List<UserMarket> list = userManager.getMarketEmailbyProperty("userEmail", userEmail, true);
				//List<UserMarket> umlist = userManager.getMarketEmailbyProperty("isSend", true, true);
				UserMarket user = new UserMarket();
				if(list!=null && list.size()>0)
					user = list.get(0);
				if(user.getIsSend()){
					user.setUserEmail(userEmail);
					user.setIsSend(false);
					userManager.saveMarketEmail(user);
					resultString = "Your email has been removed from our email list.";
					Configuration.writeMarketEmailLog(logdate,"Email address of Market User[" + userEmail + "] choosed to stop receiving the future emails.");
					return Action.SUCCESS;
				}else{
					resultString = "Your email has been removed from our email list.";
					return Action.SUCCESS;
				}
			}
		}
		else{
			resultString = "Sorry, there are some errors in our system, if you have any problems please contact us with email <a href='mailto:support@<%=domainname%>'>support@<%=domainname%></a>.";
			return "result";
		}
	}

}
