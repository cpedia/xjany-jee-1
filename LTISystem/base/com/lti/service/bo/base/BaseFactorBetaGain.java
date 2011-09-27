/**
 * 
 */
package com.lti.service.bo.base;

/**
 * @author CCD
 *
 */
public class BaseFactorBetaGain {
	// primary key
	protected java.lang.Long ID;
	
	protected java.lang.String Symbol;
	
	protected java.lang.String Factor;
	
	protected java.lang.Double OneMonth;
	
	protected java.lang.Double ThreeMonth;
	
	protected java.lang.Double HalfYear;
	
	protected java.lang.Double OneYear;
	
	protected java.lang.Double Beta;
	
	protected java.util.Date LastDate;

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

	public java.lang.String getFactor() {
		return Factor;
	}

	public void setFactor(java.lang.String factor) {
		Factor = factor;
	}

	public java.lang.Double getOneMonth() {
		return OneMonth;
	}

	public void setOneMonth(java.lang.Double oneMonth) {
		OneMonth = oneMonth;
	}

	public java.lang.Double getThreeMonth() {
		return ThreeMonth;
	}

	public void setThreeMonth(java.lang.Double threeMonth) {
		ThreeMonth = threeMonth;
	}

	public java.lang.Double getHalfYear() {
		return HalfYear;
	}

	public void setHalfYear(java.lang.Double halfYear) {
		HalfYear = halfYear;
	}

	public java.lang.Double getOneYear() {
		return OneYear;
	}

	public void setOneYear(java.lang.Double oneYear) {
		OneYear = oneYear;
	}

	public java.lang.Double getBeta() {
		return Beta;
	}

	public void setBeta(java.lang.Double beta) {
		Beta = beta;
	}

	public java.util.Date getLastDate() {
		return LastDate;
	}

	public void setLastDate(java.util.Date lastDate) {
		LastDate = lastDate;
	}
}
