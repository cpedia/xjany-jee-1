package com.lti.service.bo.base;


public class BaseBalanceStatement {

	protected java.lang.Long ID;
	
	protected java.lang.String Symbol ;
	
	//fields
	protected java.util.Date Date;
	
	protected int Year;
	
	protected int Quarter;
	
	protected java.lang.Double CashNEquiv;//Cash and Equivalence;
	
	protected java.lang.Double ShortTermInv;//ShortTerm Investments
	
	protected java.lang.Double NetRec;//Account Receivable
	
	protected java.lang.Double Inventory;
	
	protected java.lang.Double OtherCurAsset;
	
	protected java.lang.Double LongTermInv;
	
	protected java.lang.Double PPNE;//Property,Plant and Equipment
	
	protected java.lang.Double Intangibles;
	
	protected java.lang.Double OtherLongTermAsset;
	
	protected java.lang.Double AcctPayable;//Account Payable
	
	protected java.lang.Double ShortTermDebt;
	
	protected java.lang.Double OtherCurLiab;
	
	protected java.lang.Double LongTermDebt;
	
	protected java.lang.Double OtherLongTermLiab;

	protected java.lang.Double PreferredStock;
	
	protected java.lang.Double CommonStock;
	
	protected java.lang.Double RetainedEarning;
	
	protected java.lang.Double TreasuryStock;
	
	protected java.lang.Double OtherEquity;
	
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

	/**
	 * @return the date
	 */
	public java.util.Date getDate() {
		return Date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(java.util.Date date) {
		Date = date;
	}

	/**
	 * @return the cashNEquiv
	 */
	public java.lang.Double getCashNEquiv() {
		return CashNEquiv;
	}

	/**
	 * @param cashNEquiv the cashNEquiv to set
	 */
	public void setCashNEquiv(java.lang.Double cashNEquiv) {
		CashNEquiv = cashNEquiv;
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
	 * @return the netRec
	 */
	public java.lang.Double getNetRec() {
		return NetRec;
	}

	/**
	 * @param netRec the netRec to set
	 */
	public void setNetRec(java.lang.Double netRec) {
		NetRec = netRec;
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
	 * @return the otherCurAsset
	 */
	public java.lang.Double getOtherCurAsset() {
		return OtherCurAsset;
	}

	/**
	 * @param otherCurAsset the otherCurAsset to set
	 */
	public void setOtherCurAsset(java.lang.Double otherCurAsset) {
		OtherCurAsset = otherCurAsset;
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
	 * @return the pPNE
	 */
	public java.lang.Double getPPNE() {
		return PPNE;
	}

	/**
	 * @param ppne the pPNE to set
	 */
	public void setPPNE(java.lang.Double ppne) {
		PPNE = ppne;
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
	 * @return the otherAsset
	 */
	public java.lang.Double getOtherLongTermAsset() {
		return OtherLongTermAsset;
	}

	/**
	 * @param otherAsset the otherAsset to set
	 */
	public void setOtherLongTermAsset(java.lang.Double otherLTAsset) {
		OtherLongTermAsset = otherLTAsset;
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
	 * @return the otherLiab
	 */
	public java.lang.Double getOtherLongTermLiab() {
		return OtherLongTermLiab;
	}

	/**
	 * @param otherLiab the otherLiab to set
	 */
	public void setOtherLongTermLiab(java.lang.Double otherLTLiab) {
		OtherLongTermLiab = otherLTLiab;
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
	public java.lang.Double getCommonStock() {
		return CommonStock;
	}

	/**
	 * @param commonStock the commonStock to set
	 */
	public void setCommonStock(java.lang.Double commonStock) {
		CommonStock = commonStock;
	}

	/**
	 * @return the retainedEarning
	 */
	public java.lang.Double getRetainedEarning() {
		return RetainedEarning;
	}

	/**
	 * @param retainedEarning the retainedEarning to set
	 */
	public void setRetainedEarning(java.lang.Double retainedEarning) {
		RetainedEarning = retainedEarning;
	}

	/**
	 * @return the treasuryStock
	 */
	public java.lang.Double getTreasuryStock() {
		return TreasuryStock;
	}

	/**
	 * @param treasuryStock the treasuryStock to set
	 */
	public void setTreasuryStock(java.lang.Double treasuryStock) {
		TreasuryStock = treasuryStock;
	}

	/**
	 * @return the otherEquity
	 */
	public java.lang.Double getOtherEquity() {
		return OtherEquity;
	}

	/**
	 * @param otherEquity the otherEquity to set
	 */
	public void setOtherEquity(java.lang.Double otherEquity) {
		OtherEquity = otherEquity;
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
