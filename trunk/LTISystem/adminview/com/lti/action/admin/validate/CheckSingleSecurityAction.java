package com.lti.action.admin.validate;

import com.lti.action.Action;
import com.lti.util.validate.TotalReturnValidator;
import com.opensymphony.xwork2.ActionSupport;

public class CheckSingleSecurityAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	private java.lang.Long portfolioID;
	private java.lang.Double difference;
	private String symbol;

	public void validate() {

		if (this.portfolioID == null || this.portfolioID.equals("")) {

			addFieldError("portfolioID", "portfolioID is not validate!");

			return;
		}

		if (this.difference == null || this.difference.equals("")) {

			addFieldError("difference", "difference is not validate!");

			return;
		}

		if (this.symbol == null || this.symbol.equals("")) {

			addFieldError("symbol", "symbol is not validate!");

			return;
		}

	}

	public String execute() throws Exception {

		TotalReturnValidator tv = new TotalReturnValidator();
		tv.checkSingleSecurityMpt(this.portfolioID, this.symbol, this.difference);
		return Action.SUCCESS;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
