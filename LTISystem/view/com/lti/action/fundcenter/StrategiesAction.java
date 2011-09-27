package com.lti.action.fundcenter;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.bo.Strategy;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.PortfolioMPT;
import com.opensymphony.xwork2.ActionSupport;

public class StrategiesAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.String symbol;
	
	private List<Strategy> strategies;
	
	private java.lang.String sort;
	
	private java.lang.Integer chosenYear;
	
	private SecurityManager securityManager;
	
	private UserManager userManager;
	
	private java.lang.Integer size;

	private int sortBy;


	@Override
	public void validate() {
		// TODO Auto-generated method stub
		if(symbol == null || symbol.equals(""))
			return;
		if(size == null)
			size = 5;
		super.validate();
	}
	
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		if(chosenYear == null)
			strategies = securityManager.getQuotedStrategies(symbol,userManager.getLoginUser().getID(), size);
		else
		{
			if(sort == null){
				sortBy = PortfolioMPT.SORT_BY_SHARPERATIO;
			}
			else
				sortBy = PortfolioMPT.getSortNum(sort);
			strategies = securityManager.getQuotedStrategiesByOrder(symbol,userManager.getLoginUser().getID(), size, sortBy, chosenYear);
				
		}
		return Action.SUCCESS;
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


	public List<Strategy> getStrategies() {
		return strategies;
	}


	public void setStrategies(List<Strategy> strategies) {
		this.strategies = strategies;
	}


	public java.lang.String getSort() {
		return sort;
	}


	public void setSort(java.lang.String sort) {
		this.sort = sort;
	}

	public java.lang.Integer getChosenYear() {
		return chosenYear;
	}


	public void setChosenYear(java.lang.Integer chosenYear) {
		this.chosenYear = chosenYear;
	}


	public java.lang.Integer getSize() {
		return size;
	}


	public void setSize(java.lang.Integer size) {
		this.size = size;
	}


	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	

}
