package com.lti.action.security;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.opensymphony.xwork2.ActionSupport;

public class QuoteAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.String symbol;
	
	private java.lang.Integer type;
	
	private UserManager userManager;
	
	private SecurityManager securityManager;
	

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		if(symbol == null || symbol.equals("")){
			return Action.INPUT;
		}
		symbol = symbol.toUpperCase();
		Security security = securityManager.getBySymbol(symbol);
		if(security == null)
			return Action.INPUT;
		else{
			type = security.getSecurityType();
			return Action.SUCCESS;
		}
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public java.lang.Integer getType() {
		return type;
	}

	public void setType(java.lang.Integer type) {
		this.type = type;
	}

}
