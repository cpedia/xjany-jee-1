package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseRelativeStrength implements Serializable{

	protected java.lang.Long ID;
	
	protected java.lang.Long SecurityID;
	
	protected java.lang.String Symbol;
	
	protected java.lang.Integer SecurityType;
	
	protected java.util.Date Date;
	
	protected java.lang.Double Return_52w;
	
	protected java.lang.Long Rank;

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Long getSecurityID() {
		return SecurityID;
	}

	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}

	public java.lang.String getSymbol() {
		return Symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	public java.lang.Integer getSecurityType() {
		return SecurityType;
	}

	public void setSecurityType(java.lang.Integer securityType) {
		SecurityType = securityType;
	}

	public java.util.Date getDate() {
		return Date;
	}

	public void setDate(java.util.Date date) {
		Date = date;
	}

	public java.lang.Double getReturn_52w() {
		return Return_52w;
	}

	public void setReturn_52w(java.lang.Double return_52w) {
		Return_52w = return_52w;
	}

	public java.lang.Long getRank() {
		return Rank;
	}

	public void setRank(java.lang.Long rank) {
		Rank = rank;
	}
	
	
}
