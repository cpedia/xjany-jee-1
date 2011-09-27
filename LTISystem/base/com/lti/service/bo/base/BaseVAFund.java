package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseVAFund implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.String Ticker;
	
	protected java.lang.String AssetName;
	
	protected java.lang.String FundName;
	
	protected java.lang.String MSVAName;
	
	protected java.lang.String FullName;
	
	protected java.lang.String BarronName;
	
	protected java.lang.String Memo;


	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.String getTicker() {
		return Ticker;
	}

	public void setTicker(java.lang.String ticker) {
		Ticker = ticker;
	}

	public java.lang.String getAssetName() {
		return AssetName;
	}

	public void setAssetName(java.lang.String assetName) {
		AssetName = assetName;
	}

	public java.lang.String getFundName() {
		return FundName;
	}

	public void setFundName(java.lang.String fundName) {
		FundName = fundName;
	}

	public java.lang.String getMSVAName() {
		return MSVAName;
	}

	public void setMSVAName(java.lang.String name) {
		MSVAName = name;
	}

	public java.lang.String getFullName() {
		return FullName;
	}

	public void setFullName(java.lang.String fullName) {
		FullName = fullName;
	}

	public java.lang.String getBarronName() {
		return BarronName;
	}

	public void setBarronName(java.lang.String barronName) {
		BarronName = barronName;
	}

	public java.lang.String getMemo() {
		return Memo;
	}

	public void setMemo(java.lang.String memo) {
		Memo = memo;
	}

}