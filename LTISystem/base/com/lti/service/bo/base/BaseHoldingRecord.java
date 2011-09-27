/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CCD
 *
 */
public class BaseHoldingRecord implements Serializable{

	private static final long serialVersionUID = 1L;

	protected Long ID;
	
	protected Long PortfolioID;
	
	protected Long SecurityID;
	
	protected Date StartDate;
	
	protected Date EndDate;
	
	protected String DividendDateStr;

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public Long getPortfolioID() {
		return PortfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		PortfolioID = portfolioID;
	}

	public Long getSecurityID() {
		return SecurityID;
	}

	public void setSecurityID(Long securityID) {
		SecurityID = securityID;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public String getDividendDateStr() {
		return DividendDateStr;
	}

	public void setDividendDateStr(String dividendDateStr) {
		DividendDateStr = dividendDateStr;
	}
}
