package com.lti.action.fundcenter.mutualfund;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.UserManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class DetailsAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserManager userManager;
	
	private SecurityManager securityManager;
	
	private AssetClassManager assetClassManager;
	
	private String symbol;
	
	private Security security;

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		if(symbol == null || symbol.equals(""))
			addActionError("No Symbol!");
		super.validate();
		
	}
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		security = securityManager.getBySymbol(symbol);
		String securityTypeName;
		if(security.getSecurityType() != null){
			securityTypeName = Configuration.getSecurityTypeName(security.getSecurityType());
		}
		else
		{
			securityTypeName = "UNKNOWN";
		}
		security.setSecurityTypeName(securityTypeName);
		String className; 
		if(security.getClassID() != null){
			className = assetClassManager.get(security.getClassID()).getName();
		}
		else
		{
			className = "UNKNOWN";
		}
		security.setClassName(className);
		return Action.SUCCESS;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}
	
	

}
