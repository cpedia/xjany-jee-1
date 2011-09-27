package com.lti.service.bo.base;

import java.io.Serializable;

import com.lti.system.Configuration;

public abstract class BaseTransaction implements Serializable{


	protected java.lang.Long ID;


	protected java.util.Date Date;

	protected java.lang.String Operation;
	
	protected java.lang.Long PortfolioID;

	protected java.lang.Long SecurityID;
	protected java.lang.String AssetName;
	protected java.lang.Double Amount;
	protected java.lang.Long StrategyID;
	
	protected java.lang.Double Percentage;
	
	protected java.lang.String symbol;
	
	protected java.lang.Integer TransactionType = Configuration.TRANSACTION_TYPE_REAL;
	
	protected java.lang.Double Share;
	
	protected java.lang.Boolean IsIgnore=false;
	protected java.lang.Boolean reInvest=true;
	

	public java.lang.Boolean getIsIgnore() {
		return IsIgnore;
	}

	public void setIsIgnore(java.lang.Boolean isIgnore) {
		IsIgnore = isIgnore;
	}

	public java.lang.Double getShare() {
		return Share;
	}
	
	public void setShare(java.lang.Double share) {
		Share = share;
	}
	public java.lang.Long getStrategyID() {
		return StrategyID;
	}
	public void setStrategyID(java.lang.Long strategyID) {
		StrategyID = strategyID;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
	}
	public java.lang.String getOperation() {
		return Operation;
	}
	public void setOperation(java.lang.String operation) {
		Operation = operation;
	}
	public java.lang.Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(java.lang.Long portfolioID) {
		PortfolioID = portfolioID;
	}
	public java.lang.Long getSecurityID() {
		return SecurityID;
	}
	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}
	public java.lang.String getAssetName() {
		return AssetName;
	}
	public void setAssetName(java.lang.String assetName) {
		AssetName = assetName;
	}
	public java.lang.Double getAmount() {
		return Amount;
	}
	public void setAmount(java.lang.Double amount) {
		Amount = amount;
	}
	public java.lang.Double getPercentage() {
		return Percentage;
	}
	public void setPercentage(java.lang.Double percentage) {
		Percentage = percentage;
	}
	public java.lang.String getSymbol() {
		return symbol;
	}
	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}
	public java.lang.Integer getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(java.lang.Integer transactionType) {
		TransactionType = transactionType;
	}

	public java.lang.Boolean getReInvest() {
		return reInvest;
	}

	public void setReInvest(java.lang.Boolean reInvest) {
		this.reInvest = reInvest;
	}


}