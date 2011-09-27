package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BasePortfolioState;

public class PortfolioState extends BasePortfolioState implements Serializable {

	private static final long serialVersionUID = 1L;

	private long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public PortfolioState() {
	}

	public PortfolioState(Long portfolioID, Integer state) {
		super();
		PortfolioID = portfolioID;
		State = state;
	}

}