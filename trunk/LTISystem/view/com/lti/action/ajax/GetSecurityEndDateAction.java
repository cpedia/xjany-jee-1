package com.lti.action.ajax;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

public class GetSecurityEndDateAction extends ActionSupport implements Action {
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(symbol!=null){
				String endDate;
				endDate = df.format(securityManager.get(symbol).getEndDate());
				resultString = endDate;
			}
			else
				resultString = "UNKNOWN";
		} catch (Exception e) {
			resultString = "UNKNOWN";
		}
		return Action.SUCCESS;
	}

}
