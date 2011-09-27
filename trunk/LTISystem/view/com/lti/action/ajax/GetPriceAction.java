package com.lti.action.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class GetPriceAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private PortfolioManager portfolioManager;
	
	private SecurityManager securityManager;
	
	private String Symbol;
	private String StartingDate;
	
	private String resultString;

	public String getResultString() {
		return resultString;
	}


	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	public String getStartingDate() {
		return StartingDate;
	}


	public void setStartingDate(String startingDate) {
		StartingDate = startingDate;
	}


	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}


	public String getSymbol() {
		return Symbol;
	}

	public void setSymbol(String symbol) {
		Symbol = symbol;
	}
	
	public String execute(){
		resultString="";
		try {
			if(Symbol != null && StartingDate!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date startingdate = sdf.parse(StartingDate);
				Security security  = securityManager.getBySymbol(Symbol);
				Double price = securityManager.getPrice(security.getID(), startingdate);
				resultString = price.toString();
			}
			else
				resultString = "0.0";
		} catch (Exception e) {
			// TODO: handle exception
			resultString = null;
		}
		return Action.SUCCESS;
	}


	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
}
