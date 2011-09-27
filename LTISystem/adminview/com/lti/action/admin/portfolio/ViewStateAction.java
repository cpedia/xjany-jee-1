package com.lti.action.admin.portfolio;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioState;
import com.lti.type.finance.HoldingInf;
import com.opensymphony.xwork2.ActionSupport;

public class ViewStateAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ViewStateAction.class);
	

	private PortfolioManager portfolioManager;

	
	private Long portfolioID;
	

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void validate(){
		
	}

	
	private PortfolioState portfolioState;
	
	private String portfolioName;
	private String portfolioInformation;
	
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getPortfolioInformation() {
		return portfolioInformation;
	}

	public void setPortfolioInformation(String portfolioInformation) {
		this.portfolioInformation = portfolioInformation;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public PortfolioState getPortfolioState() {
		return portfolioState;
	}

	public void setPortfolioState(PortfolioState portfolioState) {
		this.portfolioState = portfolioState;
	}

	@Deprecated
	@Override
	public String execute() throws Exception {
		
//		if(portfolioID!=null){
//			portfolioState=portfolioManager.getPortfolioState(portfolioID);
//			Portfolio p=portfolioManager.get(portfolioID);
//			if(p!=null){
//				portfolioName=p.getName();
//				portfolioInformation=p.getInformation(p.getLastDate());
//			}
//		}
//		
		return Action.INPUT;

	}

	


	

}
