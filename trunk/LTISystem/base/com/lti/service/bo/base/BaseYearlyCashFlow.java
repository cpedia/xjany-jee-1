package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseYearlyCashFlow implements Serializable {
	
	protected java.lang.Long ID;
	
	protected java.lang.String Symbol;
	
	//fields
	protected java.util.Date EndDate;
	
	protected int Year;
	
	protected java.lang.Double CapExp;
	
	protected java.lang.Double FinCF;
	
	protected java.lang.Double InvCF;
	
	protected java.lang.Double OperCF;

	/**
	 * @return the iD
	 */
	public java.lang.Long getID() {
		return ID;
	}

	/**
	 * @param id the iD to set
	 */
	public void setID(java.lang.Long id) {
		ID = id;
	}

	/**
	 * @return the symbol
	 */
	public java.lang.String getSymbol() {
		return Symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	/**
	 * @return the date
	 */
	public java.util.Date getEndDate() {
		return EndDate;
	}

	/**
	 * @param date the date to set
	 */
	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return Year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		Year = year;
	}

	/**
	 * @return the capExp
	 */
	public java.lang.Double getCapExp() {
		return CapExp;
	}

	/**
	 * @param capExp the capExp to set
	 */
	public void setCapExp(java.lang.Double capExp) {
		CapExp = capExp;
	}

	/**
	 * @return the finCF
	 */
	public java.lang.Double getFinCF() {
		return FinCF;
	}

	/**
	 * @param finCF the finCF to set
	 */
	public void setFinCF(java.lang.Double finCF) {
		FinCF = finCF;
	}

	/**
	 * @return the invCF
	 */
	public java.lang.Double getInvCF() {
		return InvCF;
	}

	/**
	 * @param invCF the invCF to set
	 */
	public void setInvCF(java.lang.Double invCF) {
		InvCF = invCF;
	}

	/**
	 * @return the operCF
	 */
	public java.lang.Double getOperCF() {
		return OperCF;
	}

	/**
	 * @param operCF the operCF to set
	 */
	public void setOperCF(java.lang.Double operCF) {
		OperCF = operCF;
	}
	
	
}
