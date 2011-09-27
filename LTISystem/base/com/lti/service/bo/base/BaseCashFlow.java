package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseCashFlow implements Serializable{

	protected java.lang.Long ID;
	
	protected java.lang.String Symbol;
	
	//fields
	protected java.util.Date Date;
	
	protected int Year;
	
	protected int Quarter;
	
	protected java.lang.Double DeprAmort;//Depreciation&amortization
	
	protected java.lang.Double AdjNetInc;//Adjust Net Income
	
	protected java.lang.Double OtherOperCash;//Other Operation Cash
	
	protected java.lang.Double CapExp;//Capital Expenditure 
	
	protected java.lang.Double Investment;
	
	protected java.lang.Double OtherInvCash;//Other Investing Cash
	
	protected java.lang.Double Dividend;
	
	protected java.lang.Double StockSalePur;//Sale Purchase of Stock
	
	protected java.lang.Double NetBorrow;//Net Borrowing
	
	protected java.lang.Double OtherFinCash;
	
	protected java.lang.Double CurrencyAdj;//Currency Adjust;
	
	protected java.lang.Double OperCF;//Operating Cash Flow;
	
	protected java.lang.Double FinCF;//Financing Cash Flow;
	
	protected java.lang.Double InvCF;//Investing Cash Flow;
	
	
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

	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
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
	 * @return the quarter
	 */
	public int getQuarter() {
		return Quarter;
	}

	/**
	 * @param quarter the quarter to set
	 */
	public void setQuarter(int quarter) {
		Quarter = quarter;
	}

	public java.lang.Double getDeprAmort() {
		return DeprAmort;
	}

	public void setDeprAmort(java.lang.Double deprAmort) {
		DeprAmort = deprAmort;
	}

	public java.lang.Double getOtherOperCash() {
		return OtherOperCash;
	}

	public void setOtherOperCash(java.lang.Double otherOperCash) {
		OtherOperCash = otherOperCash;
	}

	public java.lang.Double getCapExp() {
		return CapExp;
	}

	public void setCapExp(java.lang.Double capExp) {
		CapExp = capExp;
	}

	public java.lang.Double getOtherInvCash() {
		return OtherInvCash;
	}

	public void setOtherInvCash(java.lang.Double otherInvCash) {
		OtherInvCash = otherInvCash;
	}

	public java.lang.Double getDividend() {
		return Dividend;
	}

	public void setDividend(java.lang.Double dividend) {
		Dividend = dividend;
	}

	/**
	 * @return the adjNetInc
	 */
	public java.lang.Double getAdjNetInc() {
		return AdjNetInc;
	}

	/**
	 * @param adjNetInc the adjNetInc to set
	 */
	public void setAdjNetInc(java.lang.Double adjNetInc) {
		AdjNetInc = adjNetInc;
	}

	/**
	 * @return the investment
	 */
	public java.lang.Double getInvestment() {
		return Investment;
	}

	/**
	 * @param investment the investment to set
	 */
	public void setInvestment(java.lang.Double investment) {
		Investment = investment;
	}

	/**
	 * @return the stockSalePur
	 */
	public java.lang.Double getStockSalePur() {
		return StockSalePur;
	}

	/**
	 * @param stockSalePur the stockSalePur to set
	 */
	public void setStockSalePur(java.lang.Double stockSalePur) {
		StockSalePur = stockSalePur;
	}

	/**
	 * @return the netBorrow
	 */
	public java.lang.Double getNetBorrow() {
		return NetBorrow;
	}

	/**
	 * @param netBorrow the netBorrow to set
	 */
	public void setNetBorrow(java.lang.Double netBorrow) {
		NetBorrow = netBorrow;
	}

	/**
	 * @return the currencyAdj
	 */
	public java.lang.Double getCurrencyAdj() {
		return CurrencyAdj;
	}

	/**
	 * @param currencyAdj the currencyAdj to set
	 */
	public void setCurrencyAdj(java.lang.Double currencyAdj) {
		CurrencyAdj = currencyAdj;
	}

	/**
	 * @return the otherFinCash
	 */
	public java.lang.Double getOtherFinCash() {
		return OtherFinCash;
	}

	/**
	 * @param otherFinCash the otherFinCash to set
	 */
	public void setOtherFinCash(java.lang.Double otherFinCash) {
		OtherFinCash = otherFinCash;
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
	

	
}
