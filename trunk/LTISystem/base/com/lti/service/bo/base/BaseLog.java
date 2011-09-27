package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseLog implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.Long PortfolioID;
	
	protected java.lang.Long StrategyID;
	
	protected java.util.Date LogDate;
	
	protected java.lang.String Message;
	
	protected java.lang.Integer Type;
	
	public java.lang.Long getID() {
		return ID;
	}
	
	public void setID(java.lang.Long id) {
		ID = id;
	}
	
	public java.lang.Long getPortfolioID() {
		return PortfolioID;
	}

	public void setPortfolioID(java.lang.Long portfolioID) {
		PortfolioID = portfolioID;
	}
	
	public java.lang.Long getStrategyID() {
		return StrategyID;
	}
	
	public void setStrategyID(java.lang.Long strategyID) {
		StrategyID = strategyID;
	}
	
	public java.util.Date getLogDate() {
		return LogDate;
	}
	
	public void setLogDate(java.util.Date logDate) {
		LogDate = logDate;
	}
	
	public java.lang.String getMessage() {
		return Message;
	}
	
	public void setMessage(java.lang.String message) {
		Message = message;
	}
	
	public java.lang.Integer getType() {
		return this.Type;
	}
	
	public void setType(java.lang.Integer type) {
		this.Type = type;
	}

}