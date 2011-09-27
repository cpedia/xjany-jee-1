package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseHoliday implements Serializable{

	protected java.lang.Long ID;

	protected java.util.Date Date;
	
	protected java.lang.String Description;
	
	protected java.lang.String Symbol;
	
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
	}
	public java.lang.String getDescription() {
		return Description;
	}
	public void setDescription(java.lang.String description) {
		Description = description;
	}
	public java.lang.String getSymbol() {
		return Symbol;
	}
	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}


}