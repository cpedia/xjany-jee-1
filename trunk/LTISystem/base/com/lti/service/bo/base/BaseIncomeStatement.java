package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseIncomeStatement implements Serializable{
	
	protected java.lang.Long ID;
	
	protected java.lang.String Symbol;
	
	//fields
	
	protected java.util.Date Date;
	
	protected int Year;
	
	protected int Quarter;
	
	protected java.lang.Double Revenue;
	
	protected java.lang.Double COGS;
	
	protected java.lang.Double SGNA;//Selling,General and Administrative Expenses
	
	protected java.lang.Double RND;//Research and Development
	
    protected java.lang.Double OtherOperExp;//Other Operation Expenses
    
    protected java.lang.Double TotalOtherInc;
    
    protected java.lang.Double IntExp;//Net Interest Income
    
    protected java.lang.Double AcctChange;//Accounting Change
    
    protected java.lang.Double DiscOper;//Discount Operation
    
    protected java.lang.Double ExtItem;//Extra Item
    
    protected java.lang.Double IncomeTax;//Income Taxes
    
    protected java.lang.Double Shares;
    
    protected java.lang.Double Floats;
    
    protected java.lang.Double InstitutionHolder;
    
    protected java.lang.Double GrossProfit;
    
    protected java.lang.Double TotalOperExp;
    
    protected java.lang.Double OperInc;
    
    protected java.lang.Double PreTaxInc;
    
    protected java.lang.Double AfterTaxInc;
    
    protected java.lang.Double NetInc;
	
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

	public java.lang.Double getRevenue() {
		return Revenue;
	}

	public void setRevenue(java.lang.Double revenue) {
		Revenue = revenue;
	}

	public java.lang.Double getCOGS() {
		return COGS;
	}

	public void setCOGS(java.lang.Double cogs) {
		COGS = cogs;
	}

	public java.lang.Double getSGNA() {
		return SGNA;
	}

	public void setSGNA(java.lang.Double sgna) {
		SGNA = sgna;
	}

	public java.lang.Double getRND() {
		return RND;
	}

	public void setRND(java.lang.Double rnd) {
		RND = rnd;
	}

	public java.lang.Double getOtherOperExp() {
		return OtherOperExp;
	}

	public void setOtherOperExp(java.lang.Double otherOperExp) {
		OtherOperExp = otherOperExp;
	}

	public java.lang.Double getIntExp() {
		return IntExp;
	}

	public void setIntExp(java.lang.Double intExp) {
		IntExp = intExp;
	}

	public java.lang.Double getAcctChange() {
		return AcctChange;
	}

	public void setAcctChange(java.lang.Double acctChange) {
		AcctChange = acctChange;
	}

	public java.lang.Double getDiscOper() {
		return DiscOper;
	}

	public void setDiscOper(java.lang.Double discOper) {
		DiscOper = discOper;
	}

	public java.lang.Double getExtItem() {
		return ExtItem;
	}

	public void setExtItem(java.lang.Double extItem) {
		ExtItem = extItem;
	}

	public java.lang.Double getIncomeTax() {
		return IncomeTax;
	}

	public void setIncomeTax(java.lang.Double incomeTax) {
		IncomeTax = incomeTax;
	}

	/**
	 * @return the totalOtherInc
	 */
	public java.lang.Double getTotalOtherInc() {
		return TotalOtherInc;
	}

	/**
	 * @param totalOtherInc the totalOtherInc to set
	 */
	public void setTotalOtherInc(java.lang.Double totalOtherInc) {
		TotalOtherInc = totalOtherInc;
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
	 * @return the institutionHolder
	 */
	public java.lang.Double getInstitutionHolder() {
		return InstitutionHolder;
	}

	/**
	 * @param institutionHolder the institutionHolder to set
	 */
	public void setInstitutionHolder(Double institutionHolder) {
		InstitutionHolder = institutionHolder;
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


}
