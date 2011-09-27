package com.lti.bean;

public class EmailAlertBean {
	
	private Long portfolioID;
	
	private String portfolioName;
	
	private Boolean choosed;
	
	private String lastTransactionDate;
	
	private String lastSentDate;

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Boolean getChoosed() {
		return choosed;
	}

	public void setChoosed(Boolean choosed) {
		this.choosed = choosed;
	}

	public String getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(String lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public String getLastSentDate() {
		return lastSentDate;
	}

	public void setLastSentDate(String lastSentDate) {
		this.lastSentDate = lastSentDate;
	}
}
