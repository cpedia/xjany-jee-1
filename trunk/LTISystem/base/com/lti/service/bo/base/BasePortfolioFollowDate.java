/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public class BasePortfolioFollowDate implements Serializable{
	
	protected java.lang.Long ID;
	protected java.lang.Long PortfolioID;
	protected java.lang.String DateString;
	
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
	public java.lang.String getDateString() {
		return DateString;
	}
	public void setDateString(java.lang.String dateString) {
		DateString = dateString;
	}
	
}
