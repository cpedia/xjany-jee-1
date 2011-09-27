package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseTradingDate implements Serializable{


	// primary key
	protected java.lang.Long ID;

	// fields
	protected java.util.Date TradingDate;
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.util.Date getTradingDate() {
		return TradingDate;
	}
	public void setTradingDate(java.util.Date tradingDate) {
		TradingDate = tradingDate;
	}
	


}