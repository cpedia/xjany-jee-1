package com.lti.service.bo.base;

public class BaseEmailNotification {
	
	private java.lang.Long ID;
	private java.lang.Long PortfolioID;
	private java.lang.Long UserID;
	private java.lang.Integer Span;
	private java.util.Date LastSentDate;
	
	
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
	public java.lang.Long getUserID() {
		return UserID;
	}
	public void setUserID(java.lang.Long userID) {
		UserID = userID;
	}
	
	public java.lang.Integer getSpan() {
		return Span;
	}
	public void setSpan(java.lang.Integer span) {
		Span = span;
	}
	public java.util.Date getLastSentDate() {
		return LastSentDate;
	}
	public void setLastSentDate(java.util.Date lastSentDate) {
		LastSentDate = lastSentDate;
	}
	
	

}
