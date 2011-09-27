package com.lti.action.register;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.UserMarket;
import com.opensymphony.xwork2.ActionSupport;

public class FreeNewsletterAction extends ActionSupport implements Action{
	
	private String email;
	private String action;
	private UserManager userManager;
	
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

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public String execute() throws Exception {
		if(action.equals("link")){
			return "link";
		}
		else if(action.equals("send")){
			UserMarket usermarket = new UserMarket();
			usermarket.setUserEmail(email);
			usermarket.setGroupKey2("SignUp");
			usermarket.setIsSend(true);
			userManager.saveMarketEmail(usermarket);
			return Action.SUCCESS;
		}
		else
			return Action.ERROR;
	}
}
