package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseIndicator implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.String Symbol;
	
	protected java.lang.String Description;
	
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.String getSymbol() {
		return Symbol;
	}
	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}
	public java.lang.String getDescription() {
		return Description;
	}
	public void setDescription(java.lang.String description) {
		Description = description;
	}




}