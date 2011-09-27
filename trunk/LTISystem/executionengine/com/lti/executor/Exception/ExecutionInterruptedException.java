package com.lti.executor.Exception;

public class ExecutionInterruptedException extends Exception {
	

	public ExecutionInterruptedException(String message, Throwable cause,Long portfolioID,String portfolioName) {
		super(message, cause);
		this.portfolioID=portfolioID;
		this.portfolioName=portfolioName;
	}


	private static final long serialVersionUID = 1L;
	
	private String portfolioName;
	private Long portfolioID;

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public ExecutionInterruptedException(String message,Long portfolioID,String portfolioName){
		super(message);
		this.portfolioID=portfolioID;
		this.portfolioName=portfolioName;
	}
}
