package com.lti.bean;

import java.util.Date;

import com.lti.service.bo.Profile;

public class ProfileItem extends Profile{
	protected Double AR1;
	protected Double AR3;
	protected Double AR5;
	protected Double SharpeRatio1;
	protected Double SharpeRatio3;
	protected Double SharpeRatio5;
	protected Date EndDate;
	protected Date LastTransactionDate;
	protected Long MainStrategyID;
	protected Boolean IsModelPortfolio;
	protected String startToFollowDate;
	protected String imitationDate;
	protected String Frequency;
	protected String StrategyName;
	protected Long StrategyID;
	
	protected boolean isPublic;
	
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getFrequency() {
		return Frequency;
	}
	public void setFrequency(String frequency) {
		Frequency = frequency;
	}
	public String getStrategyName() {
		return StrategyName;
	}
	public void setStrategyName(String strategyName) {
		StrategyName = strategyName;
	}
	public Long getStrategyID() {
		return StrategyID;
	}
	public void setStrategyID(Long strategyID) {
		StrategyID = strategyID;
	}
	public String getStartToFollowDate() {
		return startToFollowDate;
	}
	public void setStartToFollowDate(String startToFollowDate) {
		this.startToFollowDate = startToFollowDate;
	}
	public Double getAR1() {
		return AR1;
	}
	public void setAR1(Double ar1) {
		AR1 = ar1;
	}
	public Double getAR3() {
		return AR3;
	}
	public void setAR3(Double ar3) {
		AR3 = ar3;
	}
	public Double getAR5() {
		return AR5;
	}
	public void setAR5(Double ar5) {
		AR5 = ar5;
	}
	public Double getSharpeRatio1() {
		return SharpeRatio1;
	}
	public void setSharpeRatio1(Double sharpeRatio1) {
		SharpeRatio1 = sharpeRatio1;
	}
	public Double getSharpeRatio3() {
		return SharpeRatio3;
	}
	public void setSharpeRatio3(Double sharpeRatio3) {
		SharpeRatio3 = sharpeRatio3;
	}
	public Double getSharpeRatio5() {
		return SharpeRatio5;
	}
	public void setSharpeRatio5(Double sharpeRatio5) {
		SharpeRatio5 = sharpeRatio5;
	}
	public Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	public Date getLastTransactionDate() {
		return LastTransactionDate;
	}
	public void setLastTransactionDate(Date lastTransactionDate) {
		LastTransactionDate = lastTransactionDate;
	}
	public Long getMainStrategyID() {
		return MainStrategyID;
	}
	public void setMainStrategyID(Long mainStrategyID) {
		MainStrategyID = mainStrategyID;
	}
	public Boolean getIsModelPortfolio() {
		return IsModelPortfolio;
	}
	public void setIsModelPortfolio(Boolean isModelPortfolio) {
		IsModelPortfolio = isModelPortfolio;
	}
	public String getImitationDate() {
		return imitationDate;
	}
	public void setImitationDate(String imitationDate) {
		this.imitationDate = imitationDate;
	}
}
