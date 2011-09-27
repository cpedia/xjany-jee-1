package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Hashtable;


public abstract class BaseSecurity implements Serializable{

	protected java.lang.Long ID;

	// fields
	protected java.lang.Long ClassID;

	protected java.lang.Double CurrentPrice;

	protected java.lang.String Symbol;

	protected boolean Diversified;

	protected java.lang.String Name;

	protected java.lang.Integer SecurityType;
	
	protected java.lang.Integer Manual;
	
	protected java.util.Date StartDate;
	
	protected java.util.Date NAVStartDate;
	
	protected java.util.Date EndDate;
	
	protected java.util.Date NewDividendDate;
	
	protected java.lang.Double Quality;
	
	protected java.lang.Double ARRating;
	
	protected java.lang.Double AlphaRating;
	
	protected java.lang.Double TreynorRating;
	
	protected Boolean isClosed=false;

	public Boolean getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	public java.lang.Double getQuality() {
		return Quality;
	}

	public void setQuality(java.lang.Double quality) {
		this.Quality = quality;
	}

	public java.util.Date getNewDividendDate() {
		return NewDividendDate;
	}

	public void setNewDividendDate(java.util.Date newDividendDate) {
		NewDividendDate = newDividendDate;
	}

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Long getClassID() {
		return ClassID;
	}

	public void setClassID(java.lang.Long classID) {
		ClassID = classID;
	}

	public java.lang.Double getCurrentPrice() {
		return CurrentPrice;
	}

	public void setCurrentPrice(java.lang.Double currentPrice) {
		CurrentPrice = currentPrice;
	}

	public java.lang.String getSymbol() {
		return Symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	public boolean isDiversified() {
		return Diversified;
	}

	public void setDiversified(boolean diversified) {
		Diversified = diversified;
	}

	public java.lang.String getName() {
		return Name;
	}

	public void setName(java.lang.String name) {
		Name = name;
	}

	public java.lang.Integer getSecurityType() {
		return SecurityType;
	}

	public void setSecurityType(java.lang.Integer securityType) {
		SecurityType = securityType;
	}


	/*
	 * manual=0:normal update, if flash, will over right all data
	 * manual=1:all manual, will not update from Yahoo,and not overwrite all data
	 * manual=2:partial, partial update from Yahoo,if flash, do not overwrite
	 */
	public java.lang.Integer getManual() {
		return Manual;
	}

	public void setManual(java.lang.Integer manual) {
		Manual = manual;
	}
	


	public java.util.Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.StartDate = startDate;
	}

	public java.util.Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.EndDate = endDate;
	}

	public java.lang.Double getARRating() {
		return ARRating;
	}

	public void setARRating(java.lang.Double rating) {
		ARRating = rating;
	}

	public java.lang.Double getAlphaRating() {
		return AlphaRating;
	}

	public void setAlphaRating(java.lang.Double alphaRating) {
		AlphaRating = alphaRating;
	}

	public java.lang.Double getTreynorRating() {
		return TreynorRating;
	}

	public void setTreynorRating(java.lang.Double treynorRating) {
		TreynorRating = treynorRating;
	}

	public java.util.Date getNAVStartDate() {
		return NAVStartDate;
	}

	public void setNAVStartDate(java.util.Date startDate) {
		NAVStartDate = startDate;
	}
	
}

