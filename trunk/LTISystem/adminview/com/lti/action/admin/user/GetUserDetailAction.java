package com.lti.action.admin.user;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionSupport;

public class GetUserDetailAction extends ActionSupport implements Action{
	
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(GetUserDetailAction.class);
	
	private String userName;
	private UserManager userManager;
	private User user;
	private String action;
	private String Email;
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String execute(){
		if(action.equals("Name")){
			user = userManager.get(userName);
			return Action.SUCCESS;
		}
		else if(action.equals("Email")){
			user = userManager.getUserByEmail(Email);
			return Action.SUCCESS;
		}
		else
			return Action.ERROR;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
}
