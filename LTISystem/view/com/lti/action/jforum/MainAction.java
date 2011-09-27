package com.lti.action.jforum;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.action.jforum.MainAction;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.User;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
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
	
	public String execute(){

		User user = userManager.getLoginUser();
		Long userID;
		if(user == null){
			userID = 0l;
		}
		else
		{
			userID = user.getID();
		}
		customizeRegion = customizeRegionManager.get(CustomizeUtil.JFORUM_MAIN);
		CustomizeUtil.setRegion(customizeRegion, userID);
		return Action.SUCCESS;
	}
}
