package com.lti.action.register;

import com.lti.action.Action;
import com.lti.service.ProfileManager;
import com.lti.service.bo.Profile;
import com.lti.service.bo.User;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class UserMyAccount extends ActionSupport implements Action{
	
	private Profile profile;
	private User user;
	private long userID = -1l;
	
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	@Override
	public String execute() throws Exception{
		user=ContextHolder.getUserManager().getLoginUser();
		userID = user.getID();
		profile=((ProfileManager)ContextHolder.getInstance().getApplicationContext().getBean("profileManager")).get(0l, userID, 0l);
		return Action.SUCCESS;
	}
}
