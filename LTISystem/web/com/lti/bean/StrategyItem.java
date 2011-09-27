package com.lti.bean;

import com.lti.system.Configuration;

public class StrategyItem {

	private Long ID;

	private String showName = "NA";

	private String name = "NA";

	private String userName = "NA";

	private String portfolioName = "NA";

	private String portfolioShortName = "NA";

	private String portfolioUserName = "NA";

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	private Long portfolioID;

	private String lastValidDate = "NA";

	private String lastTransactionDate = "NA";

	private String categoryName;

	private Boolean delayed = false;
	
	private Long userID;

	private java.lang.String AR1 = "NA";
	private java.lang.String AR3 = "NA";
	private java.lang.String AR5 = "NA";

	private java.lang.String beta1 = "NA";
	private java.lang.String beta3 = "NA";
	private java.lang.String beta5 = "NA";

	private java.lang.String sharpeRatio1 = "NA";
	private java.lang.String sharpeRatio3 = "NA";
	private java.lang.String sharpeRatio5 = "NA";

	private java.lang.String portfolioState;

	private java.lang.String strategyClass;
	private java.lang.String styles;
	
	private boolean isPublic;

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public java.lang.String getStrategyClass() {
		return strategyClass;
	}

	public void setStrategyClass(java.lang.String strategyClass) {
		this.strategyClass = strategyClass;
	}

	public java.lang.String getStyles() {
		return styles;
	}

	public void setStyles(java.lang.String styles) {
		this.styles = styles;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public java.lang.String getAR1() {
		return AR1;
	}

	public void setAR1(java.lang.String ar1) {
		AR1 = ar1;
	}

	public java.lang.String getAR3() {
		return AR3;
	}

	public void setAR3(java.lang.String ar3) {
		AR3 = ar3;
	}

	public java.lang.String getAR5() {
		return AR5;
	}

	public void setAR5(java.lang.String ar5) {
		AR5 = ar5;
	}

	public java.lang.String getBeta1() {
		return beta1;
	}

	public void setBeta1(java.lang.String beta1) {
		this.beta1 = beta1;
	}

	public java.lang.String getBeta3() {
		return beta3;
	}

	public void setBeta3(java.lang.String beta3) {
		this.beta3 = beta3;
	}

	public java.lang.String getBeta5() {
		return beta5;
	}

	public void setBeta5(java.lang.String beta5) {
		this.beta5 = beta5;
	}

	public java.lang.String getSharpeRatio1() {
		return sharpeRatio1;
	}

	public void setSharpeRatio1(java.lang.String sharpeRatio1) {
		this.sharpeRatio1 = sharpeRatio1;
	}

	public java.lang.String getSharpeRatio3() {
		return sharpeRatio3;
	}

	public void setSharpeRatio3(java.lang.String sharpeRatio3) {
		this.sharpeRatio3 = sharpeRatio3;
	}

	public java.lang.String getSharpeRatio5() {
		return sharpeRatio5;
	}

	public void setSharpeRatio5(java.lang.String sharpeRatio5) {
		this.sharpeRatio5 = sharpeRatio5;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName() {
		this.showName = this.name;
		this.name = "(" + this.userName + ")" + this.name;
	}

	public String getPortfolioShortName() {
		return portfolioShortName;
	}

	public void setPortfolioShortName(Integer State) {
		this.portfolioShortName = this.portfolioName;
		if (State.equals(Configuration.PORTFOLIO_STATE_ALIVE)) {
			this.portfolioShortName += "  (Live)";
		}
		this.portfolioName = "(" + this.portfolioUserName + ")" + this.portfolioName;
	}

	public String getLastValidDate() {
		return lastValidDate;
	}

	public void setLastValidDate(String lastValidDate) {
		this.lastValidDate = lastValidDate;
	}

	public java.lang.String getPortfolioState() {
		return portfolioState;
	}

	public void setPortfolioState(java.lang.String portfolioState) {
		this.portfolioState = portfolioState;
	}

	public String getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(String lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		else {
			StrategyItem si = (StrategyItem) o;
			if (this.ID == si.getID())
				return true;
		}
		return false;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPortfolioUserName() {
		return portfolioUserName;
	}

	public void setPortfolioUserName(String portfolioUserName) {
		this.portfolioUserName = portfolioUserName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Boolean getDelayed() {
		return delayed;
	}

	public void setDelayed(Boolean delayed) {
		this.delayed = delayed;
	}
}
