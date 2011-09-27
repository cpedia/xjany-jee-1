package com.lti.action.customizeregion;

import com.lti.action.Action;
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionSupport;

public class CustomizeAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private CustomizeRegion customizeRegion;
	
	private UserManager userManager;
	
	private GroupManager groupManager;
	
	private CustomizeRegionManager customizeRegionManager;
	

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}
	
	public String Home() throws Exception{
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get("Home Page");
		CustomizeUtil.setRegion(customizeRegion, userID);
		return Action.SUCCESS;
	}
	
	public String PortfolioMain() throws Exception{
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get("Portfolio Main Page");
		CustomizeUtil.setRegion(customizeRegion, userID);
		return Action.SUCCESS;
	}
	
	public String StrategyMain() throws Exception{
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get("Strategy Main Page");
		CustomizeUtil.setRegion(customizeRegion, userID);
		return Action.SUCCESS;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}
}
