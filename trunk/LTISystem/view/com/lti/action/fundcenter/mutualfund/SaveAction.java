package com.lti.action.fundcenter.mutualfund;

import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserManager userManager;
	
	private SecurityManager securityManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return Action.SUCCESS;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}
	
	

}
