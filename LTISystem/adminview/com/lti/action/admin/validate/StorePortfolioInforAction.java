package com.lti.action.admin.validate;

import com.lti.action.Action;
import com.lti.util.validate.PortfolioValidator;
import com.opensymphony.xwork2.ActionSupport;

public class StorePortfolioInforAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private Long portfolioID;
	
	public void validate() {

		if (this.portfolioID == null || this.portfolioID.equals("")) {

			addFieldError("portfolioID", "portfolioID is not validate!");

			return;
		}

	}

	public String execute() throws Exception {
		PortfolioValidator pv = new PortfolioValidator(this.portfolioID);
//		pv.backupInformationFromDB();
		pv.writeMptToFile(this.portfolioID);
		pv.writePddToFile(this.portfolioID);
		return Action.SUCCESS;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

}
