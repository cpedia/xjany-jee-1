package com.lti.action.admin.validate;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.util.validate.PortfolioValidator;
import com.opensymphony.xwork2.ActionSupport;

public class CheckPortfolioAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Long portfolioID;
	
	
	public void validate() {

		if (this.portfolioID == null || this.portfolioID.equals("")) {

			addFieldError("portfolioID", "portfolioID is not validate!");

			return;
		}

	}
	
	@Override
	public String execute() throws Exception {
		PortfolioValidator pv = new PortfolioValidator(this.portfolioID);
		List<PortfolioMPT> pMpt = pv.getMptsFromFile(this.portfolioID + "PortfolioMpt.csv");
		pv.setPortfolioMPTs(pMpt);
		List<PortfolioDailyData> pdd = pv.getDailyDataFromFile(this.portfolioID + "PortfolioDailydata.csv");
		pv.setPortfolioDailyDatas(pdd);
		pv.check();
		return  Action.SUCCESS;
	}
	
	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

}
