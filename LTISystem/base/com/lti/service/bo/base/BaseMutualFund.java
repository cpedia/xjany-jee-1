package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseMutualFund implements Serializable{

	protected java.lang.Long ID;
	
	protected java.lang.String Symbol;

	protected java.util.Date StartDate;
	
	protected java.util.Date EndDate;
	
	protected java.lang.Long createTime;
	
	protected java.lang.Boolean IsRAA;
	
	protected String[] index;


	public java.lang.String getSymbol() {
		return Symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	public java.util.Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(java.util.Date startDate) {
		StartDate = startDate;
	}

	public java.util.Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}

	public String[] getIndex() {
		return index;
	}

	public void setIndex(String[] index) {
		this.index = index;
	}

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Long createTime) {
		this.createTime = createTime;
	}

	public java.lang.Boolean getIsRAA() {
		return IsRAA;
	}

	public void setIsRAA(java.lang.Boolean isRAA) {
		IsRAA = isRAA;
	}
	



}