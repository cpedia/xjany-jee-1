package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseYearlyBalanceStatement implements Serializable{
	
    protected java.lang.Long ID;
	
	protected java.lang.String Symbol ;
	
	//fields
	protected java.util.Date EndDate;
	
	protected int Year;
	
	protected java.lang.Double Cash;
	
	protected java.lang.Double ShortTermInv;
	
	protected java.lang.Double AcctRec;
	
	protected java.lang.Double Inventory;
	
	protected java.lang.Double OtherCurAssets;
	
	protected java.lang.Double PPE;
	
	protected java.lang.Double LongTermInv;
	
	protected java.lang.Double Intangibles;
	
	protected java.lang.Double OtherLTAssets;
	
	protected java.lang.Double AcctPayable;
	
	protected java.lang.Double ShortTermDebt;
	
	protected java.lang.Double LongTermDebt;
	
	protected java.lang.Double OtherCurLiab;
	
	protected java.lang.Double OtherLTLiab;
	
	protected java.lang.Double PreferredStock;
	
	protected java.lang.Double TotalEquity;
	
	protected java.lang.Double CurAssets;
	
	protected java.lang.Double Assets;
	
	protected java.lang.Double CurLiab;
	
	protected java.lang.Double Liab;

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
	 * @return the cash
	 */
	public java.lang.Double getCash() {
		return Cash;
	}

	/**
	 * @param cash the cash to set
	 */
	public void setCash(java.lang.Double cash) {
		Cash = cash;
	}

	/**
	 * @return the shortTermInv
	 */
	public java.lang.Double getShortTermInv() {
		return ShortTermInv;
	}

	/**
	 * @param shortTermInv the shortTermInv to set
	 */
	public void setShortTermInv(java.lang.Double shortTermInv) {
		ShortTermInv = shortTermInv;
	}

	/**
	 * @return the acctRec
	 */
	public java.lang.Double getAcctRec() {
		return AcctRec;
	}

	/**
	 * @param acctRec the acctRec to set
	 */
	public void setAcctRec(java.lang.Double acctRec) {
		AcctRec = acctRec;
	}

	/**
	 * @return the inventory
	 */
	public java.lang.Double getInventory() {
		return Inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(java.lang.Double inventory) {
		Inventory = inventory;
	}

	/**
	 * @return the otherCurAssets
	 */
	public java.lang.Double getOtherCurAssets() {
		return OtherCurAssets;
	}

	/**
	 * @param otherCurAssets the otherCurAssets to set
	 */
	public void setOtherCurAssets(java.lang.Double otherCurAssets) {
		OtherCurAssets = otherCurAssets;
	}

	/**
	 * @return the pPE
	 */
	public java.lang.Double getPPE() {
		return PPE;
	}

	/**
	 * @param ppe the pPE to set
	 */
	public void setPPE(java.lang.Double ppe) {
		PPE = ppe;
	}

	/**
	 * @return the longTermInv
	 */
	public java.lang.Double getLongTermInv() {
		return LongTermInv;
	}

	/**
	 * @param longTermInv the longTermInv to set
	 */
	public void setLongTermInv(java.lang.Double longTermInv) {
		LongTermInv = longTermInv;
	}

	/**
	 * @return the intangibles
	 */
	public java.lang.Double getIntangibles() {
		return Intangibles;
	}

	/**
	 * @param intangibles the intangibles to set
	 */
	public void setIntangibles(java.lang.Double intangibles) {
		Intangibles = intangibles;
	}

	/**
	 * @return the otherLTAssets
	 */
	public java.lang.Double getOtherLTAssets() {
		return OtherLTAssets;
	}

	/**
	 * @param otherLTAssets the otherLTAssets to set
	 */
	public void setOtherLTAssets(java.lang.Double otherLTAssets) {
		OtherLTAssets = otherLTAssets;
	}

	/**
	 * @return the acctPayable
	 */
	public java.lang.Double getAcctPayable() {
		return AcctPayable;
	}

	/**
	 * @param acctPayable the acctPayable to set
	 */
	public void setAcctPayable(java.lang.Double acctPayable) {
		AcctPayable = acctPayable;
	}

	/**
	 * @return the shortTermDebt
	 */
	public java.lang.Double getShortTermDebt() {
		return ShortTermDebt;
	}

	/**
	 * @param shortTermDebt the shortTermDebt to set
	 */
	public void setShortTermDebt(java.lang.Double shortTermDebt) {
		ShortTermDebt = shortTermDebt;
	}

	/**
	 * @return the longTermDebt
	 */
	public java.lang.Double getLongTermDebt() {
		return LongTermDebt;
	}

	/**
	 * @param longTermDebt the longTermDebt to set
	 */
	public void setLongTermDebt(java.lang.Double longTermDebt) {
		LongTermDebt = longTermDebt;
	}

	/**
	 * @return the otherCurLiab
	 */
	public java.lang.Double getOtherCurLiab() {
		return OtherCurLiab;
	}

	/**
	 * @param otherCurLiab the otherCurLiab to set
	 */
	public void setOtherCurLiab(java.lang.Double otherCurLiab) {
		OtherCurLiab = otherCurLiab;
	}

	/**
	 * @return the otherLTLiab
	 */
	public java.lang.Double getOtherLTLiab() {
		return OtherLTLiab;
	}

	/**
	 * @param otherLTLiab the otherLTLiab to set
	 */
	public void setOtherLTLiab(java.lang.Double otherLTLiab) {
		OtherLTLiab = otherLTLiab;
	}

	/**
	 * @return the preferredStock
	 */
	public java.lang.Double getPreferredStock() {
		return PreferredStock;
	}

	/**
	 * @param preferredStock the preferredStock to set
	 */
	public void setPreferredStock(java.lang.Double preferredStock) {
		PreferredStock = preferredStock;
	}

	/**
	 * @return the commonStock
	 */
	public java.lang.Double getTotalEquity() {
		return TotalEquity;
	}

	/**
	 * @param commonStock the commonStock to set
	 */
	public void setTotalEquity(java.lang.Double totalEquity) {
		TotalEquity = totalEquity;
	}

	/**
	 * @return the curAssets
	 */
	public java.lang.Double getCurAssets() {
		return CurAssets;
	}

	/**
	 * @param curAssets the curAssets to set
	 */
	public void setCurAssets(java.lang.Double curAssets) {
		CurAssets = curAssets;
	}

	/**
	 * @return the assets
	 */
	public java.lang.Double getAssets() {
		return Assets;
	}

	/**
	 * @param assets the assets to set
	 */
	public void setAssets(java.lang.Double assets) {
		Assets = assets;
	}

	/**
	 * @return the curLiab
	 */
	public java.lang.Double getCurLiab() {
		return CurLiab;
	}

	/**
	 * @param curLiab the curLiab to set
	 */
	public void setCurLiab(java.lang.Double curLiab) {
		CurLiab = curLiab;
	}

	/**
	 * @return the liab
	 */
	public java.lang.Double getLiab() {
		return Liab;
	}

	/**
	 * @param liab the liab to set
	 */
	public void setLiab(java.lang.Double liab) {
		Liab = liab;
	}
	
	
	
	
	

}
