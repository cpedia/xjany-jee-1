package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseCachePortfolioItem implements Serializable {

	protected Long GroupID;
	protected Long RoleID;
	protected Long PortfolioID;
	protected Double AR1;
	protected Double AR3;
	protected Double AR5;
	protected Double SharpeRatio1;
	protected Double SharpeRatio3;
	protected Double SharpeRatio5;
	protected Long UserID;
	protected Date EndDate;
	protected Date LastTransactionDate;
	protected Long MainStrategyID;
	protected Long Type;
	protected String PortfolioName;
	protected String UserName;
	protected Integer state;
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getGroupID() {
		return GroupID;
	}
	public void setGroupID(Long groupID) {
		GroupID = groupID;
	}
	public Long getRoleID() {
		return RoleID;
	}
	public void setRoleID(Long roleID) {
		RoleID = roleID;
	}
	public Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		PortfolioID = portfolioID;
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
	public Long getUserID() {
		return UserID;
	}
	public void setUserID(Long userID) {
		UserID = userID;
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
	public String getPortfolioName() {
		return PortfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		PortfolioName = portfolioName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((GroupID == null) ? 0 : GroupID.hashCode());
		result = prime * result + ((PortfolioID == null) ? 0 : PortfolioID.hashCode());
		result = prime * result + ((RoleID == null) ? 0 : RoleID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BaseCachePortfolioItem other = (BaseCachePortfolioItem) obj;
		if (GroupID == null) {
			if (other.GroupID != null)
				return false;
		} else if (!GroupID.equals(other.GroupID))
			return false;
		if (PortfolioID == null) {
			if (other.PortfolioID != null)
				return false;
		} else if (!PortfolioID.equals(other.PortfolioID))
			return false;
		if (RoleID == null) {
			if (other.RoleID != null)
				return false;
		} else if (!RoleID.equals(other.RoleID))
			return false;
		return true;
	}
	public Long getType() {
		return Type;
	}
	public void setType(Long type) {
		Type = type;
	}

}