package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseGlobalObject implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String Key;
	protected Long PortfolioID;
	protected Long StrategyID;
	protected byte[] Bytes;
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		PortfolioID = portfolioID;
	}
	public Long getStrategyID() {
		return StrategyID;
	}
	public void setStrategyID(Long strategyID) {
		StrategyID = strategyID;
	}
	public byte[] getBytes() {
		return Bytes;
	}
	public void setBytes(byte[] bytes) {
		Bytes = bytes;
	}
	
	
}
