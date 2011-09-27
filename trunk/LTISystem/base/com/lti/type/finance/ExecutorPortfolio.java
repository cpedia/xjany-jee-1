/**
 * 
 */
package com.lti.type.finance;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CCD
 *
 */
public class ExecutorPortfolio implements Serializable{
	
	private static final long serialVersionUID = 956412671320954861L;

	private Long portfolioID;
	
	private String portfolioName;
	
	private boolean forceMonitor;
	
	private Date endDate;
	
	private int priority;

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

	public boolean getForceMonitor() {
		return forceMonitor;
	}

	public void setForceMonitor(boolean forceMonitor) {
		this.forceMonitor = forceMonitor;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
