package com.lti.action.ajax;


import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

public class GetSecurityNameAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private String resultString;
	
	private String symbol;
	
	private com.lti.service.SecurityManager securityManager;

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setSecurityManager(com.lti.service.SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public String execute(){
		resultString="";
		try {
			if(symbol!=null){
				String name;
				name = securityManager.get(symbol).getName();
				resultString = name;
			}
			else
				resultString = "UNKNOWN";
		} catch (Exception e) {
			resultString = "UNKNOWN";
		}
		return Action.SUCCESS;
	}

}
