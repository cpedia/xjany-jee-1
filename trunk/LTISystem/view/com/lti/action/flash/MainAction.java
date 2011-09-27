package com.lti.action.flash;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.User;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(MainAction.class);
	
	private CustomizeRegionManager customizeRegionManager;
	
	private UserManager userManager;
	
	private CustomizeRegion customizeRegion;
	
	private String portfolioName;
	
	private Long portfolioID;

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setCustomizeRegionManager(CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
