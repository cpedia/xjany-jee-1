package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseSecurityMPT implements Serializable{
	protected static final long serialVersionUID = 1L;
	
	protected java.lang.Long ID;
	
	protected java.lang.Long SecurityID;
	
	protected java.lang.String SecurityName;
	
	protected java.lang.String Symbol;
	
	protected java.lang.Integer SecurityType;
	
	protected java.lang.Long AssetClassID;
	
	protected java.lang.Long Year;
	
	protected java.lang.Double Alpha;
	
	protected java.lang.Double Beta;
	
	protected java.lang.Double AR;
	
	protected java.lang.Double RSquared;
	
	protected java.lang.Double SharpeRatio;
	
	protected java.lang.Double StandardDeviation;
	
	protected java.lang.Double TreynorRatio;
	
	protected java.lang.Double DrawDown;
	
	protected java.lang.Double Return;
	
	/**********************************************
	protected java.lang.Double sigmaX;
	protected java.lang.Double sigmaB;
	protected java.lang.Double sigmaXX;
	protected java.lang.Double sigmaBB;
	protected java.lang.Double sigmaXB;
	protected java.lang.Double sigmaR;
	protected java.lang.Double sigmaLogX;
	protected java.lang.Double sigmaLogXX;
	
	public java.lang.Double getSigmaX() {
		return sigmaX;
	}

	public void setSigmaX(java.lang.Double sigmaX) {
		this.sigmaX = sigmaX;
	}

	public java.lang.Double getSigmaB() {
		return sigmaB;
	}

	public void setSigmaB(java.lang.Double sigmaB) {
		this.sigmaB = sigmaB;
	}

	public java.lang.Double getSigmaXX() {
		return sigmaXX;
	}

	public void setSigmaXX(java.lang.Double sigmaXX) {
		this.sigmaXX = sigmaXX;
	}

	public java.lang.Double getSigmaBB() {
		return sigmaBB;
	}

	public void setSigmaBB(java.lang.Double sigmaBB) {
		this.sigmaBB = sigmaBB;
	}

	public java.lang.Double getSigmaXB() {
		return sigmaXB;
	}

	public void setSigmaXB(java.lang.Double sigmaXB) {
		this.sigmaXB = sigmaXB;
	}

	public java.lang.Double getSigmaR() {
		return sigmaR;
	}

	public void setSigmaR(java.lang.Double sigmaR) {
		this.sigmaR = sigmaR;
	}

	public java.lang.Double getSigmaLogX() {
		return sigmaLogX;
	}

	public void setSigmaLogX(java.lang.Double sigmaLogX) {
		this.sigmaLogX = sigmaLogX;
	}

	public java.lang.Double getSigmaLogXX() {
		return sigmaLogXX;
	}

	public void setSigmaLogXX(java.lang.Double sigmaLogXX) {
		this.sigmaLogXX = sigmaLogXX;
	}
	/**********************************************/

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Long getSecurityID() {
		return SecurityID;
	}

	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}

	public java.lang.String getSecurityName() {
		return SecurityName;
	}

	public void setSecurityName(java.lang.String securityName) {
		SecurityName = securityName;
	}

	public java.lang.String getSymbol() {
		return Symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	public java.lang.Integer getSecurityType() {
		return SecurityType;
	}

	public void setSecurityType(java.lang.Integer securityType) {
		SecurityType = securityType;
	}


	public java.lang.Long getYear() {
		return Year;
	}

	public void setYear(java.lang.Long year) {
		Year = year;
	}

	public java.lang.Double getAlpha() {
		return Alpha;
	}

	public void setAlpha(java.lang.Double alpha) {
		Alpha = alpha;
	}

	public java.lang.Double getBeta() {
		return Beta;
	}

	public void setBeta(java.lang.Double beta) {
		Beta = beta;
	}

	public java.lang.Double getAR() {
		return AR;
	}

	public void setAR(java.lang.Double ar) {
		AR = ar;
	}

	public java.lang.Double getRSquared() {
		return RSquared;
	}

	public void setRSquared(java.lang.Double squared) {
		RSquared = squared;
	}

	public java.lang.Double getSharpeRatio() {
		return SharpeRatio;
	}

	public void setSharpeRatio(java.lang.Double sharpeRatio) {
		SharpeRatio = sharpeRatio;
	}

	public java.lang.Double getStandardDeviation() {
		return StandardDeviation;
	}

	public void setStandardDeviation(java.lang.Double standardDeviation) {
		StandardDeviation = standardDeviation;
	}

	public java.lang.Double getTreynorRatio() {
		return TreynorRatio;
	}

	public void setTreynorRatio(java.lang.Double treynorRatio) {
		TreynorRatio = treynorRatio;
	}

	public java.lang.Double getDrawDown() {
		return DrawDown;
	}

	public void setDrawDown(java.lang.Double drawDown) {
		DrawDown = drawDown;
	}

	public java.lang.Double getReturn() {
		return Return;
	}

	public void setReturn(java.lang.Double return1) {
		Return = return1;
	}

	public java.lang.Long getAssetClassID() {
		return AssetClassID;
	}

	public void setAssetClassID(java.lang.Long assetClassID) {
		AssetClassID = assetClassID;
	}

	public BaseSecurityMPT(Long securityID, String securityName, String symbol, Integer securityType, Long assetClassID, Long year) {
		super();
		SecurityID = securityID;
		SecurityName = securityName;
		Symbol = symbol;
		SecurityType = securityType;
		AssetClassID = assetClassID;
		Year = year;
	}

	public BaseSecurityMPT() {
		super();
	}	
	
}
