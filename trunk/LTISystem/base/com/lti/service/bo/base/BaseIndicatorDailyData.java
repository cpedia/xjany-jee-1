package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseIndicatorDailyData implements Serializable{

	protected java.lang.Long ID;

	protected java.util.Date Date;
	
	protected java.lang.Double Value;
	
	protected java.lang.Long IndicatorID;
	
	public java.lang.Long getIndicatorID()
	{
		return IndicatorID;
	}
	public void setIndicatorID(java.lang.Long indicatorID)
	{
		IndicatorID = indicatorID;
	}
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
	public java.lang.Double getValue() {
		return Value;
	}
	public void setValue(java.lang.Double value) {
		Value = value;
	}



}