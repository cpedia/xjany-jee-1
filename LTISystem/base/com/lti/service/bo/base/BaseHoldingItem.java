/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public class BaseHoldingItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private java.lang.Long ID;
	private java.lang.Long portfolioID;
	private java.lang.String assetName;
	private java.lang.Long securityID;
	private java.util.Date date;
	private java.lang.Double share;
	private java.util.Date lastDividendDate;
	private java.lang.Boolean reInvest=false;
	private java.lang.Double price;
	private java.lang.String symbol;
	private java.lang.Double percentage;
	
	public java.lang.Double getPrice() {
		return price;
	}
	public void setPrice(java.lang.Double price) {
		this.price = price;
	}
	public java.lang.Boolean getReInvest() {
		return reInvest;
	}
	public void setReInvest(java.lang.Boolean reInvest) {
		this.reInvest = reInvest;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.Long getPortfolioID() {
		return portfolioID;
	}
	public void setPortfolioID(java.lang.Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	public java.lang.String getAssetName() {
		return assetName;
	}
	public void setAssetName(java.lang.String assetName) {
		this.assetName = assetName;
	}
	public java.lang.Long getSecurityID() {
		return securityID;
	}
	public void setSecurityID(java.lang.Long securityID) {
		this.securityID = securityID;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	public java.lang.Double getShare() {
		return share;
	}
	public void setShare(java.lang.Double share) {
		this.share = share;
	}
	public java.util.Date getLastDividendDate() {
		return lastDividendDate;
	}
	public void setLastDividendDate(java.util.Date lastDividendDate) {
		this.lastDividendDate = lastDividendDate;
	}
	public java.lang.String getSymbol() {
		return symbol;
	}
	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}
	public java.lang.Double getPercentage() {
		return percentage;
	}
	public void setPercentage(java.lang.Double percentage) {
		this.percentage = percentage;
	}
	
	
	
	
}
