/**
 * 
 */
package com.lti.service.bo.base;

/**
 * @author Administrator
 *
 */
public class BaseSecurityRanking {
	// primary key
	protected java.lang.Long ID;
	
	protected java.lang.Integer Interval;
	
	protected java.lang.Integer Ranking;
	
	protected java.util.Date EndDate;
	
	protected java.lang.Long SecurityID;
	
	protected java.lang.Double BetaGain;
	
	protected java.lang.Long AssetClassID;
	
	protected java.lang.String Symbol;
	
	protected java.lang.Long SecondClassID;
	
	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Integer getInterval() {
		return Interval;
	}

	public void setInterval(java.lang.Integer interval) {
		Interval = interval;
	}

	public java.lang.Integer getRanking() {
		return Ranking;
	}

	public void setRanking(java.lang.Integer ranking) {
		Ranking = ranking;
	}

	public java.util.Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}

	public java.lang.Long getSecurityID() {
		return SecurityID;
	}

	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}

	public java.lang.Double getBetaGain() {
		return BetaGain;
	}

	public void setBetaGain(java.lang.Double betaGain) {
		BetaGain = betaGain;
	}

	public java.lang.Long getAssetClassID() {
		return AssetClassID;
	}

	public void setAssetClassID(java.lang.Long assetClassID) {
		AssetClassID = assetClassID;
	}

	public java.lang.String getSymbol() {
		return Symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	public java.lang.Long getSecondClassID() {
		return SecondClassID;
	}

	public void setSecondClassID(java.lang.Long secondClassID) {
		SecondClassID = secondClassID;
	}

	
	
	
}
