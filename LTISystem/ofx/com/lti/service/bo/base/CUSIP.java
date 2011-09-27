package com.lti.service.bo.base;

public class CUSIP {
	private long ID;
	private String CUSIP;
	private String Type;
	private String Symbol;
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getCUSIP() {
		return CUSIP;
	}
	public void setCUSIP(String cusip) {
		CUSIP = cusip;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getSymbol() {
		return Symbol;
	}
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

}
