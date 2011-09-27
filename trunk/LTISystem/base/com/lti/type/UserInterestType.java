package com.lti.type;

import java.util.Date;
import java.util.List;

public class UserInterestType {
	Long userID;
	List<Long> portfolioIDs;
	Date lastSentDate;
	
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public List<Long> getPortfolioIDs() {
		return portfolioIDs;
	}
	public void setPortfolioIDs(List<Long> portfolioIDs) {
		this.portfolioIDs = portfolioIDs;
	}
	public Date getLastSentDate() {
		return lastSentDate;
	}
	public void setLastSentDate(Date lastSentDate) {
		this.lastSentDate = lastSentDate;
	}
}
