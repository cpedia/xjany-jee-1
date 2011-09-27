package com.lti.action.commentary;


import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.User;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private UserManager userManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;
	
	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public String execute() throws Exception{
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null){
			userID = 0l;
		}
		else
			userID = user.getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.COMMENTARY_MAIN);
		CustomizeUtil.setRegion(customizeRegion, userID);
		return Action.SUCCESS;
	}
}
