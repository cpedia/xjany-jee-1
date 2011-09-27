package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseYearlyIncomeStatement implements Serializable{
	protected java.lang.Long ID;
	
	protected java.lang.String Symbol;
	
	//fields
	
	protected java.util.Date EndDate;
	
	protected int Year;
	
	protected java.lang.Double Revenue;
	
	protected java.lang.Double COGS;
	
	protected java.lang.Double RD;
	
	protected java.lang.Double IntExp;
	
	protected java.lang.Double IncomeTax;
	
	protected java.lang.Double PreTaxInc;
	
	protected java.lang.Double AfterTaxInc;
	
	protected java.lang.Double GrossProfit;
	
	protected java.lang.Double TotalOperExp;
	
	protected java.lang.Double OperInc;
	
	protected java.lang.Double NetInc;
	
	protected java.lang.Double Floats;
	
	protected java.lang.Double Shares;
	
	protected java.lang.Double InstitutionHolder;

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
	 * @return the revenue
	 */
	public java.lang.Double getRevenue() {
		return Revenue;
	}

	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(java.lang.Double revenue) {
		Revenue = revenue;
	}

	/**
	 * @return the cOGS
	 */
	public java.lang.Double getCOGS() {
		return COGS;
	}

	/**
	 * @param cogs the cOGS to set
	 */
	public void setCOGS(java.lang.Double cogs) {
		COGS = cogs;
	}

	/**
	 * @return the rD
	 */
	public java.lang.Double getRD() {
		return RD;
	}

	/**
	 * @param rd the rD to set
	 */
	public void setRD(java.lang.Double rd) {
		RD = rd;
	}

	/**
	 * @return the intExp
	 */
	public java.lang.Double getIntExp() {
		return IntExp;
	}

	/**
	 * @param intExp the intExp to set
	 */
	public void setIntExp(java.lang.Double intExp) {
		IntExp = intExp;
	}

	/**
	 * @return the incomeTax
	 */
	public java.lang.Double getIncomeTax() {
		return IncomeTax;
	}

	/**
	 * @param incomeTax the incomeTax to set
	 */
	public void setIncomeTax(java.lang.Double incomeTax) {
		IncomeTax = incomeTax;
	}

	/**
	 * @return the preTaxInc
	 */
	public java.lang.Double getPreTaxInc() {
		return PreTaxInc;
	}

	/**
	 * @param preTaxInc the preTaxInc to set
	 */
	public void setPreTaxInc(java.lang.Double preTaxInc) {
		PreTaxInc = preTaxInc;
	}

	/**
	 * @return the afterTaxInc
	 */
	public java.lang.Double getAfterTaxInc() {
		return AfterTaxInc;
	}

	/**
	 * @param afterTaxInc the afterTaxInc to set
	 */
	public void setAfterTaxInc(java.lang.Double afterTaxInc) {
		AfterTaxInc = afterTaxInc;
	}

	/**
	 * @return the grossProfit
	 */
	public java.lang.Double getGrossProfit() {
		return GrossProfit;
	}

	/**
	 * @param grossProfit the grossProfit to set
	 */
	public void setGrossProfit(java.lang.Double grossProfit) {
		GrossProfit = grossProfit;
	}

	/**
	 * @return the totalOperExp
	 */
	public java.lang.Double getTotalOperExp() {
		return TotalOperExp;
	}

	/**
	 * @param totalOperExp the totalOperExp to set
	 */
	public void setTotalOperExp(java.lang.Double totalOperExp) {
		TotalOperExp = totalOperExp;
	}

	/**
	 * @return the operInc
	 */
	public java.lang.Double getOperInc() {
		return OperInc;
	}

	/**
	 * @param operInc the operInc to set
	 */
	public void setOperInc(java.lang.Double operInc) {
		OperInc = operInc;
	}

	/**
	 * @return the netInc
	 */
	public java.lang.Double getNetInc() {
		return NetInc;
	}

	/**
	 * @param netInc the netInc to set
	 */
	public void setNetInc(java.lang.Double netInc) {
		NetInc = netInc;
	}

	/**
	 * @return the floats
	 */
	public java.lang.Double getFloats() {
		return Floats;
	}

	/**
	 * @param floats the floats to set
	 */
	public void setFloats(java.lang.Double floats) {
		Floats = floats;
	}

	/**
	 * @return the shares
	 */
	public java.lang.Double getShares() {
		return Shares;
	}

	/**
	 * @param shares the shares to set
	 */
	public void setShares(java.lang.Double shares) {
		Shares = shares;
	}

	/**
	 * @return the institutionHolder
	 */
	public Double getInstitutionHolder() {
		return InstitutionHolder;
	}

	/**
	 * @param institutionHolder the institutionHolder to set
	 */
	public void setInstitutionHolder(Double institutionHolder) {
		InstitutionHolder = institutionHolder;
	}
	
	

}
