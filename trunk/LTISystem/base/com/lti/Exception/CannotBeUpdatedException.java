package com.lti.Exception;

public class CannotBeUpdatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String portfolioName;
	private Long portfolioID;
	private Boolean isFresh=false;

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Boolean getIsFresh() {
		return isFresh;
	}

	public void setIsFresh(Boolean isFresh) {
		this.isFresh = isFresh;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	public CannotBeUpdatedException(String message,Long portfolioID,String portfolioName,Boolean isFresh) {
		super(message);
		this.portfolioID=portfolioID;
		this.portfolioName=portfolioName;
		this.isFresh=isFresh;
	}


}
